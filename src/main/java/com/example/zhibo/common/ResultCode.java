package com.example.zhibo.common;

import lombok.Data;

/**
 * Created by jesse on 2021/3/29 下午4:51
 */
public enum ResultCode {
    SUCCESS(1, "成功"),
    FAILED(0, "失败");

    private final Integer code;
    private final String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
