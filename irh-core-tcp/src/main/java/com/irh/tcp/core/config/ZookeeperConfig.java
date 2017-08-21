/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  May 17, 2014  11:12:56 AM
 */
package com.irh.tcp.core.config;

/**
 * Zookeeper配置
 *
 * @author iritchie.ren
 */
public final class ZookeeperConfig implements IConfig {

    /**
     *
     */
    private static final ZookeeperConfig INSTACE = new ZookeeperConfig();

    /**
     * 地址
     */
    private String address;

    /**
     *
     */
    private ZookeeperConfig() {
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    @Override
    public void parse() throws Exception {
        this.address = ConfigFileUtil.getElmtString("//zookeeper/address");
    }

    /**
     * @return
     */
    public static ZookeeperConfig getInstance() {
        return ZookeeperConfig.INSTACE;
    }
}
