/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  2014-2-24  下午3:02:17
 */
package com.irh.tcp.core.manager.socket.netty;

import com.irh.core.util.LogUtil;
import com.irh.tcp.core.manager.socket.Connection;
import com.irh.tcp.core.manager.socket.ConnectionEventType;
import com.irh.tcp.core.manager.socket.SocketUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;

/**
 * 游戏Socket处理
 *
 * @author iritchie.ren
 */
final class NettySocketHandler extends ChannelInboundHandlerAdapter {
    /**
     * 附件属性名字
     */
    private static final AttributeKey<Connection> ATTRIBUTE_CONNECTION = AttributeKey.valueOf("ATTRIBUTE_CONNECTION");

    /**
     *
     */
    NettySocketHandler() {
    }

    /**
     *
     */
    @Override
    public void channelRegistered(final ChannelHandlerContext ctx) throws Exception {
        NioSocketChannel nioSocketChannel = (NioSocketChannel) ctx.channel();
        Connection connection = new NettyConnection(nioSocketChannel);
        nioSocketChannel.attr(NettySocketHandler.ATTRIBUTE_CONNECTION).set(connection);
        nioSocketChannel.attr(NettyStrictCodecFactory.DECRYPTION_KEY).set(SocketUtil.copyDefaultKey());
        nioSocketChannel.attr(NettyStrictCodecFactory.ENCRYPTION_KEY).set(SocketUtil.copyDefaultKey());
    }

    /**
     *
     */
    @Override
    public void channelUnregistered(final ChannelHandlerContext ctx) throws Exception {
        closeClient(ctx);
    }

    /**
     *
     */
    @Override
    public void channelRead(final ChannelHandlerContext ctx, final Object message) throws Exception {
        Connection connection = getConnection(ctx);
        connection.process(message);
    }

    /**
     *
     */
    @Override
    public void channelReadComplete(final ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    /**
     *
     */
    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) throws Exception {
        LogUtil.error("NettySocketHandler的exceptionCaught方法", cause);
        closeClient(ctx);
    }

    /**
     * 获得connection
     */
    private Connection getConnection(final ChannelHandlerContext ctx) {
        return ctx.channel().attr(NettySocketHandler.ATTRIBUTE_CONNECTION).get();
    }

    /**
     * 关闭客户端
     */
    private void closeClient(ChannelHandlerContext ctx) {
        Connection conn = getConnection(ctx);
        conn.closeConnection();
        conn.notifyListeners(ConnectionEventType.CLOSE_CONNECTION, conn);
    }

}
