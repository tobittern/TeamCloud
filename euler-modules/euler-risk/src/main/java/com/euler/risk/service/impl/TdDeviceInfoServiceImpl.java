package com.euler.risk.service.impl;

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.euler.common.core.domain.dto.DeviceInfoDto;
import com.euler.common.core.domain.dto.KeyValueDto;
import com.euler.common.core.domain.dto.RequestHeaderDto;
import com.euler.common.core.enums.DeviceEnum;
import com.euler.common.core.utils.HttpRequestHeaderUtils;
import com.euler.common.core.utils.JsonHelper;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.core.utils.uuid.Seq;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.risk.api.domain.TdDeviceInfoVo;
import com.euler.risk.domain.dto.TdDeviceInfoPageDto;
import com.euler.risk.domain.entity.TdDeviceInfo;
import com.euler.risk.domain.vo.UserDeviceIdInfoVo;
import com.euler.risk.enums.DeviceInfoEnum;
import com.euler.risk.mapper.TdDeviceInfoMapper;
import com.euler.risk.service.ITdDeviceInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * 设备信息Service业务层处理
 *
 * @author euler
 * @date 2022-08-24
 */
@RequiredArgsConstructor
@Service
public class TdDeviceInfoServiceImpl extends ServiceImpl<TdDeviceInfoMapper, TdDeviceInfo> implements ITdDeviceInfoService {

    @Autowired
    private TdDeviceInfoMapper baseMapper;


    /**
     * 查询设备信息
     *
     * @param id 设备信息主键
     * @return 设备信息
     */
    @Override
    public TdDeviceInfoVo queryById(String id) {
        return baseMapper.selectVoById(id);
    }


    /**
     * 查询ip账号信息列表
     *
     * @param pageDto ip账号信息
     * @return ip账号信息
     */
    @Override
    public TableDataInfo<UserDeviceIdInfoVo> getUserDeviceInfoByParams(TdDeviceInfoPageDto pageDto) {
        // 连表查询数据
        QueryWrapper<UserDeviceIdInfoVo> wrapper = getDeviceDetailVoQueryWrapper(pageDto);
        Page<UserDeviceIdInfoVo> result = baseMapper.getDeviceDetailPageList(pageDto.build(), wrapper);
        return TableDataInfo.build(result);
    }

    private QueryWrapper<UserDeviceIdInfoVo> getDeviceDetailVoQueryWrapper(TdDeviceInfoPageDto pageDto) {
        QueryWrapper<UserDeviceIdInfoVo> wrapper = Wrappers.query();
        wrapper.eq(StringUtils.isNotBlank(pageDto.getId()), "tdi.id", pageDto.getId())
            .eq(StringUtils.isNotBlank(pageDto.getDeviceMac()), "tdi.device_mac", pageDto.getDeviceMac())
            .eq(StringUtils.isNotBlank(pageDto.getDeviceOaid()), "tdi.device_oaid", pageDto.getDeviceOaid())
            .eq(StringUtils.isNotBlank(pageDto.getDeviceImei()), "tdi.device_imei", pageDto.getDeviceImei())
            .eq(StringUtils.isNotBlank(pageDto.getDeviceAndroid()), "tdi.device_android", pageDto.getDeviceAndroid())
            .eq(StringUtils.isNotBlank(pageDto.getDeviceUuid()), "tdi.device_uuid", pageDto.getDeviceUuid())
            .eq(StringUtils.isNotBlank(pageDto.getDeviceIdfa()), "tdi.device_idfa", pageDto.getDeviceIdfa())
            .eq(StringUtils.isNotBlank(pageDto.getDevicePushId()), "tdi.device_push_id", pageDto.getDevicePushId());
        return wrapper;
    }


    /**
     * 根据key-value查询出设备信息
     *
     * @param keyValueDto
     * @return
     */
    @Override
    public TdDeviceInfoVo getDeviceIdByDevice(KeyValueDto<String> keyValueDto) {
        String key = keyValueDto.getKey();
        String value = keyValueDto.getValue();
        if (key != null) {
            String[] deviceList = new String[]{"mac", "oaid", "imei", "android", "uuid", "idfa", "pushId"};
            Optional<String> operationDeviceAny = Arrays.stream(deviceList).filter(a -> a.equals(key)).findAny();
            if (!operationDeviceAny.isPresent()) {
                return null;
            }
        }
        LambdaQueryWrapper<TdDeviceInfo> lqw = Wrappers.lambdaQuery();
        lqw.eq(key.equals("mac"), TdDeviceInfo::getDeviceMac, value);
        lqw.eq(key.equals("oaid"), TdDeviceInfo::getDeviceOaid, value);
        lqw.eq(key.equals("imei"), TdDeviceInfo::getDeviceImei, value);
        lqw.eq(key.equals("android"), TdDeviceInfo::getDeviceAndroid, value);
        lqw.eq(key.equals("uuid"), TdDeviceInfo::getDeviceUuid, value);
        lqw.eq(key.equals("idfa"), TdDeviceInfo::getDeviceIdfa, value);
        lqw.eq(key.equals("pushId"), TdDeviceInfo::getDevicePushId, value);
        lqw.orderByDesc(TdDeviceInfo::getId).last("limit 1");
        TdDeviceInfoVo tdDeviceInfoVo = baseMapper.selectVoOne(lqw);
        if (tdDeviceInfoVo != null && tdDeviceInfoVo.getId() != null) {
            return tdDeviceInfoVo;
        }
        return null;
    }


