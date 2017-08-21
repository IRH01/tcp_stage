/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  2014-3-31  上午10:48:35
 */
package com.irh.tcp.core.fsm;

/**
 * 消息分发器.
 *
 * @author iritchie.ren
 */
public abstract class FsmMessageDispatcher implements Runnable {

    /**
     * 发送消息.
     *
     * @param delay
     * @param sender
     * @param receiver
     * @param param
     * @throws Exception
     */
    public abstract void dispatchMessage(IFsmMessageType type, IStateOwner sender, IStateOwner receiver, Object param);

    /**
     * 发送延迟消息.
     *
     * @param delay
     * @param sender
     * @param receiver
     * @param param
     * @throws Exception
     */
    public abstract void dispatchMessage(Integer delay, IFsmMessageType type, IStateOwner sender, IStateOwner receiver, Object param);

}
