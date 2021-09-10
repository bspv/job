package com.bazzi.job.platform.service;

public interface KafkaService {
    void send(String topic, String msg);
}
