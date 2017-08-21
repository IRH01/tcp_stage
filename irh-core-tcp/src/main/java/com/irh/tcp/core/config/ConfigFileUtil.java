/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  May 14, 2014  3:06:58 PM
 */
package com.irh.tcp.core.config;

import com.irh.core.util.StringUtil;
import com.irh.tcp.core.server.AbstractServer;
import com.irh.tcp.core.util.ProjectPathUtil;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.List;

/**
 * @author iritchie.ren
 */

@SuppressWarnings("unchecked")
public final class ConfigFileUtil {
    /**
     * game_server.xml ducoment
     */
    private static Document document = null;

    /**
     * 配置文件解析util
     */
    private ConfigFileUtil() {

    }

    /**
     *
     */
    public static void init() {
        loadConfig();
    }

    /**
     *
     */
    public static Document getDocument() {
        return ConfigFileUtil.document;
    }

    /**
     * 获得元素的内容。
     *
     * @param xpath
     */
    public static String getElmtString(final String xpath) {
        Element elmt = (Element) ConfigFileUtil.document
                .selectSingleNode(xpath);
        if (elmt == null) {
            return "";
        }
        return elmt.getTextTrim();
    }

    /**
     * 获得元素的内容。
     *
     * @param xpath
     */
    public static Integer getElmtInteger(final String xpath) {
        String str = ConfigFileUtil.getElmtString(xpath);
        if (StringUtil.isNullOrEmpty(str)) {
            return null;
        } else {
            return Integer.parseInt(str);
        }
    }

    /**
     * 获得元素的内容。
     *
     * @param xpath
     */
    public static Boolean getElmtBoolean(final String xpath) {
        String str = ConfigFileUtil.getElmtString(xpath);
        if (str != null) {
            return Boolean.parseBoolean(str);
        } else {
            return false;
        }
    }

    /**
     * 获得元素属性的内容。
     *
     * @param xpath
     */
    public static String getElmtAttrString(final String xpath,
                                           final String attrName) {
        Element elmt = (Element) ConfigFileUtil.document
                .selectSingleNode(xpath);
        if (elmt == null) {
            return "";
        }
        if (elmt.attributeValue(attrName) != null) {
            return elmt.attributeValue(attrName).trim();
        } else {
            return "";
        }
    }

    /**
     * 获得元素属性的内容。
     *
     * @param xpath
     */
    public static Integer getElmtAttrInteger(final String xpath,
                                             final String attrName) {
        String str = ConfigFileUtil.getElmtAttrString(xpath, attrName);
        if (StringUtil.isNullOrEmpty(str)) {
            return null;
        } else {
            return Integer.parseInt(str);
        }
    }

    /**
     * 获得元素属性的内容。
     *
     * @param xpath
     */
    public static Boolean getElmtAttrBoolean(final String xpath,
                                             final String attrName) {
        return Boolean.parseBoolean(
                ConfigFileUtil.getElmtAttrString(xpath, attrName));
    }

    /**
     * 获得指定的元素.
     *
     * @param xpath
     */
    public static List<Element> getElmts(final String xpath) {
        return ConfigFileUtil.document.selectNodes(xpath);
    }

    /**
     * 获得指定的元素.
     *
     * @param xpath
     */
    public static Element getElmt(final String xpath) {
        return (Element) ConfigFileUtil.document.selectSingleNode(xpath);
    }

    /**
     * 是否包含指定元素。
     *
     * @param xpath
     */
    public static Boolean containElmt(final String xpath) {
        return ConfigFileUtil.document.selectSingleNode(xpath) != null;
    }

    /**
     * 加载config文件
     */
    private static void loadConfig() {
        SAXReader reader = new SAXReader();
        try {
            ConfigFileUtil.document = reader.read(new File(ProjectPathUtil
                    .getConfigDirPath() + File.separator
                    + System.getProperty(
                    AbstractServer.CONFIG_FILE_NAME)));
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
    }
}
