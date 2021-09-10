package com.bazzi.job.manager.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
@EnableScheduling
@EnableAsync
public class ScheduleConfig {
	@Value("${spring.pool.size}")
	private int poolSize;
	@Value("${spring.pool.thread-name-prefix}")
	private String threadNamePrefix;
	@Value("${spring.pool.await-termination-seconds}")
	private int awaitTerminationSeconds;
	@Value("${spring.pool.wait-for-tasks-to-complete}")
	private boolean waitForTasksToComplete;

	//线程池
	@Bean(destroyMethod = "shutdown")
	public ThreadPoolTaskScheduler taskScheduler() {
		ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
		scheduler.setPoolSize(poolSize);
		scheduler.setThreadNamePrefix(threadNamePrefix);
		scheduler.setAwaitTerminationSeconds(awaitTerminationSeconds);
		scheduler.setWaitForTasksToCompleteOnShutdown(waitForTasksToComplete);
		return scheduler;
	}

}
