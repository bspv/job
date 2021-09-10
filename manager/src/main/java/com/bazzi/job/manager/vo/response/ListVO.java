package com.bazzi.job.manager.vo.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListVO<T> implements Serializable {
    private static final long serialVersionUID = -6055270312440957067L;

    @ApiModelProperty(value = "集合")
    private List<T> list;
}
