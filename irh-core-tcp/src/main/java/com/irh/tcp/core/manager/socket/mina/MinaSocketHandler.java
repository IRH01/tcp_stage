/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  2014-2-24  下午3:02:17
 */
package com.irh.tcp.core.manager.socket.mina;

import com.irh.core.util.LogUtil;
import com.irh.tcp.core.manager.socket.Connection;
import com.irh.tcp.core.manager.socket.ConnectionEventType;
import com.irh.tcp.core.manager.socket.SocketUtil;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

/**
 * MINA socket处理器
 *
 * @author iritchie.ren
 */
public final class MinaSocketHandler extends IoHandlerAdapter {
    /**
     * 附件属性名字
     */
    private static final String ATTRIBUTE_CONNECTION = "ATTRIBUTE_CONNECTION";

    /**
     *
     */
    public MinaSocketHandler() {
    }

    @Override
    public void sessionOpened(final IoSession session) throws Exception {
    }

    @Override
    public void sessionCreated(final IoSession session) throws Exception {
        Connection conn = new MinaConnection(session);
        session.setAttribute(MinaSocketHandler.ATTRIBUTE_CONNECTION, conn);
        session.setAttribute(MinaStrictCodecFactory.DECRYPTION_KEY,
                SocketUtil.copyDefaultKey());
        session.setAttribute(MinaStrictCodecFactory.ENCRYPTION_KEY,
                SocketUtil.copyDefaultKey());
    }

    @Override
    public void sessionClosed(final IoSession session) throws Exception {
        closeClient(session);
    }

    @Override
    public void messageReceived(final IoSession session, final Object message)
            throws Exception {
        Connection conn = (Connection) session
                .getAttribute(MinaSocketHandler.ATTRIBUTE_CONNECTION);
        conn.process(message);

    }

    @Override
    public void exceptionCaught(final IoSession session, final Throwable cause)
            throws Exception {
        LogUtil.error("MinaSocketHandler的exceptionCaught方法 local:"
                + session.getLocalAddress() + "remote:"
                + session.getRemoteAddress(), cause);
    }

    /**
     * 关闭客户端
     */
    private void closeClient(IoSession session) {
        Connection conn = (Connection) session
                .getAttribute(MinaSocketHandler.ATTRIBUTE_CONNECTION);
        if (conn != null) {
            conn.notifyListeners(ConnectionEventType.CLOSE_CONNECTION, conn);
        } else {
            session.close(true);
        }
    }

}
