/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  May 17, 2014  11:12:56 AM
 */
package com.irh.tcp.core.config;

import com.irh.core.util.FileUtil;
import com.irh.tcp.core.util.ProjectPathUtil;

import java.io.File;

/**
 * 基础配置
 *
 * @author iritchie.ren
 */
public final class BaseConfig implements IConfig {

    /**
     *
     */
    private static final BaseConfig INSTACE = new BaseConfig();

    /**
     * 游戏服务器端口
     */
    private int port;

    /**
     * 游戏服务器端口号
     */
    private int webPort;

    /**
     * socket类型
     */
    private String socketType = "";
    /**
     * 是否采用密文
     */
    private boolean ciphertext = true;

    /**
     * 心跳时间
     */
    private int heartbeat = 0;

    /**
     * command执行时间，超过这个时间记录人日志 毫秒
     */
    private int actionRuntime = 16;

    /**
     * 是否使用批处理
     */
    private boolean useBatch = false;

    /**
     * 批处理数量
     */
    private int batchNum = 0;

    /**
     * 版本号
     */
    private String version;

    /**
     * 包扫描
     */
    private String scanPackage;

    /**
     *
     */
    private BaseConfig() {
    }

    /**
     * @return
     */
    public static BaseConfig getInstance() {
        return INSTACE;
    }

    /**
     * @return the port
     */
    public Integer getPort() {
        return port;
    }

    /**
     * @return the webPort
     */
    public Integer getWebPort() {
        return webPort;
    }

    /**
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * @return the socketType
     */
    public String getSocketType() {
        return socketType;
    }

    /**
     * @return the heartbeat
     */
    public int getHeartbeat() {
        return heartbeat;
    }

    /**
     * @return the batchNum
     */
    public Integer getBatchNum() {
        return batchNum;
    }

    /**
     * @return the ciphertext
     */
    public Boolean getCiphertext() {
        return ciphertext;
    }

    /**
     * @return the actionRuntime
     */
    public Integer getActionRuntime() {
        return actionRuntime;
    }

    /**
     * @return the useBatch
     */
    public Boolean getUseBatch() {
        return useBatch;
    }

    /**
     * @return the scanPackage
     */
    public String getScanPackage() {
        return scanPackage;
    }

    @Override
    public void parse() throws Exception {
        this.port = ConfigFileUtil.getElmtInteger("//base/port");
        this.webPort = ConfigFileUtil.getElmtInteger("//base/webPort");
        this.socketType = ConfigFileUtil.getElmtString("//base/socketType")
                .toLowerCase();
        this.actionRuntime = ConfigFileUtil
                .getElmtInteger("//base/actionRuntime");
        this.batchNum = ConfigFileUtil.getElmtAttrInteger("//base/useBatch",
                "num");
        this.ciphertext = ConfigFileUtil.getElmtAttrBoolean("//base/socketType",
                "isCiphertext");
        this.heartbeat = ConfigFileUtil.getElmtAttrInteger("//base/socketType", "heartbeat");
        this.useBatch = ConfigFileUtil.getElmtBoolean("//base/useBatch");
        this.scanPackage = ConfigFileUtil.getElmtString("//base/scanPackage");
        this.version = FileUtil.readFileToString(
                new File(ProjectPathUtil.getConfigDirPath()
                        + File.separator + "/version.txt"));
    }
}
