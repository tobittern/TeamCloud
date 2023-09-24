package com.euler.sdk.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.euler.common.core.utils.StringUtils;
import com.euler.sdk.domain.entity.PopupMemberShowTime;
import com.euler.sdk.domain.vo.PopupMemberShowTimeVo;
import com.euler.sdk.mapper.PopupMemberShowTimeMapper;
import com.euler.sdk.service.IPopupMemberShowTimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 每个用户对一个弹框的展示次数Service业务层处理
 *
 * @author euler
 * @date 2022-09-05
 */
@RequiredArgsConstructor
@Service
public class PopupMemberShowTimeServiceImpl extends ServiceImpl<PopupMemberShowTimeMapper, PopupMemberShowTime> implements IPopupMemberShowTimeService {

    @Autowired
    private PopupMemberShowTimeMapper baseMapper;

    @Override
    public List<PopupMemberShowTimeVo> getMemberPopupList(Long memberId, String startOccasion) {
        LambdaQueryWrapper<PopupMemberShowTime> lqw = buildQueryWrapper(null, memberId, startOccasion);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<PopupMemberShowTime> buildQueryWrapper(Integer popupId, Long memberId, String startOccasion) {
        LambdaQueryWrapper<PopupMemberShowTime> lqw = Wrappers.lambdaQuery();
        lqw.eq(popupId != null && popupId > 0, PopupMemberShowTime::getPopupId, popupId);
        lqw.eq(memberId != null && memberId > 0, PopupMemberShowTime::getMemberId, memberId);
        lqw.eq(StringUtils.isNotBlank(startOccasion), PopupMemberShowTime::getStartOccasion, startOccasion);
        return lqw;
    }

    @Override
    public void insertMemberBrowse(Integer popupId, Long memberId, String startOccasion) {
        LambdaQueryWrapper<PopupMemberShowTime> lqw = buildQueryWrapper(popupId, memberId, startOccasion);
        PopupMemberShowTime popupMemberShowTime = baseMapper.selectOne(lqw);
        if (popupMemberShowTime != null) {
            PopupMemberShowTime update = new PopupMemberShowTime();
            update.setId(popupMemberShowTime.getId());
            update.setTime(popupMemberShowTime.getTime() + 1);
            update.setStartOccasion(startOccasion);
            baseMapper.updateById(update);
        } else {
            // 不存在，执行新增操作
            PopupMemberShowTime insert = new PopupMemberShowTime();
            insert.setPopupId(popupId);
            insert.setMemberId(memberId);
            insert.setTime(1);
            insert.setStartOccasion(startOccasion);
            baseMapper.insert(insert);
        }
    }

    /**
     * 获取用户已经弹过的弹窗次数
     *
     * @return
     */
    public int getMemberPopupTimes(Integer popupId, Long memberId, String startOccasion) {
        int times = 0;
        LambdaQueryWrapper<PopupMemberShowTime> lqw = buildQueryWrapper(popupId, memberId, startOccasion);
        PopupMemberShowTime popupMemberShow =  baseMapper.selectOne(lqw);
        if (popupMemberShow != null) {
            times = popupMemberShow.getTime();
        }
        return times;
    }

}
