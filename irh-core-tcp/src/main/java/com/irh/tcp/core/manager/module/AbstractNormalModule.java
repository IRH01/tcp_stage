/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  2014-2-21  下午6:14:47
 */
package com.irh.tcp.core.manager.module;

/**
 * 一般性的Module抽象类.
 *
 * @author iritchie.ren
 */
public abstract class AbstractNormalModule extends AbstractModule {

    /**
     * 初始化模块
     */
    @Override
    public void start() throws Exception {
    }

    /**
     * 重启
     */
    @Override
    public void stop() throws Exception {
    }
}
