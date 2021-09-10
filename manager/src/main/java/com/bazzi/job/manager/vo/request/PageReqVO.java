package com.bazzi.job.manager.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class PageReqVO {
    @NotNull(message = "页码不能为空")
    @Min(value = 1,message = "页码不能小于1")
    @ApiModelProperty(value = "页码", required = true)
    private Integer pageIdx = 1;

    @NotNull(message = "每页数量不能为空")
    @Min(value = 1,message = "每页数量不能小于1")
    @ApiModelProperty(value = "每页数量", required = true)
    private Integer pageSize = 10;
}
