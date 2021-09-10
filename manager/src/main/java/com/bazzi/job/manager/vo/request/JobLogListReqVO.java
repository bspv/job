package com.bazzi.job.manager.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobLogListReqVO extends PageReqVO {
    @ApiModelProperty(value = "任务ID")
    private Integer id;

    @ApiModelProperty(value = "任务名称")
    private String jobName;

    @ApiModelProperty(value = "机器Host")
    private String host;
}
