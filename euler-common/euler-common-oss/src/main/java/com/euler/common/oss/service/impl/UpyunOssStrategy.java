package com.euler.common.oss.service.impl;

import com.euler.common.core.utils.StringUtils;
import com.euler.common.oss.entity.UploadResult;
import com.euler.common.oss.enumd.OssEnumd;
import com.euler.common.oss.exception.OssException;
import com.euler.common.oss.properties.OssProperties;
import com.euler.common.oss.service.abstractd.AbstractOssStrategy;
import com.upyun.RestManager;
import okhttp3.Response;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * 又拍云存储策略
 *
 * @author euler
 */
@Component
public class UpyunOssStrategy extends AbstractOssStrategy {

    private RestManager restManager;

    @Override
    public void init(OssProperties ossProperties) {
        super.init(ossProperties);
        try {
            restManager = new RestManager(ossProperties.getBucketName(), ossProperties.getAccessKey(), ossProperties.getSecretKey());
            // 切换 API 接口的域名接入点，默认为自动识别接入点
            restManager.setApiDomain(RestManager.ED_AUTO);

            // 设置连接超时时间，默认为30秒
            restManager.setTimeout(60);

        } catch (Exception e) {
            throw new OssException("又拍云存储配置错误! 请检查系统配置:[" + e.getMessage() + "]");
        }
        isInit = true;
    }

    @Override
    public void createBucket() {

        throw new OssException("创建Bucket失败, 又云拍不支持创建");


    }


    @Override
    public UploadResult upload(byte[] data, String path, String contentType) {
        return upload(new ByteArrayInputStream(data), path, contentType);
    }

    @Override
    public UploadResult upload(InputStream inputStream, String path, String contentType) {
        try {
            Response response = restManager.writeFile(path, inputStream, null);
            if (!response.isSuccessful())
                throw new OssException("上传文件失败："+path);
        } catch (Exception e) {
            throw new OssException("上传文件失败，请检查又拍云配置信息:[" + e.getMessage() + "]");
        }
        return UploadResult.builder().url(getEndpointLink() + "/" + path).filename(path).build();
    }

    @Override
    public void delete(String path) {
        try {
            Response response = restManager.deleteFile(path, null);
            if (!response.isSuccessful())
                throw new OssException("删除文件失败："+path);

        } catch (Exception e) {
            throw new OssException("上传文件失败，请检查又拍云配置信息:[" + e.getMessage() + "]");
        }
    }

    @Override
    public UploadResult uploadSuffix(byte[] data, String suffix, String contentType) {
        return upload(data, getPath(properties.getPrefix(), suffix), contentType);
    }

    @Override
    public UploadResult uploadSuffix(InputStream inputStream, String suffix, String contentType) {
        return upload(inputStream, getPath(properties.getPrefix(), suffix), contentType);
    }

    @Override
    public String getEndpointLink() {
        String endpoint = properties.getEndpoint();
        return endpoint;
    }
}
