package com.euler.community.domain.entity;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.euler.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 消息接收视图对象 message_to_users
 *
 * @author euler
 * @date 2022-04-08
 */
@Data
@ApiModel("消息接收视图对象")
@TableName("message_to_users")
@ExcelIgnoreUnannotated
public class MessageToUsers extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id")
    @ExcelProperty(value = "id")
    @ApiModelProperty("id")
    private Long id;

    /**
     * 消息id
     */
    @ExcelProperty(value = "消息id")
    @ApiModelProperty("消息id")
    private Long messageId;

    /**
     * 接收人
     */
    @ExcelProperty(value = "接收人")
    @ApiModelProperty("接收人")
    private Long toUserId;

    /**
     * 消息类型 1:点赞 2:评论 3:新粉丝 4:系统消息
     */
    private String type;

    /**
     * 关联的id
     */
    private Long relationId;

    /**
     * 阅读状态 0:未读 1:已读
     */
    @ExcelProperty(value = "阅读状态 0:未读 1:已读")
    @ApiModelProperty("阅读状态 0:未读 1:已读")
    private String readStatus;

    /**
     * 删除状态 0正常 2删除
     */
    @TableLogic
    private String delFlag;

}

