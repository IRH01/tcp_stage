/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  2014-2-20  下午2:56:44
 */
package com.irh.tcp.core.manager.database;

import com.irh.tcp.core.annotation.Manager;
import com.irh.tcp.core.config.DatabaseConfig;
import com.irh.tcp.core.config.DatabaseConfig.DBDetailConfig;
import com.irh.tcp.core.manager.IManager;
import com.irh.tcp.core.manager.ManagerType;
import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;

import java.sql.SQLException;

/**
 * 数据库管理器.
 *
 * @author iritchie.ren
 */
@SuppressWarnings({"unchecked", "rawtypes"})
@Manager(type = ManagerType.DATABASE, desc = "数据库管理器")
public final class DataBaseManager implements IManager {
    /**
     *
     */
    private static final DataBaseManager INSTANCE = new DataBaseManager();
    /**
     * gameDB connection pool
     */
    private BoneCP gameDBCP = null;
    /**
     * baseDB connection pool
     */
    private BoneCP baseDBCP = null;
//    /**
//     * logDB connection pool
//     */
//    private BoneCP logDBCP = null;

    /**
     * 传入配置文件路径.
     */
    private DataBaseManager() {
    }

    /**
     * @return
     */
    public static DataBaseManager getInstance() {
        return DataBaseManager.INSTANCE;
    }

    /**
     *
     */
    @Override
    public void start() throws Exception {
        gameDBCP = new BoneCP(getBoneCPConfig(DatabaseType.GAME));
        baseDBCP = new BoneCP(getBoneCPConfig(DatabaseType.BASE));
    }

    /**
     *
     */
    @Override
    public void stop() throws Exception {
        if (gameDBCP != null) {
            gameDBCP.shutdown();
        }
        if (baseDBCP != null) {
            baseDBCP.shutdown();
        }
//        if (logDBCP != null) {
//            logDBCP.shutdown();
//        }
    }

    /**
     * 获取Base数据库的连接.
     *
     * @return
     */
    public BoneCP getGameDBPool() {
        return gameDBCP;
    }

    /**
     * 获取Base数据库的连接.
     *
     * @return
     */
    public BoneCP getBaseDBPool() {
        return baseDBCP;
    }

//    /**
//     * 获取Base数据库的连接.
//     * 
//     * @return
//     */
//    public BoneCP getLogDBPool() {
//        return logDBCP;
//    }

    /**
     * 根据前缀获得不同数据库的BonceCPConfig
     *
     * @throws SQLException
     */
    private BoneCPConfig getBoneCPConfig(final DatabaseType type)
            throws SQLException {
        DBDetailConfig dbDetailConfig = DatabaseConfig.getInstance()
                .getDBDetail(type);
        BoneCPConfig boneConf = new BoneCPConfig();
        boneConf.setJdbcUrl(dbDetailConfig.getUrl());
        boneConf.setUsername(dbDetailConfig.getUsername());
        boneConf.setPassword(dbDetailConfig.getPassword());
        boneConf.setPartitionCount(dbDetailConfig.getPartitionCount());
        boneConf.setMinConnectionsPerPartition(
                dbDetailConfig.getMinConnectionsPerPartition());
        boneConf.setMaxConnectionsPerPartition(
                dbDetailConfig.getMaxConnectionsPerPartition());
        boneConf.setAcquireIncrement(dbDetailConfig.getAcquireIncrement());
        boneConf.setLazyInit(false);
        boneConf.setDisableJMX(true);
        return boneConf;
    }
}
