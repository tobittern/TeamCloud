package com.euler.risk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.risk.api.domain.BehaviorType;
import com.euler.risk.domain.dto.DeviceSearchDto;
import com.euler.risk.domain.dto.IpSearchDto;
import com.euler.risk.domain.vo.TdDeviceBehaviorVo;
import com.euler.risk.domain.vo.TdIpBehaviorVo;
import com.euler.risk.mapper.BehaviorTypeMapper;
import com.euler.risk.service.IBehaviorTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 行为类型Service业务层处理
 *
 * @author euler
 * @date 2022-08-24
 */
@RequiredArgsConstructor
@Service
public class BehaviorTypeServiceImpl extends ServiceImpl<BehaviorTypeMapper, BehaviorType> implements IBehaviorTypeService {
    @Autowired
    private BehaviorTypeMapper baseMapper;


    /**
     * 查询设备行为历史记录
     *
     * @param dto
     * @return
     */
    @Override
    public TableDataInfo<TdDeviceBehaviorVo> queryDevicePageList(DeviceSearchDto dto) {
        QueryWrapper<TdDeviceBehaviorVo> wrapper = getDeviceVoQueryWrapper(dto);
        Page<TdDeviceBehaviorVo> result = baseMapper.getDevicePageList(dto.build(), wrapper);
        return TableDataInfo.build(result);
    }

    private QueryWrapper<TdDeviceBehaviorVo> getDeviceVoQueryWrapper(DeviceSearchDto dto) {
        QueryWrapper<TdDeviceBehaviorVo> wrapper = Wrappers.query();
        switch (dto.getDeviceType()) {
            case "1":
                wrapper.eq(StringUtils.isNotBlank(dto.getTarget()), "tdi.device_mac", dto.getTarget());
                break;
            case "2":
                wrapper.eq(StringUtils.isNotBlank(dto.getTarget()), "tdi.device_oaid", dto.getTarget());
                break;
            case "3":
                wrapper.eq(StringUtils.isNotBlank(dto.getTarget()), "tdi.device_imei", dto.getTarget());
                break;
            case "4":
                wrapper.eq(StringUtils.isNotBlank(dto.getTarget()), "tdi.device_android", dto.getTarget());
                break;
            case "5":
                wrapper.eq(StringUtils.isNotBlank(dto.getTarget()), "tdi.device_uuid", dto.getTarget());
                break;
            case "6":
                wrapper.eq(StringUtils.isNotBlank(dto.getTarget()), "tdi.device_idfa", dto.getTarget());
                break;
            default:
                wrapper.eq(StringUtils.isNotBlank(dto.getTarget()), "tdi.device_push_id", dto.getTarget());
                break;
        }
        wrapper.ge(dto.getStartTime() != null, "b.create_time", dto.getStartTime());
        wrapper.le(dto.getEndTime() != null, "b.create_time", dto.getEndTime());
        wrapper.orderByDesc("b.create_time");
        return wrapper;
    }

    /**
     * 查询设备行为历史记录
     *
     * @param dto
     * @return
     */
    @Override
    public List<TdDeviceBehaviorVo> queryDeviceList(DeviceSearchDto dto) {
        QueryWrapper<TdDeviceBehaviorVo> wrapper = getDeviceVoQueryWrapper(dto);
        return baseMapper.getDeviceList(wrapper);
    }


    /**
     * 查询ip行为历史记录
     *
     * @param dto
     * @return
     */
    @Override
    public TableDataInfo<TdIpBehaviorVo> queryIpPageList(IpSearchDto dto) {
        QueryWrapper<TdIpBehaviorVo> wrapper = getIpVoQueryWrapper(dto);
        Page<TdIpBehaviorVo> result = baseMapper.getIpPageList(dto.build(), wrapper);
        return TableDataInfo.build(result);
    }

    private QueryWrapper<TdIpBehaviorVo> getIpVoQueryWrapper(IpSearchDto dto) {
        QueryWrapper<TdIpBehaviorVo> wrapper = Wrappers.query();
        wrapper.eq(StringUtils.isNotBlank(dto.getIp()), "b.ip", dto.getIp());
        wrapper.ge(dto.getStartTime() != null, "b.create_time", dto.getStartTime());
        wrapper.le(dto.getEndTime() != null, "b.create_time", dto.getEndTime());
        wrapper.orderByDesc("b.create_time");
        return wrapper;
    }

    /**
     * 查询ip行为历史记录
     *
     * @param dto
     * @return
     */
    @Override
    public List<TdIpBehaviorVo> queryIpList(IpSearchDto dto) {
        QueryWrapper<TdIpBehaviorVo> wrapper = getIpVoQueryWrapper(dto);
        return baseMapper.getIpList(wrapper);
    }

}
