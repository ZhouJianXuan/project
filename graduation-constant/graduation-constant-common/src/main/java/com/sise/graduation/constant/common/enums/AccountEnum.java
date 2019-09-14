package com.sise.graduation.constant.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AccountEnum {
    EXIST((byte)0, "存在"),
    DELETED((byte)1, "已删除"),

    ADMIN((byte)0,"管理员账号"),
    GENERAL((byte)1,"普通账号"),
    ;
    byte code;
    String dec;
}
