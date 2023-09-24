package com.euler.community.domain.dto;

import com.euler.common.core.validate.EditGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 消息业务对象 message
 *
 * @author euler
 * @date 2022-06-06
 */

@Data
@ApiModel("消息业务对象")
public class MessageTypeDto {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private String id;

    /**
     * 推送平台，'1':SDK，'2': 开放平台，'3': 九区玩家App
     */
    @ApiModelProperty(value = "推送平台，'1':SDK，'2': 开放平台，'3': 九区玩家App")
    @NotBlank(message = "推送平台，'1':SDK，'2': 开放平台，'3': 九区玩家App不能为空", groups = { EditGroup.class })
    private String platformType;

    /**
     * 推送用户标识 '0':全部用户 '1':部分用户
     */
    @ApiModelProperty(value = "推送用户标识 '0':全部用户 '1':部分用户")
    private String pushUserType;

    /**
     * 消息类型 '1':点赞 '2':评论 '3':新粉丝 '4':系统消息
     */
    @ApiModelProperty(value = "消息类型 '1':点赞 '2':评论 '3':新粉丝 '4':系统消息")
    private String type;

    @ApiModelProperty(value = "公告类型（1通知 2公告）")
    private String noticeType;

    @ApiModelProperty(value = "阅读状态 0:未读 1:已读")
    private String readStatus;

    @ApiModelProperty(value = "推送状态 0:待推送 1:已推送")
    private String pushStatus;

}
