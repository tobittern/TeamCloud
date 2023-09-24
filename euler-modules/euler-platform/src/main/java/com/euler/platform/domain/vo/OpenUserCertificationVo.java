package com.euler.platform.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


/**
 * 用户的资质认证记录视图对象 open_user_auth
 *
 * @author open
 * @date 2022-02-23
 */
@Data
@ApiModel("用户的资质认证记录视图对象")
@ExcelIgnoreUnannotated
public class OpenUserCertificationVo {

    private static final long serialVersionUID = 1L;

    /**
     * 主键自增
     */
    @ExcelProperty(value = "主键自增")
    @ApiModelProperty("主键自增")
    private Integer id;

    /**
     * 用户ID
     */
    @ExcelProperty(value = "用户ID")
    @ApiModelProperty("用户ID")
    private Long userId;

    /**
     * 审核状态(0初始状态 1待审核 2审核通过 3审核拒绝)
     */
    @ExcelProperty(value = "审核状态(0初始状态 1待审核 2审核通过 3审核拒绝)")
    @ApiModelProperty("审核状态(0初始状态 1待审核 2审核通过 3审核拒绝)")
    private Integer auditStatus;

    /**
     * 功能名称
     */
    @ExcelProperty(value = "功能名称")
    @ApiModelProperty("功能名称")
    private String companyName;

    /**
     * 营业执照
     */
    @ExcelProperty(value = "营业执照")
    @ApiModelProperty("营业执照")
    private String businessLicense;

    /**
     * 公司法人
     */
    @ExcelProperty(value = "公司法人")
    @ApiModelProperty("公司法人")
    private String legalPerson;

    /**
     * 税号
     */
    @ExcelProperty(value = "税号")
    @ApiModelProperty("税号")
    private String dutyParagraph;

    /**
     * 注册地址
     */
    @ExcelProperty(value = "注册地址")
    @ApiModelProperty("注册地址")
    private String registeredAddress;

    /**
     * 经营期限 - 开始时间
     */
    @ExcelProperty(value = "经营期限 - 开始时间")
    @ApiModelProperty("经营期限 - 开始时间")
    private Date operatingPeriodStart;

    /**
     * 经营期限 - 结束时间
     */
    @ExcelProperty(value = "经营期限 - 结束时间")
    @ApiModelProperty("经营期限 - 结束时间")
    private Date operatingPeriodEnd;

    /**
     * 是否长期有效
     */
    @ExcelProperty(value = "是否长期有效")
    @ApiModelProperty("是否长期有效")
    private Integer isLongEffective;

    /**
     * 经营范围
     */
    @ExcelProperty(value = "经营范围")
    @ApiModelProperty("经营范围")
    private String natureOfBusiness;

    /**
     * 联系人姓名
     */
    @ExcelProperty(value = "联系人姓名")
    @ApiModelProperty("联系人姓名")
    private String contactName;

    /**
     * 联系人身份证
     */
    @ExcelProperty(value = "联系人身份证")
    @ApiModelProperty("联系人身份证")
    private String contactIdCard;

    /**
     * 联系人电话
     */
    @ExcelProperty(value = "联系人电话")
    @ApiModelProperty("联系人电话")
    private String contactPhone;

    /**
     * 审核时间
     */
    @ExcelProperty(value = "审核时间")
    @ApiModelProperty("审核时间")
    private Date auditTime;

    /**
     * 认证主题类型
     */
    @ExcelProperty(value = "认证主体类型")
    @ApiModelProperty(value = "认证主体类型")
    private String authType;

    /**
     * 提交时间
     */
    @ExcelProperty(value = "提交时间")
    @ApiModelProperty("提交时间")
    private Date createTime;



}
