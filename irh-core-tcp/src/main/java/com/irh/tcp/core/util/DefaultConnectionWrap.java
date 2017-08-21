/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  Jun 13, 2014  10:00:38 AM
 */
package com.irh.tcp.core.util;

import com.irh.tcp.core.manager.socket.Connection;
import com.irh.tcp.core.manager.socket.Message;

/**
 * @author iritchie.ren
 */
public final class DefaultConnectionWrap implements IConnectionWrap {

    /**
     * 连接
     */
    private Connection connection;

    /**
     */
    public DefaultConnectionWrap(final Connection connection) {
        this.connection = connection;
    }

    /**
     * @return the connection
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     *
     */
    public String getHostAddress() {
        return this.connection.getHostAddress();
    }

    @Override
    public void sendMessage(Message message) {
        connection.sendMessage(message);
    }

    @Override
    public void closeConnection() {
        connection.closeConnection();
    }

    @Override
    public int getPort() {
        return connection.getPort();
    }
}
