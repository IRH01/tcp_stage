/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  2014-2-20  上午11:30:43
 */
package com.irh.tcp.core.util;

import java.io.File;

/**
 * 项目路径工具类.
 *
 * @author iritchie.ren
 */
public final class ProjectPathUtil {
    /**
     * 是否windows
     */
    private static boolean isWindows = System.getProperty("os.name")
            .startsWith("Windows");
    /**
     * 是否linux
     */
    private static boolean isLinux = System.getProperty("os.name")
            .startsWith("Linux");
    /**
     * 项目根目录路径
     */
    private static String projectDirPath = initProjectDirPath();
    /**
     * config目录路径
     */
    private static String configDirPath = initDirPath("config");
    /**
     * agent目录路径
     */
    private static String agentDirPath = initDirPath("agent");

    /**
     * 默认构造函数
     */
    private ProjectPathUtil() {
    }

    /**
     * @return 返回项目的根目录
     */
    public static String getProjectDirPath() {
        return ProjectPathUtil.projectDirPath;
    }

    /**
     * @return 获得config目录
     */
    public static String getConfigDirPath() {
        return ProjectPathUtil.configDirPath;
    }

    /**
     * @return 获得agent目录
     */
    public static String getAgentDirPath() {
        return ProjectPathUtil.agentDirPath;
    }

    /**
     * @return 是否windows
     */
    public static boolean isWindows() {
        return ProjectPathUtil.isWindows;
    }


    /**
     * @return 是否linux
     */
    public static boolean isLinux() {
        return ProjectPathUtil.isLinux;
    }


    /**
     * 初始化项目目录结构
     */
    private static String initProjectDirPath() {
        return System.getProperty("user.dir");
    }

    /**
     * 初始化配置目录结构
     */
    private static String initDirPath(String subDirStr) {
        try {
            File file = new File(projectDirPath + "/" + subDirStr);
            if (!file.exists()) {
                File binFile = new File(
                        Thread.currentThread().getContextClassLoader()
                                .getResource("").getFile());
                file = new File(binFile, subDirStr);
            }
            return file.getCanonicalPath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
