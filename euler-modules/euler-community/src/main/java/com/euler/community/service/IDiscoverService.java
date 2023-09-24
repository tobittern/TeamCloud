package com.euler.community.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdDto;
import com.euler.common.core.domain.dto.IdNameTypeDicDto;
import com.euler.community.domain.bo.DiscoverBo;
import com.euler.community.domain.dto.DiscoverDto;
import com.euler.community.domain.entity.Discover;
import com.euler.community.domain.vo.DiscoverVo;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.community.domain.vo.OpenGameForAppVo;

import java.util.Collection;
import java.util.List;

/**
 * 发现配置Service接口
 *
 * @author euler
 * @date 2022-06-06
 */
public interface IDiscoverService extends IService<Discover> {

    /**
     * 查询发现配置
     *
     * @param id 发现配置主键
     * @return 发现配置
     */
    DiscoverVo queryById(Long id);

    /**
     * 查询发现配置列表
     *
     * @param dto 发现配置
     * @return 发现配置集合
     */
    TableDataInfo<DiscoverVo> queryPageList(DiscoverDto dto);

    /**
     * 查询发现配置列表
     *
     * @param dto 发现配置
     * @return 发现配置集合
     */
    List<DiscoverVo> queryList(DiscoverDto dto);

    /**
     * 修改发现配置
     *
     * @param bo 发现配置
     * @return 结果
     */
    R insertByBo(DiscoverBo bo);

    /**
     * 修改发现配置
     *
     * @param bo 发现配置
     * @return 结果
     */
   R updateByBo(DiscoverBo bo);

    /**
     * 逻辑删除
     * @param ids
     * @return
     */
   R deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 操作上下架
     */
    R  operation(IdNameTypeDicDto dto, Long userId);


    OpenGameForAppVo getGameInfo(IdDto<Integer> idDto);

    void clearCache();

    R getGameListByName(DiscoverDto dto);

}
