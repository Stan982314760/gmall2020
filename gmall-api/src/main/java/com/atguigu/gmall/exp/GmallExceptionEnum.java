package com.atguigu.gmall.exp;

import lombok.Getter;

@Getter
public enum GmallExceptionEnum {

    SAVE_ERROR(20002, "数据保存出错");


    private int code;
    private String msg;

    private GmallExceptionEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
