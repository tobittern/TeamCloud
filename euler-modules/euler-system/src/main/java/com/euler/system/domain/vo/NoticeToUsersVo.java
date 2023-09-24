package com.euler.system.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


/**
 * 公告接收视图对象 notice_to_users
 *
 * @author euler
 * @date 2022-04-08
 */
@Data
@ApiModel("公告接收视图对象")
@ExcelIgnoreUnannotated
public class NoticeToUsersVo {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
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

}
