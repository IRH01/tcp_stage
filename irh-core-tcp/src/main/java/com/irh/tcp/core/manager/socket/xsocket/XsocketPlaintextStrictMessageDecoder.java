/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  2014-2-24  下午3:02:17
 */
package com.irh.tcp.core.manager.socket.xsocket;

import com.irh.core.util.LogUtil;
import com.irh.tcp.core.manager.socket.Message;
import org.xsocket.connection.INonBlockingConnection;

import java.io.IOException;

/**
 * Xsocket解密器.线程安全单例类.
 *
 * @author iritchie.ren
 */
final class XsocketPlaintextStrictMessageDecoder extends XsocketStrictCodecFactory.XsocketAbstractDecoder {
    /**
     *
     */
    XsocketPlaintextStrictMessageDecoder() {

    }

    /**
     * 解码
     */
    @Override
    public Message doDecode(final INonBlockingConnection session) throws IOException {
        if (session.available() < 4) {
            return null;
        }
        //记录一下当前的位置
        session.markReadPosition();

        int header = session.readShort();
        int packetLength = session.readShort();

        // 预解密长度信息成功，回溯位置
        session.resetToReadMark();

        //如果不是标识头，发送给客户端说，断开连接
        if (header != Message.HEADER) {
            try {
                String ip = session.getRemoteAddress().getHostAddress();
                LogUtil.error(String.format("IP为[%s]发送的消息头不对，断开连接.", ip));
                session.close();
                return null;
            } catch (IOException e) {
                LogUtil.error("关闭xsocket错误", e);
            }
        }

        if (session.available() < packetLength) {
            // 数据长度不足，等待下次接收
            return null;
        }

        // 读取数据并解密数据
        byte[] data = session.readBytesByLength(packetLength);
        Message packet = Message.parse(data);
        if (packet != null) {
            return packet;
        }
        return null;
    }
}
