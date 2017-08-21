/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  2014-2-20  下午2:56:15
 */
package com.irh.tcp.core.manager.module;

import com.irh.core.util.ClassUtil;
import com.irh.core.util.LogUtil;
import com.irh.tcp.core.annotation.Manager;
import com.irh.tcp.core.annotation.Module;
import com.irh.tcp.core.config.BaseConfig;
import com.irh.tcp.core.config.ExcludeModuleConfig;
import com.irh.tcp.core.config.IncludeModuleConfig;
import com.irh.tcp.core.manager.IManager;
import com.irh.tcp.core.manager.ManagerType;

import java.util.*;

/**
 * 模块管理器.负责对系统所有模块进行管理.
 *
 * @author iritchie.ren
 */
@SuppressWarnings("unchecked")
@Manager(type = ManagerType.MODULE, desc = "模块管理器", dependences = {
        ManagerType.WEB_SERVER, ManagerType.DATABASE,
        ManagerType.ZOOKEEPER, ManagerType.TIMER})
public final class ModuleManager implements IManager {
    /**
     *
     */
    private static final ModuleManager INSTANCE = new ModuleManager();
    /**
     * 模块缓存表
     */
    private Map<String, AbstractModule> modules = new HashMap<>();

    /**
     * 构造函数.
     */
    private ModuleManager() {
    }

    /**
     * @return
     */
    public static ModuleManager getInstance() {
        return ModuleManager.INSTANCE;
    }

    /**
     * 开始
     */
    @Override
    public void start() throws Exception {
        //加载class文件
        List<Class<AbstractModule>> classes = ClassUtil.getClassesByAnnotation(
                BaseConfig.getInstance().getScanPackage(),
                Module.class);
        //组装成map
        Module moduleAnnotation = null;
        String type = null;
        Map<String, Class<?>> classMap = new LinkedHashMap<>();
        for (Class<?> clazz : classes) {
            moduleAnnotation = clazz.getAnnotation(Module.class);
            if (moduleAnnotation != null) {
                type = moduleAnnotation.type();
                if (IncludeModuleConfig.getInstance().contains(type)
                        && !ExcludeModuleConfig.getInstance()
                        .contains(type)) {
                    classMap.put(type, clazz);
                }
            }
        }

        //启动Module
        AbstractModule moduleInstance = null;
        for (Map.Entry<String, Class<?>> entry : classMap.entrySet()) {
            List<String> checkLoopDenpendencesList = new ArrayList<>();
            checkLoopDenpendencesList.add(entry.getKey());
            parseDependencesModule(entry.getValue(), classMap,
                    checkLoopDenpendencesList);
            moduleInstance = (AbstractModule) entry.getValue().newInstance();
            if (!modules.containsKey(entry.getKey())) {
                modules.put(entry.getKey(), moduleInstance);
                startModule(moduleInstance);
            }
        }

        //启动完成调
        for (AbstractModule module : modules.values()) {
            module.afterAllModuleLoadOver();
        }
    }

    /**
     * @param moduleInstance
     * @throws Exception
     */
    private void startModule(AbstractModule moduleInstance) throws Exception {
        LogUtil.info(String.format("***********【%s】模块开始启动!***********",
                moduleInstance.getClass().getName()));
        long startTime = System.currentTimeMillis();
        moduleInstance.start();
        LogUtil.info(String.format("***********【%s】模块启动成功，启动用了%dMS***********",
                moduleInstance.getClass().getName(),
                System.currentTimeMillis() - startTime));
    }

    /**
     * 停止
     */
    @Override
    public void stop() throws Exception {
        for (Map.Entry<String, AbstractModule> entry : modules.entrySet()) {
            long startTime = System.currentTimeMillis();
            entry.getValue().stop();
            LogUtil.info("***********停止模块【%s】，停止用了%dms***********",
                    entry.getKey(),
                    (System.currentTimeMillis() - startTime));
        }
    }

    /**
     * 根据模块名获取模块
     *
     * @return
     */
    public <T extends AbstractModule> T getModule(final String moduleType) {
        return (T) modules.get(moduleType);
    }

    /**
     * 获取所有模块
     *
     * @return
     */
    public Collection<AbstractModule> getAllModule() {
        return modules.values();
    }

    /**
     * @param moduleClazz
     * @param classMap
     * @param checkLoopDenpendencesList
     */
    private void parseDependencesModule(final Class<?> moduleClazz,
                                        final Map<String, Class<?>> classMap,
                                        final List<String> checkLoopDenpendencesList)
            throws Exception {
        Module moduleAnnotation = moduleClazz.getAnnotation(Module.class);
        String[] dependenceTypes = moduleAnnotation.dependences();
        AbstractModule moduleInstance = null;
        Class<?> dependenceClass = null;
        if (dependenceTypes != null && dependenceTypes.length > 0) {
            for (String moduleType : dependenceTypes) {
                dependenceClass = classMap.get(moduleType);

                if (!modules.containsKey(moduleType)) {
                    if (checkLoopDenpendencesList.contains(moduleType)) {
                        checkLoopDenpendencesList.add(moduleType);
                        StringBuilder sb = new StringBuilder();
                        for (String mt : checkLoopDenpendencesList) {
                            sb.append(mt.toString());
                            sb.append("->");
                        }
                        sb.delete(sb.length() - 2, sb.length());
                        throw new RuntimeException(
                                String.format("[%s]出现循环依赖", sb));
                    } else {
                        checkLoopDenpendencesList.add(moduleType);
                    }
                    parseDependencesModule(dependenceClass, classMap,
                            checkLoopDenpendencesList);
                    checkLoopDenpendencesList.add(moduleType);
                    moduleInstance = (AbstractModule) dependenceClass
                            .newInstance();
                    modules.put(moduleType, moduleInstance);
                    startModule(moduleInstance);

                }
            }
        }
    }
}
