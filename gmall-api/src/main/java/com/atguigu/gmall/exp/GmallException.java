package com.atguigu.gmall.exp;


public class GmallException extends RuntimeException {

    private int code;
    private String msg;

    public GmallException(int code, String msg) {
        super(msg);
        this.msg = msg;
        this.code = code;
    }

    public GmallException(GmallExceptionEnum gmallExceptionEnum) {
        super(gmallExceptionEnum.getMsg());
        this.msg = gmallExceptionEnum.getMsg();
        this.code = gmallExceptionEnum.getCode();
    }

}
