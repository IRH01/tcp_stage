/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  2014-6-16  下午3:23:28
 */
package com.irh.tcp.core.manager.database;


/**
 * 间隔时间保存数据的组件需实现此接口
 *
 * @author Demon
 */
public interface ISaveDataToDB {

    /**
     * 5分钟缓存数据保存.
     */
    void saveDataForFiveMinutes();

    /**
     * 10分钟缓存数据保存.
     */
    void saveDataForTenMinutes();

    /**
     * 20分钟缓存数据保存.
     */
    void saveDataForTwentyMinutes();

    /**
     * 添加Entity到5分钟缓存
     *
     * @param baseEntity
     */
    void addFiveMinsCache(BaseEntity entity);

    /**
     * 添加Entity到10分钟缓存
     *
     * @param baseEntity
     */
    void addTenMinsCache(BaseEntity entity);

    /**
     * 添加Entity到20分钟缓存
     *
     * @param baseEntity
     */
    void addTwentyMinsCache(BaseEntity entity);

    /**
     * 把Entity放到5分钟缓存。
     *
     * @param baseEntity
     */
    void putEntityToFiveMinsCache();

    /**
     * 把Entity放到10分钟缓存。
     *
     * @param baseEntity
     */
    void putEntityToTenMinsCache();

    /**
     * 把Entity放到20分钟缓存。
     *
     * @param baseEntity
     */
    void putEntityToTwentyMinsCache();
}
