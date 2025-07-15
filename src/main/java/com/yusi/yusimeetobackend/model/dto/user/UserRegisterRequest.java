package com.yusi.yusimeetobackend.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: 止束
 * @Version: 1.0
 * @DateTime: 2025/7/2 19:54
 * @Description:
 */
@Data
public class UserRegisterRequest implements Serializable {
    private static final long serialVersionUID = -6714308884165511539L;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 密码
     */
    private String userPassword;

    /**
     * 校验密码
     */
    private String checkPassword;

}
