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
 * 话题业务对象 topic
 *
 * @author euler
 * @date 2022-06-06
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("话题业务对象")
public class TopicBo extends BaseEntity {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键", required = true)
    @NotNull(message = "主键不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private Long memberId;

    /**
     * 话题名称
     */
    @ApiModelProperty(value = "话题名称", required = true)
    @NotBlank(message = "话题名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String topicName;

    /**
     * 话题访问次数
     */
    @ApiModelProperty(value = "话题访问次数")
    private Long searchCount = 0L;

    /**
     * 话题曝光次数
     */
    @ApiModelProperty(value = "话题曝光次数")
    private Long exposureCount = 0L;

    /**
     * 状态： 0:未发布 1:已发布
     */
    @ApiModelProperty(value = "状态： 0:未发布 1:已发布")
    private String status;

}
