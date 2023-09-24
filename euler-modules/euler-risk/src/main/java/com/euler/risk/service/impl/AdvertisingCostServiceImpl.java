package com.euler.risk.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.euler.common.core.domain.R;
import com.euler.common.core.utils.DateUtils;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.risk.domain.bo.AdvertisingCostBo;
import com.euler.risk.domain.dto.AdvertisingCostDto;
import com.euler.risk.domain.entity.AdvertisingCost;
import com.euler.risk.domain.vo.AdvertisingCostVo;
import com.euler.risk.mapper.AdvertisingCostMapper;
import com.euler.risk.service.IAdvertisingCostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

/**
 * 广告成本管理Service业务层处理
 *
 * @author euler
 * @date 2022-08-23
 */
@RequiredArgsConstructor
@Service
public class AdvertisingCostServiceImpl extends ServiceImpl<AdvertisingCostMapper, AdvertisingCost>
    implements IAdvertisingCostService {
    @Autowired
    private AdvertisingCostMapper baseMapper;

    /**
     * 查询广告成本管理
     *
     * @param id 广告成本管理主键
     * @return 广告成本管理
     */
    @Override
    public AdvertisingCostVo queryById(Long id) {
      AdvertisingCostVo vo = baseMapper.selectVoById(id);
      if (ObjectUtil.isNotNull(vo)) {
        vo.setCostDate(DateUtil.format(DateUtils.parseDate(vo.getCostDate()), "yyyy-MM-dd"));
      }
      return vo;
    }

    /**
     * 查询广告成本管理列表
     *
     * @param dto 广告成本管理
     * @return 广告成本管理
     */
    @Override
    public TableDataInfo<AdvertisingCostVo> queryPageList(AdvertisingCostDto dto) {
      LambdaQueryWrapper<AdvertisingCost> lqw = buildQueryWrapper(dto);
      Page<AdvertisingCostVo> result = baseMapper.selectVoPage(dto.build(), lqw);
      if (result.getRecords().size() > 0) {
        result.getRecords().forEach(a -> {
          a.setCostDate(DateUtil.format(DateUtils.parseDate(a.getCostDate()), "yyyy-MM-dd"));
        });
      }
      return TableDataInfo.build(result);
    }

      private LambdaQueryWrapper<AdvertisingCost> buildQueryWrapper(AdvertisingCostDto dto) {
        LambdaQueryWrapper<AdvertisingCost> lqw = Wrappers.lambdaQuery();
        lqw.eq(dto.getId() != null, AdvertisingCost::getId, dto.getId());
          lqw.eq(dto.getGameId() != null, AdvertisingCost::getGameId, dto.getGameId());

          lqw.eq(dto.getMediaId() != null, AdvertisingCost::getMediaId, dto.getMediaId());

          lqw.ge(dto.getAdvertisingDateFrom()!= null, AdvertisingCost::getCostDate, DateUtils.getBeginOfDay(dto.getAdvertisingDateFrom()));
        lqw.le(dto.getAdvertisingDateTo() != null, AdvertisingCost::getCostDate, DateUtils.getEndOfDay(dto.getAdvertisingDateTo()));
        lqw.like(StringUtils.isNotBlank(dto.getAdvertisingPlatform()), AdvertisingCost::getAdvertisingPlatform, dto.getAdvertisingPlatform());
        lqw.ge(dto.getCostFrom() != null, AdvertisingCost::getCost, dto.getCostFrom());
        lqw.le(dto.getCostTo() != null, AdvertisingCost::getCost, dto.getCostTo());
        // 按照id进行排序
        lqw.orderByAsc(AdvertisingCost::getId);
        return lqw;
    }
    /**
     * 查询广告成本管理列表
     *
     * @param dto 广告成本管理
     * @return 广告成本管理
     */
    @Override
    public List<AdvertisingCostVo> queryList(AdvertisingCostDto dto) {
        LambdaQueryWrapper<AdvertisingCost> lqw = buildQueryWrapper(dto);
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 新增广告成本管理
     *
     * @param bo 广告成本管理
     * @return 结果
     */
    @Override
    public R insertByBo(AdvertisingCostBo bo) {
        //保留小数点后两位
        BigDecimal cost = NumberUtil.round(bo.getCost(), 2);
        bo.setCost(Convert.toFloat(cost));
        AdvertisingCost add = BeanUtil.toBean(bo, AdvertisingCost.class);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return R.ok();
    }

    /**
     * 修改广告成本管理
     *
     * @param bo 广告成本管理
     * @return 结果
     */
    @Override
    public R updateByBo(AdvertisingCostBo bo) {
        //保留小数点后两位
        BigDecimal cost = NumberUtil.round(bo.getCost(), 2);
        bo.setCost(Convert.toFloat(cost));
        AdvertisingCost update = BeanUtil.toBean(bo, AdvertisingCost.class);
        int i = baseMapper.updateById(update);
        if (i > 0) {
            return R.ok();
        }
        return R.fail("修改失败");
    }

    /**
     * 批量删除广告成本管理
     *
     * @param ids 需要删除的广告成本管理主键
     * @return 结果
     */
    @Override
    public R deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (baseMapper.deleteBatchIds(ids) > 0) {
            return R.ok("删除成功");
        }
        return R.fail("删除失败");
    }

}
