/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  2014-2-20  下午2:55:39
 */
package com.irh.tcp.core.manager.socket.xsocket;

import com.irh.core.util.LogUtil;
import com.irh.tcp.core.config.BaseConfig;
import com.irh.tcp.core.manager.socket.Connection;
import com.irh.tcp.core.manager.socket.ConnectionEventType;
import com.irh.tcp.core.manager.socket.Message;
import com.irh.tcp.core.manager.socket.SocketUtil;
import org.xsocket.connection.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 游戏Socket处理
 *
 * @author iritchie.ren
 */
@SuppressWarnings("unchecked")
final class XsocketSocketHandler implements IConnectHandler,
        IDisconnectHandler, IDataHandler, IConnectExceptionHandler {
    /**
     * 连接附件名字
     */
    private static final String ATTACHMENT_CONNECTION = "ATTACHMENT_CONNECTION";

    /**
     * 解码器
     */
    private static XsocketStrictCodecFactory.XsocketAbstractDecoder decoder = null;

    static {
        XsocketSocketHandler.decoder = XsocketStrictCodecFactory
                .getDecoder(BaseConfig.getInstance().getCiphertext());
    }

    /**
     *
     */
    XsocketSocketHandler() {
    }

    /**
     * 连接上
     */
    @Override
    public boolean onConnect(final INonBlockingConnection session)
            throws IOException {
        //由于xsocket的原因。solinger永远都是开启的。修改socket的源代码。
        session.setOption(IConnection.SO_LINGER, 1);
        session.setOption(IConnection.TCP_NODELAY, true);
        session.setOption(IConnection.SO_KEEPALIVE, true);
        Connection connection = new XsocketConnection(session);

        //附件 放入连接，加密密钥，解密密钥
        Map<String, Object> attachmentMap = new HashMap<>();
        attachmentMap.put(XsocketSocketHandler.ATTACHMENT_CONNECTION,
                connection);
        attachmentMap.put(XsocketStrictCodecFactory.DECRYPTION_KEY,
                SocketUtil.copyDefaultKey());
        attachmentMap.put(XsocketStrictCodecFactory.ENCRYPTION_KEY,
                SocketUtil.copyDefaultKey());
        session.setAttachment(attachmentMap);
        return false;
    }

    /**
     * 接收到数据
     */
    @Override
    public boolean onData(final INonBlockingConnection session)
            throws IOException {
        if (session.available() == -1) {
            return false;
        }
        if (session.getAttachment() == null) {
            return false;
        }
        // 读取封包头，获得封包长度，长度不足则抛出BufferUnderflowException，并重置标记等待下次数据
        Map<String, Object> attachmentMap = (Map<String, Object>) session
                .getAttachment();
        Connection connection = (Connection) attachmentMap
                .get(XsocketSocketHandler.ATTACHMENT_CONNECTION);
        Message message = XsocketSocketHandler.decoder.doDecode(session);
        if (message != null) {
            connection.process(message);
        }
        return false;
    }

    @Override
    public boolean onDisconnect(final INonBlockingConnection session)
            throws IOException {
        closeClient(session);
        return false;
    }

    @Override
    public boolean onConnectException(final INonBlockingConnection session,
                                      final IOException cause) throws IOException {
        LogUtil.error("XsocketSocketHandler的onConnectException方法", cause);
        return false;
    }

    /**
     * 关闭客户端
     */
    private void closeClient(final INonBlockingConnection session) {
        try {
            Map<String, Object> attachmentMap = (Map<String, Object>) session
                    .getAttachment();
            if (attachmentMap != null) {
                Connection conn = (Connection) attachmentMap.get(
                        XsocketSocketHandler.ATTACHMENT_CONNECTION);
                conn.notifyListeners(ConnectionEventType.CLOSE_CONNECTION,
                        conn);
            } else {
                session.close();
            }
        } catch (IOException e) {
            LogUtil.error("连接断开错误", e);
        }
    }
}
