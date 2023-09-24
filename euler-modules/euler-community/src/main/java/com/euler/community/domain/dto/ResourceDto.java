package com.euler.community.domain.dto;

import com.euler.common.mybatis.core.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 动态所有资源业务对象 resource
 *
 * @author euler
 * @date 2022-06-09
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("动态所有资源业务对象")
public class ResourceDto extends PageQuery {

    /**
     * 创建人id
     */
    @ApiModelProperty(value = "创建人id", required = true)
    private Long memberId;

    /**
     * 动态表主键id
     */
    @ApiModelProperty(value = "动态表主键id", required = true)
    private Long dynamicId;

    /**
     * 附件类型，1图片 2视频
     */
    @ApiModelProperty(value = "附件类型，1图片 2视频", required = true)
    private Integer fileType;

    /**
     * 附件名称
     */
    @ApiModelProperty(value = "附件名称", required = true)
    private String fileName;


    /**
     * 审核状态 0 初始状态 1 待审中  2审核成功 3审核拒绝
     */
    @ApiModelProperty(value = "审核状态 0 初始状态 1 待审中  2审核成功 3审核拒绝", required = true)
    private Integer auditStatus = 0;

    /**
     * 审核原因
     */
    @ApiModelProperty(value = "审核原因", required = true)
    private String auditContent;


}
