package com.bazzi.job.manager.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobListReqVO extends PageReqVO {
    @ApiModelProperty(value = "任务ID")
    private Integer id;

    @ApiModelProperty(value = "任务状态，0禁用，1启用")
    private Integer jobStatus;

    @ApiModelProperty(value = "规则名称")
    private String ruleName;

    @ApiModelProperty(value = "任务名称")
    private String jobName;

    @ApiModelProperty(value = "机器Host")
    private String host;
}
