/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  2014-2-20  下午2:56:59
 */
package com.irh.tcp.core.manager.timer;

import com.irh.core.util.LogUtil;
import com.irh.tcp.core.annotation.Manager;
import com.irh.tcp.core.manager.IManager;
import com.irh.tcp.core.manager.ManagerType;
import com.irh.tcp.core.util.ProjectPathUtil;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.File;
import java.util.Date;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

/**
 * 定时器管理器. 分别对应: 秒>分>小时>日>月>周>年
 *
 * @author iritchie.ren
 */
@Manager(type = ManagerType.TIMER, desc = "定时器管理器")
public final class TimerManager implements IManager {

    /**
     * 1小时
     */
    private static final String GROUP_1HOUR = "GROUP_1HOUR";
    /**
     * 20分钟
     */
    private static final String GROUP_20MINUTES = "GROUP_20MINUTES";

    /**
     * 10分钟
     */
    private static final String GROUP_10MINUTES = "GROUP_10MINUTES";

    /**
     * 5分钟
     */
    private static final String GROUP_5MINUTES = "GROUP_5MINUTES";

    /**
     * 1分钟
     */
    private static final String GROUP_1MINUTE = "GROUP_1MINUTE";

    /**
     * 每日
     */
    private static final String GROUP_DAILY = "GROUP_DAILY";

    /**
     * 一周
     */
    private static final String GROUP_WEEK = "GROUP_WEEK";

    /**
     * 一月
     */
    private static final String GROUP_MONTH = "GROUP_MONTH";

    /**
     *
     */
    private static final TimerManager INSTANCE = new TimerManager();
    /**
     * 调度器
     */
    private Scheduler scheduler = null;

    /**
     *
     */
    private TimerManager() {
    }

    /**
     * @return
     */
    public static TimerManager getInstance() {
        return TimerManager.INSTANCE;
    }

    /**
     *
     */
    @Override
    public void start() throws Exception {
        System.setProperty(StdSchedulerFactory.PROPERTIES_FILE,
                ProjectPathUtil.getConfigDirPath() + File.separator
                        + "quartz.properties");
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        scheduler = schedulerFactory.getScheduler();
        scheduler.start();
    }

    /**
     * 停止
     */
    @Override
    public void stop() throws Exception {
        if (scheduler != null) {
            scheduler.shutdown();
        }
    }

    /**
     * 添加1个小时任务
     */
    public void addOneHourJob(final Class<? extends Job> jobClass) {
        this.addForExpressionJob("1HOUR-TIME_JOB_" + jobClass.getName(),
                TimerManager.GROUP_1HOUR, jobClass, "0 0/60 * * * ?");
    }

    /**
     * 添加20分钟定时任务
     */
    public void addTwentyMinutesJob(final Class<? extends Job> jobClass) {
        this.addForExpressionJob("20MINUTES-TIME_JOB_" + jobClass.getName(),
                TimerManager.GROUP_20MINUTES, jobClass,
                "0 0/20 * * * ?");
    }

    /**
     * 添加10分钟定时任务
     */
    public void addTenMinutesJob(final Class<? extends Job> jobClass) {
        this.addForExpressionJob("10MINUTES-TIME_JOB_" + jobClass.getName(),
                TimerManager.GROUP_10MINUTES, jobClass,
                "0 0/10 * * * ?");
    }

    /**
     * 添加5分钟定时任务
     */
    public void addFiveMinutesJob(final Class<? extends Job> jobClass) {
        this.addForExpressionJob("5MINUTES-TIME_JOB_" + jobClass.getName(),
                TimerManager.GROUP_5MINUTES, jobClass, "0 0/5 * * * ?");
    }

    /**
     * 添加1分钟定时任务
     */
    public void addOneMinuteJob(final Class<? extends Job> jobClass) {
        this.addForExpressionJob("1MINUTE-TIME_JOB_" + jobClass.getName(),
                TimerManager.GROUP_1MINUTE, jobClass, "0 0/1 * * * ?");
    }

    /**
     * @param jobClass
     */
    public void addMonthJob(Class<? extends Job> jobClass) {
        this.addForExpressionJob("MONTH-TIME_JOB_" + jobClass.getName(),
                TimerManager.GROUP_MONTH, jobClass, "0 0 0 1 * ?");
    }

    /**
     * @param jobClass
     */
    public void addWeekJob(Class<? extends Job> jobClass) {
        this.addForExpressionJob("WEEK-TIME_JOB_" + jobClass.getName(),
                TimerManager.GROUP_WEEK, jobClass, "0 0 0 ? * MON");
    }

    /**
     * @param jobClass
     */
    public void addDailyJob(Class<? extends Job> jobClass) {
        this.addForExpressionJob("DAILY-TIME_JOB_" + jobClass.getName(),
                TimerManager.GROUP_DAILY, jobClass, "0 0 0 * * ?");
    }

    /**
     * 添加定时任务
     */
    public void addDelayMinuteJob(final Class<? extends Job> jobClass,
                                  Date startTime, final int minutes) {
        this.addForMinutesJob("DELAY_MINUTIES_JOB_" + jobClass.getName(),
                Scheduler.DEFAULT_GROUP, jobClass, startTime, minutes);
    }


