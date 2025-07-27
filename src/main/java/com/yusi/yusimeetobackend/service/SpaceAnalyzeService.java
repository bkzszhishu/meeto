package com.yusi.yusimeetobackend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yusi.yusimeetobackend.model.dto.space.analyze.*;
import com.yusi.yusimeetobackend.model.entity.Picture;
import com.yusi.yusimeetobackend.model.entity.Space;
import com.yusi.yusimeetobackend.model.entity.User;
import com.yusi.yusimeetobackend.model.vo.space.analyze.*;

import java.util.List;

/**
 * @Author: 止束
 * @Version: 1.0
 * @DateTime: 2025/7/27 12:15
 * @Description:
 */
public interface SpaceAnalyzeService {

    SpaceUsageAnalyzeResponse getSpaceUsageAnalyze(SpaceUsageAnalyzeRequest spaceUsageAnalyzeRequest, User loginUser);

    List<SpaceCategoryAnalyzeResponse> getSpaceCategoryAnalyze(SpaceCategoryAnalyzeRequest spaceCategoryAnalyzeRequest, User loginUser);


    List<SpaceTagAnalyzeResponse> getSpaceTagAnalyze(SpaceTagAnalyzeRequest spaceTagAnalyzeRequest, User loginUser);


    List<SpaceSizeAnalyzeResponse> getSpaceSizeAnalyze(SpaceSizeAnalyzeRequest spaceSizeAnalyzeRequest, User loginUser);

    List<SpaceUserAnalyzeResponse> getSpaceUserAnalyze(SpaceUserAnalyzeRequest spaceUserAnalyzeRequest, User loginUser);

    List<Space> getSpaceRankAnalyze(SpaceRankAnalyzeRequest spaceRankAnalyzeRequest, User loginUser);

    void checkSpaceAnalyzeAuth(SpaceAnalyzeRequest spaceAnalyzeRequest, User loginUser);


    void fillAnalyzeQueryWrapper(SpaceAnalyzeRequest spaceAnalyzeRequest, QueryWrapper<Picture> queryWrapper);
}
