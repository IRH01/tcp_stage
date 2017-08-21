/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  2014-3-7  下午4:59:09
 */
package com.irh.tcp.core.manager.socket;

import java.util.List;

/**
 * socket接收器
 *
 * @author iritchie.ren
 */
public abstract class SocketAcceptor {
    /**
     *
     */
    protected SocketAcceptor() {
    }

    /**
     * 停止
     */
    protected abstract void stop() throws Exception;

    /**
     * 各自的SOCKET框架负责 实现startSocket这个方法
     */
    protected abstract void start() throws Exception;

    /**
     * @return
     */
    protected List<Connection> getAllConnections() {
        return null;
    }

}
