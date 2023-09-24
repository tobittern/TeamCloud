package com.euler.risk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.risk.domain.dto.TdIpMemberPageDto;
import com.euler.risk.domain.entity.TdIpMember;
import com.euler.risk.domain.vo.UserDeviceIdInfoVo;
import com.euler.risk.mapper.TdIpMemberMapper;
import com.euler.risk.service.ITdIpMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * ip账号信息Service业务层处理
 *
 * @author euler
 * @date 2022-08-25
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class TdIpMemberServiceImpl extends ServiceImpl<TdIpMemberMapper, TdIpMember> implements ITdIpMemberService {

    @Autowired
    private TdIpMemberMapper baseMapper;

    /**
     * 查询ip账号信息列表
     *
     * @param pageDto ip账号信息
     * @return ip账号信息
     */
    @Override
    public TableDataInfo<UserDeviceIdInfoVo> getUserDeviceInfoByParams(TdIpMemberPageDto pageDto) {
        // 连表查询数据
        QueryWrapper<UserDeviceIdInfoVo> wrapper = getDeviceDetailVoQueryWrapper(pageDto);
        Page<UserDeviceIdInfoVo> result = baseMapper.getDeviceDetailPageList(pageDto.build(), wrapper);
        return TableDataInfo.build(result);
    }

    private QueryWrapper<UserDeviceIdInfoVo> getDeviceDetailVoQueryWrapper(TdIpMemberPageDto pageDto) {
        QueryWrapper<UserDeviceIdInfoVo> wrapper = Wrappers.query();
        wrapper.eq(StringUtils.isNotBlank(pageDto.getIp()), "tim.ip", pageDto.getIp())
            .eq(StringUtils.isNotBlank(pageDto.getMobile()), "tim.mobile", pageDto.getMobile())
            .eq(StringUtils.isNotBlank(pageDto.getDeviceId()), "tim.device_id", pageDto.getDeviceId())
            .eq(StringUtils.isNotBlank(pageDto.getDeviceId()), "tim.device_id", pageDto.getDeviceId())
            .eq(pageDto.getUserId() != null, "tim.user_id", pageDto.getUserId());
        return wrapper;
    }

    @Override
    public TdIpMember getByUserAndIp(Long userId, String ip) {
        var wrapper = Wrappers.<TdIpMember>lambdaQuery().eq(TdIpMember::getUserId, userId).eq(TdIpMember::getIp, ip).last("limit 1");
        return getOne(wrapper);

    }

}
