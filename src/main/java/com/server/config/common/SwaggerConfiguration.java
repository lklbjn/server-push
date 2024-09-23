package com.server.config.common;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author lklbjn
 */
@Configuration
@EnableKnife4j
@EnableSwagger2
//@Profile({"dev", "test"})
public class SwaggerConfiguration {

    private ApiInfo buildApiInfo() {
        return new ApiInfoBuilder()
                .title("API接口文档")
                .description("Swagger使用演示")
                .termsOfServiceUrl("http://blog.csdn.net/myservice网址链接")
                .contact(new Contact("wbz", "http://blog.csdn.net", "sudo_i@126.com"))
                .version("1.0")
                .build();
    }

    @Bean
    public Docket buildDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(buildApiInfo())
                .select()
                //controller类的包路径
                .apis(RequestHandlerSelectors.basePackage("com.vps.controller"))
                .paths(PathSelectors.any())
                .build();
    }
}