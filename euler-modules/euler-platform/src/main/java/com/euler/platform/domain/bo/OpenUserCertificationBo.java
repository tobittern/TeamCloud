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
import java.util.Date;

/**
 * 用户的资质认证记录业务对象 open_user_auth
 *
 * @author open
 * @date 2022-02-23
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("用户的资质认证记录业务对象")
public class OpenUserCertificationBo extends BaseEntity {

    /**
     * 主键自增
     */
    @ApiModelProperty(value = "主键自增", required = true)
    @NotNull(message = "主键自增不能为空", groups = { EditGroup.class })
    private Integer id;

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID")
    private Long userId;

    /**
     * 审核状态(0初始状态 1待审核 2审核通过 3审核拒绝)
     */
    @ApiModelProperty(value = "审核状态(0初始状态 1待审核 2审核通过 3审核拒绝)")
    private Integer auditStatus;

    /**
     * 功能名称
     */
    @ApiModelProperty(value = "公司名称", required = true)
    @NotBlank(message = "公司名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String companyName;

    /**
     * 营业执照
     */
    @ApiModelProperty(value = "营业执照", required = true)
    @NotBlank(message = "营业执照不能为空", groups = { AddGroup.class, EditGroup.class })
    private String businessLicense;

    /**
     * 公司法人
     */
    @ApiModelProperty(value = "公司法人", required = true)
    @NotBlank(message = "公司法人不能为空", groups = { AddGroup.class, EditGroup.class })
    private String legalPerson;

    /**
     * 税号
     */
    @ApiModelProperty(value = "税号", required = true)
    @NotBlank(message = "税号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String dutyParagraph;

    /**
     * 注册地址
     */
    @ApiModelProperty(value = "注册地址", required = true)
    @NotBlank(message = "注册地址不能为空", groups = { AddGroup.class, EditGroup.class })
    private String registeredAddress;

    /**
     * 经营期限 - 开始时间
     */
    @ApiModelProperty(value = "经营期限 - 开始时间", required = true)
    private Date operatingPeriodStart;

    /**
     * 经营期限 - 结束时间
     */
    @ApiModelProperty(value = "经营期限 - 结束时间", required = true)
    private Date operatingPeriodEnd;

    /**
     * 是否长期有效
     */
    @ApiModelProperty(value = "是否长期有效")
    private Integer isLongEffective;

    /**
     * 经营范围
     */
    @ApiModelProperty(value = "经营范围", required = true)
    @NotBlank(message = "经营范围不能为空", groups = { AddGroup.class, EditGroup.class })
    private String natureOfBusiness;

    /**
     * 联系人姓名
     */
    @ApiModelProperty(value = "联系人姓名", required = true)
    @NotBlank(message = "联系人姓名不能为空", groups = { AddGroup.class, EditGroup.class })
    private String contactName;

    /**
     * 联系人身份证
     */
    @ApiModelProperty(value = "联系人身份证", required = true)
    @NotBlank(message = "联系人身份证不能为空", groups = { AddGroup.class, EditGroup.class })
    private String contactIdCard;

    /**
     * 联系人电话
     */
    @ApiModelProperty(value = "联系人电话", required = true)
    @NotBlank(message = "联系人电话不能为空", groups = { AddGroup.class, EditGroup.class })
    private String contactPhone;

    /**
     * 认证主题类型
     */
    @ApiModelProperty(value = "认证主体类型", required = true)
    @NotBlank(message = "认证主体类型", groups = { AddGroup.class, EditGroup.class })
    private String authType;

}
