package com.euler.community.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.euler.common.core.utils.DateUtils;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.community.domain.bo.ShieldBo;
import com.euler.community.domain.dto.ShieldPageDto;
import com.euler.community.domain.entity.Shield;
import com.euler.community.domain.vo.ShieldVo;
import com.euler.community.mapper.ShieldMapper;
import com.euler.community.service.IShieldService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * 屏蔽信息Service业务层处理
 *
 * @author euler
 * @date 2022-09-15
 */
@RequiredArgsConstructor
@Service
public class ShieldServiceImpl extends ServiceImpl<ShieldMapper, Shield> implements IShieldService {

    @Autowired
    private ShieldMapper baseMapper;

    /**
     * 查询屏蔽信息
     *
     * @param id 屏蔽信息主键
     * @return 屏蔽信息
     */
    @Override
    public ShieldVo queryById(Integer id) {
        return baseMapper.selectVoById(id);
    }


    /**
     * 查询屏蔽信息列表
     *
     * @param pageDto 屏蔽信息
     * @return 屏蔽信息
     */
    @Override
    public TableDataInfo<ShieldVo> queryPageList(ShieldPageDto pageDto) {
        LambdaQueryWrapper<Shield> lqw = buildQueryWrapper(pageDto);
        Page<ShieldVo> result = baseMapper.selectVoPage(pageDto.build(), lqw);
        return TableDataInfo.build(result);
    }


    /**
     * 查询屏蔽信息列表
     *
     * @param pageDto 屏蔽信息
     * @return 屏蔽信息
     */
    @Override
    public List<ShieldVo> queryList(ShieldPageDto pageDto) {
        LambdaQueryWrapper<Shield> lqw = buildQueryWrapper(pageDto);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<Shield> buildQueryWrapper(ShieldPageDto pageDto) {
        LambdaQueryWrapper<Shield> lqw = Wrappers.lambdaQuery();
        lqw.eq(pageDto.getMemberId() != null, Shield::getMemberId, pageDto.getMemberId());
        lqw.eq(pageDto.getBusinessId() != null, Shield::getBusinessId, pageDto.getBusinessId());
        lqw.eq(pageDto.getBusinessType() != null, Shield::getBusinessType, pageDto.getBusinessType());
        lqw.ge(StringUtils.isNotBlank(pageDto.getBeginTime()), Shield::getCreateTime, DateUtils.getBeginOfDay(pageDto.getBeginTime()));
        lqw.le(StringUtils.isNotBlank(pageDto.getEndTime()), Shield::getCreateTime, DateUtils.getEndOfDay(pageDto.getEndTime()));
        return lqw;
    }

    /**
     * 新增屏蔽信息
     *
     * @param bo 屏蔽信息
     * @return 结果
     */
    @Override
    public Boolean insertByBo(ShieldBo bo) {
        Shield add = BeanUtil.toBean(bo, Shield.class);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改屏蔽信息
     *
     * @param bo 屏蔽信息
     * @return 结果
     */
    @Override
    public Boolean updateByBo(ShieldBo bo) {
        Shield update = BeanUtil.toBean(bo, Shield.class);
        return baseMapper.updateById(update) > 0;
    }


    /**
     * 批量删除屏蔽信息
     *
     * @param ids 需要删除的屏蔽信息主键
     * @return 结果
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Integer> ids) {
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
