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
public final class ExcludeModuleConfig implements IConfig {

    /**
     *
     */
    private static final ExcludeModuleConfig INSTACE = new ExcludeModuleConfig();

    /**
     * 模块类型
     */
    private Set<String> moduleTypes = new HashSet<>();

    /**
     *
     */
    private ExcludeModuleConfig() {
    }

    /**
     * 是否包含ModuleType
     *
     * @param moduleType
     * @return
     */
    public boolean contains(String moduleType) {
        return moduleTypes.contains(moduleType);
    }

    /**
     * @return
     */
    public static ExcludeModuleConfig getInstance() {
        return ExcludeModuleConfig.INSTACE;
    }

    @Override
    public void parse() {
        List<Element> excludeModuleElmts = ConfigFileUtil
                .getElmts("//excludeModule");
        for (Element element : excludeModuleElmts) {
            moduleTypes.add(element.attributeValue("type").trim());
        }
    }
}
