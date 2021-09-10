//package com.bazzi.job.manager.service.impl;
//
//import com.bazzi.job.common.job.JobHandlerType;
//import com.bazzi.job.common.job.JobNotify;
//import com.bazzi.job.common.job.JobView;
//import com.bazzi.job.common.util.JobConstant;
//import com.bazzi.job.common.util.JsonUtil;
//import com.bazzi.job.manager.config.DefinitionProperties;
//import com.bazzi.job.manager.service.CuratorService;
//import com.bazzi.job.manager.vo.response.JobViewVO;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.curator.framework.CuratorFramework;
//import org.apache.curator.framework.CuratorFrameworkFactory;
//import org.apache.curator.retry.ExponentialBackoffRetry;
//import org.apache.zookeeper.data.Stat;
//import org.quartz.Trigger;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//import javax.annotation.Resource;
//import java.nio.charset.StandardCharsets;
//import java.util.*;
//
//@Slf4j
//@Component
//public class CuratorServiceImpl implements CuratorService {
//    @Resource
//    private DefinitionProperties definitionProperties;
//
//    private CuratorFramework client;
//
//    public List<String> listHosts() {
//        try {
//            String path = JobConstant.JOB_LISTENER_BASE;
//            Stat stat = client.checkExists().forPath(path);
//            if (stat == null)
//                return new ArrayList<>();
//
//            return client.getChildren().forPath(path);
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            return new ArrayList<>();
//        }
//    }
//
//    public Map<Integer, List<JobViewVO>> getDataByJobId(List<String> hosts, List<Integer> jobIds) {
//        Map<Integer, List<JobViewVO>> map = new HashMap<>();
//        try {
//            if (hosts == null || hosts.size() == 0 || jobIds == null || jobIds.size() == 0)
//                return map;
//            for (Integer jobId : jobIds) {
//                List<JobViewVO> jobViews = new ArrayList<>();
//                for (String host : hosts) {
//                    JobViewVO jobViewVO;
//                    String curPath = JobConstant.getJobViewPathForOne(host, jobId);
//                    Stat stat = client.checkExists().forPath(curPath);
//                    if (stat == null) {
//                        jobViewVO = new JobViewVO(jobId, host, -1);
//                    } else {
//                        String data = new String(client.getData().forPath(curPath), StandardCharsets.UTF_8);
//                        JobView jobView = JsonUtil.parseObject(data, JobView.class);
//                        jobViewVO = new JobViewVO(jobView, host);
//                    }
//                    jobViews.add(jobViewVO);
//                }
//                map.put(jobId, jobViews);
//            }
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//        }
//        return map;
//    }
//
//    public void add(Integer id, String hostIp, JobHandlerType jobHandlerType) {
//        try {
//            List<String> listHost = getHost(hostIp);
//
//            JobNotify jobNotify = new JobNotify(id, jobHandlerType, new Date());
//            byte[] dataBytes = JsonUtil.toJsonString(jobNotify).getBytes(StandardCharsets.UTF_8);
//            for (String host : listHost) {
//                String path = JobConstant.getJobListenerPathForOne(host, id);
//                Stat stat = client.checkExists().forPath(path);
//                if (stat == null) {
//                    client.create().creatingParentsIfNeeded().forPath(path, dataBytes);
//                }
//            }
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//        }
//    }
//
//    public void update(Integer id, String hostIp, JobHandlerType jobHandlerType) {
//        try {
//            List<String> listHost = getHost(hostIp);
//
//            JobNotify jobNotify = new JobNotify(id, jobHandlerType, new Date());
//            byte[] dataBytes = JsonUtil.toJsonString(jobNotify).getBytes(StandardCharsets.UTF_8);
//            for (String host : listHost) {
//                String path = JobConstant.getJobListenerPathForOne(host, id);
//                Stat stat = client.checkExists().forPath(path);
//                if (stat != null) {
//                    client.setData().forPath(path, dataBytes);
//                }
//            }
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//        }
//    }
//
//    public void pause(Integer id, String hostIp, JobHandlerType jobHandlerType) {
//        try {
//            List<String> listHost = getHost(hostIp);
//
//            JobNotify jobNotify = new JobNotify(id, jobHandlerType, new Date());
//            byte[] dataBytes = JsonUtil.toJsonString(jobNotify).getBytes(StandardCharsets.UTF_8);
//            for (String host : listHost) {
//                String path = JobConstant.getJobListenerPathForOne(host, id);
//                Stat stat = client.checkExists().forPath(path);
//                if (stat != null) {
//                    String viewPath = JobConstant.getJobViewPathForOne(host, id);
//                    if (client.checkExists().forPath(viewPath) == null)
//                        continue;
//                    byte[] bytes = client.getData().forPath(viewPath);
//                    String data = new String(bytes, StandardCharsets.UTF_8);
//                    JobView jobView = JsonUtil.parseObject(data, JobView.class);
//                    if (Trigger.TriggerState.NORMAL.ordinal() == jobView.getStateIdx())
//                        client.setData().forPath(path, dataBytes);
//                }
//            }
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//        }
//    }
//
//    public void resume(Integer id, String hostIp, JobHandlerType jobHandlerType) {
//        try {
//            List<String> listHost = getHost(hostIp);
//
//            JobNotify jobNotify = new JobNotify(id, jobHandlerType, new Date());
//            byte[] dataBytes = JsonUtil.toJsonString(jobNotify).getBytes(StandardCharsets.UTF_8);
//            for (String host : listHost) {
//                String path = JobConstant.getJobListenerPathForOne(host, id);
//                Stat stat = client.checkExists().forPath(path);
//                if (stat != null) {
//                    String viewPath = JobConstant.getJobViewPathForOne(host, id);
//                    if (client.checkExists().forPath(viewPath) == null)
//                        continue;
//                    byte[] bytes = client.getData().forPath(viewPath);
//                    String data = new String(bytes, StandardCharsets.UTF_8);
//                    JobView jobView = JsonUtil.parseObject(data, JobView.class);
//                    if (Trigger.TriggerState.PAUSED.ordinal() == jobView.getStateIdx())
//                        client.setData().forPath(path, dataBytes);
//                }
//            }
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//        }
//    }
//
//    public void delete(Integer id, String hostIp, JobHandlerType jobHandlerType) {
//        try {
//            List<String> listHost = getHost(hostIp);
//
//            for (String host : listHost) {
//                String path = JobConstant.getJobListenerPathForOne(host, id);
//                Stat stat = client.checkExists().forPath(path);
//                if (stat != null) {
//                    client.delete().deletingChildrenIfNeeded().forPath(path);
//                }
//            }
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//        }
//    }
//
//    public void listAll(Integer id, String hostIp, JobHandlerType jobHandlerType) {
//        try {
//            List<String> listHost = getHost(hostIp);
//
//            JobNotify jobNotify = new JobNotify(id, jobHandlerType, new Date());
//            byte[] dataBytes = JsonUtil.toJsonString(jobNotify).getBytes(StandardCharsets.UTF_8);
//            for (String host : listHost) {
//                String path = JobConstant.getJobListenerPathForOne(host, JobConstant.LIST_NODE_NAME);
//                Stat stat = client.checkExists().forPath(path);
//                if (stat == null) {
//                    client.create().creatingParentsIfNeeded().forPath(path, dataBytes);
//                } else {
//                    client.setData().forPath(path, dataBytes);
//                }
//            }
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//        }
//
//    }
//
//    private List<String> getHost(String hostIp) {
//        List<String> listHost;
//        if (StringUtils.isBlank(hostIp)) {
//            listHost = listHosts();
//        } else {
//            listHost = new ArrayList<>();
//            listHost.add(hostIp);
//        }
//        return listHost;
//    }
//
//    @PostConstruct
//    public void init() {
//        initClient();
//    }
//
//    private synchronized void initClient() {
//        if (client == null) {
//            ExponentialBackoffRetry exponentialBackoffRetry = new ExponentialBackoffRetry(definitionProperties.getZookeeperTimeout(), 3);
//            client = CuratorFrameworkFactory.newClient(definitionProperties.getZookeeperUrl(), exponentialBackoffRetry);
//            client.start();
//        }
//    }
//
//}
