package com.euler.resource.service;

import com.euler.resource.domain.SysOss;
import com.euler.resource.domain.bo.SysOssBo;
import com.euler.resource.domain.vo.SysOssVo;
import com.euler.common.mybatis.core.page.PageQuery;
import com.euler.common.mybatis.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;

/**
 * 文件上传 服务层
 *
 * @author euler
 */
public interface ISysOssService {

    TableDataInfo<SysOssVo> queryPageList(SysOssBo sysOss, PageQuery pageQuery);

    SysOss getById(Long ossId);

    SysOss upload(MultipartFile file);

    SysOss upload(MultipartFile file, String type);

    SysOss upload(MultipartFile file, String type, Boolean isSave);


    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}
