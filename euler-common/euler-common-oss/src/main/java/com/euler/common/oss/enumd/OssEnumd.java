package com.euler.common.oss.enumd;

import com.euler.common.oss.service.impl.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 对象存储服务商枚举
 *
 * @author euler
 */
@Getter
@AllArgsConstructor
public enum OssEnumd {

    /**
     * 七牛云
     */
    QINIU("qiniu", QiniuOssStrategy.class),

    /**
     * 阿里云
     */
    ALIYUN("aliyun", AliyunOssStrategy.class),

    /**
     * 腾讯云
     */
    QCLOUD("qcloud", QcloudOssStrategy.class),

    /**
     * 开放平台用户
     */
    UPYUNOPEN("upyun_open", UpyunOssStrategy.class),
    /**
     * SDK用户
     */
    SDKUSER("upyun_sdk", UpyunOssStrategy.class),
    /**
     * 又拍云上传
     */
    UPYUN("upyun", UpyunOssStrategy.class),

    /**
     *sdk补丁上传
     */
    UPYUNPATCH("upyun_patch", UpyunOssStrategy.class),
    /**
     * minio
     */
    MINIO("minio", MinioOssStrategy.class);



    private final String value;

    private final Class<?> beanClass;

    public static OssEnumd find(String value) {
        for (OssEnumd enumd : values()) {
            if (enumd.getValue().equals(value)) {
                return enumd;
            }
        }
        return null;
    }

}
