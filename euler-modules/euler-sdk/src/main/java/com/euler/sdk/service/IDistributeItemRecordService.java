package com.euler.sdk.service;

import com.euler.common.core.domain.R;
import com.euler.sdk.domain.dto.DistributeItemRecordDto;
import com.euler.sdk.domain.vo.DistributeItemRecordVo;
import com.euler.sdk.domain.bo.DistributeItemRecordBo;
import com.euler.common.mybatis.core.page.TableDataInfo;

/**
 * 派发物品记录Service接口
 *
 * @author euler
 * @date 2022-04-09
 */
public interface IDistributeItemRecordService {

    /**
     * 查询派发物品记录
     *
     * @param id 派发物品记录主键
     * @return 派发物品记录
     */
    DistributeItemRecordVo queryById(Integer id);

    /**
     * 查询派发物品记录列表
     *
     * @param dto 派发物品记录
     * @return 派发物品记录集合
     */
    TableDataInfo<DistributeItemRecordVo> queryPageList(DistributeItemRecordDto dto);

    /**
     * 修改派发物品记录
     *
     * @param bo 派发物品记录
     * @return 结果
     */
    R insertByBo(DistributeItemRecordBo bo);

    /**
     * 修改派发物品记录
     *
     * @param bo 派发物品记录
     * @return 结果
     */
    R updateByBo(DistributeItemRecordBo bo);

}
