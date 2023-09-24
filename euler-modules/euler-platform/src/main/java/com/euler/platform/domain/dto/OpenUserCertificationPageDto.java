package com.euler.platform.domain.dto;

import com.euler.common.mybatis.core.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户的资质认证记录业务对象 open_user_auth
 *
 * @author open
 * @date 2022-02-23
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("用户的资质认证记录业务对象")
public class OpenUserCertificationPageDto extends PageQuery {

    /**
     * 主键自增
     */
    @ApiModelProperty(value = "主键自增", required = true)
    private Integer id;

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID", required = true)
    private Long userId;

    /**
     * 审核状态(0初始状态 1待审核 2审核通过 3审核拒绝)
     */
    @ApiModelProperty(value = "审核状态(0初始状态 1待审核 2审核通过 3审核拒绝)", required = true)
    private Integer auditStatus;

    /**
     * 功能名称
     */
    @ApiModelProperty(value = "公司名称", required = true)
    private String companyName;
    /**
     * 联系人姓名
     */
    @ApiModelProperty(value = "联系人姓名", required = true)
    private String contactName;
    /**
     * 联系人电话
     */
    @ApiModelProperty(value = "联系人电话", required = true)
    private String contactPhone;

}
