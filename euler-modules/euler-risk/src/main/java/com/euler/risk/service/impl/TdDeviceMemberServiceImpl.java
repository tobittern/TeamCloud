package com.euler.risk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.risk.domain.dto.DeviceSearchDto;
import com.euler.risk.domain.dto.TdDeviceMemberPageDto;
import com.euler.risk.domain.entity.TdDeviceMember;
import com.euler.risk.domain.vo.TdDeviceBehaviorVo;
import com.euler.risk.domain.vo.TdDeviceMemberVo;
import com.euler.risk.mapper.TdDeviceMemberMapper;
import com.euler.risk.service.IBanlistService;
import com.euler.risk.service.ITdDeviceMemberService;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 设备账号信息Service业务层处理
 *
 * @author euler
 * @date 2022-08-24
 */
@RequiredArgsConstructor
@Service
public class TdDeviceMemberServiceImpl extends ServiceImpl<TdDeviceMemberMapper, TdDeviceMember> implements ITdDeviceMemberService {

    @Autowired
    private TdDeviceMemberMapper baseMapper;
    @Autowired
    private IBanlistService iBanlistService;

    /**
     * 查询设备账号信息列表
     *
     * @param pageDto 设备账号信息
     * @return 设备账号信息
     */
    @Override
    public TableDataInfo<TdDeviceMemberVo> queryPageList(TdDeviceMemberPageDto pageDto) {
        LambdaQueryWrapper<TdDeviceMember> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(pageDto.getDeviceId()), TdDeviceMember::getDeviceId, pageDto.getDeviceId());
        lqw.eq(StringUtils.isNotBlank(pageDto.getAccount()), TdDeviceMember::getAccount, pageDto.getAccount());
        lqw.eq(StringUtils.isNotBlank(pageDto.getMobile()), TdDeviceMember::getMobile, pageDto.getMobile());
        lqw.eq(pageDto.getUserId() != null, TdDeviceMember::getUserId, pageDto.getUserId());
        Page<TdDeviceMemberVo> result = baseMapper.selectVoPage(pageDto.build(), lqw);
        if (result.getRecords().size() > 0) {
            List<Long> memberIds = result.getRecords().stream().map(TdDeviceMemberVo::getUserId).collect(Collectors.toList());
            List<Long> checkMemberIds = iBanlistService.checkUserBanStatus(memberIds);
            result.getRecords().forEach(a -> {
                if (checkMemberIds.contains(a.getUserId())) {
                    a.setBanStatus(1);
                }
            });
        }
        return TableDataInfo.build(result);
    }

    /**
     * 根据用户Id和设备Id获取实体
     *
     * @param userId
     * @param deviceId
     * @return
     */
    @Override
    public TdDeviceMember getByDeviceIdAndUserId(Long userId, String deviceId) {
        var wrapper = Wrappers.<TdDeviceMember>lambdaQuery().eq(TdDeviceMember::getUserId, userId).eq(TdDeviceMember::getDeviceId, deviceId).last("limit 1");
        return getOne(wrapper);

    }

}
