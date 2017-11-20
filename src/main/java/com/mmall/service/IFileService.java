package com.mmall.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @Author lx
 * @Date 2017/11/19 16:37
 */
public interface IFileService {
    String upload(MultipartFile file, String path);
}
