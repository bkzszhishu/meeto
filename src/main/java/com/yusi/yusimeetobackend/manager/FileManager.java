package com.yusi.yusimeetobackend.manager;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.model.ciModel.persistence.ImageInfo;
import com.yusi.yusimeetobackend.config.CosClientConfig;
import com.yusi.yusimeetobackend.exception.BusinessException;
import com.yusi.yusimeetobackend.exception.ErrorCode;
import com.yusi.yusimeetobackend.exception.ThrowUtils;
import com.yusi.yusimeetobackend.model.dto.file.UploadPictureResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class FileManager {  
  
    @Resource
    private CosClientConfig cosClientConfig;
  
    @Resource  
    private CosManager cosManager;

    /**
     * 上传图片返回上传后的图片信息
     * @param multipartFile 传入的文件
     * @param uploadPathPrefix 传入的文件前缀
     * @return
     */
    public UploadPictureResult uploadPicture(MultipartFile multipartFile, String uploadPathPrefix) {
        //校验图片
        validPicture(multipartFile);
        //构造图片上传地址
        String uuid = RandomUtil.randomString(16);
        //获取原始文件名
        String originFilename = multipartFile.getOriginalFilename();
        //构造上传文件名，上传文件名为：时间戳_uuid.后缀名
        String uploadFilename = String.format("%s_%s.%s", DateUtil.formatDate(new Date()), uuid, FileUtil.getSuffix(originFilename));
        //构造上传路径，上传路径为：/上传该文件的用户信息/上传文件名
        String uploadPath = String.format("/%s/%s", uploadPathPrefix, uploadFilename);
        //进行文件上传
        File file = null;
        try {
            //创建临时文件
            file = File.createTempFile(uploadPath, null);
            multipartFile.transferTo(file); //将用户上传的文件写给本地临时文件
            //上传文件
            //返回上传后的图片信息
            PutObjectResult putObjectResult = cosManager.putPictureObject(uploadPath, file);
            //获得图片信息
            ImageInfo imageInfo = putObjectResult.getCiUploadResult().getOriginalInfo().getImageInfo();
            //封装返回结果
            UploadPictureResult uploadPictureResult = new UploadPictureResult();
            //先获取需要的信息:
            //图片宽度
            int picWidth = imageInfo.getWidth();
            //图片高度
            int picHeight = imageInfo.getHeight();
            //计算图片宽高比
            double picScale = NumberUtil.round(picWidth * 1.0 / picHeight, 2).doubleValue();
            //开始封装
            uploadPictureResult.setUrl(cosClientConfig.getHost() + "/" + uploadPath);
            uploadPictureResult.setPicName(FileUtil.mainName(originFilename));
            uploadPictureResult.setPicSize(FileUtil.size(file));
            uploadPictureResult.setPicWidth(picWidth);
            uploadPictureResult.setPicHeight(picHeight);
            uploadPictureResult.setPicScale(picScale);
            uploadPictureResult.setPicFormat(imageInfo.getFormat()); //图片格式
            return uploadPictureResult;
        } catch (Exception e) {
            log.error("上传图片到对象存储失败 ", e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "上传失败");
        } finally {
            //删除临时文件
            this.deleteTempFile(file);
        }
    }

    public void deleteTempFile(File file) {
        if (file == null) {
            return ;
        }
        //删除临时文件
        boolean deleteResult = file.delete();
        if (!deleteResult) {
            log.error("file delete error, filepath = {}", file.getAbsolutePath());
        }
    }


    /**
     * 校验文件格式
     * @param multipartFile
     */
    public void validPicture(MultipartFile multipartFile) {
        ThrowUtils.throwIf(multipartFile == null, ErrorCode.PARAMS_ERROR, "文件不能为空");
        //1. 校验文件大小
        final long ONE_M = 1024 * 1024;
        //获取文件大小
        long fileSize = multipartFile.getSize();
        ThrowUtils.throwIf(fileSize > 2 * ONE_M, ErrorCode.PARAMS_ERROR, "文件大小不能超过 2MB");
        //2. 校验文件后缀
        //使用 hutool 获取文件后缀名
        String fileSuffix = FileUtil.getSuffix(multipartFile.getOriginalFilename());
        //创建可以使用的文件后缀名列表
        final List<String> ALLOW_FORMAT_LIST = Arrays.asList("jpeg", "png", "jpg", "webp");
        //判断文件后缀名是否符合
        ThrowUtils.throwIf(!ALLOW_FORMAT_LIST.contains(fileSuffix), ErrorCode.PARAMS_ERROR, "文件类型错误");
    }
}
