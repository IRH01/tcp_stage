/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  May 17, 2014  12:31:03 PM
 */
package com.irh.tcp.core.behaviortree;

/**
 * 顺序复合节点【相当于and 子节点有一个返回FALSE，则停止迭代，并返回FALSE给父节点】。
 *
 * @param <T>
 * @author iritchie.ren
 */
public class SequenceNode<T> extends CompositeNode<T> {
    /**
     * @param params
     */
    public SequenceNode(final String params) {
        super(params);
    }

    /**
     * @param params
     * @param name
     */
    public SequenceNode(final String params, final String name) {
        super(params, name);
    }

    /**
     * 子节点执行成功（返回TRUE），寻找下一个子节点，继续迭代。
     */
    @Override
    public boolean childSucceed(final T t) {
        int curIndex = subNodes.indexOf(curNode);
        if (curIndex == (subNodes.size() - 1)) {
            return true;
        } else {
            curNode = subNodes.get(curIndex + 1);
            if (curNode.checkConditions()) {
                return curNode.doExecute(t);
            }

            return false;
        }
    }

    /**
     * 子节点执行失败（返回FALSE），停止迭代，并返回FALSE给父节点
     */
    @Override
    public boolean childFailed(final T t) {
        return false;
    }
}
