/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  2014-2-20  下午2:55:39
 */
package com.irh.tcp.core.manager.socket.netty;

import com.irh.tcp.core.config.BaseConfig;
import com.irh.tcp.core.manager.socket.Message;
import com.irh.tcp.core.manager.socket.SocketAcceptor;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * MINA接收器.
 *
 * @author iritchie.ren
 */
public final class NettySocketAcceptor extends SocketAcceptor {
    /**
     * 接收器
     */
    private ServerBootstrap serverBootstrap = null;

    /**
     *
     */
    @Override
    protected void start() throws Exception {
        boolean isCiphertext = BaseConfig.getInstance().getCiphertext();
        final MessageToByteEncoder<Message> encoder = NettyStrictCodecFactory
                .getEncoder(isCiphertext);
        final ByteToMessageDecoder decoder = NettyStrictCodecFactory
                .getDecoder(isCiphertext);
        serverBootstrap = new ServerBootstrap();
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup(
                Runtime.getRuntime().availableProcessors() * 2 + 1);
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(final SocketChannel ch)
                            throws Exception {
                        ch.pipeline().addLast("encoder", encoder);
                        ch.pipeline().addLast("decoder", decoder);
                        ch.pipeline().addLast("handler",
                                new NettySocketHandler());
                    }
                });
        serverBootstrap.option(ChannelOption.SO_RCVBUF, 1024 * 64);
        serverBootstrap.option(ChannelOption.SO_LINGER, 0);
        serverBootstrap.option(ChannelOption.SO_REUSEADDR, true);
        serverBootstrap.childOption(ChannelOption.SO_RCVBUF, 1024 * 64);
        serverBootstrap.childOption(ChannelOption.SO_LINGER, 0);
        serverBootstrap.childOption(ChannelOption.TCP_NODELAY, true);
        serverBootstrap.bind(BaseConfig.getInstance().getPort()).sync();

    }

    /**
     * 停止
     */
    @Override
    protected void stop() throws Exception {
        if (serverBootstrap != null) {
            serverBootstrap.group().shutdownGracefully();
        }
    }
}
