/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  2014-2-21  下午3:47:16
 */
package com.irh.tcp.core.event;

/**
 * 事件监听器接口
 *
 * @author iritchie.ren
 */
public interface IEventListener {
    /**
     * 事件触发时的回调。
     *
     * @param event 事件。
     */
    void onEvent(Event event);
}
