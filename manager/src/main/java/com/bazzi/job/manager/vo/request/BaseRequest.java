package com.bazzi.job.manager.vo.request;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class BaseRequest implements Serializable {

    private Date createTime;

    private Date updateTime;


}
