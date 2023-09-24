package com.euler.sdk.domain.dto;

import com.euler.common.mybatis.core.page.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SearchMemberlDto extends PageQuery {

    @ApiModelProperty(value = "会员id")
    private Long memberId;

    @ApiModelProperty(value = "用户昵称")
    private String nickName;

    @ApiModelProperty(value = "手机号码")
    private String mobile;

}
