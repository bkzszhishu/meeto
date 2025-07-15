package com.yusi.yusimeetobackend.exception;

/**
 * @Author: 止束
 * @Version: 1.0
 * @DateTime: 2025/7/2 11:30
 * @Description: 异常处理工具类，根据条件抛出异常
 */
public class ThrowUtils {
    /**
     * 条件成立则抛异常
     * @param condition 条件
     * @param runtimeException 异常
     */
    public static void throwIf(boolean condition, RuntimeException runtimeException) {
        if (condition) {
            throw runtimeException;
        }
    }

    /**
     * 条件成立则抛异常
     * @param condition 条件
     * @param errorCode 错误码
     */
    public static void throwIf(boolean condition, ErrorCode errorCode) {
        throwIf(condition, new BusinessException(errorCode));
    }

    /**
     * 条件成立则抛异常
     * @param condition 条件
     * @param errorCode 错误码
     * @param message 错误信息
     */
    public static void throwIf(boolean condition, ErrorCode errorCode, String message) {
        throwIf(condition, new BusinessException(errorCode, message));
    }


}
