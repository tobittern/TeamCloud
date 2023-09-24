package com.euler.community.domain.dto;

import com.euler.common.mybatis.core.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * 消息业务对象 message
 *
 * @author euler
 * @date 2022-06-06
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("消息业务对象")
public class MessageDto extends PageQuery {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 推送平台，'1':SDK，'2': 开放平台，'3': 九区玩家App
     */
    @ApiModelProperty(value = "推送平台，'1':SDK，'2': 开放平台，'3': 九区玩家App")
    @NotBlank(message = "推送平台，'1':SDK，'2': 开放平台，'3': 九区玩家App不能为空")
    private String platformType;

    /**
     * 推送用户标识 '0':全部用户 '1':部分用户
     */
    @ApiModelProperty(value = "推送用户标识 '0':全部用户 '1':部分用户")
    private String pushUserType;

    /**
     * 自动推送标识，'0':自动，'1':手动
     */
    @ApiModelProperty(value = "自动推送标识，'0':自动，'1':手动")
    private String autoPush;

    /**
     * 状态，'0':待推送，'1':已推送
     */
    @ApiModelProperty(value = "状态，'0':待推送，'1':已推送")
    private String pushStatus;

    /**
     * 阅读状态 0:未读 1:已读
     */
    @ApiModelProperty("阅读状态 0:未读 1:已读")
    private String readStatus;

    /**
     * 是否置顶，'0':不置顶，'1':置顶
     */
    @ApiModelProperty(value = "是否置顶，'0':不置顶，'1':置顶")
    private String isTop;

    /**
     * 消息类型 '1':点赞 '2':评论 '3':新粉丝 '4':系统消息
     */
    @ApiModelProperty(value = "消息类型 '1':点赞 '2':评论 '3':新粉丝 '4':系统消息")
    private String type;

    @ApiModelProperty(value = "公告类型（1通知 2公告）")
    private String noticeType;

}
