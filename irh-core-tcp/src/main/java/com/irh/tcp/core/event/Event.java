/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  2014-2-21  下午3:47:16
 */

package com.irh.tcp.core.event;

/**
 * 事件
 *
 * @author iritchie.ren
 */
@SuppressWarnings("unchecked")
public class Event {

    /**
     * 事件类型
     */
    private IEventType eventType;

    /**
     * 自定义参数
     */
    private Object data;

    /**
     * 触发器参数(如果事件会或发触发器的话，有可能会设置此参数)
     */
    private String triParams;

    /**
     * 事件源
     */
    private EventSource eventSource;

    /**
     * 当前事件源
     */
    private EventSource currentEventSource;

    /**
     * 是否链
     */
    private boolean chain = true;

    /**
     * 事件构造器
     *
     * @param eventSource 事件源
     * @param eventType   事件类型
     * @param data        自定义参数
     */
    public Event(final EventSource eventSource, final IEventType eventType,
                 final Object data, final boolean enableChain) {
        this.eventSource = eventSource;
        this.eventType = eventType;
        this.data = data;
        this.currentEventSource = eventSource;
        this.chain = enableChain;
    }

    /**
     * 获取事件类型
     *
     * @return 事件类型
     */
    public IEventType getEventType() {
        return eventType;
    }

    /**
     * 获取自定义参数
     *
     * @return 自定义参数
     */
    public Object getData() {
        return data;
    }

    /**
     * @return the cuurrentEventSource
     */
    public EventSource getCurrentEventSource() {
        return currentEventSource;
    }

    /**
     * 获得当前事件源
     *
     * @param currentEventSource the cuurrentEventSource to set
     */
    public void setCurrentEventSource(EventSource currentEventSource) {
        this.currentEventSource = currentEventSource;
    }

    /**
     * @param data
     */
    public void setData(final Object data) {
        this.data = data;
    }

    /**
     * @return the triParams
     */
    public String getTriParams() {
        return triParams;
    }

    /**
     * 原始事件源
     *
     * @return the eventSource
     */
    public <T extends EventSource> T getOriginalEventSource() {
        return (T) eventSource;
    }

    /**
     * @param triParams the triParams to set
     */
    public void setTriParams(final String triParams) {
        this.triParams = triParams;
    }

    /**
     * @return the chain
     */
    public boolean isChain() {
        return chain;
    }

    /**
     * @param chain the chain to set
     */
    public void setChain(boolean chain) {
        this.chain = chain;
    }

}
