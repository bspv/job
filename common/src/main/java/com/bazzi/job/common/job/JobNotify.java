package com.bazzi.job.common.job;

import com.bazzi.job.common.util.DateUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
public class JobNotify {
    private int id;
    private int handleType;
    private String currentTime;

    public JobNotify(int id, JobHandlerType jobHandlerType, Date date) {
        this.id = id;
        this.handleType = jobHandlerType.getCode();
        this.currentTime = DateUtil.formatDate(LocalDateTime.now(),DateUtil.FULL_FORMAT);
    }
}
