/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  2014-2-21  下午3:47:16
 */
package com.irh.core.util;

import com.google.common.base.Preconditions;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 随机函数类
 *
 * @author iritchie.ren
 */
public final class RandomUtil {

    /**
     *
     */
    private RandomUtil() {
    }

    /**
     * 对随机的均匀分布有要求
     *
     * @param i
     * @return
     */
    public static int next(final int i) {
        return ThreadLocalRandom.current().nextInt(i);
    }

    /**
     * 随机一半
     *
     * @return
     */
    public static boolean randomHalf() {
        return inRandom(1, 1);
    }

    /**
     * 是否 满足 value / maxValue 的概率。
     *
     * @param value
     * @param maxValue
     * @return
     */
    public static boolean inRandom(final int value, final int maxValue) {
        int ran = ThreadLocalRandom.current().nextInt(1, maxValue + 1);
        return ran <= value;
    }

    /**
     * 随机区间值，如 min=1 maxValue=5 随机，其结果值不包括5
     *
     * @param minValue 开始值
     * @param maxValue 结束值
     * @return
     */
    public static int next(final int minValue, final int maxValue) {
        if (minValue < maxValue) {
            return ThreadLocalRandom.current().nextInt(maxValue - minValue) + minValue;
        } else if (minValue > maxValue) {
            return ThreadLocalRandom.current().nextInt(minValue - maxValue) + minValue;
        }
        return minValue;
    }

    /**
     * 随机区间值，如 min=1 maxValue=5 随机，其结果值包括5
     *
     * @param minValue 开始值
     * @param maxValue 结束值
     * @return
     */
    public static int nextInclud(final int minValue, final int maxValue) {
        if (minValue < maxValue) {
            return ThreadLocalRandom.current().nextInt(maxValue + 1 - minValue) + minValue;
        } else if (minValue > maxValue) {
            return ThreadLocalRandom.current().nextInt(minValue + 1 - maxValue) + maxValue;
        }
        return minValue;
    }

    /**
     * 给定一个字符串区间，在区间内随机一个值（包括minValue和maxValue）
     *
     * @param region minValue|maxValue
     * @return
     */
    public static int next(String region) {
        if (region == null || region.trim().equals("")) {
            LogUtil.error("params error!!!");
            return 0;
        }
        String[] value = region.split("\\|");
        if (value.length != 2) {
            LogUtil.error("params error!!!");
            return 0;
        }
        int minValue = Integer.valueOf(value[0].trim());
        int maxValue = Integer.valueOf(value[1].trim());
        return nextInclud(minValue, maxValue);
    }

    public static Set<Integer> randomArrayInclude(final int[] source, int size) {
        Preconditions.checkArgument(source.length >= size, "size:{}必须小于source的size:{}", size, source.length);

        //先放到链表中
        LinkedList<Integer> temp = new LinkedList();
        for (int i : source) {
            temp.push(i);
        }

        Set<Integer> result = new HashSet<>();

        while (result.size() < size) {
            int index = RandomUtil.nextInclud(0, temp.size() - 1);

            result.add(temp.get(index));

            //每次随机到的元素直接移除
            temp.remove(index);
        }

        return result;
    }

    /**
     * 是否 满足 value / maxValue 的概率。
     *
     * @return
     */
    public static boolean inRandom(float rate) {
        return inRandom((int) (rate * 10000), 10000);
    }
}
