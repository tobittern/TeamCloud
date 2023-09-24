package com.euler.community.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.euler.common.excel.annotation.ExcelDictFormat;
import com.euler.common.excel.convert.ExcelDictConvert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


/**
 * 动态视图对象 dynamic
 *
 * @author euler
 * @date 2022-06-01
 */
@Data
@ApiModel("动态视图对象")
@ExcelIgnoreUnannotated
public class DynamicVo {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    @ApiModelProperty("主键")
    private Long id;

    /**
     * 分类ID
     */
    @ExcelProperty(value = "分类ID")
    @ApiModelProperty("分类ID")
    private Integer categoryId;

    /**
     * 用户ID
     */
    @ExcelProperty(value = "用户ID")
    @ApiModelProperty("用户ID")
    private Long memberId;

    /**
     * 用户昵称
     */
    @ExcelProperty(value = "用户昵称")
    @ApiModelProperty("用户昵称")
    private String memberNickName;

    /**
     * 动态类型，1视频，2动态，3攻略
     */
    @ExcelProperty(value = "动态类型，1视频，2动态，3攻略")
    @ApiModelProperty("动态类型，1视频，2动态，3攻略")
    private Integer type;

    /**
     * 专题
     */
    @ExcelProperty(value = "专题")
    @ApiModelProperty("专题")
    private String topic;

    /**
     * 标题
     */
    @ExcelProperty(value = "标题")
    @ApiModelProperty("标题")
    private String title;

    /**
     * 短标题（副标题）
     */
    @ExcelProperty(value = "短标题", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "副=标题")
    @ApiModelProperty("短标题（副标题）")
    private String shortTitle;

    /**
     * 封面图片
     */
    @ExcelProperty(value = "封面图片")
    @ApiModelProperty("封面图片")
    private String cover;

    /**
     * 封面图片宽
     */
    @ExcelProperty(value = "封面图片宽")
    @ApiModelProperty("封面图片宽")
    private Integer coverWidth;

    /**
     * 封面图片高
     */
    @ExcelProperty(value = "封面图片高")
    @ApiModelProperty("封面图片高")
    private Integer coverHeight;

    /**
     * 封面图片类型 1:横图  2:竖图  3:2图 4:9图
     */
    @ExcelProperty(value = "封面图片类型 1:横图  2:竖图  3:2图 4:9图 ")
    @ApiModelProperty("封面图片类型 1:长图  2:横图  3:2图 4:9图 ")
    private Integer coverType;

    /**
     * 资源URL
     */
    @ExcelProperty(value = "资源URL")
    @ApiModelProperty("资源URL")
    private String resourceUrl;

    /**
     * 文字内容
     */
    @ExcelProperty(value = "文字内容")
    @ApiModelProperty("文字内容")
    private String content;

    /**
     * 0：初稿，10：流转中，20：待发布，30：已发布，40：已下线，50：重新编辑，70：人工未通过，80 AI未通过
     */
    @ExcelProperty(value = "0：初稿，10：审核中，20：待发布，30：已发布，40：已下线，50：重新编辑，60：人工未通过，70 AI未通过")
    @ApiModelProperty("0：初稿，10：审核中，20：待发布，30：已发布，40：已下线，50：重新编辑，60：人工未通过，70 AI未通过")
    private Integer status;

    /**
     * 转发量
     */
    @ExcelProperty(value = "转发量")
    @ApiModelProperty("转发量")
    private Integer forwardNum;

    /**
     * 虚拟转发量
     */
    @ExcelProperty(value = "虚拟转发量")
    @ApiModelProperty("虚拟转发量")
    private Integer virtualForwardNum;

    /**
     * 点赞量
     */
    @ExcelProperty(value = "点赞量")
    @ApiModelProperty("点赞量")
    private Integer praiseNum;

    /**
     * 虚拟点赞量
     */
    @ExcelProperty(value = "虚拟点赞量")
    @ApiModelProperty("虚拟点赞量")
    private Integer virtualPraiseNum;

    /**
     * 评论量
     */
    @ExcelProperty(value = "评论量")
    @ApiModelProperty("评论量")
    private Integer commentNum;

    /**
     * 虚拟评论量
     */
    @ExcelProperty(value = "虚拟评论量")
    @ApiModelProperty("虚拟评论量")
    private Integer virtualCommentNum;

    /**
     * 收藏量
     */
    @ExcelProperty(value = "收藏量")
    @ApiModelProperty("收藏量")
    private Integer collectNum;

