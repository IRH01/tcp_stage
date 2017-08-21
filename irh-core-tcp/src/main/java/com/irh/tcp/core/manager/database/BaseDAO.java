/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  2014-2-26  下午5:41:37
 */
package com.irh.tcp.core.manager.database;

import com.irh.core.util.LogUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据库基础DAO，所有的DAO都应该是无状态的。
 *
 * @param <V>
 * @author iritchie.ren
 */
@SuppressWarnings("unchecked")
public abstract class BaseDAO<V extends BaseEntity> implements IGetData {
    /**
     * 数据库Helper
     */
    protected DBHelper dbhelper = null;

    /**
     *
     */
    public BaseDAO(final DBHelper dbhelper) {
        this.dbhelper = dbhelper;
    }

    /**
     * @return
     */
    protected DBHelper getDBHelper() {
        return dbhelper;
    }

    /**
     * 根据脚本执行更新
     *
     * @param sql          查询的脚本
     * @param paramWrapper 参数
     * @return
     */
    public boolean update(final String sql, final DBParamWrapper paramWrapper) {
        boolean result = false;
        result = dbhelper.executeNotSelect(sql, paramWrapper) > -1;
        return result;
    }

    /**
     * 根据脚本执行查询操作，不带参数
     *
     * @return
     */
    public V query(final String sql) {
        return query(sql, null);
    }

    /**
     * 根据脚本执行查询操作，带参数
     *
     * @param sql          查询的脚本
     * @param paramWrapper 参数
     * @return
     */
    public V query(final String sql, final DBParamWrapper paramWrapper) {
        V result = dbhelper.executeQuery(sql, paramWrapper, new DataReader<V>() {
            @Override
            public V readData(final ResultSet rs, final Object... objects) throws Exception {
                if (rs.last()) {
                    return BaseDAO.this.rsToEntity(rs);
                }
                return null;
            }
        });
        return result;
    }

    /**
     * 根据脚本执行查询操作,不带参数
     *
     * @param sql 查询的脚本
     * @return
     */
    public List<V> queryList(final String sql) {
        return queryList(sql, null);
    }

    /**
     * 根据脚本执行查询操作
     *
     * @param sql          sql 查询的脚本
     * @param paramWrapper 参数
     * @return 返回查询结果对象集合
     */
    public List<V> queryList(final String sql, final DBParamWrapper paramWrapper) {
        List<V> entitis = dbhelper.executeQuery(sql, paramWrapper, new DataReader<List<V>>() {
            @Override
            public List<V> readData(final ResultSet rs, final Object... objects) throws Exception {
                return BaseDAO.this.rsToEntityList(rs);
            }
        });
        return entitis;
    }

//    public <V extends BaseEntity> V get(long id) {
//        if (BaseConfig.getInstance().isCross()) {
//            CrossServerModule crossServer = ModuleManager.getInstance().getModule(ModuleType.CROSS_SERVER);
//            return (V) crossServer.getWsClient().getEntity(id);
//        } else {
//            return getSub(id);
//        }
//    }
//
//    /**
//     * @param id
//     * @return
//     */
//    protected abstract <V extends BaseEntity> V getSub(long id);

    /**
     * 根据脚本查询实体，返回key字段作为hash的Map
     *
     * @param sql          脚本
     * @param paramWrapper 参数
     * @param key          列名，它的值作为哈希的key值 如果key只有一个的时候，返回的泛型可以是任意类型，但是key传多个进行的时候，
     *                     返回的泛型只能是String
     * @return
     */
    public <K> Map<K, V> queryMap(final String sql, final DBParamWrapper paramWrapper, final Object... key) {
        Map<K, V> resultMap = dbhelper.executeQuery(sql, paramWrapper, new DataReader<Map<K, V>>() {
            @Override
            public Map<K, V> readData(final ResultSet rs, final Object... objects) throws Exception {
                Map<K, V> resultMap = new HashMap<K, V>();
                while (rs.next()) {
                    if (objects.length > 1) {
                        String hashKey = "";
                        for (Object string : objects) {
                            hashKey += rs.getObject((String) string) + "_";
                        }
                        hashKey = hashKey.substring(0, hashKey.length() - 1);
                        resultMap.put((K) hashKey, rsToEntity(rs));
                    } else if (objects.length == 1) {
                        resultMap.put((K) rs.getObject((String) objects[0]), rsToEntity(rs));
                    }
                }

                return resultMap;
            }
        }, key);

        return resultMap;
    }

    /**
     * @param procName
     * @return
     */
    public boolean runProcePrepared(final String procName) {
        return dbhelper.runProcePrepared(procName);
    }

    /**
     * 执行存储过程
     *
     * @param procName 存储过程名字
     * @return
     */
    public <K> K runProceReturnValue(final String procName, final DBParamWrapper params, final int returnIndex,
                                     final Class<K> clazz) {
        return dbhelper.runProceReturnValue(procName, params, returnIndex, clazz);
    }

    /**
     * 将ResultSet转换成List
     *
     * @param rs
     * @return
     */
    protected List<V> rsToEntityList(final ResultSet rs) {
        List<V> entitis = new ArrayList<V>();
        if (rs != null) {
            try {
                while (rs.next()) {
                    V entity = rsToEntity(rs);
                    entitis.add(entity);
                }
            } catch (Exception e) {
                LogUtil.error("Resultset转成实体出错", e);
            }
        }
        return entitis;
    }

    /**
     * 开始增加.
     */
    protected boolean beginAdd(final BaseEntity entity) {
        if (entity.getOperation() == BaseEntity.EntityOperation.INSERT) {
            entity.setOperation(BaseEntity.EntityOperation.NONE);
            return true;
        }
        return false;
    }

    /**
     * 提交增加.
     *
     * @param result
     */
    protected void commitAdd(final boolean result, final BaseEntity entity) {
        if (!result) {
            entity.setOperation(BaseEntity.EntityOperation.INSERT);
            LogUtil.error(String.format("添加【%s】出错了，状态还原：【%s】.", entity.getClass().getName(), BaseEntity.EntityOperation.INSERT));
        }
    }

    /**
     * 开始更新.
     *
     * @return
     */
    protected boolean beginUpdate(final BaseEntity entity) {
        if (entity.getOperation() == BaseEntity.EntityOperation.UPDATE) {
            entity.setOperation(BaseEntity.EntityOperation.NONE);
            return true;
        }
        return false;
    }

    /**
     * 提交更新.
     *
     * @param result
     */
    protected void commitUpdate(final boolean result, final BaseEntity entity) {
        if (!result) {
            entity.setOperation(BaseEntity.EntityOperation.UPDATE);
            LogUtil.error(String.format("更新【%s】出错了，状态还原：【%s】.", entity.getClass().getName(), BaseEntity.EntityOperation.UPDATE));
        }
    }

    /**
     * 将resultset转为实体对象
     *
     * @param rs
     * @return
     * @throws SQLException
     */
    protected abstract V rsToEntity(ResultSet rs) throws SQLException;

    @Override
    public Object get(long id) {
        return null;
    }

}
