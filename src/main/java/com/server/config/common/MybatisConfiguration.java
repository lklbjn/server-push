package com.server.config.common;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lklbjn
 */
@Configuration
public class MybatisConfiguration {

    @Bean
    public MybatisInterceptor getMybatisInterceptor() {
        return new MybatisInterceptor();
    }

    @Bean
    public SqlPrintInterceptor getSqlPrintInterceptor(){
        return new SqlPrintInterceptor();
    }

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }
}