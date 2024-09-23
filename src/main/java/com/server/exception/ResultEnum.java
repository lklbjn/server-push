package com.server.exception;

/**
 * @author lklbjn
 */

public enum ResultEnum implements ConsumeExceptionEnum {
    //这里是可以自己定义的，方便与前端交互即可
    SUCCESS(Result.ok()),
    UNKNOWN_ERROR(Result.error(-1, "未知错误")),
    ELECTRIC_NOT_EXIST(Result.error("该条数据不存在！")),
    NO_DATA_BY_YEAR(Result.error("这一年没有数据哦！")),
    THE_YEAR_NOT_START(Result.error("这年还没开始呢！")),
    NO_DATA_BY_MONTH(Result.error("这个月没有数据哦！")),
    THE_MONTH_NOT_START(Result.error("这个月还没开始呢！")),
    NO_DATA_BY_DAY(Result.error("这一天没有数据哦！")),
    THE_DAY_NOT_START(Result.error("今天都还没过完呢！")),
    ;

    private final Result<String> result;

    ResultEnum(Result<String> result) {
        this.result = result;
    }

    @Override
    public Result<String> getResult() {
        return result;
    }
}