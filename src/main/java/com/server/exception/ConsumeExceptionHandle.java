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
@Order(-2147483648)
@RestControllerAdvice
public class ConsumeExceptionHandle implements InitializingBean {
    private static final Logger log = LoggerFactory.getLogger(ConsumeExceptionHandle.class);

    public ConsumeExceptionHandle() {
    }

    /*@ExceptionHandler({ConsumeException.class})
    public Result<String> methodArgumentNotValidException(ConsumeException e) {
        return e.getResult();
    }*/

    @ExceptionHandler({ConsumeException.class})
    public Result<String> mSRException(ConsumeException e) {
        return e.getResult();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("Global Exception Init Success:{}", ConsumeExceptionHandle.class.getName());
    }
}
