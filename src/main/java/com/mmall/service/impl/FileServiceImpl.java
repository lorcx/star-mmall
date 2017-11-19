package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.mmall.service.IFileService;
import com.mmall.util.FtpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @Author lx
 * @Date 2017/11/19 16:37
 */
@Service("iFileService")
public class FileServiceImpl implements IFileService {

    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    /**
     * 文件上传
     * @param file
     * @param path
     * @return
     */
    public String upload(MultipartFile file, String path) {
        // 文件原始名字
        String fileName = file.getOriginalFilename();
        // 文件扩展名
        String fileExtensionName = fileName.substring(fileName.lastIndexOf(".") + 1);
        // 上传文件名
        String uploadFileName = UUID.randomUUID().toString() + "." +fileExtensionName;
        logger.info("文件开始上传,上传文件名称:{},上传文件路径:{},新文件名:{}", fileName, path, uploadFileName);

        File fileDir = new File(path);
        if (!fileDir.exists()) {
            fileDir.setWritable(true);// 设置可写
            fileDir.mkdirs();// mkdirs 会创建所有不存在的 mkdir只会创建一层
        }

        File tagetFile = new File(path, uploadFileName);
        try {
            // 上传到tomcat 项目下的文件夹
            file.transferTo(tagetFile);

            //上传到ftp服务器上
            FtpUtil.uploadFile(Lists.newArrayList(tagetFile));

            // 删除临时文件
            tagetFile.delete();
        } catch (IOException e) {
            logger.error("上传文件异常", e);
            return null;
        }
        return tagetFile.getName();
    }

}
