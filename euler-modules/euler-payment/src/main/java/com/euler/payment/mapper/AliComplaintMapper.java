package com.euler.payment.mapper;

import com.euler.common.mybatis.core.mapper.BaseMapperPlus;
import com.euler.payment.domain.AliComplaint;
import com.euler.payment.domain.vo.AliComplaintInfoVo;
import com.euler.payment.domain.vo.AliComplaintVo;
import org.apache.ibatis.annotations.Mapper;


/**
 * 阿里支付的投诉Mapper接口
 *
 * @author euler
 * @date 2022-10-18
 */
@Mapper
public interface AliComplaintMapper extends BaseMapperPlus<AliComplaintMapper, AliComplaint, AliComplaintInfoVo> {

}
