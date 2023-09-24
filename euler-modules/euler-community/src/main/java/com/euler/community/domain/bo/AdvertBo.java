package com.euler.community.domain.bo;

import com.euler.common.core.validate.AddGroup;
import com.euler.common.core.validate.EditGroup;
import com.euler.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;

import java.util.Date;

/**
 * 广告业务对象 advert
 *
 * @author euler
 * @date 2022-06-06
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("广告业务对象")
public class AdvertBo extends BaseEntity {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键", required = true)
    @NotNull(message = "主键不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id", required = true)
    private Long memberId;

    /**
     * 推广素材，0视频，1图片
     */
    @ApiModelProperty(value = "推广素材，0视频，1图片", required = true)
    @NotNull(message = "推广素材不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer type;

    /**
     * 状态，0待启用，1已启用，2已停用
     */
    @ApiModelProperty(value = "状态，0未启用，1已启用", required = true)
    private Integer status;

    /**
     * 广告在第几页
     */
    @ApiModelProperty(value = "广告在第几页", required = true)
    @NotNull(message = "广告在第几页不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer page;

    /**
     * 广告在第几行
     */
    @ApiModelProperty(value = "广告在第几行", required = true)
    @NotNull(message = "广告在第几行不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer row;

    /**
     * 每个用户的曝光次数
     */
    @ApiModelProperty(value = "每个用户的曝光次数", required = true)
    @NotNull(message = "每个用户的曝光次数不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer exposureTimes;

    /**
     * 昵称
     */
    @ApiModelProperty(value = "昵称", required = true)
    @NotBlank(message = "昵称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String nickName;

    /**
     * 头像附件名称
     */
    @ApiModelProperty(value = "头像附件名称", required = true)
    private String headFileName;

    /**
     * 头像url
     */
    @ApiModelProperty(value = "头像url", required = true)
    @NotBlank(message = "上传头像不能为空", groups = { AddGroup.class, EditGroup.class })
    private String headUrl;

    /**
     * 封面附件名称
     */
    @ApiModelProperty(value = "封面附件名称", required = true)
    private String coverFileName;

    /**
     * 封面附件路径
     */
    @ApiModelProperty(value = "封面附件路径", required = true)
    private String coverUrl;

    /**
     * 封面--宽
     */
    @ApiModelProperty(value = "封面--宽", required = true)
    private Integer coverWidth;

    /**
     * 封面-高度
     */
    @ApiModelProperty(value = "封面-高度", required = true)
    private Integer coverHeight;

    /**
     * 内容
     */
    @ApiModelProperty(value = "内容", required = true)
    @NotBlank(message = "内容不能为空", groups = { AddGroup.class, EditGroup.class })
    private String content;

    /**
     * 话题
     */
    @ApiModelProperty(value = "话题", required = true)
    @NotBlank(message = "话题不能为空", groups = { AddGroup.class, EditGroup.class })
    private String topics;

    /**
     * 游戏id
     */
    @ApiModelProperty(value = "游戏id", required = true)
    @NotNull(message = "游戏id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long gameId;

    /**
     * 游戏名字
     */
    @ApiModelProperty(value = "游戏名字", required = true)
    @NotBlank(message = "游戏名字不能为空", groups = { AddGroup.class, EditGroup.class })
    private String gameName;

    /**
     * 跳转链接
     */
    @ApiModelProperty(value = "跳转链接", required = true)
    @NotBlank(message = "跳转链接不能为空", groups = { AddGroup.class, EditGroup.class })
    private String jumpUrl;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间", required = true)
    @NotNull(message = "开始时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间", required = true)
    @NotNull(message = "结束时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date endTime;

    /**
     * 转发量
     */
    @ApiModelProperty(value = "转发量", required = true)
    @NotNull(message = "转发量不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer forwardNum;

    /**
     * 点赞量
     */
    @ApiModelProperty(value = "点赞量", required = true)
    @NotNull(message = "点赞量不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer praiseNum;

    /**
     * 评论量
     */
    @ApiModelProperty(value = "评论量", required = true)
    @NotNull(message = "评论量不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer commentNum;

    /**
     * 被举报次数
     */
    @ApiModelProperty(value = "被举报次数", required = true)
    private Integer reportNum;

    /**
     * 收藏量
     */
    @ApiModelProperty(value = "收藏量", required = true)
    @NotNull(message = "收藏量不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer collectNum;

    /**
     * 点击量
     */
    @ApiModelProperty(value = "点击量", required = true)
    private Integer hitNum;

    /**
     * 查看广告总量
     */
    @ApiModelProperty(value = "查看广告总量", required = true)
    private Integer viewNum;

    /**
     * 附件内容，json格式存储 [{"fileName":"a.jpg","filePath":""},{}]
     */
    @ApiModelProperty(value = "附件内容", required = true)
    private String fileContent;


}
