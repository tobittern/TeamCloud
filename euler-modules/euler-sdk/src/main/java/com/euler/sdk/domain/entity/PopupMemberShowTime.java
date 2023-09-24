package com.euler.sdk.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.euler.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import com.euler.common.core.web.domain.BaseEntity;

/**
 * 每个用户对一个弹框的展示次数对象 popup_member_show_time
 *
 * @author euler
 * @date 2022-09-05
 */
@Data
@TableName("popup_member_show_time")
public class PopupMemberShowTime {

private static final long serialVersionUID=1L;

    /**
     * 主键
     */
     @TableId(value = "id")
    private Integer id;
    /**
     * 弹框ID
     */
    private Integer popupId;
    /**
     * 用户id
     */
    private Long memberId;
    /**
     * 启动时机(0:每次启动 1:首充 2:游戏到达等级 3:累计充值 4:累计在线时长(分) 5:实名认证 6:绑定手机号)
     */
    private String startOccasion;
    /**
     * 展示次数
     */
    private Integer time;

}
