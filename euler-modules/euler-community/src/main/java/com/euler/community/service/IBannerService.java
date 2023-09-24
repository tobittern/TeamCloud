package com.euler.community.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.euler.common.core.domain.R;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.community.domain.bo.BannerBo;
import com.euler.community.domain.dto.BannerDto;
import com.euler.community.domain.entity.Banner;
import com.euler.community.domain.vo.BannerVo;

import java.util.Collection;
import java.util.List;

/**
 * banner列Service接口
 *
 * @author euler
 * @date 2022-06-07
 */
public interface IBannerService extends IService<Banner> {

    /**
     * 查询banner列
     *
     * @return banner列
     */
    List<BannerVo> searchByIds(List<Long> ids);

    /**
     * 查询banner列
     *
     * @param id banner列主键
     * @return banner列
     */
    BannerVo queryById(Long id);

    /**
     * 查询banner列列表
     *
     * @param dto banner列
     * @return banner列集合
     */
    TableDataInfo<BannerVo> queryPageList(BannerDto dto);

    /**
     * 修改banner列
     *
     * @param bo banner列
     * @return 结果
     */
    R insertByBo(BannerBo bo);

    /**
     * 修改banner列
     *
     * @param bo banner列
     * @return 结果
     */
    R updateByBo(BannerBo bo);

    /**
     * 逻辑删除
     *
     * @param ids
     * @return
     */
    R deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}
