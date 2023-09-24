package com.euler.community.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


/**
 * 广告视图对象 advert
 *
 * @author euler
 * @date 2022-06-06
 */
@Data
@ApiModel("广告视图对象")
@ExcelIgnoreUnannotated
public class AdvertVo {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    @ApiModelProperty("主键")
    private Long id;

    /**
     * 用户id
     */
    @ExcelProperty(value = "用户id")
    @ApiModelProperty("用户id")
    private Long memberId;

    /**
     * 推广素材，0视频，1图片
     */
    @ExcelProperty(value = "推广素材，0视频，1图片")
    @ApiModelProperty("推广素材，0视频，1图片")
    private Integer type;

    /**
     * 状态，0未启用，1已启用
     */
    @ExcelProperty(value = "状态，0未启用，1已启用")
    @ApiModelProperty("状态，0未启用，1已启用")
    private Integer status;

    /**
     * 广告在第几页
     */
    @ExcelProperty(value = "广告在第几页")
    @ApiModelProperty("广告在第几页")
    private Integer page;

    /**
     * 广告在第几行
     */
    @ExcelProperty(value = "广告在第几行")
    @ApiModelProperty("广告在第几行")
    private Integer row;

    /**
     * 每个用户的曝光次数
     */
    @ExcelProperty(value = "每个用户的曝光次数")
    @ApiModelProperty("每个用户的曝光次数")
    private Integer exposureTimes;

    /**
     * 昵称
     */
    @ExcelProperty(value = "昵称")
    @ApiModelProperty("昵称")
    private String nickName;

    /**
     * 头像附件名称
     */
    @ExcelProperty(value = "头像附件名称")
    @ApiModelProperty("头像附件名称")
    private String headFileName;

    /**
     * 头像url
     */
    @ExcelProperty(value = "头像url")
    @ApiModelProperty("头像url")
    private String headUrl;

    /**
     * 封面附件名称
     */
    @ExcelProperty(value = "封面附件名称")
    @ApiModelProperty("封面附件名称")
    private String coverFileName;

    /**
     * 封面附件路径
     */
    @ExcelProperty(value = "封面附件路径")
    @ApiModelProperty("封面附件路径")
    private String coverUrl;

    /**
     * 封面--宽
     */
    @ExcelProperty(value = "封面--宽")
    @ApiModelProperty("封面--宽")
    private Integer coverWidth;

    /**
     * 封面-高度
     */
    @ExcelProperty(value = "封面-高度")
    @ApiModelProperty("封面-高度")
    private Integer coverHeight;

    /**
     * 内容
     */
    @ExcelProperty(value = "内容")
    @ApiModelProperty("内容")
    private String content;

    /**
     * 话题
     */
    @ExcelProperty(value = "话题")
    @ApiModelProperty("话题")
    private String topics;

    /**
     * 游戏id
     */
    @ExcelProperty(value = "游戏id")
    @ApiModelProperty("游戏id")
    private Long gameId;

    /**
     * 游戏名字
     */
    @ExcelProperty(value = "游戏名字")
    @ApiModelProperty("游戏名字")
    private String gameName;

    /**
     * 跳转链接
     */
    @ExcelProperty(value = "跳转链接")
    @ApiModelProperty("跳转链接")
    private String jumpUrl;

    /**
     * 开始时间
     */
    @ExcelProperty(value = "开始时间")
    @ApiModelProperty("开始时间")
    private Date startTime;

    /**
     * 结束时间
     */
    @ExcelProperty(value = "结束时间")
    @ApiModelProperty("结束时间")
    private Date endTime;

    /**
     * 转发量
     */
    @ExcelProperty(value = "转发量")
    @ApiModelProperty("转发量")
    private Integer forwardNum;

    /**
     * 点赞量
     */
    @ExcelProperty(value = "点赞量")
    @ApiModelProperty("点赞量")
    private Integer praiseNum;

    /**
     * 评论量
     */
    @ExcelProperty(value = "评论量")
    @ApiModelProperty("评论量")
    private Integer commentNum;

    /**
     * 被举报次数
     */
    @ExcelProperty(value = "被举报次数")
    @ApiModelProperty("被举报次数")
    private Integer reportNum;

    /**
     * 收藏量
     */
    @ExcelProperty(value = "收藏量")
    @ApiModelProperty("收藏量")
    private Integer collectNum;

    /**
     * 点击量
     */
    @ExcelProperty(value = "点击量")
    @ApiModelProperty("点击量")
    private Integer hitNum;

    /**
     * 查看广告总量
     */
    @ExcelProperty(value = "查看广告总量")
    @ApiModelProperty("查看广告总量")
    private Integer viewNum;

    /**
     * 附件内容，json格式存储 [{"fileName":"a.jpg","filePath":""},{}]
     */
    @ExcelProperty(value = "附件内容")
    @ApiModelProperty("附件内容")
    private String fileContent;

    /**
     *
     */
    @ExcelProperty(value = "创建时间")
    @ApiModelProperty("创建时间")
    private Date createTime;


}
