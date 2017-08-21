/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  2014-3-24  上午9:48:09
 */
package com.irh.tcp.core.manager.module;

import com.irh.tcp.core.manager.database.BaseEntity;
import com.irh.tcp.core.manager.database.DBHelper;
import com.irh.tcp.core.manager.database.DatabaseType;
import com.irh.tcp.core.manager.database.ISaveDataToDB;
import com.irh.tcp.core.util.ISelfDrivenAction;
import com.irh.tcp.core.util.NamedThreadFactory;
import com.irh.tcp.core.util.SelfDrivenActionQueue;
import org.apache.mina.util.ConcurrentHashSet;

import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 抽象模块
 *
 * @author iritchie.ren
 */
public abstract class AbstractModule implements ISaveDataToDB {

    /**
     * 线程池大小
     */
    private static final int THREAD_COUNT = 4;

    /**
     * 场景线程池
     */
    private static final ThreadPoolExecutor THREADPOOL_EXECUTOR = new ThreadPoolExecutor(
            AbstractModule.THREAD_COUNT, AbstractModule.THREAD_COUNT, 0,
            TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(),
            new NamedThreadFactory("MODULE_PROCESS_THREADS",
                    Thread.NORM_PRIORITY));

    /**
     * 自驱动的action队列
     */
    protected SelfDrivenActionQueue<ISelfDrivenAction> actionQueue = new SelfDrivenActionQueue<>(
            THREADPOOL_EXECUTOR);

    /**
     * 5分钟缓存。
     */
    private Set<BaseEntity> fiveMinsCache = new ConcurrentHashSet<>();

    /**
     * 10分钟缓存。
     */
    private Set<BaseEntity> tenMinsCache = new ConcurrentHashSet<>();

    /**
     * 20分钟缓存。
     */
    private Set<BaseEntity> twentyMinsCache = new ConcurrentHashSet<>();

    /**
     * @throws Exception
     */
    public void start() throws Exception {
    }

    /**
     * @throws Exception
     */
    public void stop() throws Exception {
        saveDataForFiveMinutes();
        saveDataForTenMinutes();
        saveDataForTwentyMinutes();
    }

    /**
     * 添加action
     *
     * @param action
     */
    public void addAction(final ISelfDrivenAction action) {
        actionQueue.add(action);
    }

    /**
     * @param action
     */
    public void addPriAction(final ISelfDrivenAction action) {
        actionQueue.addPriority(action);
    }

    /**
     * @throws Exception
     */
    public void afterAllModuleLoadOver() throws Exception {
    }

    /**
     * 添加Entity到5分钟缓存
     *
     * @param baseEntity
     */
    @Override
    public void addFiveMinsCache(BaseEntity entity) {
        if (entity.getOperation() != BaseEntity.EntityOperation.NONE) {
            fiveMinsCache.add(entity);
        }
    }

    /**
     * 添加Entity到10分钟缓存
     *
     * @param baseEntity
     */
    @Override
    public void addTenMinsCache(BaseEntity entity) {
        if (entity.getOperation() != BaseEntity.EntityOperation.NONE) {
            tenMinsCache.add(entity);
        }
    }

    /**
     * 添加Entity到20分钟缓存
     *
     * @param baseEntity
     */
    @Override
    public void addTwentyMinsCache(BaseEntity entity) {
        if (entity.getOperation() != BaseEntity.EntityOperation.NONE) {
            twentyMinsCache.add(entity);
        }
    }

    @Override
    public void saveDataForFiveMinutes() {
        putEntityToFiveMinsCache();
        DBHelper dbHelp = DBHelper.obtainDBHelper(DatabaseType.GAME);
        dbHelp.executeSQLBatchForSyncDB(fiveMinsCache, this);
        fiveMinsCache.clear();
    }

    @Override
    public void saveDataForTenMinutes() {
        putEntityToTenMinsCache();
        DBHelper dbHelp = DBHelper.obtainDBHelper(DatabaseType.GAME);
        dbHelp.executeSQLBatchForSyncDB(tenMinsCache, this);
        tenMinsCache.clear();
    }

    @Override
    public void saveDataForTwentyMinutes() {
        putEntityToTwentyMinsCache();
        DBHelper dbHelp = DBHelper.obtainDBHelper(DatabaseType.GAME);
        dbHelp.executeSQLBatchForSyncDB(twentyMinsCache, this);
        twentyMinsCache.clear();
    }

    /**
     * 把Entity放到5分钟缓存。
     *
     * @param baseEntity
     */
    @Override
    public void putEntityToFiveMinsCache() {

    }

    /**
     * 把Entity放到10分钟缓存。
     *
     * @param baseEntity
     */
    @Override
    public void putEntityToTenMinsCache() {

    }

    /**
     * 把Entity放到20分钟缓存。
     *
     * @param baseEntity
     */
    @Override
    public void putEntityToTwentyMinsCache() {

    }
}
