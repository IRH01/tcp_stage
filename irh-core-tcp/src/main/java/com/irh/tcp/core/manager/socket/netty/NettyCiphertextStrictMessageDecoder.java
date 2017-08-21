/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  2014-2-24  下午3:02:17
 */
package com.irh.tcp.core.manager.socket.netty;

import com.irh.core.util.LogUtil;
import com.irh.tcp.core.manager.socket.Message;
import com.irh.tcp.core.manager.socket.SocketUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * Netty解码器-密文.
 *
 * @author iritchie.ren
 */
final class NettyCiphertextStrictMessageDecoder extends ByteToMessageDecoder {

    /**
     *
     */
    NettyCiphertextStrictMessageDecoder() {

    }

    /**
     * 解密
     */
    @Override
    protected void decode(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out) throws Exception {
        if (in.readableBytes() < 4) {
            return;
        }

        int header = 0;
        int packetLength = 0;
        int[] decryptKey = getKey(ctx);
        int cipherByte1 = 0, cipherByte2 = 0;

        // 此处4字节头部的解码使用直接解码形式，规避频繁的对象创建
        int plainByte1, plainByte2;
        int key;

        // 解密两字节header
        cipherByte1 = in.readByte() & 0xff;
        key = decryptKey[0];
        plainByte1 = (cipherByte1 ^ key) & 0xff;

        cipherByte2 = in.readByte() & 0xff;
        key = ((decryptKey[1] ^ cipherByte1));
        plainByte2 = ((cipherByte2 - cipherByte1) ^ key) & 0xff;

        header = ((plainByte1 << 8) + plainByte2);
        // 两字节length,没有加密
        packetLength = in.readShort();
        // 预解密长度信息成功，回溯位置
        in.readerIndex(in.readerIndex() - 4);
        //如果不是标识头，发送给客户端说，断开连接
        if (header != Message.HEADER || packetLength < Message.HEAD_SIZE) {
            // 数据包长度错误，断开连接
            InetSocketAddress socketAddr = (InetSocketAddress) ctx.channel().remoteAddress();
            String ip = socketAddr.getAddress().getHostAddress();
            LogUtil.error(String.format("IP为[%s]发送的消息头不对，断开连接.", ip));
            ctx.channel().close();
            return;
        }

        if (in.readableBytes() < packetLength) {
            // 数据长度不足，等待下次接收
            return;
        }

        // 读取数据并解密数据
        byte[] data = new byte[packetLength];
        in.getBytes(in.readerIndex(), data, 0, packetLength);
        data = SocketUtil.decode(data, decryptKey);
        in.readerIndex(in.readerIndex() + packetLength);
        Message packet = Message.parse(data);
        if (packet != null) {
            out.add(packet);
        }
        return;
    }

    /**
     * 获取当前加解密密钥
     *
     * @return
     */
    private int[] getKey(final ChannelHandlerContext ctx) {
        int[] key = ctx.channel().attr(NettyStrictCodecFactory.DECRYPTION_KEY).get();
        return key;
    }
}