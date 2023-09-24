package com.euler.sdk.domain.dto;

import com.euler.common.mybatis.core.page.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class MemberPageDto extends PageQuery {

    @ApiModelProperty(value = "会员id")
    private Long memberId;

    @ApiModelProperty(value = "会员ids")
    private List<Long> memberIds;

    @ApiModelProperty("全网唯一标识")
    private String uniqueId;

    @ApiModelProperty(value = "用户昵称")
    private String nickName;

    @ApiModelProperty(value = "手机号码")
    private String mobile;

    @ApiModelProperty(value = "身份证")
    private String idCardNo;

    @ApiModelProperty("来源游戏")
    private  String gameName;

    @ApiModelProperty("来源渠道号")
    private  String packageCode;

    @ApiModelProperty("会员等级")
    private Long memberRightsId;

    @ApiModelProperty("最后登录时间")
    private String lastLoginTime;

    @ApiModelProperty(value = "真实姓名")
    private String realName;

    @ApiModelProperty(value = "实名认证状态")
    private String verifyStatus;

    @ApiModelProperty(value = "用户性别（1男 0女 2未知）")
    private String sex;

    @ApiModelProperty(value = "主渠道id")
    private Integer channelId;

    @ApiModelProperty(value = "是否是官方账号")
    private Integer isOfficial;

}
