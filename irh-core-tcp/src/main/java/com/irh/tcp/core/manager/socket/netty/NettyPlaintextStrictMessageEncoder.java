/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  2014-2-24  下午3:02:17
 */
package com.irh.tcp.core.manager.socket.netty;

import com.irh.tcp.core.manager.socket.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Netty编码器-明文.
 *
 * @author iritchie.ren
 */
final class NettyPlaintextStrictMessageEncoder extends MessageToByteEncoder<Message> {
    /**
     *
     */
    NettyPlaintextStrictMessageEncoder() {

    }

    @Override
    protected void encode(final ChannelHandlerContext ctx, final Message message, final ByteBuf out) throws Exception {
        // 若存在不同线程给同一玩家发送数据的情况，因此加密过程需要同步处理
        byte[] plainText = message.toByteBuffer().array();
        out.writeBytes(plainText);
    }
}