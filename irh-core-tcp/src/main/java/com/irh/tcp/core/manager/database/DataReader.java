/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  2014-2-20  下午2:56:44
 */
package com.irh.tcp.core.manager.database;

import java.sql.ResultSet;

/**
 * db查询结果集数据读取接口
 *
 * @param <T>
 * @author iritchie.ren
 */
public interface DataReader<T> {
    /**
     * 从查询结果集中读取一条记录并返回，本接口为泛型接口，如需读取的数据无明显类型特征（如抽取不规则字段集等），可直接使用Object类型进行读取
     *
     * @param rs
     * @return
     */
    T readData(ResultSet rs, Object... objects) throws Exception;

}
