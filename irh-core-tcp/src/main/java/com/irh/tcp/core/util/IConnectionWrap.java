/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  Jun 13, 2014  9:51:08 AM
 */
package com.irh.tcp.core.util;

import com.irh.tcp.core.manager.socket.Connection;
import com.irh.tcp.core.manager.socket.Message;

/**
 * 连接包装!
 *
 * @author iritchie.ren
 */
public interface IConnectionWrap {
    /**
     * 发送消息
     *
     * @param message
     */
    void sendMessage(final Message message);

    /**
     *
     */
    void closeConnection();

    /**
     * @return the connection
     */
    Connection getConnection();

    /**
     * @return
     */
    String getHostAddress();

    /**
     *
     */
    int getPort();
}
