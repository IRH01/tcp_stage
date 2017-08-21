/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  May 17, 2014  11:12:56 AM
 */
package com.irh.tcp.core.config;

import com.irh.tcp.core.manager.ManagerType;
import org.dom4j.Element;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 基础配置
 *
 * @author iritchie.ren
 */
public final class ExcludeManagerConfig implements IConfig {

    /**
     *
     */
    private static final ExcludeManagerConfig INSTACE = new ExcludeManagerConfig();

    /**
     * 模块类型
     */
    private Set<ManagerType> managerTypes = new HashSet<>();

    /**
     *
     */
    private ExcludeManagerConfig() {
    }

    /**
     * 是否包含ManagerType
     *
     * @param ManagerType
     * @return
     */
    public boolean contains(ManagerType managerType) {
        return managerTypes.contains(managerType);
    }

    /**
     * @return
     */
    public static ExcludeManagerConfig getInstance() {
        return ExcludeManagerConfig.INSTACE;
    }

    @Override
    public void parse() {
        List<Element> excludeManagerElmts = ConfigFileUtil
                .getElmts("//excludeManager");
        for (Element element : excludeManagerElmts) {
            managerTypes.add(ManagerType
                    .valueOf(element.attributeValue("type").trim()));
        }
    }
}
