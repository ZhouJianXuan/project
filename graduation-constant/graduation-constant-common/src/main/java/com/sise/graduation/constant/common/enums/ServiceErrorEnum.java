package com.sise.graduation.constant.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ServiceErrorEnum {
    SYSTEM_ERROR(-999,"系统异常，请联系管理员"),

    LOGIN_MISS_SEQNO(19100001, "参数异常,缺失SeqNo"),

    INVALID_LOGIN(19100031, "invalid token"),

    OPEN_PARAM_ERROR(19100061, "%s 参数错误: %s"),
    ;
    int code;
    String dec;
}
