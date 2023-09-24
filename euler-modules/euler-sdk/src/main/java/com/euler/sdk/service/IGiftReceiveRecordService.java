package com.euler.sdk.service;

import com.euler.common.core.domain.R;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.sdk.domain.bo.GiftReceiveRecordBo;
import com.euler.sdk.domain.dto.GiftReceiveRecordDto;
import com.euler.sdk.domain.vo.GiftReceiveRecordVo;

import java.util.ArrayList;

/**
 * 礼包领取记录Service接口
 *
 * @author euler
 * @date 2022-04-13
 */
public interface IGiftReceiveRecordService {

    /**
     * 查询礼包领取记录
     *
     * @param id 礼包领取记录主键
     * @return 礼包领取记录
     */
    GiftReceiveRecordVo queryById(Integer id);

    /**
     * 查询礼包领取记录列表
     *
     * @param dto 礼包领取记录
     * @return 礼包领取记录集合
     */
    TableDataInfo<GiftReceiveRecordVo> queryPageList(GiftReceiveRecordDto dto);

    /**
     * 修改礼包领取记录
     *
     * @param bo 礼包领取记录
     * @return 结果
     */
    R insertByBo(GiftReceiveRecordBo bo);

    /**
     * 获取指定用户获取指定类型礼包的列表
     *
     * @param type
     * @param userId
     * @return
     */
    ArrayList<Integer> selectUserAlreadyList(String type, Long userId);

}
