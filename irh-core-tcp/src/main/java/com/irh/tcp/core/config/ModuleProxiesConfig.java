/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  Jun 20, 2014  10:23:14 AM
 */
package com.irh.tcp.core.config;

import org.dom4j.Element;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 模块代理配置集合
 *
 * @author iritchie.ren
 */
public final class ModuleProxiesConfig implements IConfig {
    /**
     *
     */
    private static final ModuleProxiesConfig INSTACE = new ModuleProxiesConfig();

    /**
     *
     */
    private Map<String, ModuleProxyConfig> configMap = new HashMap<>();

    /**
     *
     */
    private ModuleProxiesConfig() {
    }

    /**
     * @return
     */
    public static ModuleProxiesConfig getInstance() {
        return ModuleProxiesConfig.INSTACE;
    }

    /**
     *
     */
    public ModuleProxyConfig getModuleProxyConfig(String moduleType) {
        return configMap.get(moduleType);
    }

    @Override
    public void parse() {
        List<Element> moduleProxiesElmts = ConfigFileUtil
                .getElmts("//moduleProxy");
        String moduleType = null;
        String ip = null;
        Integer port = null;
        Boolean isCiphertext = null;
        ModuleProxyConfig moduleProxyConfig = null;
        for (Element moduleProxyElmt : moduleProxiesElmts) {
            moduleProxyConfig = new ModuleProxyConfig();
            moduleType = moduleProxyElmt.attributeValue("type");
            ip = moduleProxyElmt.attributeValue("ip").trim();
            port = Integer.parseInt(
                    moduleProxyElmt.attributeValue("port").trim());
            isCiphertext = Boolean.parseBoolean(moduleProxyElmt
                    .attributeValue("isCiphertext").trim());
            moduleProxyConfig.ip = ip;
            moduleProxyConfig.port = port;
            moduleProxyConfig.isCiphertext = isCiphertext;
            configMap.put(moduleType, moduleProxyConfig);
        }
    }

    /**
     * 模块代理配置
     *
     * @author iritchie.ren
     */
    public static final class ModuleProxyConfig {
        /**
         *
         */
        private String ip = null;
        /**
         *
         */
        private Integer port = null;

        /**
         *
         */
        private Boolean isCiphertext = null;

        /**
         *
         */
        private ModuleProxyConfig() {

        }

        /**
         * @return the ip
         */
        public String getIp() {
            return ip;
        }

        /**
         * @return the port
         */
        public int getPort() {
            return port;
        }

        /**
         * @return the isCiphertext
         */
        public Boolean getIsCiphertext() {
            return isCiphertext;
        }

    }
}
