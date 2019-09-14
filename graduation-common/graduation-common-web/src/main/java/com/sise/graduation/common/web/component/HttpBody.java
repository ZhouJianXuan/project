package com.sise.graduation.common.web.component;

import com.sise.graduation.constant.common.exception.ServiceException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 这个类被ExceptionAspect监控，别乱用，会导致返回发生改变
 *
 * @author jjluo
 * @date 2018/6/21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HttpBody<T> {

    public static final HttpBody<Object> SUCCESS = new HttpBody<Object>(0, "OK");

    public static final int NOTE_CODE = 1;

    /**
     * code 为0 时为成功
     * code 为1 时为有提示语 常量参见 CommonConstant.NOTE_CODE
     * code 为大等于19100000 为错误
     */
    private int code;

    private String message;

    private T data;


    private HttpBody(int code, String message) {
        this.code = code;
        this.message = message;
    }
    
    public static <T> HttpBody<T> getSucInstance(T data) {
        return new HttpBody<>(SUCCESS.code, SUCCESS.message, data);
    }

    public static <T> HttpBody<T> getFailInstance(ServiceException e) {
        return new HttpBody<>(e.getCode(), e.getMsg());
    }

    public static <T> HttpBody<T> getInstance(int code, String message) {
        return new HttpBody<>(code, message);
    }

    public static <T> HttpBody<T> getInstance(int code, String message, T data) {
        return new HttpBody<>(code, message, data);
    }
}
