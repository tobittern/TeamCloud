package com.euler.community.domain.bo;

import com.euler.common.core.validate.AddGroup;
import com.euler.common.core.validate.EditGroup;
import com.euler.common.mybatis.core.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * banner列业务对象 banner
 *
 * @author euler
 * @date 2022-06-07
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("banner列业务对象")
public class BannerBo extends PageQuery {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键", required = true)
    @NotNull(message = "主键不能为空", groups = {EditGroup.class})
    private Long id;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id", required = true)
    private Long memberId;

    /**
     * banner名
     */
    @ApiModelProperty(value = "banner名", required = true)
    @NotBlank(message = "banner名不能为空", groups = {AddGroup.class, EditGroup.class})
    private String bannerName;

    /**
     * banner图
     */
    @ApiModelProperty(value = "banner图", required = true)
    @NotBlank(message = "banner图不能为空", groups = {AddGroup.class, EditGroup.class})
    private String bannerIcon;

    /**
     * 跳转路径
     */
    @ApiModelProperty(value = "跳转路径", required = true)
    private String jumpUrl;

}
