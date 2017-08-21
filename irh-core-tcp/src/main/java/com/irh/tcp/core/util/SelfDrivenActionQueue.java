/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  2014-2-25  上午11:26:21
 */
package com.irh.tcp.core.util;

import com.irh.core.util.LogUtil;
import com.irh.tcp.core.config.BaseConfig;
import com.irh.tcp.core.manager.socket.ExcuteCommand;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 自驱动任务队列，队列中同时最多只能一个任务在执行。使用线程池执行任务。
 *
 * @param <T> 所有任务的基类
 * @author iritchie.ren
 */
public class SelfDrivenActionQueue<T extends ISelfDrivenAction>
        implements Runnable {

    /**
     * 执行Task的线程池
     */
    private ExecutorService executorService = null;

    /**
     * 任务队列。队头元素是正在执行的任务。
     */
    private Queue<T> taskQueue = null;

    /** **/
    private Queue<T> priorityQueue = null;

    /**
     * 运行锁，用于确保同时最多只能有一个任务在执行。任务队列本身是线程安全的。
     */
    private ReentrantLock runningLock = null;

    /** **/
    private boolean isRunning = false;

    /**
     * 创建自驱动队列
     *
     * @param exeService
     */
    public SelfDrivenActionQueue(final ThreadPoolExecutor exeService) {
        this.executorService = exeService;
        // 使用无锁线程安全队列
        this.taskQueue = new LinkedList<T>(); //这里不要用ConcurrentLinkedQueue。因为ConcurrentLinkedQueue size() 是一个遍历。
        this.priorityQueue = new LinkedList<T>();
        this.runningLock = new ReentrantLock();
    }

    /**
     * 清空队列
     */
    public void clear() {
        try {
            this.runningLock.lock();
            this.taskQueue.clear();
        } finally {
            this.runningLock.unlock();
        }
    }

    /**
     * 往任务队列中添加任务。
     *
     * @param task
     */
    public void add(final T task) {
        try {
            this.runningLock.lock();
            this.taskQueue.add(task);
            if (!(this.taskQueue.isEmpty() && this.priorityQueue.isEmpty())
                    && !isRunning) {
                isRunning = true;
                // 没有任务在执行，开始执行新添加的。
                this.executorService.execute(this);
            }
        } finally {
            this.runningLock.unlock();
        }
    }

    /**
     * @param task
     */
    public void addPriority(final T task) {
        try {
            this.runningLock.lock();
            this.priorityQueue.add(task);
            if (!(this.taskQueue.isEmpty() && this.priorityQueue.isEmpty())
                    && !isRunning) {
                isRunning = true;
                // 没有任务在执行，开始执行新添加的。
                this.executorService.execute(this);
            }
        } finally {
            this.runningLock.unlock();
        }
    }

    /* 
     * 自驱动队列
     */
    @Override
    public void run() {

        T action = null;
        if (!priorityQueue.isEmpty()) {
            action = this.priorityQueue.peek();
        } else if (!taskQueue.isEmpty()) {
            action = this.taskQueue.peek();
        }

        if (action != null) {
            long startTime = System.currentTimeMillis();

            try {
                action.execute();
            } catch (Exception e) {
                //为了更精确。应该先获得endTime，然后再回调下一个函数。
                long currentRuntime = System.currentTimeMillis() - startTime;
                LogUtil.error(String.format("自驱动队列出错!,执行时间为[%d]！",
                        currentRuntime), e);
            } finally {
                //为了更精确。应该先获得endTime，然后再回调下一个函数。
                long endTime = System.currentTimeMillis();
                complete();
                long currentRuntime = endTime - startTime;
                int cmdRuntime = BaseConfig.getInstance().getActionRuntime();
                if (currentRuntime > cmdRuntime) {
                    if (action instanceof ExcuteCommand) {
                        ExcuteCommand command = (ExcuteCommand) action;
                        LogUtil.warn(String.format(
                                "Action执行超过限制时间，限制的时间为【%d】，而当前执行的时间为【%d】  Action信息:[%s]",
                                cmdRuntime, currentRuntime,
                                command.toString()));
                    } else {
                        LogUtil.warn(String.format(
                                "[%s]执行超过限制时间，限制的时间为【%d】，而当前执行Action的时间为【%d】",
                                action, cmdRuntime, currentRuntime));
                    }

                }

            }
        }
    }

    /**
     * 完成一个任务。 任务完成的时候，必须调用本方法来驱动后续的任务，“自驱动”没有你想像中的智能哈~
     */
    private void complete() {
        try {
            this.runningLock.lock();
            isRunning = false;
            if (!priorityQueue.isEmpty()) {
                this.priorityQueue.poll();
            } else {
                this.taskQueue.poll();
            }

            // 完成一个任务后，如果还有任务，则继续执行。
            if (!this.taskQueue.isEmpty() || !this.priorityQueue.isEmpty()) {
                isRunning = true;
                this.executorService.execute(this);
            }
        } finally {
            this.runningLock.unlock();
        }
    }

    /**
     * 获取任务数量
     *
     * @return
     */
    public int getTaskQueueSize() {
        return taskQueue.size();
    }

    /**
     * @return
     */
    public boolean isEmpty() {
        return taskQueue.isEmpty();
    }

}
