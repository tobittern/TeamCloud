package com.euler.community.domain.dto;

import com.euler.common.mybatis.core.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 话题业务对象 topic
 *
 * @author euler
 * @date 2022-06-06
 */

@Data
@ApiModel("话题业务对象")
public class TopicDto extends PageQuery {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private Long id;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private Long memberId;

    /**
     * 话题名称
     */
    @ApiModelProperty(value = "话题名称")
    private String topicName;

    /**
     * 状态：0:未发布 1:已发布
     */
    @ApiModelProperty(value = "状态：0:未发布 1:已发布")
    private String status;

}
