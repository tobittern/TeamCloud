package com.euler.system.domain.dto;

import com.euler.common.mybatis.core.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("通知公告业务对象")
public class NoticeToUsersDto extends PageQuery{
    /**
     * 公告ID
     */
    @ApiModelProperty(value = "公告ID")
    private Integer noticeId;

    /**
     * 接收人
     */
    @ApiModelProperty("接收人")
    private Long toUserId;

    /**
     * 阅读状态 0:未读 1:已读
     */
    @ApiModelProperty("阅读状态 0:未读 1:已读")
    private String readStatus;

}
