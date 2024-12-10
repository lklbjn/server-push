package com.server.config.common;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lklbjn
 */
@Data
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "xxl.job")
public class XxlJobConfig {

    private boolean enabled;

    private String addresses;


    private String accessToken;


    private String appName;


    private String address;


    private String ip;


    private int port;


    private String logPath;


    private int logRetentionDays;


    @Bean
    public XxlJobSpringExecutor xxlJobExecutor() {
        if (!enabled) {
            log.info(">>>>>>>>>>> xxl-job not enable");
            return null;
        }
        log.info(">>>>>>>>>>> xxl-job config init.");
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAdminAddresses(addresses);
        xxlJobSpringExecutor.setAppname(appName);
        xxlJobSpringExecutor.setAddress(address);
        // InetUtils inetUtils = new InetUtils(new InetUtilsProperties());
        // log.info("xxl-job config init ip: {}", inetUtils.findFirstNonLoopbackHostInfo().getIpAddress());
        // xxlJobSpringExecutor.setIp(inetUtils.findFirstNonLoopbackHostInfo().getIpAddress());
        xxlJobSpringExecutor.setPort(port);
        xxlJobSpringExecutor.setAccessToken(accessToken);
        xxlJobSpringExecutor.setLogPath(logPath);
        xxlJobSpringExecutor.setLogRetentionDays(logRetentionDays);
        log.info("xxl-job config init success: {}", xxlJobSpringExecutor);
        return xxlJobSpringExecutor;
    }

    /**
     * 针对多网卡、容器内部署等情况，可借助 "spring-cloud-commons" 提供的 "InetUtils" 组件灵活定制注册IP；
     *
     *      1、引入依赖：
     *          <dependency>
     *             <groupId>org.springframework.cloud</groupId>
     *             <artifactId>spring-cloud-commons</artifactId>
     *             <version>${version}</version>
     *         </dependency>
     *
     *      2、配置文件，或者容器启动变量
     *          spring.cloud.inetutils.preferred-networks: 'xxx.xxx.xxx.'
     *
     *      3、获取IP
     *          String ip_ = inetUtils.findFirstNonLoopbackHostInfo().getIpAddress();
     */

}
