package com.euler.risk.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.euler.common.mybatis.core.mapper.BaseMapperPlus;
import com.euler.risk.api.domain.BehaviorType;
import com.euler.risk.domain.vo.BehaviorTypeVo;
import com.euler.risk.domain.vo.TdDeviceBehaviorVo;
import com.euler.risk.domain.vo.TdIpBehaviorVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 行为类型Mapper接口
 *
 * @author euler
 * @date 2022-08-24
 */
@Mapper
public interface BehaviorTypeMapper extends BaseMapperPlus<BehaviorTypeMapper, BehaviorType, BehaviorTypeVo> {

    Page<TdDeviceBehaviorVo> getDevicePageList(@Param("page") Page<TdDeviceBehaviorVo> page, @Param(Constants.WRAPPER) Wrapper<TdDeviceBehaviorVo> queryWrapper);

    List<TdDeviceBehaviorVo> getDeviceList(@Param(Constants.WRAPPER) Wrapper<TdDeviceBehaviorVo> queryWrapper);

    Page<TdIpBehaviorVo> getIpPageList(@Param("page") Page<TdIpBehaviorVo> page, @Param(Constants.WRAPPER) Wrapper<TdIpBehaviorVo> queryWrapper);

    List<TdIpBehaviorVo> getIpList(@Param(Constants.WRAPPER) Wrapper<TdIpBehaviorVo> queryWrapper);

}
