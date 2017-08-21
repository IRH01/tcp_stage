/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  2014-2-26  下午9:18:13
 */
package com.irh.tcp.core.manager.database;

/**
 * @author iritchie.ren
 */
public class BaseEntity implements Cloneable {

    /**
     * 操作
     */
    private EntityOperation operation = EntityOperation.NONE;

    /**
     *
     */
    public BaseEntity() {
    }

    /**
     * @return the operation
     */
    public EntityOperation getOperation() {
        return operation;
    }

    /**
     * @param operation the operation to set
     */
    public void setOperation(final EntityOperation operation) {
        if ((this.operation == EntityOperation.INSERT) && (operation == EntityOperation.UPDATE)) {
            return;
        }
        this.operation = operation;
    }

    /**
     * 同步数据是否使用批处理
     *
     * @return
     */
    public boolean isSyncDBUseBatch() {
        return true;
    }

    /**
     * 同步数据使用DAO
     */
    public void syncDBUseDAO() {

    }

    /**
     * 缓存同步到数据库.
     */
    public String getSyncToDBSQL() throws Exception {
        throw new UnsupportedOperationException(String.format("该[%s]Entitiy不支持这个方法!", this.getClass().getName()));
    }

    /**
     * @author iritchie.ren
     */
    public static enum EntityOperation {
        /**
         * 无
         */
        NONE,

        /**
         * INSERT
         */
        INSERT,

        /**
         * UPDATE
         */
        UPDATE,
    }

}
