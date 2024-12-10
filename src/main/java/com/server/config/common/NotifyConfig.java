package com.server.config.common;

import com.server.config.property.EmailProperty;
import com.server.config.property.GotifyProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author lklbjn
 */
@Data
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "notify")
public class NotifyConfig {

    /**
     * 邮件发送
     */
    private EmailProperty email = new EmailProperty();

    /**
     * gotify
     */
    private GotifyProperty gotify = new GotifyProperty();

}
