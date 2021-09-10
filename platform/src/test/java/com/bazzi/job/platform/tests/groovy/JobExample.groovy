package com.bazzi.job.platform.tests.groovy

import com.bazzi.job.common.util.DateUtil
import com.bazzi.job.platform.service.KafkaService
import com.bazzi.job.platform.service.RedisService
import org.quartz.Job
import org.quartz.JobExecutionContext
import org.quartz.JobExecutionException
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.annotation.Resource

class JobExample implements Job {
    private Logger logger = LoggerFactory.getLogger(JobExample.class);

    @Resource
    private RedisService redisService;
    @Resource
    private KafkaService kafkaService

    void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        def date = DateUtil.formatDate(new Date(), DateUtil.DEFAULT_FORMAT);
        logger.info("时间--------：" + date)
    }
}
