/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  May 17, 2014  11:12:56 AM
 */
package com.irh.tcp.core.config;

import com.irh.tcp.core.manager.ManagerType;
import org.dom4j.Element;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 基础配置
 *
 * @author iritchie.ren
 */
public final class IncludeManagerConfig implements IConfig {

    /**
     *
     */
    private static final IncludeManagerConfig INSTACE = new IncludeManagerConfig();

    /**
     * 模块类型
     */
    private Set<ManagerType> managerTypes = new HashSet<>();

    /**
     *
     */
    private IncludeManagerConfig() {
    }

    /**
     * 是否包含ModuleType
     *
     * @return
     */
    public boolean contains(ManagerType managerType) {
        return managerTypes.contains(managerType);
    }

    /**
     * @return
     */
    public static IncludeManagerConfig getInstance() {
        return IncludeManagerConfig.INSTACE;
    }

    @Override
    public void parse() {
        String type = ConfigFileUtil
                .getElmtAttrString("//includeManagers", "type")
                .toLowerCase();
        //不包含includeManagers或者includeManagers的type为all就把所有的类型加入。
        if (!ConfigFileUtil.containElmt("//includeManagers")
                || type.equals("all")) {
            Field[] fields = ManagerType.class.getFields();
            for (Field field : fields) {
                managerTypes.add(ManagerType.valueOf(field.getName()));
            }
        } else {
            List<Element> includeManagerElmts = ConfigFileUtil
                    .getElmts("//includeManager");
            for (Element includeManagerElmt : includeManagerElmts) {
                managerTypes.add(ManagerType.valueOf(
                        includeManagerElmt.attributeValue("type")));
            }
        }
    }
}
