package com.bazzi.job.platform.service;

import com.bazzi.job.platform.model.JobExecuteLog;

public interface JobService {

    /**
     * 启动所有任务
     */
    void startEnableJobs();

    /**
     * 注册节点监听
     */
    void registerPathListener();

    /**
     * 删除本机在zookeeper相关节点的数据
     */
    void clearZKData();

    /**
     * 记录任务执行日志
     *
     * @param jobLog 执行日志
     */
    void insertJobLog(JobExecuteLog jobLog);
}
