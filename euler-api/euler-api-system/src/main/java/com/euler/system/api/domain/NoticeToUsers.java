package com.euler.system.api.domain;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.euler.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 公告接收视图对象 notice_to_users
 *
 * @author euler
 * @date 2022-04-08
 */
@Data
@ApiModel("公告接收视图对象")
@TableName("notice_to_users")
@ExcelIgnoreUnannotated
public class NoticeToUsers extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id")
    @ExcelProperty(value = "id")
    @ApiModelProperty("id")
    private Integer id;

    /**
     * 公告id
     */
    @ExcelProperty(value = "公告id")
    @ApiModelProperty("公告id")
    private Integer noticeId;

    /**
     * 接收人
     */
    @ExcelProperty(value = "接收人")
    @ApiModelProperty("接收人")
    private Long toUserId;

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
