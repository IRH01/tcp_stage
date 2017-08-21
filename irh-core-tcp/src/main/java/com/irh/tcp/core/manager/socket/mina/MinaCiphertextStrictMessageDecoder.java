/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  2014-2-24  下午3:02:17
 */
package com.irh.tcp.core.manager.socket.mina;

import com.irh.core.util.LogUtil;
import com.irh.tcp.core.manager.socket.Message;
import com.irh.tcp.core.manager.socket.SocketUtil;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import java.net.InetSocketAddress;

/**
 * MINA解码器-密文.
 *
 * @author iritchie.ren
 */
final class MinaCiphertextStrictMessageDecoder
        extends CumulativeProtocolDecoder {
    /**
     * 解码
     */
    @Override
    protected boolean doDecode(final IoSession session, final IoBuffer in,
                               final ProtocolDecoderOutput out) throws Exception {

        if (in.remaining() < 4) {
            // 剩余不足4字节，不足以解析数据包头，暂不处理
            return false;
        }
        in.mark();
        in.position(in.position() + 2);
        int packetLength = in.getShort();
        in.reset();
        if (packetLength < Message.HEAD_SIZE) {
            // 数据包长度错误，断开连接
            LogUtil.error(String.format(
                    "error packet length: packetlength=%d Packet.HDR_SIZE=%d",
                    packetLength, Message.HEAD_SIZE));
            session.close(true);
            return false;
        }
        if (in.remaining() < packetLength) {
            // 数据长度不足，等待下次接收
            return false;
        }

        int header = 0;
        int[] decryptKey = getKey(session);
        int cipherByte1 = 0, cipherByte2 = 0;
        // 此处4字节头部的解码使用直接解码形式，规避频繁的对象创建
        int plainByte1, plainByte2;
        int key;
        in.mark();
        // 解密两字节header
        cipherByte1 = in.get() & 0xff;
        key = decryptKey[0];
        plainByte1 = (cipherByte1 ^ key) & 0xff;

        cipherByte2 = in.get() & 0xff;
        key = ((decryptKey[1] ^ cipherByte1)) & 0xff;
        plainByte2 = ((cipherByte2 - cipherByte1) ^ key) & 0xff;

        header = ((plainByte1 << 8) + plainByte2);
        in.reset();
        if (header != Message.HEADER) {
            // 包头不相等
            InetSocketAddress socketAddr = (InetSocketAddress) session
                    .getRemoteAddress();
            String ip = socketAddr.getAddress().getHostAddress();
            LogUtil.error(String.format("IP为[%s]发送的消息头不对，断开连接.", ip));
            session.close(true);
            return false;
        }

        // 读取数据并解密数据
        byte[] data = new byte[packetLength];
        in.get(data, 0, packetLength);

        data = SocketUtil.decode(data, decryptKey);

        Message packet = Message.parse(data);

        if (packet != null) {
            out.write(packet);
        }
        return true;
    }

    /**
     * 获取当前加解密密钥
     *
     * @param session
     * @return
     */
    private int[] getKey(final IoSession session) {
        int[] key = (int[]) session
                .getAttribute(MinaStrictCodecFactory.DECRYPTION_KEY);
        return key;
    }
}
