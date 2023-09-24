package com.euler.community.domain.dto;

import com.euler.common.mybatis.core.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Date;
import java.util.List;

/**
 * banner组业务对象 banner_group
 *
 * @author euler
 * @date 2022-06-06
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("banner组业务对象")
public class BannerGroupDto extends PageQuery {

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
     * 应用场景，0发现菜单，1个人中心
     */
    @ApiModelProperty(value = "应用场景，0发现菜单，1个人中心")
    private String applicationType;

    /**
     * banner组名
     */
    @ApiModelProperty(value = "banner组名", required = true)
    private String groupName;

    /**
     * 显示开始时间
     */
    @ApiModelProperty(value = "显示开始时间")
    private Date startTime;

    /**
     * 显示结束时间
     */
    @ApiModelProperty(value = "显示结束时间")
    private Date endTime;

    /**
     * 状态，0待启用，1已启用，2已停用
     */
    @ApiModelProperty(value = "状态，0待启用，1已启用，2已停用")
    private String status;

    /**
     * banner组集合
     */
    @ApiModelProperty(value = "banner组集合")
    private List<Long> bannerGroupIds;

}
