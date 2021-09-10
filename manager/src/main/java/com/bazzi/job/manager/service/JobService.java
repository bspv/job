package com.bazzi.job.manager.service;

import com.bazzi.job.common.generic.Page;
import com.bazzi.job.manager.vo.request.JobListReqVO;
import com.bazzi.job.manager.vo.request.JobLogListReqVO;
import com.bazzi.job.manager.vo.request.JobReqVO;
import com.bazzi.job.manager.vo.response.JobListResponseVO;
import com.bazzi.job.manager.vo.response.JobLogResponseVO;
import com.bazzi.job.manager.vo.response.StringResponseVO;

public interface JobService {
    Page<JobListResponseVO> list(JobListReqVO jobListReqVO);

    StringResponseVO process(JobReqVO jobReqVO);

    Page<JobLogResponseVO> logList(JobLogListReqVO jobLogListReqVO);

    int clearJobExecuteLog();
}
