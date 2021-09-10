package com.bazzi.job.manager.vo.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class JobLogResponseVO implements Serializable {
    private static final long serialVersionUID = 7782345656891840056L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "任务ID")
    private Integer ruleId;

    @ApiModelProperty(value = "任务名称")
    private String jobName;

    @ApiModelProperty(value = "任务分组")
    private String jobGroup;

    @ApiModelProperty(value = "机器host")
    private String hostAddr;

    @ApiModelProperty(value = "执行状态，0成功，1失败")
    private Integer status;

    @ApiModelProperty(value = "消耗时间，单位ms")
    private Integer expendTime;

    @ApiModelProperty(value = "描述信息")
    private String errMsg;

    @ApiModelProperty(value = "生成时间")
    private Date createTime;
}
