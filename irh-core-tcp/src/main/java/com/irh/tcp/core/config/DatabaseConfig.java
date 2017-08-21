/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  May 17, 2014  11:12:56 AM
 */
package com.irh.tcp.core.config;

import com.irh.tcp.core.manager.database.DatabaseType;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据库配置
 *
 * @author iritchie.ren
 */
public final class DatabaseConfig implements IConfig {

    /**
     *
     */
    private static final DatabaseConfig INSTACE = new DatabaseConfig();

    /**
     *
     */
    private Map<DatabaseType, DBDetailConfig> dbDetails = new HashMap<>();

    /**
     *
     */
    private DatabaseConfig() {
    }

    /**
     * @return
     */
    public static DatabaseConfig getInstance() {
        return DatabaseConfig.INSTACE;
    }

    /**
     * 获得DBDetailConfig
     *
     * @param type
     * @return
     */
    public DBDetailConfig getDBDetail(final DatabaseType type) {
        return dbDetails.get(type);
    }

    @Override
    public void parse() {
        dbDetails.put(DatabaseType.GAME, parseConfig("gameDB"));
        dbDetails.put(DatabaseType.BASE, parseConfig("gameDB"));
    }

    /**
     * 根据前缀获得不同数据库的BonceCPConfig
     *
     * @param prefix
     * @param databaseCfg
     */
    private DBDetailConfig parseConfig(final String prefix) {
        String url = ConfigFileUtil.getElmtString(String.format("//%s/url", prefix));
        String username = ConfigFileUtil.getElmtString(String.format("//%s/username", prefix));
        String password = ConfigFileUtil.getElmtString(String.format("//%s/password", prefix));
        Integer partitionCount = ConfigFileUtil.getElmtInteger(String.format("//%s/partitionCount", prefix));
        Integer minConnectionsPerPartition = ConfigFileUtil.getElmtInteger(String.format("//%s/minConnectionsPerPartition", prefix));
        Integer maxConnectionsPerPartition = ConfigFileUtil.getElmtInteger(String.format("//%s/maxConnectionsPerPartition", prefix));
        Integer acquireIncrement = ConfigFileUtil.getElmtInteger(String.format("//%s/acquireIncrement", prefix));
        DBDetailConfig dBDetailConfig = new DBDetailConfig();
        dBDetailConfig.url = url;
        dBDetailConfig.username = username;
        dBDetailConfig.password = password;
        dBDetailConfig.partitionCount = partitionCount;
        dBDetailConfig.minConnectionsPerPartition = minConnectionsPerPartition;
        dBDetailConfig.maxConnectionsPerPartition = maxConnectionsPerPartition;
        dBDetailConfig.acquireIncrement = acquireIncrement;
        return dBDetailConfig;
    }

    /**
     * @author iritchie.ren
     */
    public static class DBDetailConfig {
        /**
         * url
         */
        private String url;
        /**
         * 用户名
         */
        private String username;
        /**
         * 密码
         */
        private String password;
        /**
         * 分区数
         */
        private Integer partitionCount;
        /**
         * 每个分区最小连接数
         */
        private Integer minConnectionsPerPartition;
        /**
         * 每个分区最大连接数
         */
        private Integer maxConnectionsPerPartition;
        /**
         * 分区连接增长数
         */
        private Integer acquireIncrement;

        /**
         * @return the url
         */
        public String getUrl() {
            return url;
        }

        /**
         * @return the username
         */
        public String getUsername() {
            return username;
        }

        /**
         * @return the password
         */
        public String getPassword() {
            return password;
        }

        /**
         * @return the partitionCount
         */
        public int getPartitionCount() {
            return partitionCount;
        }

        /**
         * @return the minConnectionsPerPartition
         */
        public int getMinConnectionsPerPartition() {
            return minConnectionsPerPartition;
        }

        /**
         * @return the maxConnectionsPerPartition
         */
        public int getMaxConnectionsPerPartition() {
            return maxConnectionsPerPartition;
        }

        /**
         * @return the acquireIncrement
         */
        public int getAcquireIncrement() {
            return acquireIncrement;
        }

    }

}
