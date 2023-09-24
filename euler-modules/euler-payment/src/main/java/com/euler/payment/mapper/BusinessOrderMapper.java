package com.euler.payment.mapper;

import com.euler.common.mybatis.core.mapper.BaseMapperPlus;
import com.euler.payment.api.domain.BusinessOrderVo;
import com.euler.payment.domain.BusinessOrder;
import com.euler.payment.domain.vo.StatisticsChargeVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 订单Mapper接口
 *
 * @author euler
 * @date 2022-03-29
 */
public interface BusinessOrderMapper extends BaseMapperPlus<BusinessOrderMapper, BusinessOrder, BusinessOrderVo> {

    List<StatisticsChargeVo> getOrderDataByparam(Map<String,Object> map);

    List<BusinessOrderVo> getOrderChargeByparam(Map<String, Object> map);

    int getOrderCountByUser(@Param("userId") Long userId, @Param("gameId") Long gameId);

}
