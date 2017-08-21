/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  2014-2-25  上午11:21:57
 */
package com.irh.tcp.core.manager.socket.xsocket;

import com.irh.core.util.LogUtil;
import com.irh.tcp.core.config.BaseConfig;
import com.irh.tcp.core.manager.socket.Connection;
import com.irh.tcp.core.manager.socket.Message;
import org.xsocket.connection.INonBlockingConnection;

import java.io.IOException;
import java.util.Collection;

/**
 * Connection的xsocket实现.
 *
 * @author iritchie.ren
 */
public class XsocketConnection extends Connection {
    /**
     * 加密器
     */
    private static XsocketStrictCodecFactory.XsocketAbstractEncoder encoder = null;
    /**
     * nio抽象连接
     */
    private INonBlockingConnection session;

    /**
     *
     */
    static {
        XsocketConnection.encoder = XsocketStrictCodecFactory
                .getEncoder(BaseConfig.getInstance().getCiphertext());
    }

    /**
     */
    public XsocketConnection(final INonBlockingConnection session) {
        this.session = session;
        this.setHostAddress(session.getRemoteAddress().getHostAddress());
        this.setPort(session.getRemotePort());
    }

    @Override
    public void sendMessage(final Message message) {
        try {
            if (this.isConnected()) {
                INonBlockingConnection session = this.getSession();
                session.write(encoder.doEncode(session, message));
                session.flush();
            }
        } catch (Exception e) {
            LogUtil.error(e);
        }

        //this.connQueue.add(new XsocketSendMessageAction(this, message, XsocketConnection.encoder));
    }

    /**
     * @param messages
     */
    public void sendMessage(Collection<Message> messages) {
        try {
            if (this.isConnected()) {
                INonBlockingConnection session = this.getSession();
                for (Message msg : messages) {
                    session.write(encoder.doEncode(session, msg));
                }
                session.flush();
            }
        } catch (Exception e) {
            LogUtil.error(e);
        }
    }

    /**
     * 关闭连接
     */
    @Override
    public void closeConnection() {
        try {
            if (isConnected()) {
                session.close();
            }
        } catch (IOException e) {
            LogUtil.error("关闭Xsocket连接错误", e);
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
    public INonBlockingConnection getSession() {
        return session;
    }

}
