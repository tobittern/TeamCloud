package com.euler.common.oss.service;


import com.euler.common.oss.entity.UploadResult;
import com.euler.common.oss.enumd.OssEnumd;

import java.io.InputStream;

/**
 * 对象存储策略
 *
 * @author euler
 */
public interface IOssStrategy {

    void createBucket();


    /**
     * 文件上传
     *
     * @param data 文件字节数组
     * @param path 文件路径，包含文件名
     * @return 返回http地址
     */
    UploadResult upload(byte[] data, String path, String contentType);

    /**
     * 文件删除
     *
     * @param path 文件路径，包含文件名
     */
    void delete(String path);

    /**
     * 文件上传
     *
     * @param data   文件字节数组
     * @param suffix 后缀
     * @return 返回http地址
     */
    UploadResult uploadSuffix(byte[] data, String suffix, String contentType);

    /**
     * 文件上传
     *
     * @param inputStream 字节流
     * @param path        文件路径，包含文件名
     * @return 返回http地址
     */
    UploadResult upload(InputStream inputStream, String path, String contentType);

    /**
     * 文件上传
     *
     * @param inputStream 字节流
     * @param suffix      后缀
     * @return 返回http地址
     */
    UploadResult uploadSuffix(InputStream inputStream, String suffix, String contentType);

}