    /**
     * 虚拟收藏量
     */
    @ExcelProperty(value = "虚拟收藏量")
    @ApiModelProperty("虚拟收藏量")
    private Integer virtualCollectNum;

    /**
     * 点击量
     */
    @ExcelProperty(value = "点击量")
    @ApiModelProperty("点击量")
    private Integer hitNum;

    /**
     * 虚拟点击量
     */
    @ExcelProperty(value = "虚拟点击量")
    @ApiModelProperty("虚拟点击量")
    private Integer virtualHitNum;

    /**
     * 被举报次数
     */
    @ExcelProperty(value = "被举报次数")
    @ApiModelProperty("被举报次数")
    private Integer reportNum = 0;

    /**
     * 评论举报次数
     */
    @ExcelProperty(value = "评论举报次数")
    @ApiModelProperty("评论举报次数")
    private Integer commentReportNum = 0;

    /**
     * 是否下架，0未下架，1已下架
     */
    @ExcelProperty(value = "是否下架，0未下架，1已下架")
    @ApiModelProperty("是否下架，0未下架，1已下架")
    private String isUp;

    /**
     * 是否置顶，0不置顶，1置顶
     */
    @ExcelProperty(value = "是否置顶，0不置顶，1置顶")
    @ApiModelProperty("是否置顶，0不置顶，1置顶")
    private String isTop;

    /**
     * 是否允许点赞 0允许 1不允许
     */
    @ExcelProperty(value = "是否允许点赞 0允许 1不允许")
    @ApiModelProperty("是否允许点赞 0允许 1不允许")
    private String isCanFav;

    /**
     * 是否仅我可见 0全部可见  1仅我可见
     */
    @ExcelProperty(value = "是否仅我可见 0全部可见  1仅我可见")
    @ApiModelProperty("是否仅我可见 0全部可见  1仅我可见")
    private String isOnlyMeSee;

    /**
     * 是否是原创 0原创 1 不是原创
     */
    @ExcelProperty(value = "是否是原创 0原创 1 不是原创")
    @ApiModelProperty("是否是原创 0原创 1 不是原创")
    private String isOriginal;

    /**
     * 审核人id,当前审核人
     */
    @ExcelProperty(value = "审核人id,当前审核人")
    @ApiModelProperty("审核人id,当前审核人")
    private Long auditUserId;

    /**
     * 审核时间，最新的审核时间
     */
    @ExcelProperty(value = "审核时间，最新的审核时间")
    @ApiModelProperty("审核时间，最新的审核时间")
    private Date auditTime;

    /**
     * 预留字段 1
     */
    @ExcelProperty(value = "预留字段 1")
    @ApiModelProperty("预留字段 1")
    private String prop1;

    /**
     * 预留字段 2
     */
    @ExcelProperty(value = "预留字段 2")
    @ApiModelProperty("预留字段 2")
    private String prop2;

    /**
     * 预留字段 3
     */
    @ExcelProperty(value = "预留字段 3")
    @ApiModelProperty("预留字段 3")
    private String prop3;

    /**
     * 预留字段 4
     */
    @ExcelProperty(value = "预留字段 4")
    @ApiModelProperty("预留字段 4")
    private Integer prop4;

    /**
     * 预留字段 5
     */
    @ExcelProperty(value = "预留字段 5")
    @ApiModelProperty("预留字段 5")
    private Long prop5;

    /**
     * 预留字段 6
     */
    @ExcelProperty(value = "预留字段 6")
    @ApiModelProperty("预留字段 6")
    private String prop6;

    /**
     * 预留字段 7
     */
    @ExcelProperty(value = "预留字段 7")
    @ApiModelProperty("预留字段 7")
    private String prop7;


    /**
     * 预留字段 8
     */
    @ExcelProperty(value = "预留字段 8")
    @ApiModelProperty("预留字段 8")
    private String prop8;

    /**
     * 预留字段 9
     */
    @ExcelProperty(value = "预留字段 9")
    @ApiModelProperty("预留字段 9")
    private Long prop9;

    /**
     * 预留字段 10
     */
    @ExcelProperty(value = "预留字段 10")
    @ApiModelProperty("预留字段 10")
    private Integer prop10;

    /**
     * 上线时间
     */
    @ExcelProperty(value = "上线时间")
    @ApiModelProperty("上线时间")
    private Date onlineTime;

    /**
     * 创建者
     */
    @ExcelProperty(value = "创建者")
    @ApiModelProperty("创建者")
    private String createBy;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    @ApiModelProperty("创建时间")
    private Date createTime;


}