    /**
     * 从请求头获取设备信息
     *
     * @return
     */
    @Override
    public TdDeviceInfoVo getDeviceInfoByHeader(RequestHeaderDto headerDto) {

        TdDeviceInfo tdDeviceInfo = new TdDeviceInfo();
        DeviceInfoDto deviceInfo = headerDto.getDeviceInfo();
        if (deviceInfo == null)
            return null;
        tdDeviceInfo.setDeviceIdfa(deviceInfo.getIdfa());
        tdDeviceInfo.setDeviceUuid(deviceInfo.getUuid());
        tdDeviceInfo.setDevicePushId(deviceInfo.getPushId());
        tdDeviceInfo.setDeviceAndroid(deviceInfo.getAndroid());
        tdDeviceInfo.setDeviceImei(deviceInfo.getImei());
        tdDeviceInfo.setDeviceOaid(deviceInfo.getOaid());
        tdDeviceInfo.setDeviceMac(deviceInfo.getMac());
        tdDeviceInfo.setMobileType(deviceInfo.getMobileType());


        return getDeviceInfoByDevice(tdDeviceInfo, headerDto.getDevice());
    }

    @Override
    public List<TdDeviceInfoVo> getDeviceInfoListByDeviceIds(List<String> deviceIds) {
        LambdaQueryWrapper<TdDeviceInfo> in = Wrappers.<TdDeviceInfo>lambdaQuery().in(TdDeviceInfo::getId, deviceIds);
        return baseMapper.selectVoList(in);
    }

    /**
     * 从请求头获取设备信息
     *
     * @return
     */
    @Override
    public TdDeviceInfoVo getDeviceInfoByHeader() {
        RequestHeaderDto headerDto = HttpRequestHeaderUtils.getFromHttpRequest();
        return getDeviceInfoByHeader(headerDto);


    }

