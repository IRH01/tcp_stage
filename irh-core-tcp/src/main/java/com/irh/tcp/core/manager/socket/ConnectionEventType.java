/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  2014-2-20  下午8:39:31
 */
package com.irh.tcp.core.manager.socket;

import com.irh.tcp.core.event.IEventType;

/**
 * Connection事件
 *
 * @author iritchie.ren
 */
public enum ConnectionEventType implements IEventType {
    /**
     * 关闭连接
     */
    CLOSE_CONNECTION;
}