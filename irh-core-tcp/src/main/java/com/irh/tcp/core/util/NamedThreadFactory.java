/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  2014-2-21  下午3:47:16
 */
package com.irh.tcp.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 命名线程生成工厂，仅为了给游戏所用线程池所创建的线程提供命名支持
 *
 * @author iritchie.ren
 */
public class NamedThreadFactory
        implements ThreadFactory, UncaughtExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(NamedThreadFactory.class);

    /**
     * 是否为后台线程
     */
    private boolean daemon;

    /**
     * 线程名
     */
    private String threadName;
    /**
     * 优先级
     */
    private int prio;
    /**
     * 线程组
     */
    private ThreadGroup group;
    /**
     * 线程数目
     */
    private AtomicInteger threadNumber = new AtomicInteger(1);

    /**
     * @param threadName
     */
    public NamedThreadFactory(final String threadName) {
        this(threadName, false);
        group = new ThreadGroup(threadName);
    }

    /**
     * @param threadName 线程名前缀
     * @param daemon     是否为后台线程
     */
    public NamedThreadFactory(final String threadName, final boolean daemon) {
        this.threadName = threadName;
        this.daemon = daemon;
        group = new ThreadGroup(threadName);
    }

    /**
     * @param prio
     */
    public NamedThreadFactory(final String threadName, final int prio) {
        this.threadName = threadName;
        this.prio = prio;
        this.daemon = false;
        group = new ThreadGroup(threadName);
    }

    /**
     * @param prio
     */
    public NamedThreadFactory(final String threadName, final boolean daemon,
                              final int prio) {
        this.prio = prio;
        this.daemon = daemon;
        this.threadName = threadName;
        group = new ThreadGroup(threadName);
    }

    @Override
    public Thread newThread(final Runnable r) {
        Thread t = new Thread(group, r);
        t.setName(threadName + "-" + threadNumber.getAndIncrement());
        t.setPriority(prio);
        t.setDaemon(daemon);
        t.setUncaughtExceptionHandler(this);
        return t;
    }

    @Override
    public void uncaughtException(final Thread thread,
                                  final Throwable throwable) {
        logger.error("线程{}抛出异常。", thread.getName(), throwable);
    }

    /**
     * @return the threadName
     */
    public String getThreadName() {
        return threadName;
    }

}
