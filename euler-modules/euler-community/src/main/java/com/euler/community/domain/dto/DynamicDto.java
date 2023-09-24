package com.euler.community.domain.dto;

import com.euler.common.mybatis.core.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 动态业务对象 dynamic
 *
 * @author euler
 * @date 2022-06-01
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("动态业务对象")
public class DynamicDto extends PageQuery {

    /**
     * 动态ID
     */
    @ApiModelProperty(value = "动态ID", required = true)
    private Long id;

    /**
     * 分类ID
     */
    @ApiModelProperty(value = "分类ID", required = true)
    private Integer categoryId;

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID", required = true)
    private Long memberId;

    /**
     * 动态类型，1视频，2动态，3攻略
     */
    @ApiModelProperty(value = "动态类型，1视频，2动态，3攻略", required = true)
    private Integer type;

    /**
     * 搜索内容
     */
    @ApiModelProperty(value = "搜索内容", required = true)
    private String keyword;

    /**
     * 0：初稿，10：审核中，20：待发布，30：已发布，40：已下线，50：重新编辑，60：人工未通过，70 AI未通过
     */
    @ApiModelProperty(value = "0：初稿，10：审核中，20：待发布，30：已发布，40：已下线，50：重新编辑，60：人工未通过，70 AI未通过", required = true)
    private Integer status;

    /**
     * 是否下架，0未下架，1已下架
     */
    @ApiModelProperty(value = "是否下架，0未下架，1已下架", required = true)
    private String isUp;

    /**
     * 是否置顶，0不置顶，1置顶
     */
    @ApiModelProperty(value = "是否置顶，0不置顶，1置顶", required = true)
    private String isTop;

    /**
     * 是否允许点赞 0允许 1不允许
     */
    @ApiModelProperty(value = "是否允许点赞 0允许 1不允许", required = true)
    private String isCanFav;

    /**
     * 举报次数
     */
    @ApiModelProperty(value = "举报次数", required = true)
    private Integer reportNum;

    /**
     * 是否被举报
     */
    @ApiModelProperty(value = "是否被举报", required = true)
    private String isReport;

    /**
     * 审核人id,当前审核人
     */
    @ApiModelProperty(value = "审核人id,当前审核人", required = true)
    private Long auditUserId;

    /**
     * 话题
     */
    @ApiModelProperty(value = "话题", required = true)
    private String topic;

    /**
     * 发帖开始时间
     */
    @ApiModelProperty(value = "发帖开始时间", required = true)
    private String startTime;

    /**
     * 发帖结束时间
     */
    @ApiModelProperty(value = "发帖结束时间", required = true)
    private String endTime;

}
