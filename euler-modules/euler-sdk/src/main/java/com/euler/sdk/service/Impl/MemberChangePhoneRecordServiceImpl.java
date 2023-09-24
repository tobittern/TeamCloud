package com.euler.sdk.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.sdk.domain.dto.MemberChangePhoneRecordDto;
import com.euler.sdk.domain.entity.MemberChangePhoneRecord;
import com.euler.sdk.domain.vo.MemberChangePhoneRecordVo;
import com.euler.sdk.mapper.MemberChangePhoneRecordMapper;
import com.euler.sdk.service.IMemberChangePhoneRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 用户更换手机号记录Service业务层处理
 *
 * @author euler
 * @date 2022-06-13
 */
@RequiredArgsConstructor
@Service
public class MemberChangePhoneRecordServiceImpl implements IMemberChangePhoneRecordService {

    private final MemberChangePhoneRecordMapper baseMapper;

    /**
     * 查询用户更换手机号记录列表
     *
     * @return 用户更换手机号记录
     */
    @Override
    public TableDataInfo<MemberChangePhoneRecordVo> queryPageList(MemberChangePhoneRecordDto dto) {
        LambdaQueryWrapper<MemberChangePhoneRecord> lqw = buildQueryWrapper(dto);
        Page<MemberChangePhoneRecordVo> result = baseMapper.selectVoPage(dto.build(), lqw);
        return TableDataInfo.build(result);
    }


    private LambdaQueryWrapper<MemberChangePhoneRecord> buildQueryWrapper(MemberChangePhoneRecordDto dto) {
        LambdaQueryWrapper<MemberChangePhoneRecord> lqw = Wrappers.lambdaQuery();
        lqw.eq(dto.getMemberId() != null, MemberChangePhoneRecord::getMemberId, dto.getMemberId());
        lqw.eq(StringUtils.isNotBlank(dto.getOriginalMobile()), MemberChangePhoneRecord::getOriginalMobile, dto.getOriginalMobile());
        lqw.eq(StringUtils.isNotBlank(dto.getNewMobile()), MemberChangePhoneRecord::getNewMobile, dto.getNewMobile());
        lqw.orderByDesc(MemberChangePhoneRecord::getId);
        return lqw;
    }

}
