/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  2014-2-25  上午11:21:57
 */
package com.irh.tcp.core.manager.socket.mina;

import com.irh.tcp.core.manager.socket.Connection;
import com.irh.tcp.core.manager.socket.Message;
import org.apache.mina.core.session.IoSession;

import java.net.InetSocketAddress;

/**
 * Connection的mima实现
 *
 * @author iritchie.ren
 */
public class MinaConnection extends Connection {
    /**
     * nio抽象连接
     */
    private IoSession session;

    /**
     */
    public MinaConnection(final IoSession session) {
        this.session = session;
        InetSocketAddress socketAddr = (InetSocketAddress) session.getRemoteAddress();
        this.setHostAddress(socketAddr.getAddress().getHostAddress());
        this.setPort(socketAddr.getPort());
    }

    @Override
    public void sendMessage(final Message message) {
        if (isConnected()) {
            session.write(message);
        }
    }

    /**
     * 关闭连接
     */
    @Override
    public void closeConnection() {
        if (isConnected()) {
            session.close(true);
        }
    }

    @Override
    public Boolean isConnected() {
        return session != null && session.isConnected();
    }

    @Override
    public Boolean isClosed() {
        return session.isClosing();
    }

    /**
     * @return the session
     */
    public IoSession getSession() {
        return session;
    }

}
