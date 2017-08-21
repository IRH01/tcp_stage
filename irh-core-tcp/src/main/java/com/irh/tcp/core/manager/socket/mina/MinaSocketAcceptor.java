/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  2014-2-20  下午2:55:39
 */
package com.irh.tcp.core.manager.socket.mina;

import com.irh.tcp.core.config.BaseConfig;
import com.irh.tcp.core.manager.socket.Connection;
import com.irh.tcp.core.manager.socket.SocketAcceptor;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * MINA接收器.
 *
 * @author iritchie.ren
 */
public final class MinaSocketAcceptor extends SocketAcceptor {

    /**
     * 接收器
     */
    private NioSocketAcceptor ioAcceptor = null;

    /**
     * 启动socket
     */
    @Override
    protected void start() throws Exception {

        ioAcceptor = new NioSocketAcceptor(
                Runtime.getRuntime().availableProcessors() * 2 + 1);

        //以下config设置是给连接的每个session设置的配置
        SocketSessionConfig config = ioAcceptor.getSessionConfig();
        //设置读缓冲区大小  - tcp层
        config.setReceiveBufferSize(1024 * 64);
        //设置写缓冲区大小 - tcp层
        config.setSendBufferSize(1024 * 8);

        //设置mina每次读的字节流缓冲区大小. 不需要设置
        //config.setReadBufferSize(2 * 1024 * 1024); 

        //acceptor.getSessionConfig().setIdleTime(IdleStatus.WRITER_IDLE, 10); //写 通道在10 秒内无任何操作就进入空闲状态  
        //acceptor.getSessionConfig().setIdleTime(IdleStatus.READER_IDLE, 10); //读 通道在10 秒内无任何操作就进入空闲状态  
        //acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);   //读写 通道均在10 秒内无任何操作就进入空闲状
        //config.setIdleTime(IdleStatus.BOTH_IDLE, 70);
        //0秒
        //SO_LINGER选项用来控制Socket关闭时的行为，默认情况下，执行Socket的close方法，该方法会立即返回，
        //但底层的Socket实际上并不会立即关闭，他会立即延迟一段时间，知道发送完剩余的数据，才会真正的关闭Socket，断开连接

        config.setSoLinger(0);

        config.setKeepAlive(true);
        config.setReuseAddress(true);

        //在默认情况下，客户端向服务器发送数据时，会根据数据包的大小决定是否立即发送。当数据包中的数据很少
        //时，如只有1个字节，而数据包的头却有几十个字节（IP头+TCP头）时，系统会在发送之前先将较小的包合并到软
        //大的包后，一起将数据发送出去。在发送下一个数据包时，系统会等待服务器对前一个数据包的响应，当收到服务
        //器的响应后，再发送下一个数据包，这就是所谓的Nagle算法；在默认情况下，Nagle算法是开启的。
        //这种算法虽然可以有效地改善网络传输的效率，但对于网络速度比较慢，而且对实现性的要求比较高的情况下
        //（如游戏、Telnet等），使用这种方式传输数据会使得客户端有明显的停顿现象。因此，最好的解决方案就是需要
        //Nagle算法时就使用它，不需要时就关闭它。而使用setTcpToDelay正好可以满足这个需求。当使用
        //setTcpNoDelay（true）将Nagle算法关闭后，客户端每发送一次数据，无论数据包的大小都会将这些数据发送出去。
        config.setTcpNoDelay(true);

        //解码handler
        ioAcceptor.getFilterChain().addLast("codec",
                new ProtocolCodecFilter(new MinaStrictCodecFactory(
                        BaseConfig.getInstance()
                                .getCiphertext())));
        //设置心跳频率

        //KeepAliveFilter heartBeat = new KeepAliveFilter(new MinaKeepAliveMessageFactoryImpl(), IdleStatus.BOTH_IDLE);
        ////设置是否forward到下一个filter
        //heartBeat.setForwardEvent(false);
        //heartBeat.setRequestInterval(30);
        //ioAcceptor.getFilterChain().addLast("heartbeat", heartBeat);

        //Executor threadPool = Executors.newFixedThreadPool(1);

        //ioAcceptor.getFilterChain().addLast("execute", new ExecutorFilter(threadPool));

//        ioAcceptor.getFilterChain().addLast("executor", new ExecutorFilter(Executors.newSingleThreadExecutor(new ThreadFactory() {
//            public Thread newThread(Runnable r) {
//                Thread thread = new Thread(r, "mina" + "-" + "getPort()" + "-thread");
//                return thread;
//            }
//        })));

        ioAcceptor.setHandler(new MinaSocketHandler());
        ioAcceptor.setReuseAddress(true);
        ioAcceptor.bind(new InetSocketAddress(
                BaseConfig.getInstance().getPort()));
    }

    /**
     * 停止
     */
    @Override
    protected void stop() throws Exception {
        if (ioAcceptor != null) {
            ioAcceptor.unbind();
        }
    }

    /* (non-Javadoc)
     * @see com.dreamway.zymj.core.manager.socket.SocketAcceptor#getAllConnections()
     */
    @Override
    protected List<Connection> getAllConnections() {
        List<Connection> connections = new java.util.ArrayList<>();
        for (IoSession session : ioAcceptor.getManagedSessions().values()) {
            connections.add((Connection) session
                    .getAttribute("ATTRIBUTE_CONNECTION"));
        }
        return connections;
    }
}
