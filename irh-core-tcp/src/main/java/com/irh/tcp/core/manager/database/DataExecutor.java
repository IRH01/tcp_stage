/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  2014-2-20  下午2:56:44
 */
package com.irh.tcp.core.manager.database;

import java.sql.PreparedStatement;

/**
 * db执行statement接口
 *
 * @param <T>
 * @author jinjin.chen
 */
public interface DataExecutor<T> {
    /**
     * 该方法为泛型方法
     *
     * @param statement 需要执行的statement
     * @param objects   传入的参数集合
     * @return
     * @throws Exception
     */
    T execute(PreparedStatement statement, Object... objects) throws Exception;
}
