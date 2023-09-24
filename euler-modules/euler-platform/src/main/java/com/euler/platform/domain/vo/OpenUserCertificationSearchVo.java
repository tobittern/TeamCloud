package com.euler.platform.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 用户的资质认证记录视图对象 open_user_auth
 *
 * @author open
 * @date 2022-02-23
 */
@Data
@ApiModel("用户的资质认证记录视图对象")
@ExcelIgnoreUnannotated
public class OpenUserCertificationSearchVo {

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
     * 公司名称
     */
    @ExcelProperty(value = "公司名称")
    @ApiModelProperty("公司名称")
    private String companyName;

    /**
     * 联系人姓名
     */
    @ExcelProperty(value = "联系人姓名")
    @ApiModelProperty("联系人姓名")
    private String contactName;

    /**
     * 联系人电话
     */
    @ExcelProperty(value = "联系人电话")
    @ApiModelProperty("联系人电话")
    private String contactPhone;

}
