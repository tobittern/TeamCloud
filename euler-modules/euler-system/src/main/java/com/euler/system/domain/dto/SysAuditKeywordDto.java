package com.euler.system.domain.dto;

import com.euler.common.mybatis.core.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 审核关键词 - 敏感词业务对象 audit_keyword
 *
 * @author euler
 * @date 2022-03-21
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("审核关键词 - 敏感词业务对象")
public class SysAuditKeywordDto extends PageQuery {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键", required = true)
    private Integer id;

    /**
     * 添加用户id
     */
    @ApiModelProperty(value = "添加用户id", required = true)
    private Long userId;

    /**
     * 类型
     */
    @ApiModelProperty(value = "类型", required = true)
    private Integer type;

    /**
     * 敏感词
     */
    @ApiModelProperty(value = "敏感词", required = true)
    private String keywords;

    /**
     * 是否有效状态 0正常 1删除
     */
    @ApiModelProperty(value = "是否有效状态 0正常 1删除", required = true)
    private Integer isItValid;


}
