//package com.bazzi.job.manager.service.impl;
//
//import com.bazzi.job.common.ex.BusinessException;
//import com.bazzi.job.common.generic.Page;
//import com.bazzi.job.common.generic.StatusCode;
//import com.bazzi.job.common.job.JobHandlerType;
//import com.bazzi.job.common.util.DateUtil;
//import com.bazzi.job.manager.mapper.JobExecuteLogMapper;
//import com.bazzi.job.manager.model.JobExecuteLog;
//import com.bazzi.job.manager.service.CuratorService;
//import com.bazzi.job.manager.service.JobService;
//import com.bazzi.job.manager.vo.request.JobListReqVO;
//import com.bazzi.job.manager.vo.request.JobLogListReqVO;
//import com.bazzi.job.manager.vo.request.JobReqVO;
//import com.bazzi.job.manager.vo.response.JobListResponseVO;
//import com.bazzi.job.manager.vo.response.JobLogResponseVO;
//import com.bazzi.job.manager.vo.response.JobViewVO;
//import com.bazzi.job.manager.vo.response.StringResponseVO;
//import com.github.pagehelper.PageHelper;
//import com.github.pagehelper.PageInfo;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.quartz.CronExpression;
//import org.springframework.beans.BeanUtils;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//import java.util.*;
//import java.util.stream.Collectors;
//
//@Slf4j
//@Component
//public class JobServiceImpl implements JobService {
//    @Resource
//    CuratorService curatorService;
//
//    @Resource
//    AnalysisRuleMapper analysisRuleMapper;
//
//    @Resource
//    JobExecuteLogMapper jobExecuteLogMapper;
//
//    public Page<JobListResponseVO> list(JobListReqVO jobListReqVO) {
//        int pageIdx = jobListReqVO.getPageIdx();
//        int pageSize = jobListReqVO.getPageSize();
//
//        String host = jobListReqVO.getHost();
//        List<JobListResponseVO> responseVOList = new ArrayList<>();
//        PageHelper.startPage(pageIdx, pageSize);
//        List<AnalysisRule> list = analysisRuleMapper.selectByParams(jobListReqVO);
//
//        Map<String, String> pathMap = new HashMap<>();
//        List<String> hosts = new ArrayList<>();
//        if (StringUtils.isNotBlank(host)) {
//            hosts.add(host);
//        } else {
//            hosts = curatorService.listHosts();
//        }
//
//        List<Integer> jobIds = list.stream().map(AnalysisRule::getRuleId).collect(Collectors.toList());
//        Map<Integer, List<JobViewVO>> map = curatorService.getDataByJobId(hosts, jobIds);
//
//        for (AnalysisRule analysisRule : list) {
//            JobListResponseVO jobListResponseVO = new JobListResponseVO();
//            BeanUtils.copyProperties(analysisRule, jobListResponseVO);
//            Integer jobId = analysisRule.getRuleId();
//            List<JobViewVO> jobViewVOList = new ArrayList<>();
//            if (map.containsKey(jobId))
//                jobViewVOList = map.get(jobId);
//
//            jobListResponseVO.setJobViewVOList(jobViewVOList);
//            responseVOList.add(jobListResponseVO);
//        }
//        return Page.of(responseVOList, pageIdx, pageSize, (int) PageInfo.of(list).getTotal());
//    }
//
//    public StringResponseVO process(JobReqVO jobReqVO) {
//        int handleType = jobReqVO.getHandlerType();
//        JobHandlerType jobHandlerType = JobHandlerType.getByCode(handleType);
//        int jobId = jobReqVO.getId();
//        String hostIp = jobReqVO.getHostIp();
//
//        if (jobHandlerType == null)
//            throw new BusinessException(StatusCode.CODE_140.getCode(),
//                    String.format(StatusCode.CODE_140.getMessage(), handleType));
//        if (JobHandlerType.LIST_ALL.equals(jobHandlerType)) {
//            curatorService.listAll(jobId, hostIp, jobHandlerType);
//            return new StringResponseVO("OK");
//        } else if (JobHandlerType.DELETE.equals(jobHandlerType)) {
//            curatorService.delete(jobId, hostIp, jobHandlerType);
//            return new StringResponseVO("OK");
//        }
//
//        AnalysisRule rule = analysisRuleMapper.selectByPrimaryKey(jobId);
//        if (rule == null)
//            throw new BusinessException(StatusCode.CODE_141.getCode(),
//                    String.format(StatusCode.CODE_141.getMessage(), jobId));
//        if (rule.getJobStatus() != 1)
//            throw new BusinessException(StatusCode.CODE_142.getCode(),
//                    String.format(StatusCode.CODE_142.getMessage(), jobId));
//        if (StringUtils.isBlank(rule.getJobName()))
//            throw new BusinessException(StatusCode.CODE_143.getCode(),
//                    String.format(StatusCode.CODE_143.getMessage(), jobId));
//        if (StringUtils.isBlank(rule.getJobGroup()))
//            throw new BusinessException(StatusCode.CODE_144.getCode(),
//                    String.format(StatusCode.CODE_144.getMessage(), jobId));
//
//        String cron = rule.getCron();
//        if (StringUtils.isBlank(cron))
//            throw new BusinessException(StatusCode.CODE_145.getCode(),
//                    String.format(StatusCode.CODE_145.getMessage(), jobId));
//        if (!CronExpression.isValidExpression(cron))
//            throw new BusinessException(StatusCode.CODE_146.getCode(),
//                    String.format(StatusCode.CODE_146.getMessage(), jobId, cron));
//        String script = rule.getRuleContent();
//        if (StringUtils.isBlank(script))
//            throw new BusinessException(StatusCode.CODE_147.getCode(),
//                    String.format(StatusCode.CODE_147.getMessage(), jobId));
//
//        if (JobHandlerType.ADD.equals(jobHandlerType)) {
//            curatorService.add(jobId, hostIp, jobHandlerType);
//        } else if (JobHandlerType.UPDATE.equals(jobHandlerType)) {
//            curatorService.update(jobId, hostIp, jobHandlerType);
//        } else if (JobHandlerType.PAUSE.equals(jobHandlerType)) {
//            curatorService.pause(jobId, hostIp, jobHandlerType);
//        } else if (JobHandlerType.RESUME.equals(jobHandlerType)) {
//            curatorService.resume(jobId, hostIp, jobHandlerType);
//        }
//        return new StringResponseVO("OK");
//    }
//
//    public Page<JobLogResponseVO> logList(JobLogListReqVO jobLogListReqVO) {
//        int pageIdx = jobLogListReqVO.getPageIdx();
//        int pageSize = jobLogListReqVO.getPageSize();
//        PageHelper.startPage(pageIdx, pageSize);
//        List<JobExecuteLog> list = jobExecuteLogMapper.selectByParams(jobLogListReqVO);
//        List<JobLogResponseVO> listVO = new ArrayList<>();
//        for (JobExecuteLog jobExecuteLog : list) {
//            JobLogResponseVO jobLogResponseVO = new JobLogResponseVO();
//            BeanUtils.copyProperties(jobExecuteLog, jobLogResponseVO);
//            listVO.add(jobLogResponseVO);
//        }
//        return Page.of(listVO, pageIdx, pageSize, (int) PageInfo.of(list).getTotal());
//    }
//
//    public int clearJobExecuteLog() {
//        Date day = DateUtil.convertToDate(DateUtil.getNextDay(DateUtil.convertToLocalDateTime(new Date()), -10));
//        return jobExecuteLogMapper.deleteByDay(day);
//    }
//}
