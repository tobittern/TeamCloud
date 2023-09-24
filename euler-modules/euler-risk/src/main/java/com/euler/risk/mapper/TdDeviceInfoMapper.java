package com.euler.risk.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.euler.common.mybatis.core.mapper.BaseMapperPlus;
import com.euler.risk.domain.entity.TdDeviceInfo;
import com.euler.risk.api.domain.TdDeviceInfoVo;
import com.euler.risk.domain.vo.UserDeviceIdInfoVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 设备信息Mapper接口
 *
 * @author euler
 * @date 2022-08-24
 */
@Mapper
public interface TdDeviceInfoMapper extends BaseMapperPlus<TdDeviceInfoMapper, TdDeviceInfo, TdDeviceInfoVo> {

    Page<UserDeviceIdInfoVo> getDeviceDetailPageList(@Param("page") Page<UserDeviceIdInfoVo> page, @Param(Constants.WRAPPER) Wrapper<UserDeviceIdInfoVo> queryWrapper);

}
