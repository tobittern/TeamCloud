package com.euler.platform.domain.dto;

import com.euler.common.mybatis.core.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 *
 * @author euler
 * @date 2022-03-31
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("游戏版本历史业务对象")
public class OpenGameVersionHistoryDto extends PageQuery {

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id", required = true)
    private Long userId;

    /**
     * 游戏id
     */
    @ApiModelProperty(value = "游戏id", required = true)
    private Integer gameId;

    /**
     * 审核状态 (0 初始状态 1审核中  2审核成功  3审核失败)
     */
    @ApiModelProperty("审核状态 (0 初始状态 1审核中  2审核成功  3审核失败)")
    private Integer auditStatus;

}
