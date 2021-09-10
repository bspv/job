package com.bazzi.job.manager.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties
public class DefinitionProperties {

    @Value("${spring.cache.redis.key-prefix}")
    private String cachePrefix;

    @Value("${spring.cache.redis.key-prefix-permanent}")
    private String permCachePrefix;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${spring.zookeeper.url}")
    private String zookeeperUrl;

    @Value("${spring.zookeeper.timeout}")
    private Integer zookeeperTimeout;

}
