package com.yusi.yusimeetobackend.controller;

import com.yusi.yusimeetobackend.common.BaseResponse;
import com.yusi.yusimeetobackend.common.ResultUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: 止束
 * @Version: 1.0
 * @DateTime: 2025/7/2 14:55
 * @Description:
 */
@RestController
@RequestMapping("/")
public class MainController {
    @RequestMapping("/health")
    public BaseResponse health() {
        return ResultUtils.success("ok");
    }
}
