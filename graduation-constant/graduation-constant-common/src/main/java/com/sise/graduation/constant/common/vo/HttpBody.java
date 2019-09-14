package com.sise.graduation.constant.common.vo;

import lombok.Data;

/**
 * @ClassName HttpBody
 * @Description
 * @Author CCJ
 * @Date 2019/9/13 16:26
 **/
@Data
public class HttpBody<T> {
    private T data;

    private int code;

    private String message;

    private int total;

    public static final int OK_CODE = 0;

    public static final String OK_MESSAGE = "OK";

    private static HttpBody SUCCESS = new HttpBody(HttpBody.OK_CODE, HttpBody.OK_MESSAGE, null);

    private HttpBody(int code, String message, T data) {
        this.data = data;
        this.code = code;
        this.message = message;
    }

    private HttpBody(int code, String message, T data, int total) {
        this.data = data;
        this.code = code;
        this.total = total;
        this.message = message;
    }


    public static <T> HttpBody<T> getInstance(T data) {
        return new HttpBody<>(HttpBody.OK_CODE, HttpBody.OK_MESSAGE, data);
    }

    public static <T> HttpBody<T> getInstance(int code, T data) {
        return new HttpBody<>(code, HttpBody.OK_MESSAGE, data);
    }

    public static <T> HttpBody<T> getInstance(int code, String message, T data) {
        return new HttpBody<>(code, message, data);
    }

    public static <T> HttpBody<T> getInstance(int code, String message, T data, int total) {
        return new HttpBody<>(code, message, data, total);
    }
    public static <T> HttpBody<T> getInstance(int code, String message) {
        return new HttpBody<>(code, message, null);
    }

}
