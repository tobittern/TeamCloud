package com.euler.sdk.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.euler.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import com.euler.common.core.web.domain.BaseEntity;

/**
 * 弹窗管理对象 popup
 *
 * @author euler
 * @date 2022-09-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("popup")
public class Popup extends BaseEntity {

private static final long serialVersionUID=1L;

    /**
     * 主键
     */
     @TableId(value = "id")
    private Integer id;
    /**
     * 用户id
     */
    private Long memberId;
    /**
     * 弹框名
     */
    private String title;
    /**
     * 弹框类型 1强退 2奖励 3运营
     */
    private Integer type;
    /**
     * 每天显示次数 大约999就代表着一直持续
     */
    private Integer times;
    /**
     * 弹窗显示开始时间
     */
    private Date startTime;
    /**
     * 弹窗显示结束时间
     */
    private Date endTime;
    /**
     * 展示类型 1图片 2文本
     */
    private Integer showType;
    /**
     * 图片横
     */
    private String pictureTransverse;
    /**
     * 图片纵
     */
    private String pictureLongitudinal;
    /**
     * 展示内容
     */
    private String showContent;
    /**
     * 跳转url
     */
    private String jumpUrl;
    /**
     * 显示优先级，默认值0，数字越小，显示级别越高
     */
    private Integer level;
    /**
     * 弹窗状态，0待启用，1已启用，2已停用
     */
    private String status;
    /**
     * 礼包ID
     */
    private Integer giftBagId;
    /**
     * 是否删除，0未删除，2已删除
     */
     @TableLogic
    private String delFlag;

    /**
     * 启动时机(0:每次启动 1:首充 2:游戏到达等级 3:累计充值 4:累计在线时长(分) 5:实名认证 6:绑定手机号)
     */
    private String startOccasion;

    /**
     * 每次启动类型(0:打开App 1:进入游戏)
     */
    private String everyStartupType;

    /**
     * 弹窗时间，单位：秒
     */
    private Integer popupTime;

    /**
     * 角色注册时间，单位：天，-1默认不限制
     */
    private Integer roleRegistTime;

    /**
     * 满足条件的值(启动时机选择【2:游戏到达等级 3:累计充值 4:累计在线时长(分)】时需要要填)
     */
    private String conditionValue;

}
