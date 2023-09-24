package com.euler.resource.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.euler.common.core.domain.R;
import com.euler.common.oss.enumd.OssEnumd;
import com.euler.common.oss.properties.OssProperties;
import com.euler.resource.mapper.SysOssMapper;
import com.euler.common.core.exception.ServiceException;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.mybatis.core.page.PageQuery;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.common.oss.entity.UploadResult;
import com.euler.common.oss.factory.OssFactory;
import com.euler.common.oss.service.IOssStrategy;
import com.euler.resource.domain.SysOss;
import com.euler.resource.domain.bo.SysOssBo;
import com.euler.resource.domain.vo.SysOssVo;
import com.euler.resource.service.ISysOssConfigService;
import com.euler.resource.service.ISysOssService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 文件上传 服务层实现
 *
 * @author euler
 */
@RequiredArgsConstructor
@Service
public class SysOssServiceImpl implements ISysOssService {

    private final SysOssMapper baseMapper;

    @Autowired
    private ISysOssConfigService ossConfigService;

    @Override
    public TableDataInfo<SysOssVo> queryPageList(SysOssBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<SysOss> lqw = buildQueryWrapper(bo);
        Page<SysOssVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    private LambdaQueryWrapper<SysOss> buildQueryWrapper(SysOssBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<SysOss> lqw = Wrappers.lambdaQuery();
        lqw.likeRight(StringUtils.isNotBlank(bo.getFileName()), SysOss::getFileName, bo.getFileName());
        lqw.likeRight(StringUtils.isNotBlank(bo.getOriginalName()), SysOss::getOriginalName, bo.getOriginalName());
        lqw.eq(StringUtils.isNotBlank(bo.getFileSuffix()), SysOss::getFileSuffix, bo.getFileSuffix());
        lqw.eq(StringUtils.isNotBlank(bo.getUrl()), SysOss::getUrl, bo.getUrl());
        lqw.between(params.get("beginCreateTime") != null && params.get("endCreateTime") != null,
            SysOss::getCreateTime, params.get("beginCreateTime"), params.get("endCreateTime"));
        lqw.eq(StringUtils.isNotBlank(bo.getCreateBy()), SysOss::getCreateBy, bo.getCreateBy());
        lqw.eq(StringUtils.isNotBlank(bo.getService()), SysOss::getService, bo.getService());
        return lqw;
    }

    @Override
    public SysOss getById(Long ossId) {
        return baseMapper.selectById(ossId);
    }


    @Override
    public SysOss upload(MultipartFile file) {
        return upload(file, null);
    }


    @Override
    public SysOss upload(MultipartFile file, String type) {
     return  upload(file,type,true);
    }

    @Override
    public SysOss upload(MultipartFile file, String ossType, Boolean isSave) {
        String originalfileName = file.getOriginalFilename();
        String suffix = StringUtils.substring(originalfileName, originalfileName.lastIndexOf("."), originalfileName.length());

        if (StringUtils.isEmpty(ossType))
            ossType= OssEnumd.UPYUN.getValue();

        OssProperties properties = ossConfigService.getOssPropertiesByKey(ossType);
        if (properties==null)
            throw new ServiceException("上传配置不存在，请联系客服处理");

        IOssStrategy storage = OssFactory.instance(ossType,properties);
        UploadResult uploadResult;
        try {
            uploadResult = storage.uploadSuffix(file.getBytes(), suffix, file.getContentType());
        } catch (IOException e) {
            throw new ServiceException(e.getMessage());
        }

        // 保存文件信息
        SysOss oss = new SysOss();
        oss.setUrl(uploadResult.getUrl());
        oss.setFileSuffix(suffix);
        oss.setFileName(uploadResult.getFilename());
        oss.setOriginalName(originalfileName);
        oss.setService(ossType);
        if (isSave) {
            baseMapper.insert(oss);
        }
        return oss;
    }


    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            // 做一些业务上的校验,判断是否需要校验
        }
        List<SysOss> list = baseMapper.selectBatchIds(ids);
        for (SysOss sysOss : list) {
            IOssStrategy storage = OssFactory.instance(sysOss.getService());
            storage.delete(sysOss.getUrl());
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

}
