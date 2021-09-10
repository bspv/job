package com.bazzi.job.manager.vo.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class JobListResponseVO implements Serializable {

    private static final long serialVersionUID = 7782345637831840056L;

    @ApiModelProperty(value = "报警规则ID")
    private Integer ruleId;

    @ApiModelProperty(value = "规则名称")
    private String ruleName;

    @ApiModelProperty(value = "规则内容")
    private String ruleContent;

    @ApiModelProperty(value = "cron表达式")
    private String cron;

    @ApiModelProperty(value = "任务名称")
    private String jobName;

    @ApiModelProperty(value = "任务分组")
    private String jobGroup;

    @ApiModelProperty(value = "任务状态，0禁用，1启用")
    private Integer jobStatus;

    @ApiModelProperty(value = "任务运行信息集合")
    List<JobViewVO> jobViewVOList;
}
