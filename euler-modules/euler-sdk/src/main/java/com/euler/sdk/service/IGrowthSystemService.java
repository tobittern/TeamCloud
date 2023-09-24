package com.euler.sdk.service;

import com.euler.common.core.domain.R;
import com.euler.sdk.domain.dto.GrowthSystemDto;
import com.euler.sdk.domain.vo.GrowthSystemVo;
import com.euler.sdk.domain.bo.GrowthSystemBo;
import com.euler.common.mybatis.core.page.TableDataInfo;

/**
 * 成长体系Service接口
 *
 * @author euler
 * @date 2022-03-22
 */
public interface IGrowthSystemService {

    /**
     * 查询成长体系
     *
     * @param id 成长体系主键
     * @return 成长体系
     */
    GrowthSystemVo queryById(Long id);

    /**
     * 查询成长体系列表
     *
     * @param dto 成长体系
     * @return 成长体系集合
     */
    TableDataInfo<GrowthSystemVo> queryPageList(GrowthSystemDto dto);

    /**
     * 修改成长体系
     *
     * @param bo 成长体系
     * @return 结果
     */
    R insertByBo(GrowthSystemBo bo);

    /**
     * 成长值升级
     *
     * @param bo 成长体系
     * @return 结果
     */
    R upgradeGrowth(GrowthSystemBo bo);

}
