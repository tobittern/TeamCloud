package com.euler.statistics.service;

import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.statistics.domain.dto.*;
import com.euler.statistics.domain.vo.*;

/**
 * 平台汇总统计数据Service接口
 *
 * @author euler
 * @date 2022-07-12
 */
public interface ITrackDatasService {


    /**
     * 查询平台汇总统计数据列表
     */
    TableDataInfo<PlatformDatasVo> queryPageList(PlatformDatasDto dto);

    /**
     * 平台渠道计划统计数据，目前只有抖音有广告计划数据
     */
    TableDataInfo<DouyinChannelAidDatasVo> douyinChannelAidDataList(DouyinChannelAidDatasDto dto);


    /**
     * 平台渠道计划统计数据，目前只有抖音有广告计划数据
     */
    TableDataInfo<DouyinChannelDatasVo> douyinChannelDataList(DouyinChannelDatasDto dto);


    /*********************** 上面的按照数据平台同学要求已经不用了 *****************************/


    TableDataInfo<PlatformTongjiDatasVo> platformTongjiDatas(PlatformTongjiDatasPageDto dto);


    TableDataInfo<PlatformTongjiChannelAidDatasVo> platformTongjiChannelAidDatas(PlatformTongjiChannelAidDatasPageDto dto);

}
