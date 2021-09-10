package com.bazzi.job.platform.service.impl;

import com.bazzi.job.common.job.JobHandlerType;
import com.bazzi.job.common.job.JobNotify;
import com.bazzi.job.common.job.JobView;
import com.bazzi.job.common.util.DateUtil;
import com.bazzi.job.common.util.IpUtil;
import com.bazzi.job.common.util.JobConstant;
import com.bazzi.job.common.util.JsonUtil;
import com.bazzi.job.platform.bean.DefineJob;
import com.bazzi.job.platform.config.DefinitionProperties;
import com.bazzi.job.platform.mapper.JobConfigMapper;
import com.bazzi.job.platform.mapper.JobExecuteLogMapper;
import com.bazzi.job.platform.model.JobConfig;
import com.bazzi.job.platform.model.JobExecuteLog;
import com.bazzi.job.platform.service.JobService;
import com.bazzi.job.platform.util.GroovyUtil;
import com.bazzi.job.platform.util.JobUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.data.Stat;
import org.quartz.*;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JobServiceImpl implements JobService {
    @Resource
    private DefinitionProperties definitionProperties;

    @Resource
    private Scheduler scheduler;

    @Resource
    private JobConfigMapper jobConfigMapper;

    @Resource
    private JobExecuteLogMapper jobExecuteLogMapper;

    private CuratorFramework client;

    @PostConstruct
    public void init() {
        initClient();
    }

    public void startEnableJobs() {
        //查询状态为启用的记录
        JobConfig jobConfig = new JobConfig();
        jobConfig.setJobStatus(1);//启用
        List<JobConfig> select = jobConfigMapper.select(jobConfig);

        //过滤掉配置缺失的数据,脚本不符合要求的
        List<JobConfig> collect = select.stream().filter(JobServiceImpl::checkJobConfig).collect(Collectors.toList());

        for (JobConfig jobConf : collect) {
            addOneJob(convertToDefineJob(jobConf), true);
        }
    }


    public void registerPathListener() {
        try {
            String hostIp = IpUtil.getHostIp();
            String path = JobConstant.getJobListenerPath(hostIp);

            Stat stat = client.checkExists().forPath(path);
            if (stat == null)
                client.create().creatingParentsIfNeeded().forPath(path);

            CuratorCache cache = CuratorCache.build(client, path);
            TreeCacheListener treeCacheListener = (curatorFramework, treeCacheEvent) -> {
                TreeCacheEvent.Type treeCacheEventType = treeCacheEvent.getType();
                switch (treeCacheEventType) {
                    case NODE_ADDED: {
                        String curPath = treeCacheEvent.getData().getPath();
                        String data = new String(treeCacheEvent.getData().getData(), StandardCharsets.UTF_8);
                        if (StringUtils.endsWith(curPath, JobConstant.LIST_NODE_NAME)) {
                            viewJobs();
                        } else {
                            JobNotify jobNotify = JsonUtil.parseObject(data, JobNotify.class);
                            int jobId = jobNotify.getId();
                            JobConfig jobConfig = jobConfigMapper.selectByPrimaryKey(jobId);
                            String errMsg = validJobConfig(jobConfig);
                            if (null == errMsg)
                                addOneJob(convertToDefineJob(jobConfig), false);
                            else {
                                JobView jobView = new JobView();
                                jobView.setJobId(jobId);
                                jobView.setStateIdx(-1);
                                jobView.setStateDesc(errMsg);
                                jobView.setCurrentTime(DateUtil.formatDate(new Date(), DateUtil.FULL_FORMAT));
                                viewOneJob(jobView, false);
                            }
                        }
                        log.info("Node added: {}，data:{}", curPath, data);
                        break;
                    }
                    case NODE_UPDATED: {
                        String curPath = treeCacheEvent.getData().getPath();
                        String data = new String(treeCacheEvent.getData().getData(), StandardCharsets.UTF_8);
                        if (StringUtils.endsWith(curPath, JobConstant.LIST_NODE_NAME)) {
                            viewJobs();
                        } else {
                            JobNotify jobNotify = JsonUtil.parseObject(data, JobNotify.class);
                            JobHandlerType type = JobHandlerType.getByCode(jobNotify.getHandleType());
                            JobConfig jobConfig = jobConfigMapper.selectByPrimaryKey(jobNotify.getId());
//                            String errMsg = validJobConfig(jobConfig);
//                            if (null == errMsg) {
                            if (checkJobConfig(jobConfig))
                                if (JobHandlerType.UPDATE.equals(type)) {
                                    updateOneJob(convertToDefineJob(jobConfig));
                                } else if (JobHandlerType.PAUSE.equals(type)) {
                                    pauseOneJob(convertToDefineJob(jobConfig));
                                } else if (JobHandlerType.RESUME.equals(type)) {
                                    resumeOneJob(convertToDefineJob(jobConfig));
                                }
//                            } else {
//                                String viewPath = JobConstant.getJobViewPathForOne(HostAddrUtil.getHostAddr(), jobNotify.getId());
//                                Stat stat = client.checkExists().forPath(viewPath);
//                                if (stat != null) {
//                                    String viewData = new String(client.getData().forPath(viewPath), StandardCharsets.UTF_8);
//                                    JobView jobView = JsonUtil.parseObject(viewData, JobView.class);
//                                    jobView.setStateDesc(errMsg);
//                                    jobView.setCurrentTime(DateUtil.formatDate(new Date(), DateUtil.FULL_FORMAT));
//                                    viewOneJob(jobView, false);
//                                }
//                            }
                        }
                        log.info("Node update: {}，data:{}", curPath, data);
                        break;
                    }
                    case NODE_REMOVED: {
                        String curPath = treeCacheEvent.getData().getPath();
                        String data = new String(treeCacheEvent.getData().getData(), StandardCharsets.UTF_8);
                        if (!StringUtils.endsWith(curPath, JobConstant.LIST_NODE_NAME)) {
                            JobNotify jobNotify = JsonUtil.parseObject(data, JobNotify.class);
                            JobConfig jobConfig = jobConfigMapper.selectByPrimaryKey(jobNotify.getId());
                            deleteOneJob(convertToDefineJob(jobConfig));
                        }
                        log.info("Node remove: {}，data:{}", curPath, data);
                        break;
                    }
                }
            };

            CuratorCacheListener listener = CuratorCacheListener.builder()
                    .forTreeCache(client, treeCacheListener)
                    .afterInitialized()
                    .build();

            cache.listenable().addListener(listener);
            cache.start();

            log.info("ZkService ------- registerPathListener---path:{}", path);
        } catch (
                Exception e) {
            log.error(e.getMessage(), e);
        }

    }

    public void clearZKData() {
        try {
            String hostIp = IpUtil.getHostIp();
            String taskPath = JobConstant.getJobViewPath(hostIp);
            String viewPath = JobConstant.getJobListenerPath(hostIp);

            Stat taskStat = client.checkExists().forPath(taskPath);
            if (taskStat != null)
                client.delete().deletingChildrenIfNeeded().forPath(taskPath);

            Stat viewStat = client.checkExists().forPath(viewPath);
            if (viewStat != null)
                client.delete().deletingChildrenIfNeeded().forPath(viewPath);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public void insertJobLog(JobExecuteLog jobLog) {
        jobExecuteLogMapper.insertSelective(jobLog);
    }

    private static String validJobConfig(JobConfig jobConfig) {
        if (jobConfig == null) {
            log.info("空数据");
            return "jobConfig为空";
        }
        Integer jobId = jobConfig.getId();
        if (jobConfig.getJobStatus() != 1) {
            log.info("JobId({})状态为禁用", jobId);
            return jobId + "状态为禁用";
        }
        if (StringUtils.isBlank(jobConfig.getJobName())) {
            log.info("JobId({})任务名称为空", jobId);
            return jobId + "任务名称为空";
        }
        if (StringUtils.isBlank(jobConfig.getJobGroup())) {
            log.info("JobId({})任务分组为空", jobId);
            return jobId + "任务分组为空";
        }
        if (StringUtils.isBlank(jobConfig.getJobCron())) {
            log.info("JobId({})cron表达式为空", jobId);
            return jobId + "cron表达式为空";
        }
        if (!CronExpression.isValidExpression(jobConfig.getJobCron())) {
            log.info("JobId({})cron表达式错误", jobId);
            return jobId + "cron表达式:" + jobConfig.getJobCron() + "错误";
        }
        if (StringUtils.isBlank(jobConfig.getJobGroovy())) {
            log.info("JobId({})groovy脚本为空", jobId);
            return jobId + "groovy脚本为空";
        }
        try {
            DefineJob defineJob = new DefineJob();
            defineJob.setJobId(jobId);
            defineJob.setJobGroovy(jobConfig.getJobGroovy());
            Class<?> clazz = GroovyUtil.loadClass(defineJob);
            if (!Job.class.isAssignableFrom(clazz)) {
                log.info("JobId({})groovy脚本未实现org.quartz.Job接口", jobId);
                return jobId + "groovy脚本未实现org.quartz.Job接口";
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            log.info("JobId({})groovy脚本加载异常", jobId);
            return jobId + "groovy脚本加载异常";
        }
        return null;
    }

    private static boolean checkJobConfig(JobConfig jobConfig) {
        return null == validJobConfig(jobConfig);
    }

    /**
     * 把所有job信息写到对应路径里，写之前清理原先的数据
     */
    private void viewJobs() {
        try {
            String hostIp = IpUtil.getHostIp();
            String path = JobConstant.getJobViewPath(hostIp);

            Stat stat = client.checkExists().forPath(path);
            if (stat != null)
                client.delete().deletingChildrenIfNeeded().forPath(path);
            client.create().creatingParentsIfNeeded().forPath(path);

            List<JobView> list = JobUtil.listJobs(scheduler);
            for (JobView jobView : list) {
                viewOneJob(jobView, false);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private void addOneJob(DefineJob defineJob, boolean isNeedAddTaskNode) {
        JobUtil.addJob(scheduler, defineJob);
        if (isNeedAddTaskNode) {
            try {
                int id = defineJob.getJobId();
                String taskPath = JobConstant.getJobListenerPathForOne(IpUtil.getHostIp(), id);
                Stat stat = client.checkExists().forPath(taskPath);
                if (stat == null) {
                    JobNotify jobNotify = new JobNotify(id, JobHandlerType.ADD, new Date());
                    client.create().creatingParentsIfNeeded().forPath(taskPath, JsonUtil.toJsonString(jobNotify).getBytes(StandardCharsets.UTF_8));
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        viewOneJob(getJobView(defineJob), false);
    }

    private void updateOneJob(DefineJob defineJob) {
        JobUtil.updateJob(scheduler, defineJob);
        viewOneJob(getJobView(defineJob), false);
    }

    private void pauseOneJob(DefineJob defineJob) {
        JobUtil.pauseJob(scheduler, defineJob);
        viewOneJob(getJobView(defineJob), false);
    }

    private void resumeOneJob(DefineJob defineJob) {
        JobUtil.resumeJob(scheduler, defineJob);
        viewOneJob(getJobView(defineJob), false);
    }

    private void deleteOneJob(DefineJob defineJob) {
        if (StringUtils.isBlank(defineJob.getJobName()))
            JobUtil.deleteJob(scheduler, defineJob.getJobId());
        else
            JobUtil.deleteJob(scheduler, defineJob);
        viewOneJob(getJobView(defineJob), true);
    }

    /**
     * 获取job信息
     *
     * @param defineJob job配置
     * @return job信息
     */
    private JobView getJobView(DefineJob defineJob) {
        try {
            String startTime = DateUtil.formatDate(JobUtil.getTrigger(defineJob).getStartTime(), DateUtil.DEFAULT_FORMAT);
            Trigger.TriggerState triggerState = scheduler.getTriggerState(JobUtil.getTriggerKey(defineJob));
            return new JobView(defineJob.getJobId(), startTime, triggerState);
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 把单个job信息更新到对应路径里
     *
     * @param jobView 任务信息
     * @param isDel   是否为删除
     */
    private void viewOneJob(JobView jobView, boolean isDel) {
        try {

            Integer id = jobView.getJobId();
            String targetPath = JobConstant.getJobViewPathForOne(IpUtil.getHostIp(), id);
            if (isDel) {
                client.delete().deletingChildrenIfNeeded().forPath(targetPath);
                return;
            }
            Stat stat = client.checkExists().forPath(targetPath);
            if (stat == null) {
                client.create().creatingParentsIfNeeded().forPath(targetPath, JsonUtil.toJsonString(jobView).getBytes(StandardCharsets.UTF_8));
            } else {
                client.setData().forPath(targetPath, JsonUtil.toJsonString(jobView).getBytes(StandardCharsets.UTF_8));
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 将jobConfig转换成DefineJob
     *
     * @param jobConfig 任务配置
     * @return job任务信息
     */
    private static DefineJob convertToDefineJob(JobConfig jobConfig) {
        DefineJob defineJob = new DefineJob();
        if (jobConfig != null) {
            defineJob.setJobId(jobConfig.getId());
            defineJob.setJobCron(jobConfig.getJobCron());
            defineJob.setJobName(jobConfig.getJobName());
            defineJob.setJobGroup(jobConfig.getJobGroup());
            defineJob.setJobGroovy(jobConfig.getJobGroovy());
        }
        return defineJob;
    }

    private synchronized void initClient() {
        if (client == null) {
            ExponentialBackoffRetry exponentialBackoffRetry = new ExponentialBackoffRetry(definitionProperties.getZookeeperTimeout(), 3);
            client = CuratorFrameworkFactory.newClient(definitionProperties.getZookeeperUrl(), exponentialBackoffRetry);
            client.start();
        }
    }
}
