package com.euler.payment.mapper;

import com.euler.common.mybatis.core.mapper.BaseMapperPlus;
import com.euler.payment.domain.AliComplaintFeedback;
import com.euler.payment.domain.vo.AliComplaintFeedbackVo;
import org.apache.ibatis.annotations.Mapper;


/**
 * 商家处理交易投诉Mapper接口
 *
 * @author euler
 * @date 2022-10-18
 */
@Mapper
public interface AliComplaintFeedbackMapper extends BaseMapperPlus<AliComplaintFeedbackMapper, AliComplaintFeedback, AliComplaintFeedbackVo> {

}
