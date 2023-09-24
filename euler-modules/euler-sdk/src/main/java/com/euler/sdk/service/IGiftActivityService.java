package com.euler.sdk.service;

import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdNameTypeDicDto;
import com.euler.common.mybatis.core.page.PageQuery;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.sdk.api.domain.MyGameVo;
import com.euler.sdk.domain.bo.GiftActivityBo;
import com.euler.sdk.domain.dto.GiftActivityDto;
import com.euler.sdk.domain.dto.GiftActivityFrontDto;
import com.euler.sdk.domain.vo.GiftActivityMiddlerVo;
import com.euler.sdk.domain.vo.GiftActivityVo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * 礼包活动管理Service接口
 *
 * @author euler
 * @date 2022-03-29
 */
public interface IGiftActivityService {

    /**
     * 查询礼包活动管理
     *
     * @param id 礼包活动管理主键
     * @return 礼包活动管理
     */
    GiftActivityVo queryById(Integer id);

    /**
     * 后台查询礼包活动管理列表
     *
     * @param
     * @return 礼包活动管理集合
     */
    TableDataInfo<GiftActivityVo> queryPageList(GiftActivityDto dto);


    /**
     * 前台查询礼包活动管理列表
     *
     * @param
     * @return 礼包活动管理集合
     */
    TableDataInfo<GiftActivityVo> queryFrontPageList(GiftActivityFrontDto dto);

    /**
     * 下面2个方法的需求数据进行统一获取
     * @param packageCode
     * @return
     */
    GiftActivityMiddlerVo getGiftNeedData(Long userId, Integer gameId, String packageCode);

    /**
     * 判断是否已经领取过了
     * @param activityId
     * @param ids
     * @return
     */
    Boolean checkIsAlreadyReceive(Integer activityId, ArrayList<Integer> ids);

    /**
     * 判断是否有资格领取
     * @param vo
     * @param myGameVo
     * @param gameId
     * @return
     */
    Boolean checkIsReceive(GiftActivityVo vo, MyGameVo myGameVo, Integer gameId);


    /**
     * 修改礼包活动管理
     *
     * @param
     * @return 结果
     */
    R insertByBo(GiftActivityBo bo);

    /**
     * 修改礼包活动管理
     *
     * @param
     * @return 结果
     */
    R updateByBo(GiftActivityBo bo);

    /**
     * 校验并批量删除礼包活动管理信息
     *
     * @param ids 需要删除的礼包活动管理主键集合
     * @param isValid 是否校验,true-删除前校验,false-不校验
     * @return 结果
     */
     Boolean deleteWithValidByIds(Collection<Integer> ids, Boolean isValid);


    /**
     * 操作上下架
     */
    R operation(IdNameTypeDicDto dto, Long userId);
}
