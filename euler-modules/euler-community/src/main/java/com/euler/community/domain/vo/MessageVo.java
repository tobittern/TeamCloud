package com.euler.community.domain.vo;

import java.util.Date;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 消息视图对象 message
 *
 * @author euler
 * @date 2022-06-06
 */
@Data
@ApiModel("消息视图对象")
@ExcelIgnoreUnannotated
public class MessageVo {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    @ApiModelProperty("主键")
    private Long id;

    /**
     * 消息标题
     */
    @ExcelProperty(value = "消息标题")
    @ApiModelProperty("消息标题")
    private String title;

    /**
     * 消息内容
     */
    @ExcelProperty(value = "消息内容")
    @ApiModelProperty("消息内容")
    private String content;

    /**
     * 发布人
     */
    @ExcelProperty(value = "发布人")
    @ApiModelProperty("发布人")
    private String publishUser;

    /**
     * 推送平台，'1':SDK，'2': 开放平台，'3': 九区玩家App
     */
    @ExcelProperty(value = "推送平台，'1':SDK，'2': 开放平台，'3': 九区玩家App")
    @ApiModelProperty("推送平台，'1':SDK，'2': 开放平台，'3': 九区玩家App")
    private String platformType;

    /**
     * 推送用户标识 '0':全部用户 '1':部分用户
     */
    @ExcelProperty(value = "推送用户标识 '0':全部用户 '1':部分用户")
    @ApiModelProperty("推送用户标识 '0':全部用户 '1':部分用户")
    private String pushUserType;

    /**
     * 自动推送标识，'0':自动，'1':手动
     */
    @ExcelProperty(value = "自动推送标识，'0':自动，'1':手动")
    @ApiModelProperty("自动推送标识，'0':自动，'1':手动")
    private String autoPush;

    /**
     * 状态，'0':待推送，'1':已推送
     */
    @ExcelProperty(value = "状态，'0':待推送，'1':已推送")
    @ApiModelProperty("状态，'0':待推送，'1':已推送")
    private String pushStatus;

    /**
     * 推送用户
     */
    @ExcelProperty(value = "推送用户")
    @ApiModelProperty("推送用户")
    private String pushUsers;

    /**
     * 推送时间
     */
    @ExcelProperty(value = "推送时间")
    @ApiModelProperty("推送时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date pushTime;

    /**
     * 是否置顶，'0':不置顶，'1':置顶
     */
    @ExcelProperty(value = "是否置顶，'0':不置顶，'1':置顶")
    @ApiModelProperty("是否置顶，'0':不置顶，'1':置顶")
    private String isTop;

    /**
     * 阅读状态 0:未读 1:已读
     */
    @ExcelProperty(value = "阅读状态 '0':未读 '1':已读")
    @ApiModelProperty("阅读状态 '0':未读 '1':已读")
    private String readStatus = "0";

    /**
     * 消息类型（1通知 2公告）
     */
    @ExcelProperty(value = "消息类型（1通知 2公告）")
    @ApiModelProperty(value = "消息类型（1通知 2公告）")
    private String noticeType;

    /**
     * 消息类型 1:点赞 2:评论 3:新粉丝 4:系统消息
     */
    @ExcelProperty(value = "消息类型 1:点赞 2:评论 3:新粉丝 4:系统消息")
    @ApiModelProperty(value = "消息类型 1:点赞 2:评论 3:新粉丝 4:系统消息")
    private String type;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    @ApiModelProperty("创建时间")
    private Date createTime;

}
