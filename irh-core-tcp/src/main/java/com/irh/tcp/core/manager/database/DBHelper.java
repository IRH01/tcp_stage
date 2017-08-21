/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * <p>
 * <p>
 * All rights reserved.
 * Created on  2014-2-26  下午5:41:37
 */
package com.irh.tcp.core.manager.database;

import com.irh.core.util.LogUtil;
import com.irh.tcp.core.config.BaseConfig;
import com.jolbox.bonecp.BoneCP;

import java.sql.*;
import java.util.Collection;
import java.util.Map;

/**
 * @author iritchie.ren
 */
@SuppressWarnings({"unchecked"})
public final class DBHelper {

    /**
     * 池
     */
    private BoneCP pool = null;

    /**
     * databaseType 类型
     */
    private DatabaseType databaseType = null;

    /**
     * 获得DBHelp。
     *
     * @param databaseType
     * @return
     */
    public static DBHelper obtainDBHelper(final DatabaseType databaseType) {
        DBHelper dbHelper = new DBHelper();
        dbHelper.databaseType = databaseType;
        switch (databaseType) {
            case GAME:
                dbHelper.pool = DataBaseManager.getInstance().getGameDBPool();
                break;
            case BASE:
                dbHelper.pool = DataBaseManager.getInstance().getBaseDBPool();
                break;
//            case LOG:
//                dbHelper.pool = DataBaseManager.getInstance().getLogDBPool();
//                break;
            default:
                throw new RuntimeException("传入的dbHelperType无效");
        }
        return dbHelper;
    }

    /**
     * @param sql    执行的脚本
     * @param params 脚本参数
     * @return
     * @Action INSERT（此方法用于插入数据时返回自增ID值）
     */
    public <K> K executeInsertByGetKey(final String sql,
                                       final DBParamWrapper params, final Class<K> clazz) {
        Connection conn = getConnection();
        if (conn == null) {
            return null;
        }
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        K result = null;
        try {
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            prepareStatement(pstmt, getParams(params));
            pstmt.executeUpdate();
            resultSet = pstmt.getGeneratedKeys();
            if (resultSet.next()) {
                //mysql jdbc驱动在3.8.0.Object返回的整数都用Long表示
                if (clazz == int.class) {
                    result = (K) new Integer(resultSet.getInt(1));
                } else {
                    result = (K) new Long(resultSet.getLong(1));
                }
            }
            return result;
        } catch (SQLException e) {
            LogUtil.error(String.format("执行[%s]脚本出错", sql), e);
        } finally {
            closeConn(conn, pstmt);
        }
        return result;
    }

//    /**
//     * 给Statement赋值
//     * 
//     * @param pstmt
//     * @param parms
//     * @throws SQLException
//     */
//    private PreparedStatement prepareCommand(final PreparedStatement pstmt, final Map<Integer, DBParameter> parms) throws SQLException {
//        if (pstmt == null || parms == null) {
//            return null;
//        }
//        for (Map.Entry<Integer, DBParameter> entry : parms.entrySet()) {
//            pstmt.setObject(entry.getKey(), entry.getValue().getResult());
//        }
//
//        return pstmt;
//    }

