package com.euler.sdk.service;

import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.sdk.domain.dto.MemberChangePhoneRecordDto;
import com.euler.sdk.domain.entity.MemberChangePhoneRecord;
import com.euler.sdk.domain.vo.MemberChangePhoneRecordVo;

/**
 * 用户更换手机号记录Service接口
 *
 * @author euler
 * @date 2022-06-13
 */
public interface IMemberChangePhoneRecordService {

    /**
     * 查询用户更换手机号记录列表
     *
     * @return 用户更换手机号记录集合
     */
    TableDataInfo<MemberChangePhoneRecordVo> queryPageList(MemberChangePhoneRecordDto dto);
}
