/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  2014-2-20  下午8:39:31
 */
package com.irh.tcp.core.manager.socket;

import com.irh.tcp.core.util.IConnectionWrap;

/**
 * Command接口
 *
 * @param <T> 泛型
 * @author iritchie.ren
 */
public interface ICommand<T extends IConnectionWrap> {

    /**
     * command的具体执行方法
     *
     * @param msg
     * @throws Exception
     */
    void excute(T connectionWrap, Message msg) throws Exception;

}
