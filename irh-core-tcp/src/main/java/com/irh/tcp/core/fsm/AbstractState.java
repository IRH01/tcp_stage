/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  2014-3-27  下午5:53:55
 */
package com.irh.tcp.core.fsm;

/**
 * @param <T> 状态抽象类.
 * @author iritchie.ren
 */
public abstract class AbstractState<T> implements IState<T> {

    /**
     *
     */
    @Override
    public void enter(final T owner) throws Exception {
    }

    /**
     *
     */
    @Override
    public void exit(final T owner) throws Exception {
    }

    /**
     *
     */
    @Override
    public abstract void execute(T owner) throws Exception;

}
