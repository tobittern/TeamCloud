package com.euler.sdk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.euler.sdk.domain.entity.PopupMemberShowTime;
import com.euler.sdk.domain.vo.PopupMemberShowTimeVo;

import java.util.List;

/**
 * 每个用户对一个弹框的展示次数Service接口
 *
 * @author euler
 * @date 2022-09-05
 */
public interface IPopupMemberShowTimeService extends IService<PopupMemberShowTime> {

    /**
     * 获取指定用户的弹框展示次数
     *
     * @param memberId
     * @param startOccasion
     * @return
     */
    List<PopupMemberShowTimeVo> getMemberPopupList(Long memberId, String startOccasion);

    /**
     * 增加用户的弹框浏览次数
     *
     * @param popupId
     * @param memberId
     * @param startOccasion
     * @return
     */
    void insertMemberBrowse(Integer popupId, Long memberId, String startOccasion);

    /**
     * 获取用户已经弹过的弹窗次数
     *
     * @param popupId
     * @param memberId
     * @param startOccasion
     * @return
     */
    int getMemberPopupTimes(Integer popupId, Long memberId, String startOccasion);

}
