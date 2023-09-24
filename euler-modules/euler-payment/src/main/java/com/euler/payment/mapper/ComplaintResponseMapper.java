package com.euler.payment.mapper;

import com.euler.common.mybatis.core.mapper.BaseMapperPlus;
import com.euler.payment.domain.ComplaintResponse;
import com.euler.payment.domain.vo.ComplaintResponseVo;
import org.apache.ibatis.annotations.Mapper;


/**
 * 投诉回复Mapper接口
 *
 * @author euler
 * @date 2022-09-13
 */
@Mapper
public interface ComplaintResponseMapper extends BaseMapperPlus<ComplaintResponseMapper, ComplaintResponse, ComplaintResponseVo> {

}
