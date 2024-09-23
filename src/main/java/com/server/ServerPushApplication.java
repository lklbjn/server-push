package com.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动
 *
 * @author lklbjn
 * @DATE 2022/11/22 9:15
 */
@SpringBootApplication
@MapperScan(basePackages = "com.server.mapper")
public class ServerPushApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServerPushApplication.class, args);
    }
}


