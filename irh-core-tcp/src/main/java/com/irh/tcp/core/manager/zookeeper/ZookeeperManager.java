/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  2014-2-20  下午2:56:59
 */
package com.irh.tcp.core.manager.zookeeper;

import com.irh.tcp.core.annotation.Manager;
import com.irh.tcp.core.config.ZookeeperConfig;
import com.irh.tcp.core.manager.IManager;
import com.irh.tcp.core.manager.ManagerType;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryOneTime;

/**
 * Zookeeper管理器
 *
 * @author iritchie.ren
 */
@Manager(type = ManagerType.ZOOKEEPER, desc = "Zookeeper管理器")
public final class ZookeeperManager implements IManager {

    /**
     * zookeeper权限采用的模式
     */
    public static final String AUTHINFO_SCHEME = "digest";

    /**
     * 根目录权限用户名密码
     */
    public static final String AUTHINFO_AUTH = "dreamway:dreamwayPwd";
    /**
     *
     */
    private static final ZookeeperManager INSTANCE = new ZookeeperManager();

    /**
     * 客户端
     */
    private CuratorFramework zkClient = null;

    /**
     *
     */
    private ZookeeperManager() {
    }

    /**
     * @return
     */
    public static ZookeeperManager getInstance() {
        return ZookeeperManager.INSTANCE;
    }

    @Override
    public void start() throws Exception {
        CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory
                .builder();
        builder.authorization(ZookeeperManager.AUTHINFO_SCHEME,
                ZookeeperManager.AUTHINFO_AUTH.getBytes()); //权限
        builder.defaultData(new byte[0]); //create等时候的默认值。
        builder.connectString(ZookeeperConfig.getInstance().getAddress()); //连接地址
        builder.retryPolicy(new RetryOneTime(10000)); //重试策略
        zkClient = builder.build();
        zkClient.start();
    }

    @Override
    public void stop() throws Exception {
        if (zkClient != null) {
            zkClient.close();
        }
    }

    /**
     * @return the zkClient
     */
    public CuratorFramework getZkClient() {
        return zkClient;
    }
}
