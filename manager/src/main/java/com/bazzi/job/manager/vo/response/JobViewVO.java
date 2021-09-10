package com.bazzi.job.manager.vo.response;

import com.bazzi.job.common.job.JobView;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class JobViewVO {

    @ApiModelProperty(value = "任务ID")
    private int jobId;

    @ApiModelProperty(value = "任务首次执行时间")
    private String startTime;

    @ApiModelProperty(value = "任务运行机器IP")
    private String hostIp;

    @ApiModelProperty(value = "任务运行状态")
    private String state;

    @ApiModelProperty(value = "任务运行状态索引，-1为未运行,0无,1运行中,2暂停,3完成,4错误,5阻塞")
    private int stateIdx;

    @ApiModelProperty(value = "任务运行状态描述")
    private String stateDesc;

    @ApiModelProperty(value = "任务信息更新时间")
    private String currentTime;

    public JobViewVO(int jobId, String hostIp, int stateIdx) {
        this.jobId = jobId;
        this.hostIp = hostIp;
        this.stateIdx = stateIdx;
    }

    public JobViewVO(JobView jobView, String hostIp) {
        BeanUtils.copyProperties(jobView, this);
        this.hostIp = hostIp;
    }
}
