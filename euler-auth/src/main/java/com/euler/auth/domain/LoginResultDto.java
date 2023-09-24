package com.euler.auth.domain;

import com.euler.sdk.api.domain.MemberBanVo;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("登录返回对象")
public class LoginResultDto {
    public LoginResultDto(Integer isLoginOut) {
        this.isLoginOut = isLoginOut;
    }
    @ApiModelProperty("登录token")
    @JsonProperty(value = "access_token")
    private String accessToken;
    @ApiModelProperty("true:需要补充密码,false:不需要补充密码")
    private Boolean fillPassword;
    @ApiModelProperty("是否实名认证，1：是，0：否")
    private String verifyStatus;
    @ApiModelProperty("用户id")
    private Long userId;
    @ApiModelProperty("是否退出登录")
    private Integer isLoginOut = 0;
    @ApiModelProperty("是否为无用户注册后的登录，1：是，0：否")
    private Integer isReg = 0;
    @ApiModelProperty("是否为新游戏登录，1：是，0：否")
    private Integer isNewGame = 0;
    @ApiModelProperty("用户封禁信息")
    private MemberBanVo memberBanVo;
}
