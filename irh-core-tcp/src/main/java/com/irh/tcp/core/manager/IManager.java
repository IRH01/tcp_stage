/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  2014-2-25  上午11:50:23
 */
package com.irh.tcp.core.manager;

/**
 * 管理器接口类，所有实现次类的Manager都要提供一个getInstance的静态方法来返回单例的对象。
 *
 * @author iritchie.ren
 */
public interface IManager {
    /**
     * 启动
     */
    void start() throws Exception;

    /**
     * 启动
     */
    void stop() throws Exception;
}
