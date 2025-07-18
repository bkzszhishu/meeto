package com.yusi.yusimeetobackend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yusi.yusimeetobackend.model.entity.Space;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yusi.yusimeetobackend.model.vo.SpaceVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author 52494
* @description 针对表【space(空间)】的数据库操作Service
* @createDate 2025-07-18 15:12:08
*/
public interface SpaceService extends IService<Space> {

    /**
     * 校验前端传来的空间数据
     * @param space 空间对象
     * @param add 判断此时是修改还是添加，如果是添加，则为true，否则为false
     */
    void validSpace(Space space, boolean add);

    Page<SpaceVO> getSpaceVOPage(Page<Space> spacePage, HttpServletRequest request);
}
