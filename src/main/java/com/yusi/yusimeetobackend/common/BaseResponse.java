package com.yusi.yusimeetobackend.common;

import com.yusi.yusimeetobackend.exception.ErrorCode;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: 止束
 * @Version: 1.0
 * @DateTime: 2025/7/2 11:39
 * @Description: 全局响应封装类
 */
@Data
public class BaseResponse<T> implements Serializable {
    private int code;
    private T data;
    private String message;

    public BaseResponse(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public BaseResponse(int code, T data) {
        this(code, data, "");
    }

    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), null, errorCode.getMessage());
    }
}
