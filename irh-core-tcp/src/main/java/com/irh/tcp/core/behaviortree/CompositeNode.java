/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  May 17, 2014  12:31:03 PM
 */
package com.irh.tcp.core.behaviortree;

import java.util.ArrayList;
import java.util.List;

/**
 * 行为树复合节点.
 *
 * @param <T> 类型。
 * @author iritchie.ren
 */
public abstract class CompositeNode<T> extends AbstractNode<T> {
    /**
     * 当前节点
     */
    protected AbstractNode<T> curNode;
    /**
     * 子节点
     */
    protected List<AbstractNode<T>> subNodes = new ArrayList<AbstractNode<T>>();

    /**
     * @param params
     */
    public CompositeNode(final String params) {
        super(params);
    }

    /**
     * @param params
     * @param name
     */
    public CompositeNode(final String params, final String name) {
        super(params, name);
    }

    /**
     * 添加节点
     *
     * @param node
     */
    public void add(final AbstractNode<T> node) {
        subNodes.add(node);
    }

    @Override
    public boolean checkConditions() {
        return subNodes.size() > 0;
    }

    @Override
    public boolean doExecute(final T t) {
        boolean result = false;

        curNode = subNodes.get(0);
        if (curNode.doExecute(t)) {
            result = this.childSucceed(t);
        } else {
            result = this.childFailed(t);
        }

        return result;
    }

    /**
     * 子节点是否成功。
     *
     * @param t
     * @return
     */
    public abstract boolean childSucceed(T t);

    /**
     * 子节点是否失败。
     *
     * @param t
     * @return
     */
    public abstract boolean childFailed(T t);

    /**
     * @return the curNode
     */
    protected AbstractNode<T> getCurNode() {
        return curNode;
    }

    /**
     * @return the subNodes
     */
    protected List<AbstractNode<T>> getSubNodes() {
        return subNodes;
    }
}
