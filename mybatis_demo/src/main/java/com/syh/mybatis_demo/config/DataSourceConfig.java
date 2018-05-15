package com.syh.mybatis_demo.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.config.TaskExecutorFactoryBean;

@Configuration
public class DataSourceConfig {

    @Bean
    @Profile("h2")
    public DataSource h2DataSouce() {
        return DataSourceBuilder.create()
                .url("jdbc:h2:~/test")
                .username("sa")
                .driverClassName("org.h2.Driver")
                .build();
    }

    @Bean
    @Profile("mysql")
    public DataSource mysqlDataSouce() {
        return DataSourceBuilder.create()
                .url("jdbc:mysql://localhost:3306/myoj")
                .username("root")
                .password("")
                .driverClassName("com.mysql.jdbc.Driver")
                .build();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(3);
        executor.setMaxPoolSize(5);
        executor.setQueueCapacity(10);
        executor.setThreadNamePrefix("push-to-wang");
        executor.setKeepAliveSeconds(120);
        return executor;
    }
}
