package com.euler.resource.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.euler.common.core.constant.Constants;
import com.euler.common.core.utils.JsonHelper;
import com.euler.common.oss.properties.OssProperties;
import com.euler.resource.domain.SysOssConfig;
import com.euler.resource.domain.vo.SysOssConfigVo;
import com.euler.resource.mapper.SysOssConfigMapper;
import com.google.common.collect.Lists;
import com.euler.common.core.constant.UserConstants;
import com.euler.common.core.exception.ServiceException;
import com.euler.common.core.utils.JsonUtils;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.mybatis.core.page.PageQuery;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.common.oss.constant.OssConstant;
import com.euler.common.oss.factory.OssFactory;
import com.euler.common.redis.utils.RedisUtils;
import com.euler.resource.domain.bo.SysOssConfigBo;
import com.euler.resource.service.ISysOssConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * 对象存储配置Service业务层处理
 *
 * @author euler
 * @author 孤舟烟雨
 * @date 2021-08-13
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class SysOssConfigServiceImpl implements ISysOssConfigService {

    private final SysOssConfigMapper baseMapper;

    /**
     * 项目启动时，初始化参数到缓存，加载配置类
     */
    @Override
    public void init() {
        List<SysOssConfig> list = baseMapper.selectList();
        // 加载OSS初始化配置
        for (SysOssConfig config : list) {
            setConfigCache(config, config.getConfigKey());
        }
    }

    @Override
    public SysOssConfigVo queryById(Integer ossConfigId) {
        return baseMapper.selectVoById(ossConfigId);
    }

    @Override
    public TableDataInfo<SysOssConfigVo> queryPageList(SysOssConfigBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<SysOssConfig> lqw = buildQueryWrapper(bo);
        Page<SysOssConfigVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }


    private LambdaQueryWrapper<SysOssConfig> buildQueryWrapper(SysOssConfigBo bo) {
        LambdaQueryWrapper<SysOssConfig> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getConfigKey()), SysOssConfig::getConfigKey, bo.getConfigKey());
        lqw.likeRight(StringUtils.isNotBlank(bo.getBucketName()), SysOssConfig::getBucketName, bo.getBucketName());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), SysOssConfig::getStatus, bo.getStatus());
        return lqw;
    }

    @Override
    public Boolean insertByBo(SysOssConfigBo bo) {
        SysOssConfig config = BeanUtil.toBean(bo, SysOssConfig.class);
        validEntityBeforeSave(config);
        boolean flag = baseMapper.insert(config) > 0;
        if (flag)
            setConfigCache(config, config.getConfigKey());
        return flag;
    }

    @Override
    public Boolean updateByBo(SysOssConfigBo bo) {
        SysOssConfig config = BeanUtil.toBean(bo, SysOssConfig.class);
        validEntityBeforeSave(config);
        LambdaUpdateWrapper<SysOssConfig> luw = new LambdaUpdateWrapper<>();
        luw.set(StringUtils.isBlank(config.getPrefix()), SysOssConfig::getPrefix, "");
        luw.set(StringUtils.isBlank(config.getRegion()), SysOssConfig::getRegion, "");
        luw.set(StringUtils.isBlank(config.getExt1()), SysOssConfig::getExt1, "");
        luw.eq(SysOssConfig::getOssConfigId, config.getOssConfigId());
        boolean flag = baseMapper.update(config, luw) > 0;
        if (flag)
            setConfigCache(config, config.getConfigKey());

        return flag;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(SysOssConfig entity) {
        if (StringUtils.isNotEmpty(entity.getConfigKey())
            && UserConstants.NOT_UNIQUE.equals(checkConfigKeyUnique(entity))) {
            throw new ServiceException("操作配置'" + entity.getConfigKey() + "'失败, 配置key已存在!");
        }
    }

    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            if (CollUtil.containsAny(ids, OssConstant.SYSTEM_DATA_IDS)) {
                throw new ServiceException("系统内置, 不可删除!");
            }
        }
        List<SysOssConfig> list = Lists.newArrayList();
        for (Long configId : ids) {
            SysOssConfig config = baseMapper.selectById(configId);
            list.add(config);
        }
        boolean flag = baseMapper.deleteBatchIds(ids) > 0;
        if (flag) {
            list.stream().forEach(sysOssConfig -> {
                RedisUtils.deleteObject(getCacheKey(sysOssConfig.getConfigKey()));
            });
        }
        return flag;
    }

    /**
     * 判断configKey是否唯一
     */
    private String checkConfigKeyUnique(SysOssConfig sysOssConfig) {
        long ossConfigId = ObjectUtil.isNull(sysOssConfig.getOssConfigId()) ? -1L : sysOssConfig.getOssConfigId();
        SysOssConfig info = baseMapper.selectOne(new LambdaQueryWrapper<SysOssConfig>()
            .select(SysOssConfig::getOssConfigId, SysOssConfig::getConfigKey)
            .eq(SysOssConfig::getConfigKey, sysOssConfig.getConfigKey()));
        if (ObjectUtil.isNotNull(info) && info.getOssConfigId() != ossConfigId) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }


    /**
     * 启用禁用状态
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateOssConfigStatus(SysOssConfigBo bo) {
        SysOssConfig config = BeanUtil.toBean(bo, SysOssConfig.class);
        int row = baseMapper.updateById(config);
        if (row > 0)
            setConfigCache(config, config.getConfigKey());
        return row;
    }

    @Override
    public OssProperties getOssPropertiesByKey(String ossType) {
        String json = RedisUtils.getCacheObject(OssConstant.SYS_OSS_KEY + ossType);
        if (json == null) {
            SysOssConfig info = baseMapper.selectOne(new LambdaQueryWrapper<SysOssConfig>()
                .eq(SysOssConfig::getConfigKey, ossType).eq(SysOssConfig::getStatus, "0").last("limit 1"));
            setConfigCache(info, ossType);
            return JsonHelper.copyObj(info, OssProperties.class);
        }
        OssProperties properties = JsonHelper.toObject(json, OssProperties.class);
        return properties;
    }


    /**
     * 设置cache key
     *
     * @param configKey 参数键
     * @return 缓存键key
     */
    private String getCacheKey(String configKey) {
        return OssConstant.SYS_OSS_KEY + configKey;
    }

    /**
     * 如果操作成功 则更新缓存
     *
     * @param config 配置
     * @return 返回操作状态
     */
    private void setConfigCache(SysOssConfig config, String ossType) {
        String jsonStr = config == null ? "" : JsonHelper.toJson(config);
        RedisUtils.setCacheObject(getCacheKey(ossType), jsonStr);

    }


}
