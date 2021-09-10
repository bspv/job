package com.bazzi.job.manager.vo.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StringResponseVO implements Serializable {
    private static final long serialVersionUID = -19864153771159955L;

    @ApiModelProperty(value = "提示信息")
    private String tips = "成功";
}
