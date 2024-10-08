package com.server.exception;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lklbjn
 */
@Data
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 成功标志
     */
    private boolean success = true;

    /**
     * 返回处理消息
     */
    private String message = "操作成功！";

    /**
     * 返回代码
     */
    private Integer code = 0;

    /**
     * 返回数据对象 data
     */
    private T data;

    /**
     * 时间戳
     */
    private long timestamp = System.currentTimeMillis();

    public Result() {

    }

    public Result<T> success(String message) {
        this.message = message;
        this.code = 200;
        this.success = true;
        return this;
    }

    public static<T> Result<T> ok() {
        Result<T> r = new Result<>();
        r.setSuccess(true);
        r.setCode(200);
        r.setMessage("成功");
        return r;
    }

    public static<T> Result<T> ok(T data) {
        Result<T> r = new Result<>();
        r.setSuccess(true);
        r.setCode(200);
        r.setData(data);
        return r;
    }

    public static<T> Result<T> ok(String msg, T data) {
        Result<T> r = new Result<>();
        r.setSuccess(true);
        r.setCode(200);
        r.setMessage(msg);
        r.setData(data);
        return r;
    }

    public static<T> Result<T> ok(int code,String msg, T data) {
        Result<T> r = new Result<>();
        r.setSuccess(true);
        r.setCode(code);
        r.setMessage(msg);
        r.setData(data);
        return r;
    }

    public static Result<String> error(String msg) {
        return error(500, msg);
    }

    public static Result<String> error(int code, String msg) {
        Result<String> r = new Result<>();
        r.setCode(code);
        r.setMessage(msg);
        r.setSuccess(false);
        return r;
    }

    public Result<T> error500(String message) {
        this.message = message;
        this.code = 500;
        this.success = false;
        return this;
    }
    /**
     * 无权限访问返回结果
     */
    public static Result<String> noAuth(String msg) {
        return error(510, msg);
    }

}