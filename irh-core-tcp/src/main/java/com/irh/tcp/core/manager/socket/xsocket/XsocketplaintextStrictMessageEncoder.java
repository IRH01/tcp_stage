/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  2014-2-24  下午3:02:17
 */
package com.irh.tcp.core.manager.socket.xsocket;

import com.irh.tcp.core.manager.socket.Message;
import org.xsocket.connection.INonBlockingConnection;

/**
 * Xsocket加密器.线程安全单例类.
 *
 * @author iritchie.ren
 */
final class XsocketplaintextStrictMessageEncoder extends XsocketStrictCodecFactory.XsocketAbstractEncoder {
    /**
     */
    XsocketplaintextStrictMessageEncoder() {

    }

    /**
     * 编码
     */
    @Override
    public byte[] doEncode(final INonBlockingConnection session, final Message message) throws Exception {
        return message.toByteBuffer().array();
    }

}
