/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  Jun 20, 2014  11:32:49 AM
 */
package com.irh.tcp.core.server;

import com.irh.core.util.ClassUtil;
import com.irh.core.util.LogUtil;
import com.irh.tcp.core.annotation.Manager;
import com.irh.tcp.core.config.*;
import com.irh.tcp.core.config.*;
import com.irh.tcp.core.manager.IManager;
import com.irh.tcp.core.manager.ManagerType;
import com.irh.tcp.core.util.ProjectPathUtil;
import org.apache.log4j.PropertyConfigurator;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 抽象服务器
 *
 * @author iritchie.ren
 */
@SuppressWarnings("unchecked")
public abstract class AbstractServer {

    /**
     * 配置文件地址
     */
    public static final String CONFIG_FILE_NAME = "server.config.file";

    /**
     * Log4j文件地址
     */
    public static final String LOG4J_FILE_NAME = "log4j.config.file";

    /**
     * Spring文件地址
     */
    public static final String SPRING_FILE_NAME = "spring.config.file";


    /**
     *
     */
    private static AbstractServer server = null;

    /**
     * 管理器
     */
    protected List<IManager> managers = new ArrayList<>();

    /**
     * 开始时间
     */
    protected long startTime = System.currentTimeMillis();
    /**
     * 是否优雅关闭
     */
    protected boolean isGracefullyShutdown = false;

    /**
     * 服务器状态
     */
    private ServerState state = null;

    /**
     * 进程ID
     */
    private String pid = "";

    /**
     * 服务器
     **/
    private String ip = "";

    /**
     *
     */
    protected AbstractServer() {
        AbstractServer.server = this;
        Runtime.getRuntime().addShutdownHook(
                new AbstractServer.AbortShutdownHockWork());
    }

    /**
     *
     */
    public static <T extends AbstractServer> T getServer() {
        return (T) server;
    }

    /**
     * 启动
     */
    public void start() throws Exception {
        //初始化log4j
        initLog();
        //初始化game_server.xml
        initConfigFile();
        //初始化管理器.
        initManager();
        //初始化PID
        initPid();
        //初始化ip
        initIp();
        //启动之后回调
        afterStart();
        //运行
        this.state = ServerState.RUNING;
    }

    /**
     * 启动
     */
    public void afterStart() throws Exception {

    }

    /**
     * @param state the state to set
     */
    public void setState(ServerState state) {
        this.state = state;
    }

    /**
     * 是否关闭中
     *
     * @return
     */
    public boolean isClosing() {
        return this.state == ServerState.CLOSEING;
    }

    /**
     * 停止
     */
    public void stop() {
        this.isGracefullyShutdown = true;
        stopManager();
        System.exit(0);
    }

    /**
     * @return the startTime
     */
    public long getStartTime() {
        return startTime;
    }

    /**
     * @return the pid
     */
    public String getPid() {
        return pid;
    }

    /**
     * @return the ip
     */
    public String getIp() {
        return ip;
    }

    /**
     *
     */
    protected void stopManager() {
        long startTime = System.currentTimeMillis();
        //停止管理器
        for (int i = managers.size() - 1; i >= 0; i--) {
            try {
                LogUtil.info(String.format("=========停止管理器【%s】===========",
                        managers.get(i).getClass().getName()));
                managers.get(i).stop();
            } catch (Exception e) {
                LogUtil.error(String.format("停止管理器【%s】错误",
                        managers.get(i).getClass().getName()), e);
            }
        }
        LogUtil.info(String.format(
                "-----------服务器停服成功，是否优雅关闭[%b]，停服用了%dms!-----------",
                this.isGracefullyShutdown,
                System.currentTimeMillis() - startTime));
    }

    /**
     * 初始化config配置
     */
    private void initConfigFile() throws Exception {
        ConfigFileUtil.init();
        List<Class<IConfig>> configClasses = ClassUtil.getClassesByInterface(
                BaseConfig.class.getPackage().getName(), IConfig.class);
        if (configClasses.isEmpty()) {
            throw new IllegalArgumentException("files is null");
        }

        Method method = null;
        IConfig iconfig = null;
        for (Class<?> iconfigClass : configClasses) {
            method = iconfigClass.getMethod("getInstance");
            iconfig = (IConfig) method.invoke(iconfigClass);
            iconfig.parse();
        }

    }

