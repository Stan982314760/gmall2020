package com.atguigu.gmall.result;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class CommonResult {
    private int code;
    private String msg;
    private Map<String, Object> data = new HashMap<>();


    public static CommonResult success() {
        CommonResult commonResult = new CommonResult();
        commonResult.setCode(CommonResultEnum.SUCCESS.getCode());
        commonResult.setMsg(CommonResultEnum.SUCCESS.getMsg());
        return commonResult;
    }

    public static CommonResult error() {
        CommonResult commonResult = new CommonResult();
        commonResult.setCode(CommonResultEnum.FAIL.getCode());
        commonResult.setMsg(CommonResultEnum.FAIL.getMsg());
        return commonResult;
    }

    public CommonResult code(int code) {
        this.setCode(code);
        return this;
    }

    public CommonResult msg(String msg) {
        this.setMsg(msg);
        return this;
    }

    public CommonResult data(String key, Object value) {
        this.getData().put(key, value);
        return this;
    }

    public CommonResult data(Map<String, Object> data) {
        this.setData(data);
        return this;
    }
}
