package com.bazzi.job.common.job;

import com.bazzi.job.common.util.DateUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.quartz.Trigger;

import java.time.LocalDateTime;

import static org.quartz.Trigger.TriggerState;

@Data
@NoArgsConstructor
public class JobView {
    private int jobId;
    private String startTime;
    private String state;
    private int stateIdx;
    private String stateDesc;
    private String currentTime;

    public JobView(int jobId, String startTime, Trigger.TriggerState triggerState) {
        this.jobId = jobId;
        this.startTime = startTime;
        this.state = triggerState.name();
        this.stateIdx = triggerState.ordinal();
        this.currentTime = DateUtil.formatDate(LocalDateTime.now(), DateUtil.FULL_FORMAT);
        if (TriggerState.NONE.equals(triggerState)) {
            stateDesc = "Trigger已经完成，且不会在执行，或者找不到该触发器，或者Trigger已经被删除";
        } else if (TriggerState.NORMAL.equals(triggerState)) {
            stateDesc = "正常";
        } else if (TriggerState.PAUSED.equals(triggerState)) {
            stateDesc = "暂停";
        } else if (TriggerState.COMPLETE.equals(triggerState)) {
            stateDesc = "触发器完成，但是任务可能还正在执行中";
        } else if (TriggerState.ERROR.equals(triggerState)) {
            stateDesc = "出现错误";
        } else if (TriggerState.BLOCKED.equals(triggerState)) {
            stateDesc = "线程阻塞";
        }
    }
}
