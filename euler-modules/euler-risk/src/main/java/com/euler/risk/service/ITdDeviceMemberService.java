package com.euler.risk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.risk.domain.dto.DeviceSearchDto;
import com.euler.risk.domain.dto.TdDeviceMemberPageDto;
import com.euler.risk.domain.entity.TdDeviceMember;
import com.euler.risk.domain.vo.TdDeviceBehaviorVo;
import com.euler.risk.domain.vo.TdDeviceMemberVo;

import java.util.List;

/**
 * 设备账号信息Service接口
 *
 * @author euler
 * @date 2022-08-24
 */
public interface ITdDeviceMemberService extends IService<TdDeviceMember> {

    /**
     * 查询设备账号信息列表
     *
     * @param pageDto 设备账号信息
     * @return 设备账号信息集合
     */
    TableDataInfo<TdDeviceMemberVo> queryPageList(TdDeviceMemberPageDto pageDto);

    /**
     * 根据用户Id和设备Id获取实体
     *
     * @param userId
     * @param deviceId
     * @return
     */
    TdDeviceMember getByDeviceIdAndUserId(Long userId, String deviceId);


}
