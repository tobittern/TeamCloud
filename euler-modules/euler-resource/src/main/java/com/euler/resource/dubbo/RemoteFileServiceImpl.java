package com.euler.resource.dubbo;

import com.euler.resource.domain.SysOss;
import com.euler.resource.mapper.SysOssMapper;
import com.euler.common.core.exception.ServiceException;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.oss.entity.UploadResult;
import com.euler.common.oss.factory.OssFactory;
import com.euler.common.oss.service.IOssStrategy;
import com.euler.resource.api.RemoteFileService;
import com.euler.resource.api.domain.SysFile;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 文件请求处理
 *
 * @author euler
 */
@Slf4j
@Service
@DubboService
public class RemoteFileServiceImpl implements RemoteFileService {

    @Autowired
    private SysOssMapper sysOssMapper;

    /**
     * 文件上传请求
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public SysFile upload(String name, String originalFilename, String contentType, String platformType,String ossType,byte[] file) {
        try {
            String suffix = StringUtils.substring(originalFilename, originalFilename.lastIndexOf("."), originalFilename.length());
            IOssStrategy storage = OssFactory.instance(ossType);
            UploadResult uploadResult = storage.uploadSuffix(file, suffix, contentType);
            // 保存文件信息
            SysOss oss = new SysOss();
            oss.setUrl(uploadResult.getUrl());
            oss.setFileSuffix(suffix);
            oss.setFileName(uploadResult.getFilename());
            oss.setOriginalName(originalFilename);
            oss.setService(platformType);
            sysOssMapper.insert(oss);
            SysFile sysFile = new SysFile();
            sysFile.setName(uploadResult.getFilename());
            sysFile.setUrl(uploadResult.getUrl());
            return sysFile;
        } catch (Exception e) {
            log.error("上传文件失败", e);
            throw new ServiceException("上传文件失败");
        }
    }

}
