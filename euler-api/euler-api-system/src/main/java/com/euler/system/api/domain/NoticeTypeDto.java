package com.euler.system.api.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@ApiModel("id入参")
public class NoticeTypeDto implements Serializable {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "公告类型（1通知 2公告）")
    private String noticeType;

    @ApiModelProperty(value = "推送用户标识，0:全部用户，1:部分用户")
    private String pushUserType;

    @ApiModelProperty(value = "阅读状态 0:未读 1:已读")
    private String readStatus;

    @ApiModelProperty(value = "推送状态 0:待推送 1:已推送")
    private String pushStatus;

    @ApiModelProperty(value = "推送平台，'1':SDK，'2': 开放平台，'3': 九区玩家App")
    @NotBlank(message = "推送平台，'1':SDK，'2': 开放平台，'3': 九区玩家App不能为空")
    private String platformType;

}
