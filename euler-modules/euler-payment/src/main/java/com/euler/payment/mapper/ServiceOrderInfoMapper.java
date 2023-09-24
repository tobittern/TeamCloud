package com.euler.payment.mapper;

import com.euler.common.mybatis.core.mapper.BaseMapperPlus;
import com.euler.payment.domain.ServiceOrderInfo;
import com.euler.payment.domain.vo.ServiceOrderInfoVo;
import org.apache.ibatis.annotations.Mapper;


/**
 * 投诉单关联服务单信息Mapper接口
 *
 * @author euler
 * @date 2022-09-13
 */
@Mapper
public interface ServiceOrderInfoMapper extends BaseMapperPlus<ServiceOrderInfoMapper, ServiceOrderInfo, ServiceOrderInfoVo> {

}
