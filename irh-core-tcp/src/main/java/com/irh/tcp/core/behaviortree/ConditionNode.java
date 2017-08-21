/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  May 17, 2014  12:31:03 PM
 */
package com.irh.tcp.core.behaviortree;

/**
 * 条件
 *
 * @param <T>
 * @author iritchie.ren
 */
public class ConditionNode<T> extends AbstractNode<T> {
    /**
     * @param params
     */
    public ConditionNode(String params) {
        super(params);

    }

    /**
     *
     */
    public boolean doExecute(T t) {
        return true;
    }

    @Override
    public boolean checkConditions() {
        return false;
    }
}
