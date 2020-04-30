package com.atguigu.gmall.result;

import lombok.Getter;

@Getter
public enum CommonResultEnum {
    SUCCESS(20000, "操作成功"),
    FAIL(20001, "操作失败");

    private int code;
    private String msg;

    private CommonResultEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
