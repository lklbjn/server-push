package com.server.config.common;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 定时任务
 *
 * @author lklbjn
 * @DATE 2024/9/24 15:44
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "scheduler")
public class SchedulerConfig {

    /**
     * cron
     */
    private String cron;

    /**
     * 提醒推送时间
     */
    private Integer expiredDay;

    /**
     * 路径
     */
    private String gotifyUrl;
}
