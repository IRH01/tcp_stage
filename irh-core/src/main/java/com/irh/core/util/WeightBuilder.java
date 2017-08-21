/**
 * Copyright(c) 2015 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  Sep 24, 2015  8:13:59 PM
 */
package com.irh.core.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @param <T> 数据类型
 * @author iritchie.ren
 */
public final class WeightBuilder<T> implements Serializable {
    /**
     *
     */
    private Map<T, WeightData<T>> weightDatas = new HashMap<>();

    /**
     *
     */
    private int totalWeight = 0;

    /**
     * <pre>
     * 是否权重累加。
     * 累加的意思是：假如有 2个data为 “test” 的值，他们的权重分别是1，2。那么累加的话，他们的权重合计是3。
     * 不累加的情况：后面的权重会顶掉前面的。默认不累加。
     * </pre>
     */
    private boolean accumulation = false;

    /**
     *
     */
    private WeightBuilder(boolean accumulation) {
        this.accumulation = accumulation;
    }

    /**
     *
     */
    public static <T> WeightBuilder<T> newBuilder() {
        return new WeightBuilder<T>(true);
    }

    /**
     *
     */
    public static <T> WeightBuilder<T> newBuilder(boolean accumulation) {
        return new WeightBuilder<T>(accumulation);
    }

    /**
     * @param weight
     * @param data   应该唯一
     * @return
     */
    public WeightBuilder<T> append(int weight, T data) {
        if (accumulation) {
            WeightData<T> weightData = this.weightDatas.get(data);
            if (weightData != null) {
                weightData.setWeight(weightData.getWeight() + weight);
            } else {
                this.weightDatas.put(data, new WeightData<T>(weight, data));
            }
        } else {
            this.weightDatas.put(data, new WeightData<T>(weight, data));
        }
        return this;
    }

    /**
     * 计算，返回期望的Data。
     *
     * @return
     */
    public T calculateAndGet() {
        calculate();
        return get();
    }

    /**
     * 返回Data，不计算，注意：如果在上次get与这次调用之间有调用append，那么调用calculateAndGet
     *
     * @return
     */
    public T get() {
        //计算得到的结果
        T result = null;
        WeightData<T> weightData = null;
        int ranWeight = RandomUtil.nextInclud(1, totalWeight);
        for (Map.Entry<T, WeightData<T>> entry : weightDatas.entrySet()) {
            weightData = entry.getValue();
            if (weightData.getMin() <= ranWeight && ranWeight <= weightData.getMax()) {
                result = weightData.getData();
                break;
            }
        }
        return result;
    }

    /**
     * 计算权重。
     *
     * @return
     */
    public void calculate() {
        totalWeight = 0;
        WeightData<T> weightData = null;
        for (Map.Entry<T, WeightData<T>> entry : weightDatas.entrySet()) {
            weightData = entry.getValue();
            int minValue = totalWeight + 1;
            int maxValue = totalWeight + weightData.getWeight();
            weightData = entry.getValue();
            weightData.setMin(minValue);
            weightData.setMax(maxValue);
            totalWeight = maxValue;
        }
    }

    /**
     * 权重数据
     *
     * @param <T> 数据类型
     * @author iritchie.ren
     */
    private static class WeightData<T> implements Serializable {
        /**
         * 权重
         */
        private int weight = 0;
        /**
         *
         */
        private T data;

        /**
         * 下限
         */
        private int min = 0;

        /**
         * 上限
         */
        private int max = 0;

        /**
         * @param weight
         * @param data
         */
        WeightData(int weight, T data) {
            this.weight = weight;
            this.data = data;
        }

        /**
         * @param min the min to set
         */
        void setMin(int min) {
            this.min = min;
        }

        /**
         * @param max the max to set
         */
        void setMax(int max) {
            this.max = max;
        }

        /**
         * @return the weight
         */
        int getWeight() {
            return weight;
        }

        /**
         * @return the data
         */
        T getData() {
            return data;
        }

        /**
         * @return the min
         */
        int getMin() {
            return min;
        }

        /**
         * @return the max
         */
        int getMax() {
            return max;
        }

        /**
         * @param weight the weight to set
         */
        void setWeight(int weight) {
            this.weight = weight;
        }

        /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return "WeightData [weight=" + weight + ", data=" + data + ", min=" + min + ", max=" + max + "]";
        }
    }

}
