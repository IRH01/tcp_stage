/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  2014-2-20  下午2:56:44
 */
package com.irh.tcp.core.manager.database;

import java.util.HashMap;
import java.util.Map;

/**
 * sql脚本参数的包装类
 *
 * @author iritchie.ren
 */
public class DBParamWrapper {
    /**
     * 参数
     */
    private Map<Integer, DBParameter> params = null;
    /**
     *
     */
    private int p = 0;

    /**
     *
     */
    public DBParamWrapper() {
        this.params = new HashMap<Integer, DBParameter>();
    }

    /**
     * 添加一个参数
     *
     * @param type 参数的类型
     * @param o    参数的值
     */
    public void put(final int type, final Object o) {
        params.put(++p, new DBParameter(type, o));
    }

    /**
     * 添加一个参数
     *
     * @param type 参数的类型
     * @param o    参数的值
     */
    public void put(final String parameterDirection, final int type, final Object o) {
        params.put(++p, new DBParameter(parameterDirection, type, o));
    }

    /**
     * @return
     */
    public Map<Integer, DBParameter> getParams() {
        return params;
    }

    /**
     * 重新初始化该sql参数包装类
     */
    public void clear() {
        params.clear();
        p = 0;
    }

    /**
     * @return the p
     */
    public int getP() {
        return p;
    }

    /**
     * @param p the p to set
     */
    public void setP(int p) {
        this.p = p;
    }

    /**
     * @param params the params to set
     */
    public void setParams(Map<Integer, DBParameter> params) {
        this.params = params;
    }

}
