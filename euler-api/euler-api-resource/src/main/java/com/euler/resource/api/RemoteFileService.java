package com.euler.resource.api;

import com.euler.resource.api.domain.SysFile;

/**
 * 文件服务
 *
 * @author euler
 */
public interface RemoteFileService {

    /**
     * 上传文件
     *
     * @param file 文件信息
     * @return 结果
     */
    SysFile upload(String name, String originalFilename, String contentType,String platformType,String ossType, byte[] file);
}
