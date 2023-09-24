package com.euler.payment.mapper;

import com.euler.common.mybatis.core.mapper.BaseMapperPlus;
import com.euler.payment.domain.ComplaintMediaList;
import com.euler.payment.domain.vo.ComplaintMediaListVo;
import org.apache.ibatis.annotations.Mapper;


/**
 * 投诉资料列Mapper接口
 *
 * @author euler
 * @date 2022-09-13
 */
@Mapper
public interface ComplaintMediaListMapper extends BaseMapperPlus<ComplaintMediaListMapper, ComplaintMediaList, ComplaintMediaListVo> {

}
