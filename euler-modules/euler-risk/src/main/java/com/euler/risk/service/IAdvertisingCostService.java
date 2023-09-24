package com.euler.risk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.euler.common.core.domain.R;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.risk.domain.bo.AdvertisingCostBo;
import com.euler.risk.domain.dto.AdvertisingCostDto;
import com.euler.risk.domain.entity.AdvertisingCost;
import com.euler.risk.domain.vo.AdvertisingCostVo;

import java.util.Collection;
import java.util.List;

/**
 * 广告成本管理Service接口
 *
 * @author euler
 * @date 2022-08-23
 */
public interface IAdvertisingCostService extends IService<AdvertisingCost> {

    /**
     * 查询广告成本管理
     *
     * @param id 广告成本管理主键
     * @return 广告成本管理
     */
    AdvertisingCostVo queryById(Long id);

    /**
     * 查询广告成本管理列表
     *
     * @param dto 广告成本管理
     * @param dto 分页参数
     * @return 广告成本管理集合
     */
    TableDataInfo<AdvertisingCostVo> queryPageList(AdvertisingCostDto dto);

    /**
     * 查询广告成本管理列表
     *
     * @param dto 广告成本管理
     * @return 广告成本管理集合
     */
    List<AdvertisingCostVo> queryList(AdvertisingCostDto dto);

    /**
     * 修改广告成本管理
     *
     * @param bo 广告成本管理
     * @return 结果
     */
    R insertByBo(AdvertisingCostBo bo);

    /**
     * 修改广告成本管理
     *
     * @param bo 广告成本管理
     * @return 结果
     */
    R updateByBo(AdvertisingCostBo bo);

    /**
     * 逻辑删除
     *
     * @param ids
     * @return
     */
    R deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}

