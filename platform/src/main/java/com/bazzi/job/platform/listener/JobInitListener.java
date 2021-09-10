package com.bazzi.job.platform.listener;

import com.bazzi.job.platform.service.JobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
@Order(value = 1)
public class JobInitListener implements CommandLineRunner {

    @Resource
    JobService jobService;

    public void run(String... arg0) {
        log.info("JobInitListener----------初始化监听器---------开始");

        jobService.clearZKData();
        log.info("JobInitListener----------清理ZooKeeper历史数据完成");

        jobService.startEnableJobs();
        log.info("JobInitListener----------开启所有启动状态任务，并同步job信息到zookeeper");

        jobService.registerPathListener();
        log.info("JobInitListener----------注册zookeeper监听");

        log.info("JobInitListener----------初始化监听器-----------完成");
    }

}