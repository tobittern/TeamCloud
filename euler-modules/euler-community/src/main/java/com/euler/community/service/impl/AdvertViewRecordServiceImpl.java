package com.euler.community.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.euler.common.core.domain.R;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.community.domain.bo.AdvertViewRecordBo;
import com.euler.community.domain.dto.AdvertViewRecordDto;
import com.euler.community.domain.entity.Advert;
import com.euler.community.domain.entity.AdvertViewRecord;
import com.euler.community.domain.vo.AdvertViewRecordVo;
import com.euler.community.mapper.AdvertMapper;
import com.euler.community.mapper.AdvertViewRecordMapper;
import com.euler.community.service.IAdvertViewRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Service业务层处理
 *
 * @author euler
 * @date 2022-06-17
 */
@RequiredArgsConstructor
@Service
public class AdvertViewRecordServiceImpl extends ServiceImpl<AdvertViewRecordMapper, AdvertViewRecord> implements IAdvertViewRecordService {

    private final AdvertViewRecordMapper baseMapper;

    private final AdvertMapper advertMapper;

    /**
     * 查询
     *
     * @param id 主键
     * @return
     */
    @Override
    public AdvertViewRecordVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询列表
     *
     * @param advertViewRecordDto
     * @return
     */
    @Override
    public TableDataInfo<AdvertViewRecordVo> queryPageList(AdvertViewRecordDto advertViewRecordDto) {
        LambdaQueryWrapper<AdvertViewRecord> lqw = buildQueryWrapper(advertViewRecordDto);
        Page<AdvertViewRecordVo> result = baseMapper.selectVoPage(advertViewRecordDto.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询列表
     *
     * @param advertViewRecordDto
     * @return
     */
    @Override
    public List<AdvertViewRecordVo> queryList(AdvertViewRecordDto advertViewRecordDto) {
        LambdaQueryWrapper<AdvertViewRecord> lqw = buildQueryWrapper(advertViewRecordDto);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<AdvertViewRecord> buildQueryWrapper(AdvertViewRecordDto advertViewRecordDto) {
        LambdaQueryWrapper<AdvertViewRecord> lqw = Wrappers.lambdaQuery();
        lqw.eq(advertViewRecordDto.getAdvertId() != null, AdvertViewRecord::getAdvertId, advertViewRecordDto.getAdvertId());
        lqw.eq(advertViewRecordDto.getMemberId() != null, AdvertViewRecord::getMemberId, advertViewRecordDto.getMemberId());
        lqw.eq(advertViewRecordDto.getViewNum() != null, AdvertViewRecord::getViewNum, advertViewRecordDto.getViewNum());
        return lqw;
    }

    /**
     * 新增
     *
     * @param bo
     * @return 结果
     */
    @Override
    public Boolean insertByBo(AdvertViewRecordBo bo) {
        AdvertViewRecord add = BeanUtil.toBean(bo, AdvertViewRecord.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改
     *
     * @param bo
     * @return 结果
     */
    @Override
    public Boolean updateByBo(AdvertViewRecordBo bo) {
        AdvertViewRecord update = BeanUtil.toBean(bo, AdvertViewRecord.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     *
     * @param entity 实体类数据
     */
    private void validEntityBeforeSave(AdvertViewRecord entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除
     *
     * @param ids 需要删除的主键
     * @return 结果
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public R<Void> view(AdvertViewRecordBo bo) {
        if (bo.getAdvertId() == null) {
            return R.fail("广告id不能为空");
        }
        if (bo.getMemberId() == null) {
            return R.fail("用户id不能为空");
        }
        Advert advert = advertMapper.selectById(bo.getAdvertId());
        if(advert==null){
            return R.fail("广告不存在");
        }
        LambdaQueryWrapper<AdvertViewRecord> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getAdvertId() != null, AdvertViewRecord::getAdvertId, bo.getAdvertId());
        lqw.eq(bo.getMemberId() != null, AdvertViewRecord::getMemberId, bo.getMemberId());
        List<AdvertViewRecord> advertViewRecords = baseMapper.selectList(lqw);
        if (advertViewRecords == null || advertViewRecords.isEmpty()) {
            insertByBo(bo);
        } else {
            AdvertViewRecord advertViewRecord = advertViewRecords.get(0);
            advertViewRecord.setViewNum(advertViewRecord.getViewNum() + 1);
            baseMapper.updateById(advertViewRecord);
        }
        //对广告的阅读总量进行累加
        advert.setViewNum(advert.getViewNum()+1);
        advertMapper.updateById(advert);
        return R.ok();
    }
}
