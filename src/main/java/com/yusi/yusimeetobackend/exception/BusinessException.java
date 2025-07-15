package com.yusi.yusimeetobackend.exception;

import lombok.Getter;

/**
 * @Author: 止束
 * @Version: 1.0
 * @DateTime: 2025/7/2 11:22
 * @Description: 异常类
 */
@Getter
public class BusinessException extends RuntimeException {
    /**
     * 错误码
     */
    private final int code;

    public BusinessException(int code, String message) {
        super(message); //初始化父类的 message
        this.code = code;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage()); //初始化父类的 message
        this.code = errorCode.getCode();
    }

    public BusinessException(ErrorCode errorCode, String message) {
        super(message); //初始化父类的 message
        this.code = errorCode.getCode();
    }

}
