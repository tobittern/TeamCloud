package com.euler.system.domain.dto;

import com.euler.common.mybatis.core.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("通知公告业务对象")
public class NoticePageDto extends PageQuery {

    /**
     * 公告ID
     */
    @ApiModelProperty(value = "公告ID")
    private Integer noticeId;

    /**
     * 公告标题
     */
    @ApiModelProperty(value = "公告标题")
    private String noticeTitle;

    /**
     * 公告类型（1通知 2公告）
     */
    @ApiModelProperty(value = "公告类型（1通知 2公告）")
    private String noticeType;


    /**
     * 公告状态（0正常 1关闭）
     */
    @ApiModelProperty(value = "公告状态（0正常 1关闭）")
    private String status;

    /**
     * 发布人
     */
    @ApiModelProperty(value = "发布人")
    private String publishUser;

    /**
     * 置顶状态，0：未置顶，1：已置顶
     */
    @ApiModelProperty(value = "置顶状态，0：未置顶，1：已置顶")
    private String toTop;

    /**
     * 创建者
     */
    @ApiModelProperty(value = "创建者")
    private String createBy;

    /**
     * 平台标识，1:sdk，2:开放平台
     */
    @ApiModelProperty(value = "平台标识，1:sdk，2:开放平台")
    private  String platformType;

    /**
     * 推送用户标识，0:全部用户，1:部分用户
     */
    @ApiModelProperty(value = "推送用户标识，0:全部用户，1:部分用户")
    private  String pushUserType;

    /**
     * 自动推送标识 0:自动 1:手动
     */
    @ApiModelProperty(value = "自动推送标识 0:自动 1:手动")
    private  String autoPush;

    /**
     * 消息公告的推送状态 0:未推送 1:已推送
     */
    @ApiModelProperty(value = "消息公告的推送状态 0:未推送 1:已推送")
    private  String pushStatus;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private Long userId;

}
