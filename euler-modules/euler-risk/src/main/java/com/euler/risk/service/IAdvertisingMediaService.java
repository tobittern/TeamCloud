package com.euler.risk.service;

import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdNameDto;
import com.euler.risk.domain.entity.AdvertisingMedia;
import com.euler.risk.domain.vo.AdvertisingMediaVo;
import com.euler.risk.domain.bo.AdvertisingMediaBo;
import com.euler.risk.domain.dto.AdvertisingMediaPageDto;
import com.baomidou.mybatisplus.extension.service.IService;
import com.euler.common.mybatis.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 广告媒体Service接口
 *
 * @author euler
 * @date 2022-09-22
 */
public interface IAdvertisingMediaService extends IService<AdvertisingMedia> {

    /**
     * 查询广告媒体
     *
     * @param id 广告媒体主键
     * @return 广告媒体
     */
    AdvertisingMediaVo queryById(Integer id);

    /**
     * 查询广告媒体列表
     *
     * @param pageDto 广告媒体
     * @return 广告媒体集合
     */
    TableDataInfo<AdvertisingMediaVo> queryPageList(AdvertisingMediaPageDto pageDto);

    /**
     * 查询广告媒体列表
     *
     * @param pageDto 广告媒体
     * @return 广告媒体集合
     */
    List<AdvertisingMediaVo> queryList(AdvertisingMediaPageDto pageDto);

    /**
     * 修改广告媒体
     *
     * @param bo 广告媒体
     * @return 结果
     */
    Boolean insertByBo(AdvertisingMediaBo bo);

    /**
     * 修改广告媒体
     *
     * @param bo 广告媒体
     * @return 结果
     */
    Boolean updateByBo(AdvertisingMediaBo bo);

    /**
     * 校验并批量删除广告媒体信息
     *
     * @param ids 需要删除的广告媒体主键集合
     * @return 结果
     */
    Boolean deleteWithValidByIds(Collection<Integer> ids);

    List<AdvertisingMediaVo> getMediaByPlatform(IdNameDto<String> idNameDto);
}
