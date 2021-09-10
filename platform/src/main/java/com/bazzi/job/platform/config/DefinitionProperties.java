package com.bazzi.job.platform.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties
public class DefinitionProperties {

    @Value("${spring.mail.username}")
    private String emailFrom;

    @Value("${spring.cache.redis.key-prefix}")
    private String cachePrefix;

    @Value("${spring.cache.redis.key-prefix-permanent}")
    private String permCachePrefix;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${emailTitle}")
    private String emailTitle;

    @Value("${ftlPath}")
    private String ftlPath;

    @Value("${spring.zookeeper.url}")
    private String zookeeperUrl;

    @Value("${spring.zookeeper.timeout}")
    private Integer zookeeperTimeout;
}
