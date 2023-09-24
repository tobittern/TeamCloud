package com.euler.risk.domain.bo;

import com.euler.common.core.validate.AddGroup;
import com.euler.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.*;

import java.util.Date;

/**
 * 黑名单业务对象 blacklist
 *
 * @author euler
 * @date 2022-08-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("黑名单业务对象")
public class BlacklistBo extends BaseEntity {

    /**
     * 自增主键
     */
    @ApiModelProperty(value = "自增主键")
    private Integer id;

    /**
     * 类型（1:手机号 2:身份证 3:ip 4:mac 5:oaid 6:imei 7:android 8:uuid 9:idfa 10:pushId）
     */
    @ApiModelProperty(value = "类型（1:手机号 2:身份证 3:ip 4:mac 5:oaid 6:imei 7:android 8:uuid 9:idfa 10:pushId）", required = true)
    @NotBlank(message = "类型（1:手机号 2:身份证 3:ip 4:mac 5:oaid 6:imei 7:android 8:uuid 9:idfa 10:pushId）不能为空", groups = {AddGroup.class})
    private String type;

    /**
     * 类型对应的目标内容
     */
    @ApiModelProperty(value = "类型对应的目标内容", required = true)
    @NotBlank(message = "类型对应的目标内容不能为空", groups = {AddGroup.class})
    private String target;

    /**
     * 封号类型
     */
    @ApiModelProperty(value = "类型", required = true)
    @NotBlank(message = "封号类型不能为空", groups = {AddGroup.class})
    private String banType;

    /**
     * 封号开始时间
     */
    @ApiModelProperty(value = "封号开始时间", required = true)
    @NotNull(message = "封号开始时间不能为空", groups = {AddGroup.class})
    private String startTime;

    /**
     * 封号截止时间
     */
    @ApiModelProperty(value = "封号截止时间", required = true)
    @NotNull(message = "封号截止时间不能为空", groups = {AddGroup.class})
    private String endTime;

    /**
     * 封号原因
     */
    @ApiModelProperty(value = "封号原因", required = true)
    @NotBlank(message = "封号原因不能为空", groups = {AddGroup.class})
    private String reason;

}
