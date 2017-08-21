/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  2014-2-20  下午2:56:44
 */
package com.irh.tcp.core.manager.database;

/**
 * DB参数
 *
 * @author iritchie.ren
 */
public class DBParameter {
    /**
     *
     */
    private String direction;
    /**
     *
     */
    private int dbtype;
    /**
     *
     */
    private Object info;

    /**
     *
     */
    public DBParameter() {

    }

    /**
     * 设置存储过程访问参数
     *
     * @param parameterDirection 设置参数传入、传出,指定范围:ParameterDirection.Input;ParameterDirection. Output ; ParameterDirection.InputOutput
     * @param types              设置参数数据类型，指定范围：Types.INTEGER;Types.VARCHAR;Types.DATE;
     * @param info               设置参数值，类型为输入时，不能为空；
     */
    public DBParameter(final String parameterDirection, final int types, final Object info) {
        this.direction = parameterDirection;
        this.dbtype = types;
        this.info = info;
    }

    /**
     * @param types 设置参数数据类型，指定范围：Types.INTEGER;Types.VARCHAR;Types.DATE;
     * @param info  设置参数值，类型为输入时，不能为空；
     */
    public DBParameter(final int types, final Object info) {
        this.dbtype = types;
        this.info = info;
    }

    /**
     * 设置存储过程访问参数（限为输出类型）
     *
     * @param parameterDirection 设置参数传入、传出,指定范围:ParameterDirection.Input;ParameterDirection. Output ;ParameterDirection.InputOutput
     * @param types              设置参数数据类型，指定范围：Types.INTEGER;Types.VARCHAR;Types.DATE;
     */
    public DBParameter(final String parameterDirection, final int types) {
        this.direction = parameterDirection;
        this.dbtype = types;
    }

    /**
     * @return
     */
    public String getDirection() {
        return direction;
    }

    /**
     * @return
     */
    public int getDbtype() {
        return dbtype;
    }

    /**
     * @return
     */
    public Object getResult() {
        return info;
    }

    /**
     * @return
     */
    public void setResult(final Object result) {
        this.info = result;
    }

    /**
     * @return the info
     */
    public Object getInfo() {
        return info;
    }

    /**
     * @param info the info to set
     */
    public void setInfo(Object info) {
        this.info = info;
    }

    /**
     * @param direction the direction to set
     */
    public void setDirection(String direction) {
        this.direction = direction;
    }

    /**
     * @param dbtype the dbtype to set
     */
    public void setDbtype(int dbtype) {
        this.dbtype = dbtype;
    }

}
