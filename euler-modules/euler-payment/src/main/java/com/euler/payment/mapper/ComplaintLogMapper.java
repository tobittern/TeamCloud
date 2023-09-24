package com.euler.payment.mapper;

import com.euler.common.mybatis.core.mapper.BaseMapperPlus;
import com.euler.payment.domain.ComplaintLog;
import org.apache.ibatis.annotations.Mapper;


/**
 * 投诉信息的log日志Mapper接口
 *
 * @author euler
 * @date 2022-09-13
 */
@Mapper
public interface ComplaintLogMapper extends BaseMapperPlus<ComplaintLogMapper, ComplaintLog, ComplaintLog> {

}
