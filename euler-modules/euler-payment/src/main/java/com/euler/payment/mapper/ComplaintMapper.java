package com.euler.payment.mapper;

import com.euler.common.mybatis.core.mapper.BaseMapperPlus;
import com.euler.payment.domain.Complaint;
import com.euler.payment.domain.vo.ComplaintVo;
import org.apache.ibatis.annotations.Mapper;


/**
 * 投诉Mapper接口
 *
 * @author euler
 * @date 2022-09-13
 */
@Mapper
public interface ComplaintMapper extends BaseMapperPlus<ComplaintMapper, Complaint, ComplaintVo> {

}