    /**
     * @param sql    执行的脚本
     * @param params 脚本参数
     * @return
     * @Action UPDATE or DELETE or Insert
     */
    public int executeNotSelect(final String sql, final DBParamWrapper params) {
        int result = -1;
        Connection conn = getConnection();
        if (conn == null) {
            return result;
        }
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);
            prepareStatement(pstmt, getParams(params));
            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            LogUtil.error(String.format("执行[%s]脚本出错", sql), e);
        } finally {
            closeConn(conn, pstmt);
        }
        return result;
    }

    /**
     * 执行无参查询并返回单一记录
     *
     * @param sql    执行的脚本
     * @param reader 记录读取接口，实现单一记录读取过程
     * @return
     */
    public <T> T executeQuery(final String sql, final DataReader<T> reader) {
        return executeQuery(sql, null, reader);
    }

    /**
     * 执行查询并返回单一记录
     *
     * @param sql     执行的脚本
     * @param params  脚本参数
     * @param reader  记录读取接口，实现单一记录读取过程
     * @param objects 额外传递的参数
     * @return
     */
    public <T> T executeQuery(final String sql, final DBParamWrapper params,
                              final DataReader<T> reader, final Object... objects) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        T resultData = null;
        Connection conn = getConnection();
        if (conn != null) {
            try {
                pstmt = conn.prepareStatement(sql);
                prepareStatement(pstmt, getParams(params));
                rs = pstmt.executeQuery();
                resultData = reader.readData(rs, objects);
            } catch (Exception e) {
                LogUtil.error(String.format("执行[%s]脚本出错", sql), e);
            } finally {
                closeConn(conn, pstmt, rs);
            }
        }
        return resultData;
    }

    /**
     * 执行查询并返回单一记录
     *
     * @param sql
     *            执行的脚本
     * @param executor
     *            statement执行接口，实现单一记录读取过程
     * @return
     */
//    public <T> T executeQuery(final String sql, final DataExecutor<T> executor) {
//        return executeQuery(sql, null, executor);
//    }

    /**
     * 执行查询并返回单一记录
     *
     * @param sql
     *            执行的脚本
     * @param params
     *            脚本参数
     * @param executor
     *            statement执行接口，实现单一记录读取过程
     * @param objects
     *            额外传递的参数
     * @return
     */
//    public <T> T executeQuery(final String sql, final DBParamWrapper params, final DataExecutor<T> executor, final Object... objects) {
//        PreparedStatement pstmt = null;
//        T resultData = null;
//        Connection conn = getConnection();
//
//        if (conn != null) {
//            try {
//                pstmt = conn.prepareStatement(sql);
//                prepareStatement(pstmt, getParams(params));
//                resultData = executor.execute(pstmt, objects);
//            } catch (Exception e) {
//                LogUtil.error(String.format("执行[%s]脚本出错", sql), e);
//            } finally {
//                closeConn(conn, pstmt);
//            }
//        }
//
//        return resultData;
//    }

    /**
     *
     * @param sql
     *            执行批量处理的脚本
     * @param entities
     *            实体集合
     * @param executor
     *            回调
     * @return
     */
//    public <T, V> T sqlBatch(final String sql, final List<V> entities, final DataExecutor<T> executor) {
//        Connection conn = getConnection();
//        if (conn == null) {
//            return null;
//        }
//        PreparedStatement pstmt = null;
//        try {
//            conn.setAutoCommit(false);
//            pstmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
//            T result = executor.execute(pstmt, entities);
//            conn.commit();
//            return result;
//        } catch (Exception e) {
//            LogUtil.error(String.format("执行[%s]脚本出错", sql), e);
//        } finally {
//            closeConn(conn, pstmt);
//        }
//        return null;
//    }

    /**
     * @Action INSERT_Batch, UPDATE_Batch or DELETE_Batch
     * @param sqlComm
     *            执行的脚本
     * @return 返回影响行数
     */
