package com.bazzi.job.manager.mapper;

import com.bazzi.job.common.mybatis.BaseMapper;
import com.bazzi.job.manager.model.JobExecuteLog;
import com.bazzi.job.manager.vo.request.JobLogListReqVO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface JobExecuteLogMapper extends BaseMapper<JobExecuteLog> {
    List<JobExecuteLog> selectByParams(JobLogListReqVO jobLogListReqVO);

    int deleteByDay(@Param("day") Date day);
}