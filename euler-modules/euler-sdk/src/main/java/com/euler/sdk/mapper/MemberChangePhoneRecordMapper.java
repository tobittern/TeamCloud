package com.euler.sdk.mapper;

import com.euler.common.mybatis.core.mapper.BaseMapperPlus;
import com.euler.sdk.domain.entity.MemberChangePhoneRecord;
import com.euler.sdk.domain.vo.MemberChangePhoneRecordVo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户更换手机号记录Mapper接口
 *
 * @author euler
 * @date 2022-06-13
 */
@Mapper
public interface MemberChangePhoneRecordMapper extends BaseMapperPlus<MemberChangePhoneRecordMapper, MemberChangePhoneRecord, MemberChangePhoneRecordVo> {

}
