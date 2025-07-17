package com.yusi.yusimeetobackend.manager.upload;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.model.ciModel.job.ProcessResult;
import com.qcloud.cos.model.ciModel.persistence.CIObject;
import com.qcloud.cos.model.ciModel.persistence.ImageInfo;
import com.qcloud.cos.model.ciModel.persistence.ProcessResults;
import com.yusi.yusimeetobackend.config.CosClientConfig;
import com.yusi.yusimeetobackend.exception.BusinessException;
import com.yusi.yusimeetobackend.exception.ErrorCode;
import com.yusi.yusimeetobackend.manager.CosManager;
import com.yusi.yusimeetobackend.model.dto.file.UploadPictureResult;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.io.File;
import java.util.Date;
import java.util.List;

@Slf4j
public abstract class PictureUploadTemplate {  
  
    @Resource
    protected CosManager cosManager;
  
    @Resource  
    protected CosClientConfig cosClientConfig;
  
    /**  
     * 模板方法，定义上传流程：
     * 校验图片 -> 上传图片到 COS -> 拿到返回结果 -> 封装返回结果给前端显示
     */  
    public final UploadPictureResult uploadPicture(Object inputSource, String uploadPathPrefix) {
        // 1. 校验图片  
        validPicture(inputSource);  
  
        // 2. 图片上传地址  
        String uuid = RandomUtil.randomString(16);
        String originFilename = getOriginFilename(inputSource);  
        String uploadFilename = String.format("%s_%s.%s", DateUtil.formatDate(new Date()), uuid,
                FileUtil.getSuffix(originFilename));
        String uploadPath = String.format("/%s/%s", uploadPathPrefix, uploadFilename);  
  
        File file = null;
        try {  
            // 3. 创建临时文件  
            file = File.createTempFile(uploadPath, null);  
            // 处理文件来源（本地或 URL）  
            processFile(inputSource, file);  
  
            // 4. 上传图片到对象存储  
            PutObjectResult putObjectResult = cosManager.putPictureObject(uploadPath, file);
            ImageInfo imageInfo = putObjectResult.getCiUploadResult().getOriginalInfo().getImageInfo();
            //获取压缩后的图片的信息
            ProcessResults processResult = putObjectResult.getCiUploadResult().getProcessResults();
            List<CIObject> objectList = processResult.getObjectList();
            if (CollUtil.isNotEmpty(objectList)) {
                //获取上传后的压缩后的图片的信息
                CIObject compressedCiObject = objectList.get(0);
                CIObject thumbnailCiObject = compressedCiObject;
                if (objectList.size() > 1) {
                    //获取上传后的缩略图的图片的信息
                    thumbnailCiObject = objectList.get(1);
                }
                //封装压缩图和缩略图的返回结果
                return buildResult(originFilename, compressedCiObject, thumbnailCiObject);
            }
            // 5. 封装返回结果  
            return buildResult(originFilename, file, uploadPath, imageInfo);  
        } catch (Exception e) {  
            log.error("图片上传到对象存储失败", e);  
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "上传失败");
        } finally {  
            // 6. 清理临时文件  
            deleteTempFile(file);  
        }  
    }

    private UploadPictureResult buildResult(String originFilename, CIObject compressedCiObject, CIObject thumbnailCiObject) {
        UploadPictureResult uploadPictureResult = new UploadPictureResult();
        int picWidth = compressedCiObject.getWidth();
        int picHeight = compressedCiObject.getHeight();
        double picScale = NumberUtil.round(picWidth * 1.0 / picHeight, 2).doubleValue();
        uploadPictureResult.setPicName(FileUtil.mainName(originFilename));
        uploadPictureResult.setPicWidth(picWidth);
        uploadPictureResult.setPicHeight(picHeight);
        uploadPictureResult.setPicScale(picScale);
        uploadPictureResult.setPicFormat(compressedCiObject.getFormat());
        uploadPictureResult.setPicSize(compressedCiObject.getSize().longValue());
        //设置图片为压缩后的地址，因为是这里要返回给前端给前端显示，所以要返回上传后的图片的地址，之前是返回的 png 等的地址
        uploadPictureResult.setUrl(cosClientConfig.getHost() + "/" + compressedCiObject.getKey());
        //设置缩略图的地址返回给前端
        uploadPictureResult.setThumbnailUrl(cosClientConfig.getHost() + "/" + thumbnailCiObject.getKey());
        return uploadPictureResult;
    }

    /**
     * 封装返回结果，这里封装的是压缩后的图片的返回结果
     * @param originFilename
     * @param compressedCiObject
     * @return
     */
    private UploadPictureResult buildResult(String originFilename, CIObject compressedCiObject) {
        UploadPictureResult uploadPictureResult = new UploadPictureResult();
        int picWidth = compressedCiObject.getWidth();
        int picHeight = compressedCiObject.getHeight();
        double picScale = NumberUtil.round(picWidth * 1.0 / picHeight, 2).doubleValue();
        uploadPictureResult.setPicName(FileUtil.mainName(originFilename));
        uploadPictureResult.setPicWidth(picWidth);
        uploadPictureResult.setPicHeight(picHeight);
        uploadPictureResult.setPicScale(picScale);
        uploadPictureResult.setPicFormat(compressedCiObject.getFormat());
        uploadPictureResult.setPicSize(compressedCiObject.getSize().longValue());
        //设置图片为压缩后的地址，因为是这里要返回给前端给前端显示，所以要返回上传后的图片的地址，之前是返回的 png 等的地址
        uploadPictureResult.setUrl(cosClientConfig.getHost() + "/" + compressedCiObject.getKey());
        return uploadPictureResult;
    }

    /**  
     * 校验输入源（本地文件或 URL）  
     */  
    protected abstract void validPicture(Object inputSource);  
  
    /**  
     * 获取输入源的原始文件名  
     */  
    protected abstract String getOriginFilename(Object inputSource);  
  
    /**  
     * 处理输入源并生成本地临时文件  
     */  
    protected abstract void processFile(Object inputSource, File file) throws Exception;  
  
    /**  
     * 封装返回结果  
     */  
    private UploadPictureResult buildResult(String originFilename, File file, String uploadPath, ImageInfo imageInfo) {  
        UploadPictureResult uploadPictureResult = new UploadPictureResult();  
        int picWidth = imageInfo.getWidth();  
        int picHeight = imageInfo.getHeight();  
        double picScale = NumberUtil.round(picWidth * 1.0 / picHeight, 2).doubleValue();
        uploadPictureResult.setPicName(FileUtil.mainName(originFilename));  
        uploadPictureResult.setPicWidth(picWidth);  
        uploadPictureResult.setPicHeight(picHeight);  
        uploadPictureResult.setPicScale(picScale);  
        uploadPictureResult.setPicFormat(imageInfo.getFormat());  
        uploadPictureResult.setPicSize(FileUtil.size(file));  
        uploadPictureResult.setUrl(cosClientConfig.getHost() + "/" + uploadPath);  
        return uploadPictureResult;  
    }  
  
    /**  
     * 删除临时文件  
     */  
    public void deleteTempFile(File file) {  
        if (file == null) {  
            return;  
        }  
        boolean deleteResult = file.delete();  
        if (!deleteResult) {  
            log.error("file delete error, filepath = {}", file.getAbsolutePath());  
        }  
    }  
}
