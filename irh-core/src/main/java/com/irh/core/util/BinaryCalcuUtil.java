package com.irh.core.util;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 二进制与第几位转换工具类
 *
 * @author zk
 * @date 2015年11月14日 上午9:40:47
 */
public final class BinaryCalcuUtil {

    private BinaryCalcuUtil() {

    }

    /**
     * @param decimalNum
     * @param sortNum
     * @return
     */
    public static Long sortNumToDecimalNum(Long decimalNum, Integer sortNum) {
        if (sortNum == null) {
            return decimalNum;
        }
        if (decimalNum == null) {
            decimalNum = 0L;
        }
        return decimalNum | (long) Math.pow(2, sortNum - 1);
    }

    /**
     * @param decimalNum
     * @param sortNumList
     * @return
     */
    public static Long sortNumToDecimalNum(Long decimalNum, List<Integer> sortNumList) {
        if (sortNumList == null) {
            return decimalNum;
        }
        for (Integer sortNum : sortNumList) {
            decimalNum = sortNumToDecimalNum(decimalNum, sortNum);
        }
        return decimalNum;
    }

    /**
     * @param decimalNum
     * @param sortNumList
     * @return
     */
    public static Long sortNumStrToDecimalNum(Long decimalNum, List<String> sortNumList) {
        if (sortNumList == null) {
            return decimalNum;
        }
        List<Integer> intSortNumList = new ArrayList<>();
        for (String s : sortNumList) {
            if (StringUtils.isNotBlank(s)) {
                intSortNumList.add(Integer.parseInt(s));
            }
        }
        return sortNumToDecimalNum(decimalNum, intSortNumList);
    }

    /**
     * @param decimalNum
     * @param sortNum
     * @return
     */
    public static boolean canMatch(Long decimalNum, Integer sortNum) {
        if (decimalNum == null || sortNum == null) {
            return false;
        }
        return (decimalNum & BinaryCalcuUtil.sortNumToDecimalNum(null, sortNum)) > 0;
    }

}
