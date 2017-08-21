/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  2014-3-27  下午2:31:50
 */
package com.irh.tcp.core.fsm;

/**
 * @param <T> 具体的对象类.
 * @author iritchie.ren
 */
public final class StateMachine<T extends IStateOwner> {
    /**
     * 拥有状态机的物体.
     */
    private T owner;

    /**
     * 当前状态.
     */
    private IState<T> currentState = null;

    /**
     * 前一个状态.
     */
    private IState<T> previousState = null;

    /**
     * 全局状态,每次状态执行都会执行的状态.
     */
    private IState<T> globalState = null;

    /**
     * @param owner        拥有状态机的物体.
     * @param currentState 当前状态.
     */
    public StateMachine(final T owner, final IState<T> currentState) {
        this.owner = owner;
        this.currentState = currentState;
    }

    /**
     * @param owner        拥有状态机的物体.
     * @param currentState 当前状态.
     * @param globalState  全局状态.
     */
    public StateMachine(final T owner, final IState<T> currentState, final IState<T> globalState) {
        this.owner = owner;
        this.currentState = currentState;
        this.globalState = globalState;
    }

    /**
     * @return the currentState
     */
    public IState<T> getCurrentState() {
        return currentState;
    }

    /**
     * @return the globalState
     */
    public IState<T> getGlobalState() {
        return globalState;
    }

    /**
     * @return the previousState
     */
    public IState<T> getPreviousState() {
        return previousState;
    }

    /**
     * 执行状态逻辑.
     */
    public void execute() throws Exception {
        if (globalState != null) {
            globalState.execute(owner);
        }
        if (currentState != null) {
            currentState.execute(owner);
        }
    }

    /**
     * 改变状态.
     */
    public void changeState(final IState<T> newState) throws Exception {
        //状态交换.
        if (newState != currentState) {
            previousState = currentState;
            currentState = newState;
            //当前状态先执行退出。新状态执行进入。
            previousState.exit(owner);
            currentState.enter(owner);
        }
    }

    /**
     * 改变状态并马上执行.
     */
    public void changeStateAndUpdate(final IState<T> newState) throws Exception {
        changeState(newState);
        execute();
    }

    /**
     * 切换到前一个状态.
     */
    public void revertToPrevious() throws Exception {
        changeState(previousState);
    }

    /**
     * 当前状态是否给定的状态。
     *
     * @throws Exception
     */
    public Boolean isCurrentState(final IState<T> state) throws Exception {
        return currentState == state;
    }
}
