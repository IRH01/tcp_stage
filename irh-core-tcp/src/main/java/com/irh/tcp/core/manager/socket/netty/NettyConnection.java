/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  2014-2-25  上午11:21:57
 */
package com.irh.tcp.core.manager.socket.netty;

import com.irh.tcp.core.manager.socket.Connection;
import com.irh.tcp.core.manager.socket.Message;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * Connection的netty实现
 *
 * @author iritchie.ren
 */
public final class NettyConnection extends Connection {
    /**
     * nio抽象连接
     */
    private NioSocketChannel session;

    /**
     */
    NettyConnection(final NioSocketChannel session) {
        this.session = session;
        InetSocketAddress socketAddr = session.remoteAddress();
        this.setHostAddress(socketAddr.getAddress().getHostAddress());
        this.setPort(socketAddr.getPort());
    }

    @Override
    public void sendMessage(final Message message) {
        if (isConnected()) {
            session.writeAndFlush(message);
        }
    }

    /**
     * 关闭连接
     */
    @Override
    public void closeConnection() {
        if (isConnected()) {
            session.close();
        }
    }

    @Override
    public Boolean isConnected() {
        return session.isOpen();
    }

    @Override
    public Boolean isClosed() {
        return !session.isOpen();
    }

    /**
     * @return the session
     */
    public NioSocketChannel getSession() {
        return session;
    }

}
