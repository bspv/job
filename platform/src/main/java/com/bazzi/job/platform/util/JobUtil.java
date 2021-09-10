package com.bazzi.job.platform.util;

import com.bazzi.job.common.job.JobView;
import com.bazzi.job.common.util.DateUtil;
import com.bazzi.job.platform.bean.DefineJob;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Slf4j
public final class JobUtil {
    private static final String JOB_ID = "JobId";

    public static List<JobView> listJobs(Scheduler scheduler) {
        List<JobView> list = new ArrayList<>();
        try {
            Set<TriggerKey> triggerKeys = scheduler.getTriggerKeys(GroupMatcher.anyGroup());
            for (TriggerKey triggerKey : triggerKeys) {
                Trigger trigger = scheduler.getTrigger(triggerKey);

                String startTime = DateUtil.formatDate(
                        scheduler.getTrigger(triggerKey).getStartTime(),
                        DateUtil.DEFAULT_FORMAT);
                Trigger.TriggerState triggerState = scheduler.getTriggerState(triggerKey);
                int jobId = Integer.parseInt(String.valueOf(
                        scheduler.getJobDetail(trigger.getJobKey()).getJobDataMap().get(JOB_ID)));

                list.add(new JobView(jobId, startTime, triggerState));
                log.info("List_Jobs======JobId:{},JobStatus:{},JobStartTime:{}", jobId, triggerState.name(), startTime);
            }
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
        }
        return list;
    }

    public static String addJob(Scheduler scheduler, DefineJob defineJob) {
        if (scheduler == null || defineJob == null)
            return null;
        log.info("addJob 入参jobName={}，groupName={}，cronTime={}",
                getJobName(defineJob), defineJob.getJobGroup(), defineJob.getJobCron());
        try {
            JobDetail jobDetail = getJobDetail(defineJob);
            Trigger trigger = getTrigger(defineJob);
            jobDetail.getJobDataMap().put(JOB_ID, defineJob.getJobId());
            Date date = scheduler.scheduleJob(jobDetail, trigger);
            // 启动
            if (!scheduler.isShutdown()) {
                scheduler.start();
            }

            String formatDate = DateUtil.formatDate(date, DateUtil.DEFAULT_FORMAT);
            log.info("addJob 成功 jobName={}，groupName={}，cronTime={}，下次运行时间:{}",
                    getJobName(defineJob), defineJob.getJobGroup(), defineJob.getJobCron(), formatDate);
            return formatDate;
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
            log.info("addJob 失败 jobName={}，groupName={}，cronTime={}，错误信息:{}",
                    getJobName(defineJob), defineJob.getJobGroup(), defineJob.getJobCron(), e.getMessage());
            return null;
        }
    }

    public static String updateJob(Scheduler scheduler, DefineJob defineJob) {
        log.info("updateJob 入参jobName={}，groupName={}，cronTime={}",
                getJobName(defineJob), defineJob.getJobGroup(), defineJob.getJobCron());
        deleteJob(scheduler, defineJob);
        String date = addJob(scheduler, defineJob);
        log.info("updateJob 成功 jobName={}，groupName={}，cronTime={}，下次运行时间:{}",
                getJobName(defineJob), defineJob.getJobGroup(), defineJob.getJobCron(), date);
        return date;
    }

    public static void pauseJob(Scheduler scheduler, DefineJob defineJob) {
        try {
            scheduler.pauseJob(getJobKey(defineJob));
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
        }
    }

    public static void resumeJob(Scheduler scheduler, DefineJob defineJob) {
        try {
            scheduler.resumeJob(getJobKey(defineJob));
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
        }
    }

    public static void deleteJob(Scheduler scheduler, DefineJob defineJob) {
        try {
            scheduler.deleteJob(getJobKey(defineJob));
            removeJobClass(defineJob);
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
        }
    }

    public static void deleteJob(Scheduler scheduler, Integer jobId) {
        try {
            Set<TriggerKey> triggerKeys = scheduler.getTriggerKeys(GroupMatcher.anyGroup());
            for (TriggerKey triggerKey : triggerKeys) {
                Trigger trigger = scheduler.getTrigger(triggerKey);
                int curJobId = Integer.parseInt(String.valueOf(
                        scheduler.getJobDetail(trigger.getJobKey()).getJobDataMap().get(JOB_ID)));
                if (curJobId == jobId) {
                    scheduler.deleteJob(trigger.getJobKey());
                    DefineJob defineJob = new DefineJob();
                    defineJob.setJobId(curJobId);
                    removeJobClass(defineJob);
                }
            }
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
        }
    }

    public static JobDetail getJobDetail(DefineJob defineJob) {
        return JobBuilder.newJob(getJobClass(defineJob))
                .withIdentity(getJobName(defineJob), defineJob.getJobGroup())// 任务名称和组构成任务key
                .build();
    }

    public static Trigger getTrigger(DefineJob defineJob) {
        return TriggerBuilder
                .newTrigger()
                .withIdentity(getTriggerName(defineJob), defineJob.getJobGroup())
                .withSchedule(CronScheduleBuilder.cronSchedule(defineJob.getJobCron()))
                .build();
    }

    public static JobKey getJobKey(DefineJob defineJob) {
        return JobKey.jobKey(getJobName(defineJob), defineJob.getJobGroup());
    }

    public static TriggerKey getTriggerKey(DefineJob defineJob) {
        return new TriggerKey(getTriggerName(defineJob), defineJob.getJobGroup());
    }

    public static int getJobId(JobExecutionContext context) {
        Object jobIdObj = context.getJobDetail().getJobDataMap().get(JOB_ID);
        return Integer.parseInt(String.valueOf(jobIdObj));
    }

    private static String getJobName(DefineJob defineJob) {
        return defineJob.getJobName();
    }

    private static String getTriggerName(DefineJob defineJob) {
        return defineJob.getJobName() + "-TriggerName";
    }

    @SuppressWarnings("unchecked")
    private static Class<? extends Job> getJobClass(DefineJob defineJob) {
        return (Class<? extends Job>) GroovyUtil.loadClass(defineJob);
    }

    private static void removeJobClass(DefineJob defineJob) {
        GroovyUtil.remove(defineJob);
    }
}
