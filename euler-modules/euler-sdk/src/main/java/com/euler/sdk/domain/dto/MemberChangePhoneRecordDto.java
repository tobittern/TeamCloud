package com.euler.sdk.domain.dto;

import com.euler.common.core.validate.AddGroup;
import com.euler.common.core.validate.EditGroup;
import com.euler.common.mybatis.core.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
 * 用户更换手机号记录业务对象 member_change_phone_record
 *
 * @author euler
 * @date 2022-06-13
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("用户更换手机号记录业务对象")
public class MemberChangePhoneRecordDto extends PageQuery {


    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID", required = true)
    @NotNull(message = "用户ID不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long memberId;

    /**
     * 原始手机号码
     */
    @ApiModelProperty(value = "原始手机号码", required = true)
    @NotBlank(message = "原始手机号码不能为空", groups = {AddGroup.class, EditGroup.class})
    private String originalMobile;

    /**
     * 新手机号
     */
    @ApiModelProperty(value = "新手机号", required = true)
    @NotBlank(message = "新手机号不能为空", groups = {AddGroup.class, EditGroup.class})
    private String newMobile;


}
