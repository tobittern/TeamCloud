package com.euler.community.domain.dto;

import com.euler.common.core.validate.EditGroup;
import com.euler.common.mybatis.core.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("消息业务对象")
public class MessageToUsersDto extends PageQuery {

    /**
     * 消息ID
     */
    @ApiModelProperty(value = "消息ID")
    private Long messageId;

    /**
     * 接收人
     */
    @ApiModelProperty("接收人")
    private Long toUserId;

    /**
     * 消息类型 1:点赞 2:评论 3:新粉丝 4:系统消息
     */
    @ApiModelProperty(value = "消息类型 1:点赞 2:评论 3:新粉丝 4:系统消息")
    private String type;

    /**
     * 阅读状态 0:未读 1:已读
     */
    @ApiModelProperty("阅读状态 0:未读 1:已读")
    private String readStatus;

    /**
     * 推送平台，'1':SDK，'2': 开放平台，'3': 九区玩家App
     */
    @ApiModelProperty(value = "推送平台，'1':SDK，'2': 开放平台，'3': 九区玩家App")
    @NotBlank(message = "推送平台，'1':SDK，'2': 开放平台，'3': 九区玩家App不能为空", groups = { EditGroup.class })
    private String platformType;

}
