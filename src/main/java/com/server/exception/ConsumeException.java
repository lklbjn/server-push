package com.server.exception;


import lombok.Getter;

/**
 * @author lklbjn
 */
@Getter
public class ConsumeException extends RuntimeException {
    private ConsumeExceptionEnum msEnum;
    private Result<String> result;

    public ConsumeException() {
    }

    public ConsumeException(ConsumeExceptionEnum msEnum) {
        super(msEnum.getResult().getMessage());
        this.msEnum = msEnum;
        this.result = msEnum.getResult();
    }

    public ConsumeException(String errorMessage) {
        super(errorMessage);
        this.result = Result.error(-1, errorMessage);
    }

    public ConsumeException(Result<String> result) {
        this.result = result;
    }

}