package com.bazzi.job.manager.task;

import com.bazzi.job.manager.service.JobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class JobLogClearTask {
    @Resource
    JobService jobService;

    @Scheduled(cron = "0 0 0 1,11,21 * ?")
    public void clearJobLog() {
        long start = System.currentTimeMillis();
        int count = jobService.clearJobExecuteLog();
        log.info("清理任务执行日志记录成功------------数量:{},耗时：{}ms",
                count, (System.currentTimeMillis() - start));
    }
}
