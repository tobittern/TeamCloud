package com.euler.community.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdNameTypeDicDto;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.community.domain.bo.BannerGroupBo;
import com.euler.community.domain.dto.BannerGroupDto;
import com.euler.community.domain.entity.BannerGroup;
import com.euler.community.domain.vo.BannerGroupListVo;
import com.euler.community.domain.vo.BannerGroupVo;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * banner组Service接口
 *
 * @author euler
 * @date 2022-06-06
 */
public interface IBannerGroupService extends IService<BannerGroup> {

    /**
     * 查询banner组
     *
     * @param id banner组主键
     * @return banner组
     */
    BannerGroupVo queryById(Long id);

    /**
     * 查询一批banner组中的banner详细信息
     *
     * @param ids banner组主键
     * @return banner组
     */
    List<BannerGroupListVo> getGroupBannerList(List<Long> ids);

    /**
     * 查询banner组列表
     *
     * @param dto banner组
     * @return banner组集合
     */
    TableDataInfo<BannerGroupVo> queryPageList(BannerGroupDto dto);

    /**
     * 修改banner组
     *
     * @param bo banner组
     * @return 结果
     */
    R insertByBo(BannerGroupBo bo);

    /**
     * 修改banner组
     *
     * @param bo banner组
     * @return 结果
     */
    R updateByBo(BannerGroupBo bo);

    /**
     * 逻辑删除
     *
     * @param ids
     * @return
     */
    R deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 操作上下架
     */
    R operation(IdNameTypeDicDto<Long> dto, Long userId);

}
