package com.euler.system.api.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 通知公告表 sys_notice
 *
 * @author euler
 */
@Data
@NoArgsConstructor
public class UserNoticeVo implements Serializable {

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
     * 公告内容
     */
    @ApiModelProperty(value = "公告内容")
    private String noticeContent;

    /**
     * 置顶状态，0：未置顶，1：已置顶
     */
    @ApiModelProperty(value = "置顶状态，0：未置顶，1：已置顶")
    private String toTop;

    /**
     * 平台标识，1:sdk，2:开放平台
     */
    @ApiModelProperty(value = "平台标识，1:sdk，2:开放平台")
    private  String platformType;

    /**
     * 公告持续开始时间
     */
    @ApiModelProperty(value = "公告持续开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private  Date durationStartTime;

    /**
     * 公告持续结束时间
     */
    @ApiModelProperty(value = "公告持续结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private  Date durationEndTime;

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
    private String readStatus = "0";

    /**
     * 发布人
     */
    @ApiModelProperty(value = "发布人")
    private String createBy;

}
