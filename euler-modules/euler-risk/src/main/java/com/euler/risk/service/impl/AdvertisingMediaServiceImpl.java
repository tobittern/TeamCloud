package com.euler.risk.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdNameDto;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.core.utils.DateUtils;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.euler.risk.domain.bo.AdvertisingMediaBo;
import com.euler.risk.domain.vo.AdvertisingMediaVo;
import com.euler.risk.domain.dto.AdvertisingMediaPageDto;
import com.euler.risk.domain.entity.AdvertisingMedia;
import com.euler.risk.mapper.AdvertisingMediaMapper;
import com.euler.risk.service.IAdvertisingMediaService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 广告媒体Service业务层处理
 *
 * @author euler
 * @date 2022-09-22
 */
@RequiredArgsConstructor
@Service
public class AdvertisingMediaServiceImpl extends ServiceImpl<AdvertisingMediaMapper, AdvertisingMedia> implements IAdvertisingMediaService {

    @Autowired
    private AdvertisingMediaMapper baseMapper;

    /**
     * 查询广告媒体
     *
     * @param id 广告媒体主键
     * @return 广告媒体
     */
    @Override
    public AdvertisingMediaVo queryById(Integer id) {
        return baseMapper.selectVoById(id);
    }


    /**
     * 查询广告媒体列表
     *
     * @param pageDto 广告媒体
     * @return 广告媒体
     */
    @Override
    public TableDataInfo<AdvertisingMediaVo> queryPageList(AdvertisingMediaPageDto pageDto) {
        LambdaQueryWrapper<AdvertisingMedia> lqw = buildQueryWrapper(pageDto);
        Page<AdvertisingMediaVo> result = baseMapper.selectVoPage(pageDto.build(), lqw);
        return TableDataInfo.build(result);
    }


    /**
     * 查询广告媒体列表
     *
     * @param pageDto 广告媒体
     * @return 广告媒体
     */
    @Override
    public List<AdvertisingMediaVo> queryList(AdvertisingMediaPageDto pageDto) {
        LambdaQueryWrapper<AdvertisingMedia> lqw = buildQueryWrapper(pageDto);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<AdvertisingMedia> buildQueryWrapper(AdvertisingMediaPageDto pageDto) {
        LambdaQueryWrapper<AdvertisingMedia> lqw = Wrappers.lambdaQuery();
        lqw.likeRight(StringUtils.isNotBlank(pageDto.getMediaName()), AdvertisingMedia::getMediaName, pageDto.getMediaName());
        lqw.eq(StringUtils.isNotBlank(pageDto.getAdvertisingPlatform()), AdvertisingMedia::getAdvertisingPlatform, pageDto.getAdvertisingPlatform());
        lqw.eq(pageDto.getRebate() != null, AdvertisingMedia::getRebate, pageDto.getRebate());
        lqw.ge(StringUtils.isNotBlank(pageDto.getBeginTime()), AdvertisingMedia::getCreateTime, DateUtils.getBeginOfDay(pageDto.getBeginTime()));
        lqw.le(StringUtils.isNotBlank(pageDto.getEndTime()), AdvertisingMedia::getCreateTime, DateUtils.getEndOfDay(pageDto.getEndTime()));
        return lqw;
    }

    /**
     * 新增广告媒体
     *
     * @param bo 广告媒体
     * @return 结果
     */
    @Override
    public Boolean insertByBo(AdvertisingMediaBo bo) {
        AdvertisingMedia add = BeanUtil.toBean(bo, AdvertisingMedia.class);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改广告媒体
     *
     * @param bo 广告媒体
     * @return 结果
     */
    @Override
    public Boolean updateByBo(AdvertisingMediaBo bo) {
        AdvertisingMedia update = BeanUtil.toBean(bo, AdvertisingMedia.class);
        return baseMapper.updateById(update) > 0;
    }


    /**
     * 批量删除广告媒体
     *
     * @param ids 需要删除的广告媒体主键
     * @return 结果
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Integer> ids) {
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public List<AdvertisingMediaVo> getMediaByPlatform(IdNameDto<String> idNameDto) {
        var eq = Wrappers.<AdvertisingMedia>lambdaQuery().eq(StringUtils.isNotBlank(idNameDto.getName()),AdvertisingMedia::getAdvertisingPlatform, idNameDto.getName());
        return baseMapper.selectVoList(eq);
    }
}
