package com.euler.community.domain.bo;

import com.euler.common.core.validate.AddGroup;
import com.euler.common.core.validate.EditGroup;
import com.euler.common.core.web.domain.BaseEntity;
import com.euler.common.core.xss.Xss;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;

import java.util.Date;

/**
 * 消息业务对象 message
 *
 * @author euler
 * @date 2022-06-06
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("消息业务对象")
public class MessageBo extends BaseEntity {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键", required = true)
    @NotNull(message = "主键不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 消息标题
     */
    @Xss(message = "消息标题不能包含脚本字符")
    @ApiModelProperty(value = "消息标题", required = true)
    @NotBlank(message = "消息标题不能为空", groups = { AddGroup.class, EditGroup.class })
    @Size(min = 0, max = 50, message = "消息标题不能超过50个字符")
    private String title;

    /**
     * 消息内容
     */
    @ApiModelProperty(value = "消息内容", required = true)
    @NotBlank(message = "消息内容不能为空", groups = { AddGroup.class, EditGroup.class })
    private String content;

    /**
     * 发布人
     */
    @ApiModelProperty(value = "发布人")
    private String publishUser;

    /**
     * 推送平台，'1':SDK，'2': 开放平台，'3': 九区玩家App
     */
    @ApiModelProperty(value = "推送平台，'1':SDK，'2': 开放平台，'3': 九区玩家App", required = true)
    @NotBlank(message = "推送平台，'1':SDK，'2': 开放平台，'3': 九区玩家App不能为空", groups = { AddGroup.class, EditGroup.class })
    private String platformType;

    /**
     * 推送用户标识 '0':全部用户 '1':部分用户
     */
    @ApiModelProperty(value = "推送用户标识 '0':全部用户 '1':部分用户", required = true)
    @NotBlank(message = "推送用户标识 '0':全部用户 '1':部分用户不能为空", groups = { AddGroup.class, EditGroup.class })
    private String pushUserType;

    /**
     * 推送用户
     */
    @ApiModelProperty(value = "推送用户")
    private String pushUsers;

    /**
     * 自动推送标识，'0':自动，'1':手动
     */
    @ApiModelProperty(value = "自动推送标识，'0':自动，'1':手动")
    private String autoPush;

    /**
     * 状态，'0':待推送，'1':已推送
     */
    @ApiModelProperty(value = "状态，'0':待推送，'1':已推送")
    private String pushStatus = "0";

    /**
     * 推送时间
     */
    @ApiModelProperty(value = "推送时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date pushTime;

    /**
     * 是否置顶，'0':不置顶，'1':置顶
     */
    @ApiModelProperty(value = "是否置顶，'0':不置顶，'1':置顶")
    private String isTop = "0";

    /**
     * 公告类型（1通知 2公告）
     */
    @ApiModelProperty(value = "公告类型（1通知 2公告）")
    private String noticeType;

    /**
     * 消息类型 1:点赞 2:评论 3:新粉丝 4:系统消息
     */
    @ApiModelProperty(value = "消息类型 1:点赞 2:评论 3:新粉丝 4:系统消息")
    private String type = "0";

}
