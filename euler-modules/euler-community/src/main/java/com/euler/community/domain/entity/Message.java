package com.euler.community.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.euler.common.core.validate.AddGroup;
import com.euler.common.core.validate.EditGroup;
import com.euler.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 消息对象 message
 *
 * @author euler
 * @date 2022-06-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("message")
public class Message extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 消息标题
     */
    private String title;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 发布人
     */
    private String publishUser;

    /**
     * 推送平台，'1':SDK，'2': 开放平台，'3': 九区玩家App
     */
    private String platformType;

    /**
     * 推送用户标识 '0':全部用户 '1':部分用户
     */
    private String pushUserType;

    /**
     * 自动推送标识，'0':自动，'1':手动
     */
    private String autoPush;

    /**
     * 状态，'0':待推送，'1':已推送
     */
    private String pushStatus;

    /**
     * 推送用户
     */
    private String pushUsers;

    /**
     * 推送时间
     */
    private Date pushTime;

    /**
     * 是否置顶，'0':不置顶，'1':置顶
     */
    private String isTop;

    /**
     * 消息类型 1:点赞 2:评论 3:新粉丝 4:系统消息
     */
    private String type;

    /**
     * 是否删除，'0':未删除，'2':已删除
     */
    @TableLogic
    private String delFlag;

}