    /**
     * 初始化管理器 启服的时候严格按照XML配置顺序加载，停服的时候是相反的顺序执行
     *
     * @throws Exception
     */
    private void initManager() throws Exception {
        List<Class<IManager>> classes = ClassUtil.getClassesByInterface(
                IManager.class.getPackage().getName(), IManager.class);
        //组装成map
        Manager managerAnnotation = null;
        ManagerType type = null;
        Map<ManagerType, Class<?>> classMap = new LinkedHashMap<>();
        for (Class<IManager> clazz : classes) {
            managerAnnotation = clazz.getAnnotation(Manager.class);
            if (managerAnnotation != null) {
                type = managerAnnotation.type();
                if (ExcludeManagerConfig.getInstance().contains(type)) {
                    continue;
                }
                if (IncludeManagerConfig.getInstance().contains(type)) {
                    classMap.put(type, clazz);
                }
            }
        }

        //根据依赖关系组装class
        IManager managetInstance = null;
        for (Entry<ManagerType, Class<?>> entry : classMap.entrySet()) {
            List<ManagerType> checkLoopDenpendencesList = new ArrayList<>();
            checkLoopDenpendencesList.add(entry.getKey());
            parseDependencesManager(entry.getValue(), classMap,
                    checkLoopDenpendencesList);
            managetInstance = (IManager) entry.getValue()
                    .getMethod("getInstance").invoke(entry.getValue());
            if (!managers.contains(managetInstance)) {
                managers.add(managetInstance);
            }
        }

        //Manager验证。
        if (managers.size() != classMap.size()) {
            throw new RuntimeException("manager大小和已有的Manager大小不一致");
        }
        for (Class<?> clazz : classMap.values()) {
            boolean find = false;
            //for (IManager manager : managers) {
            for (int i = 0; i < managers.size(); i++) {
                if (clazz == managers.get(i).getClass()) {
                    find = true;
                    break;
                }
            }
            if (!find) {
                throw new RuntimeException(String.format("[%s]类没有对应的Manager启动",
                        clazz.getName()));
            }
        }

        //启动Manager
        for (IManager manager : managers) {
            long startTime = System.currentTimeMillis();
            manager.start();
            LogUtil.info(String.format(
                    "===========【%s】管理器启动成功，启动用了%dMS===========",
                    manager.getClass().getName(),
                    System.currentTimeMillis() - startTime));
        }
    }

    /**
     * 解析依赖的Manager
     *
     * @param managerClass
     * @param classMap
     * @throws Exception
     */
    private void parseDependencesManager(final Class<?> managerClass,
                                         final Map<ManagerType, Class<?>> classMap,
                                         final List<ManagerType> checkLoopDenpendencesList)
            throws Exception {
        Manager managerAnnotation = managerClass.getAnnotation(Manager.class);
        ManagerType managerTyp = managerAnnotation.type();
        ManagerType[] dependenceTypes = managerAnnotation.dependences();
        if (dependenceTypes[0] != ManagerType.NONE) {
            for (ManagerType managerType : dependenceTypes) {
                if (managerType.equals(ManagerType.ALL)) {
                    for (ManagerType managerTypeTmp : ManagerType.values()) {
                        if (!managerTyp.equals(managerTypeTmp)) {
                            loadDependencesManager(classMap, managerTypeTmp,
                                    checkLoopDenpendencesList);
                        }
                    }
                } else {
                    loadDependencesManager(classMap, managerType,
                            checkLoopDenpendencesList);
                }
            }
        }
        checkLoopDenpendencesList.remove(checkLoopDenpendencesList.size() - 1);
    }

    /**
     * 加载依赖的Manager to managers
     *
     * @param classMap
     * @param managerType
     * @param checkLoopDenpendencesList
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     * @throws Exception
     */
    private void loadDependencesManager(
            final Map<ManagerType, Class<?>> classMap,
            final ManagerType managerType,
            final List<ManagerType> checkLoopDenpendencesList)
            throws Exception {
        IManager dependenceInstance;
        Class<?> dependenceClass;
        dependenceClass = classMap.get(managerType);
        if (dependenceClass != null) {
            dependenceInstance = (IManager) dependenceClass
                    .getMethod("getInstance").invoke(dependenceClass);
            if (!managers.contains(dependenceInstance)) {
                if (checkLoopDenpendencesList.contains(managerType)) {
                    checkLoopDenpendencesList.add(managerType);
                    StringBuilder sb = new StringBuilder();
                    for (ManagerType mt : checkLoopDenpendencesList) {
                        sb.append(mt.toString());
                        sb.append("->");
                    }
                    sb.delete(sb.length() - 2, sb.length());
                    throw new RuntimeException(String.format("[%s]出现循环依赖", sb));
                } else {
                    checkLoopDenpendencesList.add(managerType);
                }
                parseDependencesManager(dependenceClass, classMap,
                        checkLoopDenpendencesList);
                managers.add(dependenceInstance);
            }
        }
    }

    /**
     * 意外关闭钩子线程
     *
     * @author iritchie.ren
     */
    private class AbortShutdownHockWork extends Thread {
        @Override
        public void run() {
            if (!AbstractServer.this.isGracefullyShutdown) {
                AbstractServer.this.stopManager();
            }
        }
    }

    /**
     * 初始化log4j
     *
     * @throws Exception
     */
    private void initLog() throws Exception {
        String log4jFile = System.getProperty(AbstractServer.LOG4J_FILE_NAME);
        PropertyConfigurator.configure(ProjectPathUtil.getConfigDirPath()
                + File.separator + log4jFile);
    }

    /**
     *
     */
    private void initPid() {
        String name = ManagementFactory.getRuntimeMXBean().getName();
        this.pid = name.split("@")[0];
    }

    /**
     *
     */
    private void initIp() throws Exception {
        ip = InetAddress.getLocalHost().getHostAddress();
    }

}
