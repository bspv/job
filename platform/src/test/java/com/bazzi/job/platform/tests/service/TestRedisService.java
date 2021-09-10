package com.bazzi.job.platform.tests.service;

import com.bazzi.job.platform.service.RedisService;
import com.bazzi.job.platform.tests.TestBase;
import org.junit.Test;

import javax.annotation.Resource;

public class TestRedisService extends TestBase {
    @Resource
    RedisService redisService;

    @Test
    public void testGetAndSave(){
        redisService.set("fruit","apple");
        print(redisService.get("fruit"));
    }
}
