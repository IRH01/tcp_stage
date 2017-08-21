/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  2014-2-25  上午11:15:51
 */
package com.irh.tcp.core.manager.socket;

import com.irh.core.util.LogUtil;
import com.irh.tcp.core.util.IConnectionWrap;
import com.irh.tcp.core.util.ISelfDrivenAction;

/**
 * 执行命令线程
 *
 * @author iritchie.ren
 */
@SuppressWarnings("unchecked")
public class ExcuteCommand implements ISelfDrivenAction {

    /**
     * 执行体
     */
    protected ICommand<IConnectionWrap> command;

    /**
     * 消息体
     */
    protected Message message;

    /**
     * 连接
     */
    protected IConnectionWrap connection;

    /**
     * 创建新的执行指令
     *
     * @param command
     * @param message
     */
    public ExcuteCommand(final ICommand<?> command, final Message message,
                         final IConnectionWrap connection) {
        this.command = (ICommand<IConnectionWrap>) command;
        this.message = message;
        this.connection = connection;
    }

    @Override
    public void execute() {
        try {
            if (connection != null) {
                command.excute(connection, message);
            }
        } catch (ClassCastException e) {
            connection.closeConnection();
            LogUtil.error("connection:{}, Remote IP Address:{} ,Remite Port:{}, messge type:{}, command error code: {}",
                    connection.toString(), connection.getHostAddress(), connection.getPort(),
                    message.getType(), message.getCode(), e);
        } catch (Exception e) {
            LogUtil.error("connection:{}, Remote IP Address:{} ,Remite Port:{}, messge type:{}, command error code: {}",
                    connection.toString(), connection.getHostAddress(), connection.getPort(),
                    message.getType(), message.getCode(), e);
        }
    }

    @Override
    public String toString() {
        return command.getClass().getName() + ", message : "
                + message.toString();
    }
}
