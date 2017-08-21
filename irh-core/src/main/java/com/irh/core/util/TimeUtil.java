package com.irh.core.util;

import com.google.common.base.Preconditions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public final class TimeUtil {
    public final static String DF_PATTERN_YYYY_MM_TIGHT = "yyyyMM";
    public final static String DF_PATTERN_YYYY_MM_DD = "yyyy-MM-dd";
    public final static String DF_PATTERN_YYYY_MM_DD_TIGHT = "yyyyMMdd";
    public final static String DF_PATTERN_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    public final static String DF_PATTERN_YYYY_MM_DD_HH_MM_TIGHT = "yyyyMMddHHmm";
    public final static String DF_PATTERN_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public final static String DF_PATTERN_YYYY_MM_DD_HH_MM_SS_TIGHT = "yyyyMMddHHmmss";
    public final static String DF_PATTERN_MMM_DD_YYYY = "MMM-dd-yyyy";
    public final static String DF_PATTERN_MMM_DD_YYYY_HH_MM = "MMM-dd-yyyy HH:mm";
    public final static String DF_PATTERN_DD_MM_YYYY_SLASH = "dd/MM/yyyy";
    public final static String DF_PATTERN_DDDot_MMM_YYYY = "dd. MMM yyyy";
    public final static String DF_PATTERN_DD_MMM_YYYY = "dd MMM yyyy";
    public final static String DF_PATTERN_DD_MMM_YYYY_HH_MM = "dd MMM yyyy HH:mm";
    public final static String DF_PATTERN_DD_MMM_YYYYDot_HH_MM = "dd MMM yyyy, HH:mm";
    public final static String DF_PATTERN_EE_MMM_DD_YYYY = "EE, MMM dd, yyyy";
    public final static String[] DAY_OF_WEEK = {"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};

    private TimeUtil() {

    }

    /**
     * 两个日期相减,获取天数.
     *
     * @param begin 开始时间
     * @param end   结束时间
     * @return long 小时差
     */
    public static long countDay(Date begin, Date end) {
        int hour = 0;
        int day = 0;
        long total_minute = 0;

        total_minute = (end.getTime() - begin.getTime()) / (1000 * 60);
        hour = (int) total_minute / 60;
        day = (int) hour / 24;
        return day;
    }

    public static int countSeconds(Date begin, Date end) {
        if (begin == null || end == null)
            return -1;
        long interval = end.getTime() - begin.getTime();
        return (int) (interval / 1000);
    }

    /**
     * 计算两个日期相差的小时数
     *
     * @param begin
     * @param end
     * @return
     */
    public static double calcHoursInterval(Date begin, Date end) {
        Preconditions.checkArgument(begin != null & end != null, "begin和end不能为null");

        long interval = end.getTime() - begin.getTime();

        return interval / (1000 * 60 * 60.0f);
    }

    public static boolean isSameTime(Date d1, Date d2) {
        if (d1 == null) {
            if (d2 == null)
                return true;
            return false;
        } else {
            if (d2 == null)
                return false;
        }
        return d1.equals(d2);
    }

    public static boolean isSameDay(Date d1, Date d2) {
        if (d1 == null || d2 == null) {
            return false;
        }
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        c.setTime(d1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        long later = c.getTimeInMillis();
        c.setTime(d2);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        long early = c.getTimeInMillis();
        return later == early;
    }

    public static boolean isSameMonth(Date d1, Date d2) {
        if (d1 == null || d2 == null) {
            return false;
        }
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        c.setTime(d1);
        c.set(Calendar.DATE, 0);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        long later = c.getTimeInMillis();
        c.setTime(d2);
        c.set(Calendar.DATE, 0);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        long early = c.getTimeInMillis();
        return later == early;
    }

    public static String dateToEngString(Date d, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return sdf.format(d);
    }

    public static Date engStringToDate(String d, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        try {
            return sdf.parse(d);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 根据月和日返回大于当前的完整日期
     *
     * @param d
     * @param pattern
     * @return
     */
    public static Date dayAndMonthToFutureDate(String d, String pattern) {
        pattern = pattern.trim() + "-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        d = d.trim() + "-" + c.get(Calendar.YEAR);
        try {
            c.setTime(sdf.parse(d));
            if (c.getTimeInMillis() < System.currentTimeMillis())
                c.add(Calendar.YEAR, 1);
            return c.getTime();
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 返回yyyy-MM-dd HH:mm:ss格式的字符串
     *
     * @param date
     * @return
     */
    public static String getSimpleFullEngString(Date date) {
        return dateToEngString(date, DF_PATTERN_YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * 返回yyyy-MM-dd格式的字符串
     *
     * @param date
     * @return
     */
    public static String getSimpleDateEngString(Date date) {
        return dateToEngString(date, DF_PATTERN_YYYY_MM_DD);
    }

    public static String getMinuteDateEngString(Date date) {
        return dateToEngString(date, DF_PATTERN_YYYY_MM_DD_HH_MM_TIGHT);
    }

    public static Date getDateFromDateEngString(String d) {
        return engStringToDate(d, DF_PATTERN_YYYY_MM_DD);
    }

    public static Date getDateFromFullEngString(String d) {
        return engStringToDate(d, DF_PATTERN_YYYY_MM_DD_HH_MM_SS);
    }

    public static boolean isEarly(Date earlyDate, Date laterDate) {
        if (earlyDate == null || laterDate == null) {
            return false;
        }
        Calendar c0 = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        c0.setTime(earlyDate);
        Calendar c1 = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        c1.setTime(laterDate);
        return c0.getTimeInMillis() < c1.getTimeInMillis();
    }

    /**
     * 只根据日期计算两个日期间相隔的天数，忽略时分秒
     *
     * @param earlyDate
     * @param laterDate
     * @return
     */
    public static int calcDayInterval(Date earlyDate, Date laterDate) {
        if (earlyDate == null || laterDate == null) {
            return -1;
        }

        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        c.setTime(laterDate);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        long later = c.getTimeInMillis();
        c.setTime(earlyDate);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        long early = c.getTimeInMillis();
        return Math.abs(Integer.parseInt(String.valueOf((later - early) / (1000 * 3600 * 24))));
    }

    public static Date getDateAfterDate(int date) {
        return getDateAfterDate(new Date(), date);
    }

    public static Date getDateAfterDate(Date fromDate, int date) {
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        c.setTime(fromDate);
        c.add(Calendar.DATE, date);
        return c.getTime();
    }

    public static Date getDateAfterWeeks(int weeks) {
        return getDateAfterWeeks(new Date(), weeks);
    }

    public static Date getDateAfterWeeks(Date fromDate, int weeks) {
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        c.setTime(fromDate);
        c.add(Calendar.DAY_OF_YEAR, 7 * weeks);
        return c.getTime();
    }

    public static Date getDateAfterMonth(int month) {
        return getDateAfterDate(new Date(), month);
    }

    public static Date getDateAfterMonth(Date fromDate, int month) {
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        c.setTime(fromDate);
        c.add(Calendar.MONTH, month);
        return c.getTime();
    }

    public static Date getDateAfterHours(Date fromDate, int hours) {
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        c.setTime(fromDate);
        c.add(Calendar.HOUR_OF_DAY, hours);
        return c.getTime();
    }

    public static Date getDateAfterMinutes(Date fromDate, int minutes) {
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        c.setTime(fromDate);
        c.add(Calendar.MINUTE, minutes);
        return c.getTime();
    }

    public static Date getDateAfterSeconds(Date fromDate, int seconds) {
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        c.setTime(fromDate);
        c.add(Calendar.SECOND, seconds);
        return c.getTime();
    }

    public static Integer getDayOfWeekByString(String dayOfWeek) {
        for (int i = 0; i < DAY_OF_WEEK.length; i++)
            if (DAY_OF_WEEK[i].equals(dayOfWeek.trim().toUpperCase()))
                return i + 1;
        return null;
    }

    public static Date getYesterday() {
        //获取昨天日期
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date calcWeeklyDate(Date point, String dayOfWeek, String hourAndMinute, boolean isBefore) {
        Integer day = getDayOfWeekByString(dayOfWeek);
        hourAndMinute = hourAndMinute.trim().replace(":", "");
        if (hourAndMinute.length() == 3)
            hourAndMinute = "0" + hourAndMinute;

        if (day != null && hourAndMinute.length() == 4) {
            Integer hour = Integer.valueOf(hourAndMinute.substring(0, 2));
            Integer minite = Integer.valueOf(hourAndMinute.substring(2));
            Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
            c.setTime(point);
            c.set(Calendar.HOUR_OF_DAY, hour);
            c.set(Calendar.MINUTE, minite);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);
            if (isBefore) {
                while (c.get(Calendar.DAY_OF_WEEK) != day)
                    c.add(Calendar.DAY_OF_YEAR, -1);
            } else {
                while (c.get(Calendar.DAY_OF_WEEK) != day)
                    c.add(Calendar.DAY_OF_YEAR, 1);
            }
            return c.getTime();
        }
        return null;
    }

    public static Integer getDayOfWeekByString() {
        return null;
    }

    /**
     * 获取月份起始日期 忽略时分秒
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static Date getMinMonthDate(Date date) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
        return calendar.getTime();
    }

    /**
     * 获取下个月份第一天 忽略时分秒
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static Date getMaxMonthDate(Date date) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
        return calendar.getTime();
    }

}
