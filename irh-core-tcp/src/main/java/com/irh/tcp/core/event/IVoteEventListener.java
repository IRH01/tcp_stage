/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  2014年6月11日  下午5:36:10
 */
package com.irh.tcp.core.event;

/**
 * @author near
 */
public interface IVoteEventListener {
    /**
     * 事件触发时的回调。
     *
     * @param event 事件。
     */
    boolean onVoteEvent(Event event);
}
