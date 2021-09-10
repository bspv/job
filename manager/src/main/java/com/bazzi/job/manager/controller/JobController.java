package com.bazzi.job.manager.controller;

import com.bazzi.job.common.generic.Page;
import com.bazzi.job.common.generic.Result;
import com.bazzi.job.manager.service.CuratorService;
import com.bazzi.job.manager.service.JobService;
import com.bazzi.job.manager.vo.request.JobListReqVO;
import com.bazzi.job.manager.vo.request.JobLogListReqVO;
import com.bazzi.job.manager.vo.request.JobReqVO;
import com.bazzi.job.manager.vo.response.JobListResponseVO;
import com.bazzi.job.manager.vo.response.JobLogResponseVO;
import com.bazzi.job.manager.vo.response.ListVO;
import com.bazzi.job.manager.vo.response.StringResponseVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Api(value = "任务接口", tags = "任务接口")
@RestController
@RequestMapping("/job")
public class JobController {
    @Resource
    private JobService jobService;
    @Resource
    CuratorService curatorService;

    @PostMapping("/list")
    @ApiOperation(value = "任务列表")
    public Result<Page<JobListResponseVO>> list(@RequestBody JobListReqVO jobListReqVO) {
        return Result.success(jobService.list(jobListReqVO));
    }

    @GetMapping("/hosts")
    @ApiOperation(value = "机器host列表")
    public Result<ListVO<String>> hosts() {
        List<String> listHosts = curatorService.listHosts();
        return Result.success(new ListVO<>(listHosts));
    }

    @PostMapping("/process")
    @ApiOperation(value = "处理任务")
    public Result<StringResponseVO> process(@RequestBody JobReqVO jobReqVO) {
        return Result.success(jobService.process(jobReqVO));
    }

    @PostMapping("/logList")
    @ApiOperation(value = "任务执行日志列表")
    public Result<Page<JobLogResponseVO>> logList(@RequestBody JobLogListReqVO jobLogListReqVO) {
        return Result.success(jobService.logList(jobLogListReqVO));
    }
}
