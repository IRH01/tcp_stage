package com.irh.core.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class RegexUtil {

    public final static String REGEX_PATTERN_TABLE = "<table[\\s\\S]*?</table>";
    public final static String REGEX_PATTERN_TR = "<tr[\\s\\S]*?>([\\s\\S]*?)</tr>";
    public final static String REGEX_PATTERN_DIV = "<div[\\s\\S]*?>([\\s\\S]*?)</div>";
    public final static String REGEX_PATTERN_TD = "<td[\\s\\S]*?>([\\s\\S]*?)</td>";
    public final static String REGEX_PATTERN_SPAN = "<span[\\s\\S]*?>([\\s\\S]*?)</span>";
    public final static String REGEX_PATTERN_A = "<a[\\s\\S]*?>([\\s\\S]*?)</a>";
    public final static String REGEX_PATTERN_MOBILE = "^1[0-9]{10}$";
    public final static String REGEX_PATTERN_EMAIL = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
    public final static String REGEX_PATTERN_OPTION = "<option[\\s\\S]*?>([\\s\\S]*?)</option>";

    /**
     *
     */
    private RegexUtil() {

    }

    /**
     * 是否手机
     *
     * @param source
     * @return
     */
    public static boolean checkMobile(String source) {
        if (StringUtil.isNullOrEmpty(source))
            return false;
        Matcher m = doMatcher(source, REGEX_PATTERN_MOBILE);
        if (m.find())
            return true;
        return false;
    }

    /**
     * 是否email
     *
     * @param source
     * @return
     */
    public static boolean checkEmail(String source) {
        if (StringUtil.isNullOrEmpty(source))
            return false;
        Matcher m = doMatcher(source, REGEX_PATTERN_EMAIL);
        if (m.find())
            return true;
        return false;
    }

    /**
     * @param source
     * @param regex
     * @return
     */
    public static Matcher doMatcher(String source, String regex) {
        Pattern p = Pattern.compile(regex);
        return p.matcher(source);
    }

    /**
     * @param source
     * @param regex
     * @return
     */
    public static String getFirstMatch(String source, String regex) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(source);
        if (m.find())
            return m.group();
        return null;
    }

    /**
     * @param source
     * @return
     */
    public static List<String> getTables(String source) {
        if (source == null)
            return null;
        List<String> retval = new ArrayList<>();
        Matcher m = doMatcher(source, REGEX_PATTERN_TABLE);
        while (m.find()) {
            retval.add(m.group());
        }
        return retval;
    }

    /**
     * @param source
     * @return
     */
    public static List<String> getDivs(String source) {
        if (source == null) {
            return null;
        }
        List<String> retval = new ArrayList<>();
        Matcher m = doMatcher(source, REGEX_PATTERN_DIV);
        while (m.find()) {
            retval.add(m.group());
        }
        return retval;
    }

    /**
     * @param source
     * @return
     */
    public static List<String> getDivsInner(String source) {
        if (source == null) {
            return null;
        }
        List<String> retval = new ArrayList<>();
        Matcher m = doMatcher(source, REGEX_PATTERN_DIV);
        while (m.find()) {
            retval.add(m.group(1).trim());
        }
        return retval;
    }

    /**
     * @param source
     * @return
     */
    public static List<String> getRows(String source) {
        if (source == null)
            return null;
        List<String> retval = new ArrayList<>();
        Matcher m = doMatcher(source, REGEX_PATTERN_TR);
        while (m.find()) {
            retval.add(m.group());
        }
        return retval;
    }

    /**
     * @param source
     * @return
     */
    public static List<String> getRowsInner(String source) {
        if (source == null)
            return null;
        List<String> retval = new ArrayList<>();
        Matcher m = doMatcher(source, REGEX_PATTERN_TR);
        while (m.find()) {
            retval.add(m.group(1).trim());
        }
        return retval;
    }

    /**
     * @param source
     * @return
     */
    public static List<String> getOptions(String source) {
        List<String> retval = new ArrayList<>();
        if (source == null) {
            return retval;
        }
        Matcher m = doMatcher(source, REGEX_PATTERN_OPTION);
        while (m.find()) {
            retval.add(m.group());
        }
        return retval;
    }

    /**
     * @param source
     * @return
     */
    public static List<String> getOptionsInner(String source) {
        List<String> retval = new ArrayList<>();
        if (source == null) {
            return retval;
        }
        Matcher m = doMatcher(source, REGEX_PATTERN_OPTION);
        while (m.find()) {
            retval.add(m.group(1).trim());
        }
        return retval;
    }

    /**
     * @param source
     * @return
     */
    public static List<String> getCells(String source) {
        List<String> retval = new ArrayList<>();
        if (source == null) {
            return retval;
        }
        Matcher m = doMatcher(source, REGEX_PATTERN_TD);
        while (m.find()) {
            retval.add(m.group());
        }
        return retval;
    }

    /**
     * @param source
     * @return
     */
    public static List<String> getCellsInner(String source) {
        List<String> retval = new ArrayList<>();
        if (source == null) {
            return retval;
        }
        Matcher m = doMatcher(source, REGEX_PATTERN_TD);
        while (m.find()) {
            retval.add(m.group(1).trim());
        }
        return retval;
    }

    /**
     * @param source
     * @return
     */
    public static List<String> getSpans(String source) {
        List<String> retval = new ArrayList<>();
        if (source == null) {
            return retval;
        }
        Matcher m = doMatcher(source, REGEX_PATTERN_SPAN);
        while (m.find()) {
            retval.add(m.group());
        }
        return retval;
    }

    /**
     * @param source
     * @return
     */
    public static List<String> getSpansInner(String source) {
        List<String> retval = new ArrayList<>();
        if (source == null) {
            return retval;
        }
        Matcher m = doMatcher(source, REGEX_PATTERN_SPAN);
        while (m.find()) {
            retval.add(m.group(1).trim());
        }
        return retval;
    }

    /**
     * @param source
     * @return
     */
    public static List<String> getLinks(String source) {
        List<String> retval = new ArrayList<>();
        if (source == null) {
            return retval;
        }
        Matcher m = doMatcher(source, REGEX_PATTERN_A);
        while (m.find()) {
            retval.add(m.group());
        }
        return retval;
    }

    /**
     * @param source
     * @return
     */
    public static List<String> getLinksInner(String source) {
        List<String> retval = new ArrayList<>();
        if (source == null) {
            return retval;
        }
        Matcher m = doMatcher(source, REGEX_PATTERN_A);
        while (m.find()) {
            retval.add(m.group(1).trim());
        }
        return retval;
    }

    /**
     * @param source
     * @param inputFlag
     * @return
     */
    public static String getInputValue(String source, String inputFlag) {
        if (source == null || inputFlag == null) {
            return null;
        }
        String value = null;
        Matcher m = doMatcher(source, "<input[\\s\\S]*?/>");
        while (m.find()) {
            String input = m.group();
            if (input.contains(inputFlag)) {
                value = StringUtil.findBetweenTwoFlag(input, "value=\"", "\"");
                break;
            }
        }
        return value;
    }

}
