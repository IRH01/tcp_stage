/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  2014-2-24  下午3:02:17
 */
package com.irh.tcp.core.manager.socket.mina;

import com.irh.core.util.LogUtil;
import com.irh.tcp.core.manager.socket.Message;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import java.net.InetSocketAddress;

/**
 * MINA解码器-明文.
 *
 * @author iritchie.ren
 */
final class MinaPlaintextStrictMessageDecoder extends CumulativeProtocolDecoder {

    /**
     * 解码
     */
    @Override
    protected boolean doDecode(final IoSession session, final IoBuffer in, final ProtocolDecoderOutput out) throws Exception {
        if (in.remaining() < 4) {
            // 剩余不足4字节，不足以解析数据包头，暂不处理
            return false;
        }

        int header = in.getShort();
        int packetLength = in.getShort();
        // 预解密长度信息成功，回溯位置
        in.position(in.position() - 4);
        //如果不是标识头，发送给客户端说，断开连接
        if (header != Message.HEADER || packetLength < Message.HEAD_SIZE) {
            // 数据包长度错误，断开连接
            InetSocketAddress socketAddr = (InetSocketAddress) session.getRemoteAddress();
            String ip = socketAddr.getAddress().getHostAddress();
            LogUtil.error(String.format("IP为[%s]发送的消息头不对，断开连接.", ip));
            session.close(true);
            return false;
        }

        if (in.remaining() < packetLength) {
            // 数据长度不足，等待下次接收
            return false;
        }

        // 读取数据并解密数据
        byte[] data = new byte[packetLength];
        in.get(data, 0, packetLength);
        Message packet = Message.parse(data);
        if (packet != null) {
            out.write(packet);
        }
        return true;
    }
}
