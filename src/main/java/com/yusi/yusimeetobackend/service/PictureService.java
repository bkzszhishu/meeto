package com.yusi.yusimeetobackend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yusi.yusimeetobackend.model.dto.picture.PictureQueryRequest;
import com.yusi.yusimeetobackend.model.dto.picture.PictureReviewRequest;
import com.yusi.yusimeetobackend.model.dto.picture.PictureUploadRequest;
import com.yusi.yusimeetobackend.model.entity.Picture;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yusi.yusimeetobackend.model.entity.User;
import com.yusi.yusimeetobackend.model.vo.PictureVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
* @author 52494
* @description 针对表【picture(图片)】的数据库操作Service
* @createDate 2025-07-15 17:47:54
*/
public interface PictureService extends IService<Picture> {

    /**
     * 上传图片，并且给前端返回封装好的 VO 类
     *
     * @param inputSource 文件上传，可以通过源文件上传，也可以通过 url 上传
     * @param pictureUploadRequest 上传图片的参数
     * @param loginUser 当前登录用户
     * @return
     */
    PictureVO uploadPicture(Object inputSource,
                            PictureUploadRequest pictureUploadRequest,
                            User loginUser);


    QueryWrapper<Picture> getQueryWrapper(PictureQueryRequest pictureQueryRequest);

    PictureVO getPictureVO(Picture picture, HttpServletRequest request);

    Page<PictureVO> getPictureVOPage(Page<Picture> picturePage, HttpServletRequest request);

    void validPicture(Picture picture);

    /**
     * 图片审核
     *
     * @param pictureReviewRequest
     * @param loginUser
     */
    void doPictureReview(PictureReviewRequest pictureReviewRequest, User loginUser);

    /**
     * 填充审核参数
     * @param picture
     * @param loginUser
     */
    void fillReviewParams(Picture picture, User loginUser);
}
