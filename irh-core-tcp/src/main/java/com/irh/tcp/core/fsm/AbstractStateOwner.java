/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  2014-3-31  上午10:03:14
 */
package com.irh.tcp.core.fsm;

/**
 * @author iritchie.ren
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class AbstractStateOwner implements IStateOwner {

    /**
     * 状态机.
     */
    private StateMachine stateMachine = null;

    /**
     * @param stateMachine the stateMachine to set
     */
    protected void setStateMachine(final StateMachine stateMachine) {
        this.stateMachine = stateMachine;
    }

    /**
     * 执行状态逻辑.
     */
    @Override
    public void execute() throws Exception {
        stateMachine.execute();
    }

    /**
     * @return the currentState
     */
    @Override
    public IState getCurrentState() {
        return stateMachine.getCurrentState();
    }

    /**
     * @return the globalState
     */
    @Override
    public IState getGlobalState() {
        return stateMachine.getGlobalState();
    }

    /**
     * @return the previousState
     */
    @Override
    public IState getPreviousState() {
        return stateMachine.getPreviousState();
    }

    /**
     * 改变状态.
     */
    @Override
    public void changeState(final IState newState) throws Exception {
        stateMachine.changeState(newState);
    }

    /**
     * 改变状态并马上执行.
     */
    @Override
    public void changeStateAndUpdate(final IState newState) throws Exception {
        stateMachine.changeStateAndUpdate(newState);
    }

    /**
     * 切换到前一个状态.
     */
    @Override
    public void revertToPrevious() throws Exception {
        stateMachine.revertToPrevious();
    }

    /**
     * 当前状态是否给定的状态。
     *
     * @throws Exception
     */
    @Override
    public Boolean isCurrentState(final IState state) throws Exception {
        return stateMachine.isCurrentState(state);
    }

    /**
     * 处理消息.
     *
     * @param fsmMessage
     */
    @Override
    public void handlerMessage(final FsmMessage fsmMessage) {

    }
}
