package com.euler.risk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.common.core.domain.dto.FillDataDto;
import com.euler.risk.domain.dto.DeviceSearchDto;
import com.euler.risk.domain.entity.TfDeviceSummary;
import com.euler.risk.domain.vo.TfDeviceSummaryVo;

/**
 * 设备数据汇总Service接口
 *
 * @author euler
 * @date 2022-08-24
 */
public interface ITfDeviceSummaryService extends IService<TfDeviceSummary> {

    /**
     * 查询设备行为异常预警列表
     *
     * @param dto 设备检索条件
     * @return 设备行为异常预警列表
     */
    TableDataInfo<TfDeviceSummaryVo> queryPageList(DeviceSearchDto dto);

    /**
     * 定时统计设备数据
     *
     * @param fillDataDto
     */
    void fillData(FillDataDto fillDataDto);
}
