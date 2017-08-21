/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  2014-2-21  下午3:47:16
 */
package com.irh.core.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 类型转换工具。
 * 
 * @author near.li
 * 
 */
public final class TypeConversionUtil {
    /**
     * 
     */
    private TypeConversionUtil() {

    }

    /**
     * 
     * 这个方法是容错方法 如果不需要容错 直接调用 Integer.parseInt
     * 
     * @param str
     * @return
     */
    public static int intByString(final String str) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            //throw new IllegalArgumentException(e);
            return 0;
        }

    }

    /**
     * 
     * @param str
     * @return
     */
    public static long longByString(final String str) {
        try {
            return Long.parseLong(str);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * 用","号分割的字符串。
     * 
     * @param str
     * @return
     */
    public static List<Integer> arrIntByDot(final String str) {
        List<Integer> result = new ArrayList<>();
        if (StringUtil.isNullOrEmpty(str)) {
            return result;
        }
        //做些验证
        if (str.contains("，")) {
            LogUtil.warn(str + ":have 中文逗号");
        }

        String[] strings = str.split("\\,");
        //for (String tempString : strings) {
        for (int i = 0; i < strings.length; i++) {
            int value = TypeConversionUtil.intByString(strings[i]);
            result.add(value);
        }
        return result;
    }

    /**
     * 用","号分割的字符串。
     * 
     * @param str
     * @return
     */
    public static List<Long> arrLongByDot(final String str) {
        List<Long> result = new ArrayList<>();
        if (StringUtil.isNullOrEmpty(str)) {
            return result;
        }
        //做些验证
        if (str.contains("，")) {
            LogUtil.warn(str + ":have 中文逗号");
        }

        String[] strings = str.split("\\,");
        //for (String tempString : strings) {
        for (int i = 0; i < strings.length; i++) {
            long value = TypeConversionUtil.longByString(strings[i]);
            result.add(value);
        }
        return result;
    }

    /**
     * 用"|"号分割的字符串。
     * 
     * @param str
     * @return
     */
    public static List<Integer> arrIntByLine(final String str) {
        List<Integer> result = new ArrayList<>();
        if (StringUtil.isNullOrEmpty(str)) {
            return result;
        }
        String[] strings = str.split("\\|");
        //for (String tempString : strings) {
        for (int i = 0; i < strings.length; i++) {
            int value = TypeConversionUtil.intByString(strings[i]);
            result.add(value);
        }
        return result;
    }

    /**
     * @param str
     */
    public static List<String> arrStrByDot(String str) {
        List<String> result = new ArrayList<>();
        if (StringUtil.isNullOrEmpty(str)) {
            return result;
        }
        if (str.contains("，")) {
            LogUtil.warn(str + ":have 中文逗号");
        }

        String[] strings = str.split("\\,");
        //for (String tempString : strings) {
        for (int i = 0; i < strings.length; i++) {
            result.add(strings[i]);
        }
        return result;
    }

    /**
     * @param str
     */
    public static List<String> arrStrByLine(String str) {
        List<String> result = new ArrayList<>();
        if (StringUtil.isNullOrEmpty(str)) {
            return result;
        }
        String[] strings = str.split("\\|", -1);
        //for (String tempString : strings) {
        for (int i = 0; i < strings.length; i++) {
            result.add(strings[i]);
        }
        return result;
    }

    /**
     * @param arrInt
     * @return
     */
    public static String strByArrInt(List<Integer> arrInt) {
        StringBuilder str = new StringBuilder();
        //for (Integer integer : arrInt) {
        for (int i = 0; i < arrInt.size(); i++) {
            str.append(arrInt.get(i));
            str.append(",");
        }
        if (str.length() > 0) {
            str.deleteCharAt(str.length() - 1);
        }
        return str.toString();
    }

    /**
     * @param arrInt
     * @return
     */
    public static String strByArrLong(List<Long> arrInt) {
        StringBuilder str = new StringBuilder();
        //for (Integer integer : arrInt) {
        for (int i = 0; i < arrInt.size(); i++) {
            str.append(arrInt.get(i));
            str.append(",");
        }
        if (str.length() > 0) {
            str.deleteCharAt(str.length() - 1);
        }
        return str.toString();
    }

    /**
     * @param arrStrs
     * @return
     */
    public static String strByArrStr(List<String> arrStrs) {
        StringBuilder result = new StringBuilder();
        //for (String str : arrStrs) {
        for (int i = 0; i < arrStrs.size(); i++) {
            result.append(arrStrs.get(i));
            result.append(",");
        }
        if (result.length() > 0) {
            result.deleteCharAt(result.length() - 1);
        }
        return result.toString();
    }

    /**
     * @param arrStrs
     * @return
     */
    public static String strByLine(List<String> arrStrs) {
        StringBuilder result = new StringBuilder();
        //for (String str : arrStrs) {
        for (int i = 0; i < arrStrs.size(); i++) {
            result.append(arrStrs.get(i));
            result.append("|");
        }
        if (result.length() > 0) {
            result.deleteCharAt(result.length() - 1);
        }
        return result.toString();
    }

    /**
     * @param value
     * @return
     */
    public static List<Integer> partInt(int value, int segment) {

        List<Integer> result = new ArrayList<>();

        if (32 % segment != 0) {
            result.add(value);
            return result;
        }

        int count = 32 / segment;

        int toValue = (int) (Math.pow(2, count) - 1);

        for (int i = 0; i < segment; i++) {
            int temp = value & toValue;
            result.add(temp);
            value = value >> count;
        }

        return result;
    }

    /**
     * @param values
     * @return
     */
    public static int unionInt(List<Integer> values) {
        int result = 0;

        int segment = values.size();

        int count = 32 / segment;

        for (int i = 0; i < segment; i++) {
            int temp = values.get(i) << (count * i);
            result += temp;
        }
        return result;
    }

    /**
     */
    public static int getValueByWeight(List<String> strs) {
        if (strs.size() == 1) {
            return TypeConversionUtil.intByString(strs.get(0));
        }

        int totalWeight = 0;
        for (String str : strs) {
            List<Integer> item = TypeConversionUtil.arrIntByDot(str);
            if (item.size() >= 2) {
                int weight = item.get(1);
                totalWeight += weight;
            }
        }

        int index = RandomUtil.nextInclud(0, totalWeight);

        int theWeight = 0;
        for (String str : strs) {
            List<Integer> item = TypeConversionUtil.arrIntByDot(str);
            if (item.size() >= 2) {
                int value = item.get(0);
                int weight = item.get(1);

                theWeight += weight;
                if (theWeight >= index) {
                    return value;
                }
            }
        }
        return 0;
    }
}
