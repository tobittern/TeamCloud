package com.euler.payment.mapper;

import com.euler.common.mybatis.core.mapper.BaseMapperPlus;
import com.euler.payment.domain.RefundOrder;
import com.euler.payment.api.domain.RefundOrderVo;

/**
 * 退款订单Mapper接口
 *
 * @author euler
 * @date 2022-03-29
 */
public interface RefundOrderMapper extends BaseMapperPlus<RefundOrderMapper, RefundOrder, RefundOrderVo> {

}
