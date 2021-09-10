package com.bazzi.job.manager.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class JobReqVO {
    @NotNull(message = "任务ID不能为空")
    @ApiModelProperty(value = "任务ID，handlerType为5时候，传-1",required = true)
    private Integer id;

    @ApiModelProperty(value = "任务机器host，为空代表所有机器")
    private String hostIp;

    @NotNull(message = "处理类型不能为空")
    @ApiModelProperty(value = "处理类型，0添加，1更新，2暂停，3恢复，4删除，5同步信息",required = true)
    private Integer handlerType;
}
