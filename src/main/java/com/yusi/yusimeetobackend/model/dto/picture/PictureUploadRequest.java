package com.yusi.yusimeetobackend.model.dto.picture;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: 止束
 * @Version: 1.0
 * @DateTime: 2025/7/15 19:16
 * @Description:
 */
@Data
public class PictureUploadRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 前端传入 id 用于修改图片，第一次是上传，以后就是修改
     */
    private Long id;
}
