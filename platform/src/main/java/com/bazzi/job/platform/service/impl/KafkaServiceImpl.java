package com.bazzi.job.platform.service.impl;

import com.bazzi.job.platform.service.KafkaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class KafkaServiceImpl implements KafkaService {
    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    @Async
    public void send(String topic, String msg) {
        log.info("Send--->Topic:{}, Msg:{}", topic, msg);

        kafkaTemplate.send(topic, msg);
    }

}
