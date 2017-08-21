/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  2014-2-25  上午11:26:21
 */
package com.irh.tcp.core.manager.socket;

import com.irh.core.util.ClassUtil;
import com.irh.core.util.LogUtil;
import com.irh.tcp.core.annotation.Manager;
import com.irh.tcp.core.annotation.SocketCommand;
import com.irh.tcp.core.config.BaseConfig;
import com.irh.tcp.core.manager.IManager;
import com.irh.tcp.core.manager.ManagerType;
import com.irh.tcp.core.manager.socket.mina.MinaSocketAcceptor;
import com.irh.tcp.core.manager.socket.netty.NettySocketAcceptor;
import com.irh.tcp.core.manager.socket.xsocket.XsocketSocketAcceptor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 抽象的服务器类<br>
 * 由底层的具体实现
 *
 * @author iritchie.ren
 */
@SuppressWarnings({"rawtypes"})
@Manager(type = ManagerType.SOCKET, desc = "Socket管理器", dependences = {
        ManagerType.ALL})
public final class SocketManager implements IManager {
    /**
     * 单例
     */
    private static final SocketManager INSTANCE = new SocketManager();

    /**
     * 游戏Command
     */
    private Map<Integer, ICommand> commands = new HashMap<>();

    /**
     * Socket控制器
     */
    private SocketAcceptor socketAcceptor = null;

    /**
     * 私有化公共方法，不让初始化 
     */
    private SocketManager() {
    }

    /**
     * @return
     */
    public static SocketManager getInstance() {
        return SocketManager.INSTANCE;
    }

    /**
     * 开始
     */
    @Override
    public void start() throws Exception {
        //初始化内部模块指令
        initInternalCommand();
        //初始化socket
        initSocketAcceptor();
    }

    /**
     * 停止
     */
    @Override
    public void stop() throws Exception {
        if (socketAcceptor != null) {
            socketAcceptor.stop();
        }
    }

    /**
     * 返回对应的执行命令
     *
     * @param code
     * @return
     */
    public ICommand getCommand(final int code) {
        return this.commands.get(code);
    }

    /**
     * 初始化模块指令
     *
     * @throws Exception
     */
    private void initInternalCommand() throws Exception {
        SocketCommand socketCommandAnnotation = null;
        short code = 0;
        byte type = 0;
        ICommand gameCommand = null;
        //加载class文件
        List<Class<ICommand>> classes = ClassUtil.getClassesByInterface(
                BaseConfig.getInstance().getScanPackage(),
                ICommand.class);
        for (Class<?> clazz : classes) {
            socketCommandAnnotation = clazz.getAnnotation(SocketCommand.class);
            if (socketCommandAnnotation != null) {
                code = socketCommandAnnotation.code();
                type = socketCommandAnnotation.type();
                gameCommand = (ICommand) clazz.newInstance();
                commands.put(((type << 16) + code), gameCommand);
            }
        }

    }

    /**
     * 初始化socketHandler
     *
     * @return
     * @throws Exception
     */
    private void initSocketAcceptor() throws Exception {
        String socketType = BaseConfig.getInstance().getSocketType();
        switch (socketType) {
            case "mina":
                socketAcceptor = new MinaSocketAcceptor();
                break;
            case "netty":
                socketAcceptor = new NettySocketAcceptor();
                break;
            case "xsocket":
                socketAcceptor = new XsocketSocketAcceptor();
                break;
            default:
                throw new IllegalArgumentException(
                        String.format("SocketType不属于mina,netty,socket任何一种,SocketType为：%s",
                                socketType));
        }
        //启动
        socketAcceptor.start();
        String msg = String.format("使用【%s】socket架构【%s】。", socketType,
                socketAcceptor.getClass().getName());
        LogUtil.info(msg);
        if (!BaseConfig.getInstance().getCiphertext()) {
            LogUtil.warn("注意:当前使用的是明文编码!");
        }
    }
}
