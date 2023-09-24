package com.euler.sdk.domain.dto;

import com.euler.common.mybatis.core.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 会员权益业务对象 member_rights
 *
 * @author euler
 * @date 2022-03-21
 */
@Data
@ApiModel("会员权益业务对象")
public class MemberRightsDto extends PageQuery {
    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 会员名称
     */
    @ApiModelProperty(value = "会员名称")
    private String name;

    /**
     * 会员等级 1:初级 2:中级 3:高级
     */
    @ApiModelProperty(value = "会员等级 1:初级 2:中级 3:高级")
    private String lever;

    /**
     * 会员状态：1:生效中 2:已失效
     */
    @ApiModelProperty(value = "会员状态：1:生效中 2:已失效")
    private String status;

    /**
     * 是否升级：0:未升级 1:升级
     */
    @ApiModelProperty(value = "是否升级：0:未升级 1:升级")
    private String isUpgrade;

    /**
     * 有效期开始时间
     */
    @ApiModelProperty(value = "有效期开始时间")
    private Date validateStartTime;

    /**
     * 有效期结束时间
     */
    @ApiModelProperty(value = "有效期结束时间")
    private Date validateEndTime;

}
