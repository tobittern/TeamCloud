package com.euler.risk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.euler.common.core.domain.dto.KeyValueDto;
import com.euler.common.core.domain.dto.RequestHeaderDto;
import com.euler.common.core.enums.DeviceEnum;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.risk.domain.dto.TdDeviceInfoPageDto;
import com.euler.risk.domain.entity.TdDeviceInfo;
import com.euler.risk.api.domain.TdDeviceInfoVo;
import com.euler.risk.domain.vo.UserDeviceIdInfoVo;

import java.util.List;

/**
 * 设备信息Service接口
 *
 * @author euler
 * @date 2022-08-24
 */
public interface ITdDeviceInfoService extends IService<TdDeviceInfo> {


    /**
     * 查询设备信息
     *
     * @param id 设备信息主键
     * @return 设备信息
     */
    TdDeviceInfoVo queryById(String id);

    /**
     * 设备查询出用户的基础信息
     *
     * @return 账号信息集合
     */
    TableDataInfo<UserDeviceIdInfoVo> getUserDeviceInfoByParams(TdDeviceInfoPageDto dto);

    /**
     * 根据key-value查询出设备信息
     *
     * @param keyValueDto
     * @return
     */
    TdDeviceInfoVo getDeviceIdByDevice(KeyValueDto<String> keyValueDto);


    /**
     * 获取设备信息
     *
     * @param deviceInfo
     * @param device
     * @return
     */
    TdDeviceInfoVo getDeviceInfoByDevice(TdDeviceInfo deviceInfo, String device);

    /**
     * 从请求头获取设备信息
     *
     * @return
     */
    TdDeviceInfoVo getDeviceInfoByHeader();

    /**
     * 从请求头获取设备信息
     *
     * @return
     */
    TdDeviceInfoVo getDeviceInfoByHeader(RequestHeaderDto headerDto);


    /**
     * 根据设备ID获取一批设备的详细信息
     *
     * @return
     */
    List<TdDeviceInfoVo> getDeviceInfoListByDeviceIds(List<String> deviceIds);

}
