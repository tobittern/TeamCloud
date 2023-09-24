package com.euler.community.domain.bo;

import com.euler.common.core.validate.AddGroup;
import com.euler.common.core.validate.EditGroup;
import com.euler.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;

/**
 * 举报业务对象 report
 *
 * @author euler
 * @date 2022-06-09
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("举报业务对象")
public class ReportBo extends BaseEntity {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键", required = true)
    @NotNull(message = "主键不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 关联的ID
     */
    @ApiModelProperty(value = "关联的ID", required = true)
    @NotNull(message = "关联的ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long relationId;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private Long memberId;

    /**
     * 举报类型 1动态 2评论
     */
    @ApiModelProperty(value = "举报类型 1动态 2评论", required = true)
    @NotBlank(message = "举报类型 1动态 2评论不能为空", groups = { AddGroup.class, EditGroup.class })
    private String type;

    /**
     * 举报理由
     */
    @ApiModelProperty(value = "举报理由", required = true)
    @NotBlank(message = "举报理由不能为空", groups = { AddGroup.class, EditGroup.class })
    private String reason;

    /**
     * 所属动态的id
     */
    @ApiModelProperty(value = "所属动态的id", required = true)
    @NotNull(message = "所属动态的id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long dynamicId;

}
