package com.bazzi.job.manager.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class MonitorTmpVO implements Serializable {
    private static final long serialVersionUID = -4957953597787484104L;

    @ApiModelProperty(value = "类型，0字符串，1字符串数组")
    private Integer tmpType;

    @ApiModelProperty(value = "顺序值，0-5")
    private Integer idx;

    @ApiModelProperty(value = "内容，type为1时，各字符串用{#}拼接")
    private String content;

}