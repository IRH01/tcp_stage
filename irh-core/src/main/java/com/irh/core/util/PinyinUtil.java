package com.irh.core.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * 汉字转拼音辅助类
 *
 * @author zk
 * @date 2015年8月6日 下午4:55:16
 */
public final class PinyinUtil {

    /**
     *
     */
    private PinyinUtil() {

    }

    /**
     * 将汉字转换为全拼
     *
     * @param hanZi
     * @return
     */
    public static String getPingYin(String hanZi) {
        if (StringUtil.isNullOrEmpty(hanZi)) {
            return null;
        }
        char[] t1 = null;
        t1 = hanZi.toCharArray();
        String[] t2 = new String[t1.length];
        HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();

        t3.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        t3.setVCharType(HanyuPinyinVCharType.WITH_V);
        String t4 = "";
        int t0 = t1.length;
        try {
            for (int i = 0; i < t0; i++) {
                // 判断是否为汉字字符  
                if (Character.toString(t1[i]).matches(
                        "[\\u4E00-\\u9FA5]+")) {
                    t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], t3);
                    String s = t2[0];
                    t4 += s.substring(0, 1).toUpperCase() + s.substring(1);
                } else
                    t4 += Character.toString(t1[i]);
            }
            return t4;
        } catch (BadHanyuPinyinOutputFormatCombination e1) {
            e1.printStackTrace();
        }
        return t4;
    }

    /**
     * 返回中文的首字母（大写）
     *
     * @param hanZi
     * @return
     */
    public static String getPinYinHeadChar(String hanZi) {
        if (StringUtil.isNullOrEmpty(hanZi)) {
            return null;
        }
        String convert = "";
        for (int j = 0; j < hanZi.length(); j++) {
            char word = hanZi.charAt(j);
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
            if (pinyinArray != null) {
                convert += pinyinArray[0].charAt(0);
            } else {
                convert += word;
            }
        }
        return convert.toUpperCase();
    }

}