//    public int sqlBatch(final List<String> sqlComm) {
//        int[] results = null;
//        int result = 0;
//        Connection conn = getConnection();
//
//        if (conn == null) {
//            return result;
//        }
//        Statement stmt = null;
//        try {
//            conn.setAutoCommit(false);
//            stmt = conn.createStatement();
//            for (int i = 0; i < sqlComm.size(); i++) {
//                stmt.addBatch(sqlComm.get(i));
//            }
//            results = stmt.executeBatch();
//            conn.commit();
//        } catch (SQLException e) {
//            LogUtil.error(String.format("执行[%s]脚本出错", sqlComm.toString()), e);
//        } finally {
//            closeConn(conn, stmt);
//        }
//        if (results != null) {
//            for (int result2 : results) {
//                result += result2;
//            }
//        }
//        return result;
//    }

    /**
     * 给Statement赋值
     *
     * @param pstmt
     * @param parms
     * @throws SQLException
     */
    private PreparedStatement prepareStatement(final PreparedStatement pstmt,
                                               final Map<Integer, DBParameter> parms) throws SQLException {
        if (pstmt == null || parms == null) {
            return null;
        }
        for (Map.Entry<Integer, DBParameter> entry : parms.entrySet()) {
            pstmt.setObject(entry.getKey(), entry.getValue().getResult());
        }

        return pstmt;
    }

    /**
     * 给Statement赋值
     *
     * @param pstmt
     * @param parms
     * @throws SQLException
     */
    private void prepareStatement(final CallableStatement pstmt,
                                  final Map<Integer, DBParameter> parms) throws SQLException {
        if (pstmt == null || parms == null) {
            return;
        }
        DBParameter param;
        for (Map.Entry<Integer, DBParameter> entry : parms.entrySet()) {
            param = entry.getValue();
            if ("OUT".equals(param.getDirection())) {
                pstmt.registerOutParameter(entry.getKey(), param.getDbtype());
            } else {
                pstmt.setObject(entry.getKey(), param.getResult(),
                        param.getDbtype());
            }
        }
    }

    /**
     * 获取连接池的连接
     *
     * @return
     */
    private Connection getConnection() {
        try {
            return pool.getConnection();
        } catch (SQLException e) {
            LogUtil.error(e);
        }
        return null;
    }

    /**
     * 获得参数
     *
     * @param paramWrapper
     * @return
     */
    private Map<Integer, DBParameter> getParams(
            final DBParamWrapper paramWrapper) {
        Map<Integer, DBParameter> params = null;
        if (paramWrapper != null) {
            params = paramWrapper.getParams();
        }
        return params;
    }

    /**
     * 关闭Connection、Statem和ResultSet
     *
     * @param conn
     * @param stmt
     * @param rs
     */
    private void closeConn(final Connection conn, final Statement stmt,
                           ResultSet rs) {
        try {
            if (rs == null || rs.isClosed()) {
                return;
            }
            rs.close();
            rs = null;
        } catch (SQLException e) {
            LogUtil.error("关闭Resultset出错", e);
        } finally {
            closeConn(conn, stmt);
        }

    }

    /**
     * 关闭Conne和Statement
     *
     * @param conn
     * @param stmt
     */
    private void closeConn(final Connection conn, Statement stmt) {
        try {
            if (stmt == null || stmt.isClosed()) {
                return;
            }
            if (stmt instanceof PreparedStatement) {
                ((PreparedStatement) stmt).clearParameters();
            }
            stmt.close();
            stmt = null;
        } catch (SQLException e) {
            LogUtil.error("关闭statement出错", e);
        } finally {
            closeConn(conn);
        }
    }

    /**
     * 关闭Connection
     *
     * @param conn
     */
    private void closeConn(Connection conn) {
        try {
            if (conn == null || conn.isClosed()) {
                return;
            }
            conn.setAutoCommit(true);
            conn.close();
            conn = null;
        } catch (SQLException e) {
            LogUtil.error("关闭数据库连接出错", e);
        }
    }

    /**
     * @param procName
     * @return
     */
    public boolean runProcePrepared(final String procName) {
        DBParamWrapper pars = new DBParamWrapper();
        runProce(procName, pars);
        return true;
    }

    /**
     * 执行存储过程 不返回值
     *
     * @param procName 存储过程名字
     * @return
     */
    public void runProce(final String procName, final DBParamWrapper params) {
        Connection conn = getConnection();
        if (conn == null) {
            return;
        }
        String sql = parseSql(procName, params.getParams().size());
        CallableStatement csmt = null;
        try {
            csmt = conn.prepareCall(sql);
            prepareStatement(csmt, getParams(params));
            csmt.execute();
        } catch (SQLException e) {
            LogUtil.error(String.format("执行[%s]存储过程出错", procName), e);
        } finally {
            closeConn(conn, csmt);
        }
    }

    /**
     * 执行存储过程 返回一个输出参数值
     *
     * @param procName 存储过程名字
     * @return
     */
    public <K> K runProceReturnValue(final String procName,
                                     final DBParamWrapper params, final int returnIndex,
                                     final Class<K> clazz) {
        Connection conn = getConnection();
        if (conn == null) {
            return null;
        }
        K result = null;
        String sql = parseSql(procName, params.getParams().size());
        CallableStatement csmt = null;
        try {
            csmt = conn.prepareCall(sql);
            prepareStatement(csmt, getParams(params));
            csmt.execute();
            result = (K) csmt.getObject(returnIndex);
        } catch (SQLException e) {
            LogUtil.error(String.format("执行[%s]存储过程出错", procName), e);
        } finally {
            closeConn(conn, csmt);
        }
        return result;
    }

    /**
     * @return the databaseType
     */
    public DatabaseType getDatabaseType() {
        return databaseType;
    }

    /**
     * 执行批处理
     *
     * @param saveDataToDBObj
     * @return
     */
    public void executeSQLBatchForSyncDB(Collection<BaseEntity> baseEntitys,
                                         ISaveDataToDB saveDataToDBObj) {
        if (!BaseConfig.getInstance().getUseBatch()) {
            // 非批处理
            for (BaseEntity baseEntity : baseEntitys) {
                if (baseEntity.getOperation() != BaseEntity.EntityOperation.NONE) {
                    baseEntity.syncDBUseDAO();
                }
            }
        } else {
            // 批处理
            int size = baseEntitys.size();
            Connection conn = null;
            Statement stmt = null;
            BaseEntity baseEntityTmp = null;
            BaseEntity.EntityOperation entityOperationTmp = null;
            try {
                if (size > 0) {
                    conn = getConnection();
                    int batchNum = BaseConfig.getInstance().getBatchNum();
                    stmt = conn.createStatement();
                    int i = 0;
                    conn.setAutoCommit(false);
                    String sql = null;
                    for (BaseEntity baseEntity : baseEntitys) {
                        sql = null;
                        baseEntityTmp = baseEntity;
                        entityOperationTmp = baseEntityTmp.getOperation();
                        try {
                            if (baseEntity.getOperation() == BaseEntity.EntityOperation.NONE) {
                                continue;
                            }
                            if (baseEntity.isSyncDBUseBatch()) {
                                sql = baseEntity.getSyncToDBSQL();
                                stmt.addBatch(sql);
                            } else {
                                baseEntity.syncDBUseDAO();
                            }
                            i++;
                        } catch (Exception e) {
                            try {
                                LogUtil.error(String.format(
                                        "批处理执行处理[%s]!!![%s]执行的操作是[%s]",
                                        sql,
                                        baseEntityTmp.getClass()
                                                .getName(),
                                        entityOperationTmp.toString()),
                                        e);
                            } catch (Exception e1) {
                                LogUtil.error(String.format(
                                        "调用[%s]的getSyncToDBSQL方法错误",
                                        baseEntity.getClass()
                                                .getName()),
                                        e1);
                            }
                        }
                        if (i > 0 && (i % batchNum == 0 || i == size)) {
                            stmt.executeBatch();
                        }
                    }
                    if (i > 0) {
                        conn.commit();
                    }
                }
            } catch (SQLException e) {
                LogUtil.error(String.format("[%s]同步数据错误.",
                        saveDataToDBObj.getClass().getName()), e);
            } finally {
                closeConn(conn, stmt);
            }
        }
    }

    /**
     * @param procName  存储过程名字
     * @param paraCount 参数个数
     * @return
     */
    private String parseSql(final String procName, final int paraCount) {
        int paramCount = paraCount; // 参数个数
        String params = ""; // 存放问号的字符串. 如?或?,?...
        // 设置问号字符串, 以逗号隔开
        for (int i = 0; i < paramCount; i++) {
            params += "?,";
        }

        if (paraCount > 0) {
            params = params.substring(0, params.length() - 1);
        }
        return "{call " + procName + "(" + params + ")}";
    }

}
