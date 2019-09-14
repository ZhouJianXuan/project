package com.sise.graduation.constant.common.exception;

import com.sise.graduation.constant.common.enums.ServiceErrorEnum;
import lombok.Data;

/**
 * @ClassName ServiceException
 * @Description
 * @Author CCJ
 * @Date 2019/9/14 15:20
 **/
@Data
public class ServiceException extends RuntimeException {
    private int code;

    private String msg;

    private static final int ERROR_CODE = -1;

    public ServiceException(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ServiceException(String msg) {
        this.code = ERROR_CODE;
        this.msg = msg;
    }

    public ServiceException(ServiceErrorEnum serviceErrorEnum){
        this.code = serviceErrorEnum.getCode();
        this.msg = serviceErrorEnum.getDec();
    }
}
