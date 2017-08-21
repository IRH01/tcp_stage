/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  May 17, 2014  11:12:56 AM
 */
package com.irh.tcp.core.config;

import org.dom4j.Element;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 基础配置
 *
 * @author iritchie.ren
 */
public final class IncludeModuleConfig implements IConfig {

    /**
     *
     */
    private static final IncludeModuleConfig INSTACE = new IncludeModuleConfig();

    /**
     *
     */
    private boolean isAll = false;

    /**
     * 模块类型
     */
    private Set<String> moduleTypes = new HashSet<>();

    /**
     *
     */
    private IncludeModuleConfig() {
    }

    /**
     * 是否包含ModuleType
     *
     * @param moduleType
     * @return
     */
    public boolean contains(String moduleType) {
        if (isAll) {
            return true;
        }
        return moduleTypes.contains(moduleType);
    }

    /**
     * @return
     */
    public static IncludeModuleConfig getInstance() {
        return IncludeModuleConfig.INSTACE;
    }

    @Override
    public void parse() {
        String type = ConfigFileUtil
                .getElmtAttrString("//includeModules", "type")
                .toLowerCase();
        if (!ConfigFileUtil.containElmt("//includeModules")
                || type.equals("all")) {
            isAll = true;
            moduleTypes.add("all");
        } else {
            List<Element> includeManagerElmts = ConfigFileUtil
                    .getElmts("//includeModule");
            for (Element includeManagerElmt : includeManagerElmts) {
                moduleTypes.add(includeManagerElmt.attributeValue("type")
                        .trim());
            }
        }
    }
}
