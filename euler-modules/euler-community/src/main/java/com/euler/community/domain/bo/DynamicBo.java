package com.euler.community.domain.bo;

import com.euler.common.core.validate.AddGroup;
import com.euler.common.core.validate.EditGroup;
import com.euler.common.core.web.domain.BaseEntity;
import com.euler.common.core.xss.Xss;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * 动态业务对象 dynamic
 *
 * @author euler
 * @date 2022-06-01
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("动态业务对象")
public class DynamicBo extends BaseEntity {

    /**
     * 分类ID
     */
    @ApiModelProperty(value = "分类ID", required = true)
    private Integer categoryId = 1;

    /**
     * 关联的话题
     */
    @ApiModelProperty(value = "关联的话题", required = true)
    private String topicIds;

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID")
    private Long memberId;

    /**
     * 动态类型，1视频，2动态，3攻略
     */
    @ApiModelProperty(value = "动态类型，1视频，2动态，3攻略", required = true)
    @NotNull(message = "动态类型不能为空", groups = {AddGroup.class, EditGroup.class})
    private Integer type;

    /**
     * 标题
     */
    @ApiModelProperty(value = "标题", required = true)
    private String title;

    /**
     * 短标题（副标题）
     */
    @ApiModelProperty(value = "短标题（副标题）", required = true)
    private String shortTitle;

    /**
     * 封面图片
     */
    @ApiModelProperty(value = "封面图片", required = true)
    private String cover;

    /**
     * 封面图片宽
     */
    @ApiModelProperty(value = "封面图片宽")
    private Integer coverWidth;

    /**
     * 封面图片高
     */
    @ApiModelProperty(value = "封面图片高")
    private Integer coverHeight;

    /**
     * 资源URL
     */
    @ApiModelProperty(value = "资源URL", required = true)
    private String resourceUrl;

    /**
     * 文字内容
     */
    @ApiModelProperty(value = "文字内容", required = true)
    private String content;

    /**
     * 是否提交 0 不提交 1提交
     */
    @ApiModelProperty(value = "是否提交", required = true)
    private Integer isSubmit = 1;

}
