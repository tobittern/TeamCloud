package com.euler.statistics.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.euler.common.mybatis.core.mapper.BaseMapperPlus;
import com.euler.payment.api.domain.BusinessOrderVo;
import com.euler.statistics.domain.entity.BusinessOrder;
import com.euler.statistics.domain.vo.BusinessOrderSimpleDateVo;
import com.euler.statistics.domain.vo.BusinessOrderSimpleVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 订单Mapper接口
 *
 * @author euler
 * @date 2022-03-29
 */
@Mapper
public interface BusinessOrderMapper extends BaseMapperPlus<BusinessOrderMapper, BusinessOrder, BusinessOrderVo> {

    List<BusinessOrderSimpleVo> selectBusinessOrderSimpleData(@Param(Constants.WRAPPER) Wrapper<BusinessOrderSimpleVo> queryWrapper);


    List<BusinessOrderSimpleDateVo> selectBusinessOrderSimpleDataBySql(@Param(Constants.WRAPPER) Wrapper<BusinessOrderSimpleVo> queryWrapper);

}
