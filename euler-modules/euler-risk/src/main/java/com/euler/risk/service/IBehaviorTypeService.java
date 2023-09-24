package com.euler.risk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.risk.api.domain.BehaviorType;
import com.euler.risk.domain.dto.DeviceSearchDto;
import com.euler.risk.domain.dto.IpSearchDto;
import com.euler.risk.domain.vo.TdDeviceBehaviorVo;
import com.euler.risk.domain.vo.TdIpBehaviorVo;

import java.util.List;

/**
 * 行为类型Service接口
 *
 * @author euler
 * @date 2022-08-24
 */
public interface IBehaviorTypeService extends IService<BehaviorType> {


    /**
     * 查询设备行为历史记录
     *
     * @param dto
     * @return
     */
    TableDataInfo<TdDeviceBehaviorVo> queryDevicePageList(DeviceSearchDto dto);

    /**
     * 查询设备行为历史记录
     *
     * @param dto
     * @return
     */
    List<TdDeviceBehaviorVo> queryDeviceList(DeviceSearchDto dto);

    /**
     * 查询ip行为历史记录
     *
     * @param dto
     * @return
     */
    TableDataInfo<TdIpBehaviorVo> queryIpPageList(IpSearchDto dto);

    /**
     * 查询ip行为历史记录
     *
     * @param dto
     * @return
     */
    List<TdIpBehaviorVo> queryIpList(IpSearchDto dto);

}
