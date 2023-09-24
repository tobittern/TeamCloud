package com.euler.platform.domain.bo;

import com.euler.common.core.validate.AddGroup;
import com.euler.common.core.validate.EditGroup;
import com.euler.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
 * @author open
 * @date 2022-02-28
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("验证码业务对象")
public class CaptchaCodeBo extends BaseEntity {

    /**
     * 主键自增
     */
    @ApiModelProperty(value = "主键自增", required = true)
    @NotNull(message = "主键自增不能为空", groups = {EditGroup.class})
    private Long id;

    /**
     * 接收方
     */
    @ApiModelProperty(value = "接收方")
    @NotBlank(message = "接收方不能为空", groups = {AddGroup.class, EditGroup.class})
    private String receiver;

    /**
     * 验证码
     */
    @ApiModelProperty(value = "验证码")
    @NotBlank(message = "验证码不能为空", groups = {AddGroup.class, EditGroup.class})
    private String code;

    /**
     * 使用类型 1不需要极验 2需要极验
     */
    @ApiModelProperty(value = "使用类型 1不需要极验 2需要极验")
    private String type = "1";

    /**
     * 发送类型 1邮箱 2手机
     */
    @ApiModelProperty(value = "发送类型 1邮箱 2手机")
    private String sendType = "1";

    /**
     * ip
     */
    @ApiModelProperty(value = "ip")
    private String ip;

}
