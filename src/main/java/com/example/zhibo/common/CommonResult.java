package com.example.zhibo.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by jesse on 2021/3/29 下午4:58
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResult implements Serializable {
    private Integer code;
    private String message;
    private Object data;

    public CommonResult(ResultCode resultCode, Object data) {
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
        this.data = data;
    }

    public CommonResult(ResultCode resultCode) {
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
    }

    public static CommonResult success() {
        return new CommonResult(ResultCode.SUCCESS);
    }

    public static CommonResult success(Object data) {
        return new CommonResult(ResultCode.SUCCESS, data);
    }

    public static CommonResult failed() {
        return new CommonResult(ResultCode.FAILED);
    }

    public static CommonResult failed(Object data) {
        return new CommonResult(ResultCode.FAILED, data);
    }
}
