/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  2014-2-25  上午11:15:51
 */
package com.irh.tcp.core.manager.socket;

import com.irh.core.util.LogUtil;
import com.irh.tcp.core.event.EventSource;
import com.irh.tcp.core.util.*;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * socket连接接口
 *
 * @author iritchie.ren
 */
public abstract class Connection extends EventSource {
    /**
     * socket服务器管理器
     */
    protected static SocketManager socketManager = SocketManager.getInstance();
    /**
     * cmd 线程池大小
     */
    private static final int ACTION_THREAD_COUNT = Runtime.getRuntime()
            .availableProcessors() * 2 + 1;

    /**
     * cmd 自驱动线程池
     */
    private static final ThreadPoolExecutor ACTION_THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(
            Connection.ACTION_THREAD_COUNT,
            Connection.ACTION_THREAD_COUNT, 20, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>(),
            new NamedThreadFactory("GAME-CONNECTION-ACTION-THREADS",
                    Thread.NORM_PRIORITY));

    /**
     * cmd 自驱动队列
     */
    protected SelfDrivenActionQueue<ISelfDrivenAction> actionQueue = new SelfDrivenActionQueue<>(
            ACTION_THREAD_POOL_EXECUTOR);

    /**
     * socket对应的玩家连接
     */
    private IConnectionWrap connectionWrap = new DefaultConnectionWrap(this);

    /**
     * 主机ip
     */
    private String hostaddress;

    /**
     * 端口号
     */
    private Integer port;

    /**
     * 消息的处理器
     */
    public Connection() {
    }

    /**
     *
     */
    public void clearCMD() {
        actionQueue.clear();
    }

    /**
     *
     */
    public void addAction(ISelfDrivenAction action) {
        actionQueue.add(action);
    }

    /**
     * 将指令压入自驱动队列
     *
     * @param packet
     */
    public void process(final Object packet) {
        try {
            Message msg = (Message) packet;
            byte type = msg.getType();
            short code = msg.getCode();
            int newCode = (type << 16) + code;
            ICommand<?> cmd = Connection.socketManager.getCommand(newCode);
            if (cmd != null) {
                //游戏服务器的处理
                actionQueue.add(new ExcuteCommand(cmd, msg,
                        this.getConnectionWrap()));
            } else {
                LogUtil.error("执行cmd错误：{}没有找到，{}。", msg.toString(), this.getConnectionWrap());
            }
        } catch (Exception e) {
            LogUtil.error("Connection process 报错。", e);
        }
    }

    /**
     * @param msg
     */
    public void rePost(Message msg) {

    }

    /**
     * 是否已连接上.
     */
    public abstract Boolean isConnected();

    /**
     * 是否断开.
     */
    public abstract Boolean isClosed();

    /**
     * 写消息给外部<br>
     * 抽象，由具体的类实现
     *
     * @param message
     */
    public abstract void sendMessage(Message message);

    /**
     * 断开连接
     */
    public abstract void closeConnection();

    /**
     * @return the player
     */
    public IConnectionWrap getConnectionWrap() {
        return connectionWrap;
    }

    /**
     * @param connectionWrap
     */
    public void setConnectionWrap(IConnectionWrap connectionWrap) {
        this.connectionWrap = connectionWrap;
    }

    /**
     * @return the hostname
     */
    public String getHostAddress() {
        return hostaddress;
    }

    /**
     * @param hostname the hostname to set
     */
    public void setHostAddress(final String hostname) {
        this.hostaddress = hostname;
    }

    /**
     * @return the port
     */
    public Integer getPort() {
        return port;
    }

    /**
     * @param port the port to set
     */
    public void setPort(final Integer port) {
        this.port = port;
    }

    @Override
    public <T extends EventSource> T getEventOwner() {
        return null;
    }

}
