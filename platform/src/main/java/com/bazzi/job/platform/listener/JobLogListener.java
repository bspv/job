package com.bazzi.job.platform.listener;

import com.bazzi.job.common.util.IpUtil;
import com.bazzi.job.platform.model.JobExecuteLog;
import com.bazzi.job.platform.service.JobService;
import com.bazzi.job.platform.util.JobUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.JobListener;

import javax.annotation.Resource;
import java.util.Date;

@Slf4j
public class JobLogListener implements JobListener {

    private static final ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    @Resource
    JobService jobService;

    public String getName() {
        return "jobLogListener";
    }

    public void jobToBeExecuted(JobExecutionContext context) {
        threadLocal.set(System.currentTimeMillis());
        JobKey jobKey = context.getTrigger().getJobKey();
        log.info("Job:{},Group:{} start", jobKey.getName(), jobKey.getGroup());
    }

    public void jobExecutionVetoed(JobExecutionContext context) {
        log.info("jobExecutionVetoed");
    }

    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
        JobKey jobKey = context.getTrigger().getJobKey();
        JobExecuteLog jobLog = new JobExecuteLog();
        jobLog.setJobId(JobUtil.getJobId(context));
        jobLog.setJobName(jobKey.getName());
        jobLog.setJobGroup(jobKey.getGroup());
        jobLog.setHostIp(IpUtil.getHostIp());
        jobLog.setCreateTime(new Date());
        if (jobException != null) {
            jobLog.setStatus(1);//失败
            String message = jobException.getMessage();
            message = message != null && message.length() > 100 ? message.substring(0, 99) : message;
            jobLog.setErrMsg(message);
        } else {
            jobLog.setStatus(0);//成功
        }
        jobLog.setExpendTime((int) (System.currentTimeMillis() - threadLocal.get()));
        jobService.insertJobLog(jobLog);
        log.info("Job:{},Group:{} was Executed,执行状态:{},耗时:{}ms",
                jobLog.getJobName(),
                jobLog.getJobGroup(),
                (jobLog.getStatus() == 1 ? "失败，" + (jobException != null ? jobException.getMessage() : "") : "成功"),
                jobLog.getExpendTime());
        threadLocal.remove();
    }
}