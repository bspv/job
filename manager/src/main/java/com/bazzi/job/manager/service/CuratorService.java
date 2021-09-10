package com.bazzi.job.manager.service;

import com.bazzi.job.common.job.JobHandlerType;
import com.bazzi.job.manager.vo.response.JobViewVO;

import java.util.List;
import java.util.Map;

public interface CuratorService {
    /**
     * 获取JobConstant.JOB_LISTENER_BASE路径的下的子节点，全路径
     *
     * @return 获取host列表
     */
    List<String> listHosts();

    /**
     * 获取各任务在对应节点的任务状态
     *
     * @param hosts 机器host集合
     * @param jobIds  任务ID
     * @return 任务状态
     */
    Map<Integer, List<JobViewVO>> getDataByJobId(List<String> hosts, List<Integer> jobIds);

    /**
     * 如果任务不存在则添加任务，否则忽略
     *
     * @param id             任务ID
     * @param hostIp         节点hostIp，为空则处理所有host节点
     * @param jobHandlerType 处理类型
     */
    void add(Integer id, String hostIp, JobHandlerType jobHandlerType);

    /**
     * 如果任务存在，则更新任务
     *
     * @param id             任务ID
     * @param hostIp         节点hostIp，为空则处理所有host节点
     * @param jobHandlerType 处理类型
     */
    void update(Integer id, String hostIp, JobHandlerType jobHandlerType);

    /**
     * 如果任务存在且正常运行状态，则暂停任务
     *
     * @param id             任务ID
     * @param hostIp         节点hostIp，为空则处理所有host节点
     * @param jobHandlerType 处理类型
     */
    void pause(Integer id, String hostIp, JobHandlerType jobHandlerType);

    /**
     * 如果任务存在且暂停状态，则恢复任务
     *
     * @param id             任务ID
     * @param hostIp         节点hostIp，为空则处理所有host节点
     * @param jobHandlerType 处理类型
     */
    void resume(Integer id, String hostIp, JobHandlerType jobHandlerType);

    /**
     * 如果任务存在则移除任务
     *
     * @param id             任务ID
     * @param hostIp         节点hostIp，为空则处理所有host节点
     * @param jobHandlerType 处理类型
     */
    void delete(Integer id, String hostIp, JobHandlerType jobHandlerType);

    /**
     * 通知各机器节点同步任务信息到zookeeper
     *
     * @param id             任务ID
     * @param hostIp         节点hostIp，为空则处理所有host节点
     * @param jobHandlerType 处理类型
     */
    void listAll(Integer id, String hostIp, JobHandlerType jobHandlerType);

}
