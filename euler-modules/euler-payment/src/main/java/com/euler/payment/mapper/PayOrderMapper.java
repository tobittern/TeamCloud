package com.euler.payment.mapper;

import com.euler.common.mybatis.core.mapper.BaseMapperPlus;
import com.euler.payment.api.domain.PayOrderVo;
import com.euler.payment.domain.PayOrder;

/**
 * 支付订单Mapper接口
 *
 * @author euler
 * @date 2022-03-29
 */
public interface PayOrderMapper extends BaseMapperPlus<PayOrderMapper, PayOrder, PayOrderVo> {

}
