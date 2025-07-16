package com.yusi.yusimeetobackend.model.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author: 止束
 * @Version: 1.0
 * @DateTime: 2025/7/16 11:02
 * @Description: 图片标签分类列表视图
 */
@Data
public class PictureTagCategory {
    /**
     * 标签列表
     */
    private List<String> tagList;

    /**
     * 分类列表
     */
    private List<String> categoryList;
}