    /**
     * 获取设备信息
     *
     * @param deviceInfo
     * @param device
     * @return
     */
    @Override
    public TdDeviceInfoVo getDeviceInfoByDevice(TdDeviceInfo deviceInfo, String device) {
        TdDeviceInfoVo tdDeviceInfoVo = null;
        KeyValueDto<String> keyValueDto = new KeyValueDto<>();
        //区分平台，查询不同的设备码
        // 安卓设备码，oaid,android,mac,imei

        DeviceEnum deviceEnum = DeviceEnum.find(Convert.toInt(device));


        if (DeviceEnum.ANDROID.equals(deviceEnum)) {

            if (StringUtils.isNotBlank(deviceInfo.getDeviceOaid())) {
                keyValueDto.setKey(DeviceInfoEnum.oaid.getDeviceCode());
                keyValueDto.setValue(deviceInfo.getDeviceOaid());
                tdDeviceInfoVo = getDeviceIdByDevice(keyValueDto);
            }
            if (tdDeviceInfoVo == null && StringUtils.isNotBlank(deviceInfo.getDeviceAndroid())) {
                keyValueDto.setKey(DeviceInfoEnum.android.getDeviceCode());
                keyValueDto.setValue(deviceInfo.getDeviceAndroid());
                tdDeviceInfoVo = getDeviceIdByDevice(keyValueDto);

            }
            if (tdDeviceInfoVo == null && StringUtils.isNotBlank(deviceInfo.getDeviceMac())) {
                keyValueDto.setKey(DeviceInfoEnum.mac.getDeviceCode());
                keyValueDto.setValue(deviceInfo.getDeviceMac());
                tdDeviceInfoVo = getDeviceIdByDevice(keyValueDto);

            }
            if (tdDeviceInfoVo == null && StringUtils.isNotBlank(deviceInfo.getDeviceImei())) {
                keyValueDto.setKey(DeviceInfoEnum.imei.getDeviceCode());
                keyValueDto.setValue(deviceInfo.getDeviceImei());
                tdDeviceInfoVo = getDeviceIdByDevice(keyValueDto);
            }

            //补全用户信息
            if (tdDeviceInfoVo == null) {
                tdDeviceInfoVo = new TdDeviceInfoVo();
                tdDeviceInfoVo.setIsChange(true);
                tdDeviceInfoVo.setMobileType(deviceInfo.getMobileType());
                tdDeviceInfoVo.setId(Seq.createId("D"));

            }
            if (StringUtils.isNotBlank(deviceInfo.getDeviceOaid()) && StringUtils.isBlank(tdDeviceInfoVo.getDeviceOaid())) {
                tdDeviceInfoVo.setIsChange(true);
                tdDeviceInfoVo.setDeviceOaid(deviceInfo.getDeviceOaid());
            }

            if (StringUtils.isNotBlank(deviceInfo.getDeviceAndroid()) && StringUtils.isBlank(tdDeviceInfoVo.getDeviceAndroid())) {
                tdDeviceInfoVo.setIsChange(true);
                tdDeviceInfoVo.setDeviceAndroid(deviceInfo.getDeviceAndroid());
            }

            if (StringUtils.isNotBlank(deviceInfo.getDeviceMac()) && StringUtils.isBlank(tdDeviceInfoVo.getDeviceMac())) {
                tdDeviceInfoVo.setIsChange(true);
                tdDeviceInfoVo.setDeviceMac(deviceInfo.getDeviceMac());
            }

            if (StringUtils.isNotBlank(deviceInfo.getDeviceImei()) && StringUtils.isBlank(tdDeviceInfoVo.getDeviceImei())) {
                tdDeviceInfoVo.setIsChange(true);
                tdDeviceInfoVo.setDeviceImei(deviceInfo.getDeviceImei());
            }


            if (StringUtils.isBlank(tdDeviceInfoVo.getDeviceOaid()) && StringUtils.isBlank(tdDeviceInfoVo.getDeviceAndroid()) && StringUtils.isBlank(tdDeviceInfoVo.getDeviceImei()) && StringUtils.isBlank(tdDeviceInfoVo.getDeviceMac())) {
                return null;
            }

        }
        //苹果设备码 uuid,idfa,pushId
        else if (DeviceEnum.IOS.equals(deviceEnum)) {
            if (StringUtils.isNotBlank(deviceInfo.getDeviceUuid())) {
                keyValueDto.setKey(DeviceInfoEnum.uuid.getDeviceCode());
                keyValueDto.setValue(deviceInfo.getDeviceUuid());
                tdDeviceInfoVo = getDeviceIdByDevice(keyValueDto);
            }

            if (tdDeviceInfoVo == null && StringUtils.isNotBlank(deviceInfo.getDeviceIdfa())) {
                keyValueDto.setKey(DeviceInfoEnum.idfa.getDeviceCode());
                keyValueDto.setValue(deviceInfo.getDeviceIdfa());
                tdDeviceInfoVo = getDeviceIdByDevice(keyValueDto);
            }

            if (tdDeviceInfoVo == null && StringUtils.isNotBlank(deviceInfo.getDevicePushId())) {
                keyValueDto.setKey(DeviceInfoEnum.pushId.getDeviceCode());
                keyValueDto.setValue(deviceInfo.getDevicePushId());
                tdDeviceInfoVo = getDeviceIdByDevice(keyValueDto);
            }


            //补全用户信息
            if (tdDeviceInfoVo == null) {
                tdDeviceInfoVo = new TdDeviceInfoVo();
                tdDeviceInfoVo.setIsChange(true);
                tdDeviceInfoVo.setMobileType(deviceInfo.getMobileType());
                tdDeviceInfoVo.setId(Seq.createId("D"));

            }
            if (StringUtils.isNotBlank(deviceInfo.getDeviceUuid()) && StringUtils.isBlank(tdDeviceInfoVo.getDeviceUuid())) {
                tdDeviceInfoVo.setIsChange(true);
                tdDeviceInfoVo.setDeviceUuid(deviceInfo.getDeviceUuid());
            }

            if (StringUtils.isNotBlank(deviceInfo.getDeviceIdfa()) && StringUtils.isBlank(tdDeviceInfoVo.getDeviceIdfa())) {
                tdDeviceInfoVo.setIsChange(true);
                tdDeviceInfoVo.setDeviceIdfa(deviceInfo.getDeviceIdfa());
            }

            if (StringUtils.isNotBlank(deviceInfo.getDevicePushId()) && StringUtils.isBlank(tdDeviceInfoVo.getDevicePushId())) {
                tdDeviceInfoVo.setIsChange(true);
                tdDeviceInfoVo.setDevicePushId(deviceInfo.getDevicePushId());
            }

            if (StringUtils.isBlank(tdDeviceInfoVo.getDeviceUuid()) && StringUtils.isBlank(tdDeviceInfoVo.getDeviceIdfa()) && StringUtils.isBlank(tdDeviceInfoVo.getDevicePushId())) {
                return null;
            }

        }

        if (tdDeviceInfoVo != null && tdDeviceInfoVo.getIsChange()) {
            TdDeviceInfo tdDeviceInfo = JsonHelper.copyObj(tdDeviceInfoVo, TdDeviceInfo.class);
            boolean flag = saveOrUpdate(tdDeviceInfo);
            if (flag)
                return tdDeviceInfoVo;
            else
                return null;
        }
        return tdDeviceInfoVo;
    }


}
