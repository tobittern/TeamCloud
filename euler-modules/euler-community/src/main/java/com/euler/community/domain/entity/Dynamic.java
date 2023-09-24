package com.euler.community.domain.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.euler.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 动态对象 dynamic
 *
 * @author euler
 * @date 2022-06-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("dynamic")
public class Dynamic extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 分类ID
     */
    private Integer categoryId;
    /**
     * 用户ID
     */
    private Long memberId;
    /**
     * 动态类型，1视频，2动态，3攻略
     */
    private Integer type;
    /**
     * 标题
     */
    private String title;
    /**
     * 短标题（副标题）
     */
    private String shortTitle;
    /**
     * 封面图片
     */
    private String cover;
    /**
     * 封面图片宽
     */
    private Integer coverWidth;
    /**
     * 封面图片高
     */
    private Integer coverHeight;
    /**
     * 封面图片类型 1:长图  2:竖图  3:3图 4:9图
     */
    private Integer coverType;
    /**
     * 资源URL
     */
    private String resourceUrl;
    /**
     * 文字内容
     */
    private String content;
    /**
     * 0：初稿，10：审核中，20：待发布，30：已发布，40：已下线，50：重新编辑，60：人工未通过，70 AI未通过
     */
    private Integer status;
    /**
     * 转发量
     */
    private Integer forwardNum;
    /**
     * 虚拟转发量
     */
    private Integer virtualForwardNum;
    /**
     * 点赞量
     */
    private Integer praiseNum;
    /**
     * 虚拟点赞量
     */
    private Integer virtualPraiseNum;
    /**
     * 评论量
     */
    private Integer commentNum;
    /**
     * 虚拟评论量
     */
    private Integer virtualCommentNum;
    /**
     * 收藏量
     */
    private Integer collectNum;
    /**
     * 虚拟收藏量
     */
    private Integer virtualCollectNum;
    /**
     * 点击量
     */
    private Integer hitNum;
    /**
     * 虚拟点击量
     */
    private Integer virtualHitNum;
    /**
     * 被举报次数
     */
    private Integer reportNum;
    /**
     * 是否下架，0未下架，1已下架
     */
    private String isUp;
    /**
     * 是否置顶，0不置顶，1置顶
     */
    private String isTop;
    /**
     * 是否允许点赞 0允许 1不允许
     */
    private String isCanFav;
    /**
     * 是否仅我可见 0全部可见  1仅我可见
     */
    private String isOnlyMeSee;
    /**
     * 是否是原创 0原创 1 不是原创
     */
    private String isOriginal;

    /**
     * 创作的详细地址
     */
    private String location;

    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 区
     */
    private String area;
    /**
     * 审核人id,当前审核人
     */
    private Long auditUserId;
    /**
     * 审核时间，最新的审核时间
     */
    private Date auditTime;
    /**
     * 预留字段 1
     */
    private String prop1;
    /**
     * 预留字段 2
     */
    private String prop2;
    /**
     * 预留字段 3
     */
    private String prop3;
    /**
     * 预留字段 4
     */
    private Integer prop4;
    /**
     * 预留字段 5
     */
    private Long prop5;
    /**
     * 预留字段 6
     */
    private String prop6;
    /**
     * 预留字段 7
     */
    private String prop7;
    /**
     * 预留字段 8
     */
    private String prop8;
    /**
     * 预留字段 9
     */
    private Long prop9;
    /**
     * 预留字段 10
     */
    private Integer prop10;
    /**
     * 上线时间
     */
    private Date onlineTime;
    /**
     * 是否删除，0未删除，2已删除
     */
    private String delFlag;

}
