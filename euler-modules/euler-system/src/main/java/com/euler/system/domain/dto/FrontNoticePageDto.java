package com.euler.system.domain.dto;

import com.euler.common.mybatis.core.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("前台通知公告业务对象")
public class FrontNoticePageDto extends PageQuery {

    /**
     * 公告类型（1通知 2公告）
     */
    @ApiModelProperty(value = "公告类型（1通知 2公告）")
    private String noticeType;
    /**
     * 平台标识，1:sdk，2:开放平台
     */
    @ApiModelProperty(value = "平台标识，1:sdk，2:开放平台")
    private String platformType="1";

    /**
     * 阅读状态 0:未读 1:已读
     */
    @ApiModelProperty("阅读状态 0:未读 1:已读")
    private String readStatus;

    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private Long userId;

}
