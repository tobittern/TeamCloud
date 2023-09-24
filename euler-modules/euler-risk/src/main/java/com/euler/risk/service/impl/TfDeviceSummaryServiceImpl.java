package com.euler.risk.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.common.core.domain.dto.FillDataDto;
import com.euler.risk.domain.dto.DeviceSearchDto;
import com.euler.risk.domain.entity.TfDeviceSummary;
import com.euler.risk.domain.vo.TfDeviceSummaryVo;
import com.euler.risk.mapper.TfDeviceSummaryMapper;
import com.euler.risk.service.ITfDeviceSummaryService;
import com.euler.system.api.RemoteDictService;
import com.euler.system.api.domain.SysDictData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 设备数据汇总Service业务层处理
 *
 * @author euler
 * @date 2022-08-24
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class TfDeviceSummaryServiceImpl extends ServiceImpl<TfDeviceSummaryMapper, TfDeviceSummary> implements ITfDeviceSummaryService {

    @Autowired
    private TfDeviceSummaryMapper baseMapper;
    @DubboReference
    private RemoteDictService remoteDictService;

    /**
     * 查询设备行为异常预警列表
     *
     * @param dto 设备检索条件
     * @return 设备行为异常预警列表
     */
    @Override
    public TableDataInfo<TfDeviceSummaryVo> queryPageList(DeviceSearchDto dto) {
        QueryWrapper<TfDeviceSummary> lqw = buildQueryWrapper(dto);
        Page<TfDeviceSummaryVo> result = baseMapper.selectDeviceSummaryList(dto.build(), lqw);
        if (result != null && result.getRecords() != null && !result.getRecords().isEmpty()) {
            // 查询字典，获取设备类型名称
            List<SysDictData> data = remoteDictService.selectDictDataByType("risk_equipment_type");
            result.getRecords().forEach(a -> {
                // 设备类型名称
                data.stream().forEach(t -> {
                    if(t.getDictValue().equals(dto.getDeviceType())){
                        a.setDeviceTypeName(t.getDictLabel());
                    }
                });
                // 设备类型(1:mac 2:oaid 3:imei 4:android 5:uuid 6:idfa 7:pushId)
                switch (dto.getDeviceType()) {
                    case "1":
                        a.setTarget(a.getDeviceMac());
                        break;
                    case "2":
                        a.setTarget(a.getDeviceOaid());
                        break;
                    case "3":
                        a.setTarget(a.getDeviceImei());
                        break;
                    case "4":
                        a.setTarget(a.getDeviceAndroid());
                        break;
                    case "5":
                        a.setTarget(a.getDeviceUuid());
                        break;
                    case "6":
                        a.setTarget(a.getDeviceIdfa());
                        break;
                    default:
                        a.setTarget(a.getDevicePushId());
                        break;
                }
            });
        }
        return TableDataInfo.build(result);
    }

    private QueryWrapper<TfDeviceSummary> buildQueryWrapper(DeviceSearchDto dto) {
        QueryWrapper<TfDeviceSummary> lqw = Wrappers.query();
        // 关联设备信息, 根据设备类型(1:mac 2:oaid 3:imei 4:android 5:uuid 6:idfa 7:pushId)检索
        switch (dto.getDeviceType()) {
            case "1":
                lqw.eq(StringUtils.isNotBlank(dto.getTarget()), "tdi.device_mac", dto.getTarget());
                break;
            case "2":
                lqw.eq(StringUtils.isNotBlank(dto.getTarget()), "tdi.device_oaid", dto.getTarget());
                break;
            case "3":
                lqw.eq(StringUtils.isNotBlank(dto.getTarget()), "tdi.device_imei", dto.getTarget());
                break;
            case "4":
                lqw.eq(StringUtils.isNotBlank(dto.getTarget()), "tdi.device_android", dto.getTarget());
                break;
            case "5":
                lqw.eq(StringUtils.isNotBlank(dto.getTarget()), "tdi.device_uuid", dto.getTarget());
                break;
            case "6":
                lqw.eq(StringUtils.isNotBlank(dto.getTarget()), "tdi.device_idfa", dto.getTarget());
                break;
            default:
                lqw.eq(StringUtils.isNotBlank(dto.getTarget()), "tdi.device_push_id", dto.getTarget());
                break;
        }

        lqw.ge(dto.getStartTime() != null, "tds.date_id", dto.getStartTime());
        lqw.le(dto.getEndTime() != null, "tds.date_id", dto.getEndTime());
        lqw.ge(dto.getRegisterStartNum() != null, "register_num", dto.getRegisterStartNum());
        lqw.le(dto.getRegisterEndNum() != null, "register_num", dto.getRegisterEndNum());
        lqw.ge(dto.getRoleCreateStartNum() != null, "role_create_num", dto.getRoleCreateStartNum());
        lqw.le(dto.getRoleCreateEndNum() != null, "role_create_num", dto.getRoleCreateEndNum());
        lqw.ge(dto.getLoginStartNum() != null, "login_num", dto.getLoginStartNum());
        lqw.le(dto.getLoginEndNum() != null, "login_num", dto.getLoginEndNum());
        lqw.orderByDesc("register_num");
        lqw.orderByDesc("role_create_num");
        lqw.orderByDesc("login_num");
        lqw.groupBy("tds.device_id");
        return lqw;
    }

    /**
     * 定时统计设备数据
     *
     * @param fillDataDto
     */
    @Override
    public void fillData(FillDataDto fillDataDto) {
        //清除数据
        log.info("定时统计设备数据--清除数据--开始，batchNo:{}", fillDataDto.getBatchNo());

        var wrapper = new LambdaQueryWrapper<TfDeviceSummary>();
        wrapper.between(TfDeviceSummary::getDateId, fillDataDto.getBeginTime(), fillDataDto.getEndTime());
        Integer res = baseMapper.delete(wrapper);
        log.info("定时统计设备数据--清除数据--清除数据量，batchNo：{}，res：{}", fillDataDto.getBatchNo(), res);

        log.info("定时统计设备数据--填充数据--开始，batchNo:{}", fillDataDto.getBatchNo());
        baseMapper.fillData(fillDataDto);
        log.info("定时统计设备数据--填充数据--结束，batchNo:{}", fillDataDto.getBatchNo());

    }

}
