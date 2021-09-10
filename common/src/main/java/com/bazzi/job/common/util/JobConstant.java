package com.bazzi.job.common.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class JobConstant {
    public static final String JOB_BASE = "/log_job";

    public static final String LIST_NODE_NAME = "listAll";//Node /log_job/task/127.0.0.1/listAll 代表刷新job信息到zookeeper

    public static final String JOB_LISTENER_BASE = JOB_BASE + "/task";//监听路径
    public static final String JOB_LISTENER_PATH = JOB_LISTENER_BASE + "/%s";//Node /log_job/task/127.0.0.1
    public static final String JOB_LISTENER_ONE_PATH = JOB_LISTENER_PATH + "/%s";//Node /log_job/task/127.0.0.1/1

    public static final String JOB_VIEW_BASE = JOB_BASE + "/view";//运行任务信息路径
    public static final String JOB_VIEW_PATH = JOB_VIEW_BASE + "/%s";//Node /log_job/view/127.0.0.1
    public static final String JOB_VIEW_ONE_PATH = JOB_VIEW_PATH + "/%s";//Node /log_job/view/127.0.0.1/1

    public static String getJobListenerPath(String hostAddr) {
        return String.format(JOB_LISTENER_PATH, hostAddr);
    }

    public static String getJobListenerPathForOne(String hostAddr, Integer jobId) {
        return String.format(JOB_LISTENER_ONE_PATH, hostAddr, jobId);
    }

    public static String getJobListenerPathForOne(String hostAddr, String node) {
        return String.format(JOB_LISTENER_ONE_PATH, hostAddr, node);
    }

    public static String getJobViewPath(String hostAddr) {
        return String.format(JOB_VIEW_PATH, hostAddr);
    }

    public static String getJobViewPathForOne(String hostAddr, Integer jobId) {
        return String.format(JOB_VIEW_ONE_PATH, hostAddr, jobId);
    }
}
