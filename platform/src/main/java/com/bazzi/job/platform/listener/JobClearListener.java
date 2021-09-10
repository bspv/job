package com.bazzi.job.platform.listener;

import com.bazzi.job.platform.service.JobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
@Order(value = 1)
public class JobClearListener implements ApplicationListener<ContextClosedEvent> {
    @Resource
    JobService jobService;

    public void onApplicationEvent(ContextClosedEvent event) {
        jobService.clearZKData();
        log.info("JobClearListener----------清理ZK数据-----------完成." + event.getTimestamp());
    }
}
