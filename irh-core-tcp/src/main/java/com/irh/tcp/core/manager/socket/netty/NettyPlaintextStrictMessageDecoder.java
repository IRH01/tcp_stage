/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  2014-2-24  下午3:02:17
 */
package com.irh.tcp.core.manager.socket.netty;

import com.irh.core.util.LogUtil;
import com.irh.tcp.core.manager.socket.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * Netty解码器-明文.
 *
 * @author iritchie.ren
 */
final class NettyPlaintextStrictMessageDecoder extends ByteToMessageDecoder {
    /**
     *
     */
    NettyPlaintextStrictMessageDecoder() {

    }

    /**
     * 解密
     */
    @Override
    protected void decode(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out) throws Exception {
        if (in.readableBytes() < 4) {
            return;
        }
        int header = in.readShort();
        int packetLength = in.readShort();
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
        in.readerIndex(in.readerIndex() + packetLength);
        Message packet = Message.parse(data);
        if (packet != null) {
            out.add(packet);
        }
        return;
    }
}