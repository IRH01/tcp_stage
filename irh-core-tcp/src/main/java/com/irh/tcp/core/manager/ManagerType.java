/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  2014-2-25  上午11:50:23
 */
package com.irh.tcp.core.manager;

/**
 * manager 类型
 */
public enum ManagerType {
    /**
     * 无
     */
    NONE,

    /**
     * 所有
     */
    ALL,

    /**
     * 数据库
     */
    DATABASE,

    /**
     * 模块
     */
    MODULE,

    /**
     * socket
     */
    SOCKET,

    /**
     * 定时器
     */
    TIMER,

    /**
     * 定时器
     */
    WEB_SERVER,


    /**
     * spring
     */
    SPRING,

    /**
     * ZOOKEEPER
     */
    ZOOKEEPER;
}