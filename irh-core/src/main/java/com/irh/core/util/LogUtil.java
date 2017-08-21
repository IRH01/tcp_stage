/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  2014-2-21  下午4:51:56
 */
package com.irh.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志工具类
 *
 * @author iritchie.ren
 */
public final class LogUtil {

    /** */
    private static Logger debugLogger = LoggerFactory.getLogger("debugLogger");
    /** */
    private static Logger infoLogger = LoggerFactory.getLogger("infoLogger");
    /** */
    private static Logger warnLogger = LoggerFactory.getLogger("warnLogger");
    /** */
    private static Logger errorLogger = LoggerFactory.getLogger("errorLogger");
    /** */
    private static Logger socketLogger = LoggerFactory.getLogger("socketLogger");
    /** */
    private static Logger systemLogger = LoggerFactory
            .getLogger("systemLogger");

    /**
     *
     */
    private LogUtil() {

    }

    /**
     * @param message
     */
    public static void debug(final String message, final Object... arguments) {
        debugLogger.debug(message, arguments);
    }

    /**
     * @param message
     */
    public static void info(final String message, final Object... arguments) {
        infoLogger.info(message, arguments);
    }

    /**
     * @param message
     */
    public static void warn(final String message, final Object... arguments) {
        warnLogger.warn(message, arguments);
    }

    /**
     * @param message
     */
    public static void error(final String message, final Object... arguments) {
        errorLogger.error(message, arguments);
    }

    /**
     *
     */
    public static void error(final Throwable throwable) {
        errorLogger.error(throwable.getMessage(), throwable);
    }

    /**
     * @param message
     */
    public static void socket(final String message, final Object... arguments) {
        socketLogger.error(message, arguments);
    }

    /**
     * 这个接口只用来记录系统信息消息，比如模版刷新啊，agent更改啊。不记录异常。
     *
     * @param message
     */
    public static void sysLog(final String message, final Object... arguments) {
        systemLogger.info(message, arguments);
    }

}
