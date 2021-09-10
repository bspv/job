package com.bazzi.job.common.generic;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value = "Page")
public class Page<T> implements Serializable {
    private static final long serialVersionUID = -7852250835595333011L;
    @ApiModelProperty(value = "当前页码")
    private Integer pageIdx = 1;// 当前页码

    @ApiModelProperty(value = "每页大小")
    private Integer pageSize = 10;// 每页大小

    @ApiModelProperty(value = "数据集合")
    private List<T> records;// 数据

    @ApiModelProperty(value = "总记录数")
    private Integer totalRow;// 总记录数

    @ApiModelProperty(value = "总页数")
    private Integer totalPage;// 总页数

    @ApiModelProperty(value = "是否有上一页")
    private boolean hasPrev;// 是否有上一页

    @ApiModelProperty(value = "是否有下一页")
    private boolean hasNext;// 是否有下一页

    public Page() {
    }

    public Page(List<T> records, Integer pageIdx, Integer pageSize, Integer totalRow) {
        this.records = records;
        this.pageIdx = pageIdx;
        this.pageSize = pageSize;
        this.totalRow = totalRow;
        this.totalPage = totalRow % pageSize == 0 ? totalRow / pageSize : totalRow / pageSize + 1;
        this.hasPrev = pageIdx > 1;
        this.hasNext = pageIdx < totalPage;
    }

    public static <T> Page<T> of(List<T> records, Integer pageIdx, Integer pageSize, Integer totalRow) {
        Page<T> page = new Page<>();
        page.setPageIdx(pageIdx);
        page.setPageSize(pageSize);
        page.setRecords(records);
        page.setTotalRow(totalRow);
        int totalPage = totalRow % pageSize == 0 ? totalRow / pageSize : totalRow / pageSize + 1;
        page.setTotalPage(totalPage);
        page.setHasPrev(pageIdx > 1);
        page.setHasNext(pageIdx < totalPage);
        return page;
    }

}
