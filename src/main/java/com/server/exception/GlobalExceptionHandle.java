package com.server.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author lklbjn
 */
@Order(Integer.MIN_VALUE + 1)
@RestControllerAdvice
public class GlobalExceptionHandle implements InitializingBean {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandle.class);

    public GlobalExceptionHandle() {
    }

    @ExceptionHandler({Exception.class})
    public Result<String> exception(Exception e) {
        log.error("Global Exception:{}", e.getMessage());
        return Result.error(-99999, "未知的异常:" + e.getMessage());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("Global Exception Init Success:{}", GlobalExceptionHandle.class.getName());
    }
}