package com.euler.community.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.euler.common.core.domain.R;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.community.domain.bo.GiftBagCdkBo;
import com.euler.community.domain.dto.GiftBagCdkDto;
import com.euler.community.domain.entity.GiftBagCdk;
import com.euler.community.domain.vo.GiftBagCdkVo;
import com.euler.community.mapper.GiftBagCdkMapper;
import com.euler.community.service.IGiftBagCdkService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * 礼包兑换码数据Service业务层处理
 *
 * @author euler
 *  2022-06-07
 */
@RequiredArgsConstructor
@Service
public class GiftBagCdkServiceImpl extends ServiceImpl<GiftBagCdkMapper,GiftBagCdk> implements IGiftBagCdkService {

    private final GiftBagCdkMapper baseMapper;

    /**
     * 查询礼包兑换码数据
     *
     * @param id 礼包兑换码数据主键
     * @return 礼包兑换码数据
     */
    @Override
    public GiftBagCdkVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询礼包兑换码数据列表
     *
     * @param giftBagCdkDto 礼包兑换码数据
     * @return 礼包兑换码数据
     */
    @Override
    public TableDataInfo<GiftBagCdkVo> queryPageList(GiftBagCdkDto giftBagCdkDto) {
        LambdaQueryWrapper<GiftBagCdk> lqw = buildQueryWrapper(giftBagCdkDto);
        Page<GiftBagCdkVo> result = baseMapper.selectVoPage(giftBagCdkDto.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询礼包兑换码数据列表
     *
     * @param dto 礼包兑换码数据
     * @return 礼包兑换码数据
     */
    @Override
    public List<GiftBagCdkVo> queryList(GiftBagCdkDto dto) {
        LambdaQueryWrapper<GiftBagCdk> lqw = buildQueryWrapper(dto);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<GiftBagCdk> buildQueryWrapper(GiftBagCdkDto dto) {
        LambdaQueryWrapper<GiftBagCdk> lqw = Wrappers.lambdaQuery();
        lqw.eq(dto.getGiftBagId() != null, GiftBagCdk::getGiftBagId, dto.getGiftBagId());
        lqw.eq(dto.getGameId() != null, GiftBagCdk::getGameId, dto.getGameId());
        lqw.eq(dto.getMemberId() != null, GiftBagCdk::getMemberId, dto.getMemberId());
        lqw.eq(StringUtils.isNotBlank(dto.getCode()), GiftBagCdk::getCode, dto.getCode());
        lqw.eq(dto.getStatus() != null, GiftBagCdk::getStatus, dto.getStatus());
        return lqw;
    }

    /**
     * 新增礼包兑换码数据
     *
     * @param bo 礼包兑换码数据
     * @return 结果
     */
    @Override
    public R insertByBo(GiftBagCdkBo bo) {
        GiftBagCdk add = BeanUtil.toBean(bo, GiftBagCdk.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag? R.ok("新增成功"):R.fail("新增失败");
    }

    /**
     * 修改礼包SDk数据
     *
     * @param bo 礼包SDk数据
     * @return 结果
     */
    @Override
    public R updateByBo(GiftBagCdkBo bo) {
        GiftBagCdk update = BeanUtil.toBean(bo, GiftBagCdk.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0? R.ok("更新成功") : R.fail("更新失败");
    }

    /**
     * 保存前的数据校验
     *
     * @param entity 实体类数据
     */
    private void validEntityBeforeSave(GiftBagCdk entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除礼包兑换码数据
     *
     * @param ids 需要删除的礼包兑换码数据主键
     * @return 结果
     */
    @Override
    public R deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
         int i = baseMapper.deleteBatchIds(ids);
         return  i> 0? R.ok("删除成功") : R.fail("删除失败");
    }
}
