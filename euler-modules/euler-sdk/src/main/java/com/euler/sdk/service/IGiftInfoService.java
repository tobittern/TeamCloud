package com.euler.sdk.service;

import com.euler.common.core.domain.R;
import com.euler.sdk.domain.dto.GiftInfoDto;
import com.euler.sdk.domain.vo.GiftInfoVo;
import com.euler.sdk.domain.bo.GiftInfoBo;
import com.euler.common.mybatis.core.page.TableDataInfo;

import java.util.Collection;

/**
 * 礼包Service接口
 *
 * @author euler
 * @date 2022-03-24
 */
public interface IGiftInfoService {

    /**
     * 查询礼包详情
     *
     * @param id 礼包主键
     * @return 礼包详情
     */
    GiftInfoVo queryById(Integer id);

    /**
     * 查询礼包列表
     *
     * @param dto 礼包详情
     * @return 礼包列表
     */
    TableDataInfo<GiftInfoVo> queryPageList(GiftInfoDto dto);

    /**
     * 新增礼包
     *
     * @param bo 礼包
     * @return 结果
     */
    R insertByBo(GiftInfoBo bo);

    /**
     * 修改礼包
     *
     * @param bo 礼包
     * @return 结果
     */
    R updateByBo(GiftInfoBo bo);

    /**
     * 校验并批量删除礼包信息
     *
     * @param ids 需要删除的礼包主键集合
     * @param isValid 是否校验,true-删除前校验,false-不校验
     * @param giftGroupId 礼包组id
     * @return 结果
     */
    R deleteWithValidByIds(Collection<Integer> ids, Boolean isValid, Integer giftGroupId);

}
