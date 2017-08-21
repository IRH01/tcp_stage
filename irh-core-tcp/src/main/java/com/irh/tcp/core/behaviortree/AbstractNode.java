/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  May 17, 2014  12:31:03 PM
 */
package com.irh.tcp.core.behaviortree;

/**
 * 行为树节点抽象基类。
 *
 * @param <T> 类型.
 * @author iritchie.ren
 */
public abstract class AbstractNode<T> {
    /**
     * 节点名称
     */
    private String name;
    /**
     * 节点参数
     */
    private String params;

    /**
     * @param params
     */
    public AbstractNode(final String params) {
        this.params = params;
    }

    /**
     *
     */
    public AbstractNode(final String params, final String name) {
        this.name = name;
        this.params = params;
    }

    /**
     * @return the name
     */
    protected String getName() {
        return name;
    }

    /**
     * @return the params
     */
    protected String getParams() {
        return params;
    }

    /**
     * 条件验证
     */
    public abstract boolean checkConditions();

    /**
     * 执行节点
     */
    public abstract boolean doExecute(T t);
}
