package com.bazzi.job.platform.model;

import javax.persistence.Id;
import java.util.Date;

public class JobConfig {
    @Id
    private Integer id;

    private String jobName;

    private String jobNameCn;

    private String jobGroup;

    private String jobCron;

    private Integer jobStatus;

    private String jobDesc;

    private Date updateTime;

    private Date createTime;

    private String jobGroovy;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName == null ? null : jobName.trim();
    }

    public String getJobNameCn() {
        return jobNameCn;
    }

    public void setJobNameCn(String jobNameCn) {
        this.jobNameCn = jobNameCn == null ? null : jobNameCn.trim();
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup == null ? null : jobGroup.trim();
    }

    public String getJobCron() {
        return jobCron;
    }

    public void setJobCron(String jobCron) {
        this.jobCron = jobCron == null ? null : jobCron.trim();
    }

    public Integer getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(Integer jobStatus) {
        this.jobStatus = jobStatus;
    }

    public String getJobDesc() {
        return jobDesc;
    }

    public void setJobDesc(String jobDesc) {
        this.jobDesc = jobDesc == null ? null : jobDesc.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getJobGroovy() {
        return jobGroovy;
    }

    public void setJobGroovy(String jobGroovy) {
        this.jobGroovy = jobGroovy == null ? null : jobGroovy.trim();
    }
}