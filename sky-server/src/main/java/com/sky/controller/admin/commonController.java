package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 李阳
 * @Date: 2025/04/12/15:52
 * @Description: 通用接口-文件上传
 */
@RestController
@Slf4j
@Api(tags = "通用接口")
@RequestMapping("/admin/common")
public class commonController {

    @Autowired
    private AliOssUtil aliOssUtil;
    /**
     * 文件上传
     */
    @ApiOperation("文件上传")
    @RequestMapping("/upload")
    // 这里的String是接口规定要返回的图片地址
    public Result<String> upload(MultipartFile file){
        log.info("文件上传路径:{}",file);
        try {
            // 获取文件原始名称
            String originalFilename = file.getOriginalFilename();
            // 截取原始文件名后缀
            String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
            // UUID生成文件名
            String fileName = UUID.randomUUID().toString() + suffix;
            // 文件的请求路径
            String path = aliOssUtil.upload(file.getBytes(), fileName);
            return Result.success(path);

        } catch (IOException e) {
            log.error("文件上传失败:{}",e );
        }
        return Result.error(MessageConstant.UPLOAD_FAILED);
    }
}
