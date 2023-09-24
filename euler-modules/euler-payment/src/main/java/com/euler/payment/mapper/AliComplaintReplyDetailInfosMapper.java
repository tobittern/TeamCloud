package com.euler.payment.mapper;

import com.euler.common.mybatis.core.mapper.BaseMapperPlus;
import com.euler.payment.domain.AliComplaintReplyDetailInfos;
import com.euler.payment.domain.vo.AliComplaintReplyDetailInfosVo;
import org.apache.ibatis.annotations.Mapper;


/**
 * 用户与商家之间的协商记录Mapper接口
 *
 * @author euler
 * @date 2022-10-18
 */
@Mapper
public interface AliComplaintReplyDetailInfosMapper extends BaseMapperPlus<AliComplaintReplyDetailInfosMapper, AliComplaintReplyDetailInfos, AliComplaintReplyDetailInfosVo> {

}
