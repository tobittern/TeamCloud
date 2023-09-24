package com.euler.community.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.euler.common.core.domain.R;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.community.domain.bo.AdvertBo;
import com.euler.community.domain.dto.AdvertDto;
import com.euler.community.domain.dto.AdvertDynamicEsDto;
import com.euler.community.domain.entity.Advert;
import com.euler.community.domain.vo.AdvertVo;

import java.util.Collection;
import java.util.List;

/**
 * 广告Service接口
 *
 * @author euler
 * @date 2022-06-06
 */
public interface IAdvertService extends IService<Advert> {

    /**
     * 查询广告
     *
     * @param id 广告主键
     * @return 广告
     */
    AdvertVo queryById(Long id);

    /**
     * 查询广告列表
     *
     * @param advertDto 广告
     * @return 广告集合
     */
    TableDataInfo<AdvertVo> queryPageList(AdvertDto advertDto);

    /**
     * 查询广告列表
     *
     * @param advertDto 广告
     * @return 广告集合
     */
    List<AdvertVo> queryList(AdvertDto advertDto);

    /**
     * 修改广告
     *
     * @param bo 广告
     * @return 结果
     */
    R<Void> insertByBo(AdvertBo bo);

    /**
     * 修改广告
     *
     * @param bo 广告
     * @return 结果
     */
    R<Void> updateByBo(AdvertBo bo);

    /**
     * 校验并批量删除广告信息
     *
     * @param ids 需要删除的广告主键集合
     * @param isValid 是否校验,true-删除前校验,false-不校验
     * @return 结果
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 逻辑删除
     * @param bo
     * @return
     */
    Boolean logicRemove(AdvertBo bo);

    /**
     * 根据所在页数、当前登录人 获取广告
     * @param advertBo
     * @return
     */
    List<AdvertDynamicEsDto> queryByPageNum(AdvertBo advertBo);

    /**
     * 修改广告状态
     * @param bo
     * @return
     */
    R<Void> updateStatus(AdvertBo bo);

}
