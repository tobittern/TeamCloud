package com.euler.system.api.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.euler.common.core.web.domain.BaseEntity;
import com.euler.common.core.xss.Xss;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;


/**
 * 通知公告表 sys_notice
 *
 * @author euler
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@TableName("sys_notice")
public class SysNotice extends BaseEntity {

    /**
     * 公告ID
     */
    @ApiModelProperty(value = "公告ID")
    @TableId(value = "notice_id")
    private Integer noticeId;

    /**
     * 公告标题
     */
    @Xss(message = "公告标题不能包含脚本字符")
    @ApiModelProperty(value = "公告标题")
    @Size(min = 0, max = 50, message = "公告标题不能超过50个字符")
    private String noticeTitle;

    /**
     * 公告类型（1通知 2公告）
     */
    @ApiModelProperty(value = "公告类型（1通知 2公告）")
    @NotBlank(message = "公告类型（1通知 2公告）不能为空")
    private String noticeType;

    /**
     * 公告内容    @NotBlank(message = "自动推送标识 0:自动 1:手动不能为空")
     */
    @ApiModelProperty(value = "公告内容")
    private String noticeContent;

    /**
     * 公告状态（0正常 1关闭）
     */
    @ApiModelProperty(value = "公告状态（0正常 1关闭）")
    private String status;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

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
     * 平台标识，1:sdk，2:开放平台
     */
    @ApiModelProperty(value = "平台标识，1:sdk，2:开放平台")
    private String platformType;

    /**
     * 推送用户标识，0:全部用户，1:部分用户
     */
    @ApiModelProperty(value = "推送用户标识，0:全部用户，1:部分用户")
    @NotBlank(message = "推送用户标识，0:全部用户，1:部分用户不能为空")
    private String pushUserType;

    /**
     * 自动推送标识 0:自动 1:手动
     */
    @ApiModelProperty(value = "自动推送标识 0:自动 1:手动")
    private String autoPush;

    /**
     * 公告持续开始时间
     */
    @ApiModelProperty(value = "公告持续开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date durationStartTime;

    /**
     * 公告持续结束时间
     */
    @ApiModelProperty(value = "公告持续结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date durationEndTime;

    /**
     * 公告持续时间
     */
    @ApiModelProperty(value = "公告持续时间")
    @TableField(exist = false)
    private String durationTime;

    /**
     * 推送用户，以逗号分割
     */
    @ApiModelProperty("推送用户，以逗号分割")
    private String pushUsers;

    /**
     * 推送状态 0:待推送 1:已推送
     */
    @ApiModelProperty("推送状态 0:待推送 1:已推送")
    private String pushStatus;

    /**
     * 推送时间
     */
    @ApiModelProperty("推送时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date pushTime;

    /**
     * 阅读状态 0:未读 1:已读
     */
    @ApiModelProperty("阅读状态 0:未读 1:已读")
    @TableField(exist = false)
    private String readStatus = "0";

}