    /**
     * 添加定时任务，分钟周期执行 (重复执行)
     *
     * @param jobClass
     * @param minutes  分钟周期
     */
    public void addForMinutesJob(final Class<? extends Job> jobClass,
                                 final int minutes) {
        this.addForMinutesJob(jobClass, new Date(), minutes);
    }

    /**
     * 添加定时任务，分钟周期执行 (重复执行)
     *
     * @param jobClass
     * @param startTime 调度器开始的时间
     * @param minutes   分钟周期
     */
    public void addForMinutesJob(final Class<? extends Job> jobClass, Date startTime,
                                 final int minutes) {
        this.addForMinutesJob(jobClass.getSimpleName(), jobClass.getSimpleName(), jobClass, startTime, minutes);
    }

    /**
     * 添加定时任务，分钟周期执行 (重复执行)
     *
     * @param jobName
     * @param groupName
     * @param jobClass
     * @param startTime 调度器开始的时间
     * @param minutes   分钟周期
     */
    public void addForMinutesJob(final String jobName, final String groupName,
                                 final Class<? extends Job> jobClass, Date startTime,
                                 final int minutes) {
        this.addForSecondsJob(jobName, groupName, jobClass, startTime, minutes * 60, true);
    }

    /**
     * 添加定时任务，秒周期执行 (重复执行)
     *
     * @param jobClass
     * @param seconds  秒周期
     */
    public void addForSecondsJob(final Class<? extends Job> jobClass, final int seconds) {
        this.addForSecondsJob(jobClass.getSimpleName(), jobClass.getSimpleName(), jobClass, new Date(), seconds, true);
    }

    /**
     * 添加定时任务，秒周期执行 (重复执行)
     *
     * @param jobClass
     * @param startTime 调度器开始的时间
     * @param seconds   秒周期
     */
    public void addForSecondsJob(final Class<? extends Job> jobClass, Date startTime,
                                 final int seconds) {
        this.addForSecondsJob(jobClass.getSimpleName(), jobClass.getSimpleName(), jobClass, startTime, seconds, true);
    }

    /**
     * 添加定时任务，秒周期执行
     *
     * @param jobName
     * @param groupName
     * @param jobClass
     * @param startTime 调度器开始的时间
     * @param seconds   秒周期
     */
    public void addForSecondsJob(final String jobName, final String groupName,
                                 final Class<? extends Job> jobClass, Date startTime,
                                 final int seconds, boolean isRepeat) {
        try {
            JobBuilder jobBuilder = JobBuilder.newJob(jobClass);
            jobBuilder.withIdentity(jobName, groupName);
            JobDetail jobDetail = jobBuilder.build();

            TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder
                    .newTrigger();
            triggerBuilder.withIdentity(jobName, groupName);
            triggerBuilder.startAt(startTime);
            if (isRepeat) {
                triggerBuilder.withSchedule(simpleSchedule()
                        .withIntervalInSeconds(seconds).repeatForever());
            } else {
                triggerBuilder.withSchedule(simpleSchedule()
                        .withIntervalInSeconds(seconds));
            }
            Trigger trigger = triggerBuilder.build();

            scheduler.scheduleJob(jobDetail, trigger);
        } catch (Exception e) {
            LogUtil.error("添加定时任务出错", e);
        }
    }

    /**
     * 添加定时任务
     */
    public void addForExpressionJob(final String jobName,
                                    final Class<? extends Job> jobClass, final String expression) {
        this.addForExpressionJob(jobName, Scheduler.DEFAULT_GROUP, jobClass,
                expression);
    }

    /**
     * 添加定时任务
     */
    private void addForExpressionJob(final String jobName,
                                     final String groupName, final Class<? extends Job> jobClass,
                                     final String expression) {
        try {
            JobBuilder jobBuilder = JobBuilder.newJob(jobClass);
            jobBuilder.withIdentity(jobName, groupName);
            JobDetail jobDetail = jobBuilder.build();

            TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder
                    .newTrigger();
            triggerBuilder.withIdentity(jobName, groupName);
            triggerBuilder.startNow();
            triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(expression));
            Trigger trigger = triggerBuilder.build();

            scheduler.scheduleJob(jobDetail, trigger);
        } catch (Exception e) {
            LogUtil.error("添加定时任务出错", e);
        }
    }

    /**
     * 每小时任务
     */
    public boolean deleteJob(final String jobName, final String jobGroupName) {
        try {
            JobKey jobKey = new JobKey(jobName, jobGroupName);
            if (scheduler.checkExists(jobKey)) {
                scheduler.deleteJob(jobKey);
                LogUtil.info(String.format("删除job name:[%s],group:[%s]成功.",
                        jobName, jobGroupName));
            }
            return true;
        } catch (SchedulerException e) {
            LogUtil.error(String.format("删除job name:[%s],group:[%s]失败。",
                    jobName, jobGroupName), e);
        }
        return false;
    }
}
