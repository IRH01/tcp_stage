/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  May 17, 2014  12:31:03 PM
 */
package com.irh.tcp.core.behaviortree;

/**
 * 行为树选择节点 【相当于or 子节点返回TRUE，则停止迭代，并返回TRUE给父节点】。
 *
 * @param <T>
 * @author iritchie.ren
 */
public class SelectorNode<T> extends CompositeNode<T> {
    /**
     * @param params
     */
    public SelectorNode(final String params) {
        super(params);
    }

    /**
     * @param params
     * @param name
     */
    public SelectorNode(final String params, final String name) {
        super(params, name);
    }

    /**
     * 子节点执行成功（返回TRUE），停止迭代，并返回TRUE给父节点
     */
    @Override
    public boolean childSucceed(final T t) {
        return true;
    }

    /**
     * 子节点执行失败（返回FALSE），寻找下一个子节点继续迭代
     */
    @Override
    public boolean childFailed(final T t) {
        curNode = chooseNextNode();
        if (curNode != null) {
            return doExecute(t);
        } else {
            return false;
        }
    }

    /**
     * @return
     */
    private AbstractNode<T> chooseNextNode() {
        AbstractNode<T> node = null;
        boolean isFound = false;
        int curIndex = subNodes.indexOf(curNode);

        while (!isFound) {
            if (curIndex == (subNodes.size() - 1)) {
                node = null;
                break;
            }
            curIndex++;
            node = subNodes.get(curIndex);
            if (node.checkConditions()) {
                isFound = true;
            }
        }
        return node;
    }
}
