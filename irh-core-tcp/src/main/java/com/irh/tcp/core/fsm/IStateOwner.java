/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  2014-3-27  下午3:05:35
 */
package com.irh.tcp.core.fsm;

/**
 * 状态拥有者标识接口类.
 *
 * @author iritchie.ren
 */
@SuppressWarnings({"rawtypes"})
public interface IStateOwner {
    /**
     * 执行状态机的execute方法
     */
    void execute() throws Exception;

    /**
     * @return the currentState
     */
    IState getCurrentState();

    /**
     * @return the globalState
     */
    IState getGlobalState();

    /**
     * @return the previousState
     */
    IState getPreviousState();

    /**
     * 改变状态.
     */
    void changeState(IState newState) throws Exception;

    /**
     * 改变状态并马上执行.
     */
    void changeStateAndUpdate(IState newState) throws Exception;

    /**
     * 切换到前一个状态.
     */
    void revertToPrevious() throws Exception;

    /**
     * 当前状态是否给定的状态。
     *
     * @throws Exception
     */
    Boolean isCurrentState(IState state) throws Exception;

    /**
     * 处理消息.
     *
     * @param fsmMessage
     */
    void handlerMessage(FsmMessage fsmMessage);
}
