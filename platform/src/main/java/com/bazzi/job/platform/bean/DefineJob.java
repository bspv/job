package com.bazzi.job.platform.bean;

import lombok.Data;

@Data
public class DefineJob {
    private int jobId;
    private String jobName;
    private String jobGroup;
    private String jobCron;
    private String jobGroovy;
}
