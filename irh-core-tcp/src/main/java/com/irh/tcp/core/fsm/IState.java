/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  2014-3-27  下午2:30:46
 */
package com.irh.tcp.core.fsm;

/**
 * 有限状态机的状态接口，实现类应该是无状态单例的。(注意：由于每个物体有很多状态，而且游戏里面有大量的物体（怪物，NPC，玩家等）， 所以为了避免内存扩大，实现此状态接口的类，应该是单例的。)
 *
 * @param <T> 具体的对象类.
 * @author iritchie.ren
 */
public interface IState<T> {
    /**
     * 进入
     *
     * @param t
     */
    void enter(T owner) throws Exception;

    /**
     * 执行
     *
     * @param t
     */
    void execute(T owner) throws Exception;

    /**
     * 退出
     *
     * @param t
     */
    void exit(T owner) throws Exception;
}
