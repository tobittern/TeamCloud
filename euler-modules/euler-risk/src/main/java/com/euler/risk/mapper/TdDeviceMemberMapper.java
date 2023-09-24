package com.euler.risk.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.euler.common.mybatis.core.mapper.BaseMapperPlus;
import com.euler.risk.domain.entity.TdDeviceMember;
import com.euler.risk.domain.vo.TdDeviceBehaviorVo;
import com.euler.risk.domain.vo.TdDeviceMemberVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 设备账号信息Mapper接口
 *
 * @author euler
 * @date 2022-08-24
 */
@Mapper
public interface TdDeviceMemberMapper extends BaseMapperPlus<TdDeviceMemberMapper, TdDeviceMember, TdDeviceMemberVo> {

}
