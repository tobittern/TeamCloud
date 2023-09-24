package com.euler.community.domain.entity;

import com.euler.community.domain.vo.CommentVo;
import lombok.Data;

/**
 * 动态对象 dynamic es
 *
 * @author euler
 * @date 2022-06-01
 */
@Data
public class DynamicFrontEs {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;
    /**
     * 用户ID
     */
    private Long memberId;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 头像
     */
    private String avatar;

    /**
     * 分类ID
     */
    private Integer categoryId;

    /**
     * 动态类型，1视频，2动态，3攻略
     */
    private Integer type;
    /**
     * 0：初稿，10：审核中，20：待发布，30：已发布，40：已下线，50：重新编辑，60：人工未通过，70 AI未通过
     */
    private Integer status;
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
     * 封面图片的宽度
     */
    private Integer coverWidth;
    /**
     * 封面图片的高度
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
     * 专题
     */
    private String topic;
    /**
     * 转发量
     */
    private Integer forwardNum;
    /**
     * 点赞量
     */
    private Integer praiseNum;
    /**
     * 评论量
     */
    private Integer commentNum;
    /**
     * 收藏量
     */
    private Integer collectNum;
    /**
     * 点击量
     */
    private Integer hitNum;
    /**
     * 被举报次数
     */
    private Integer reportNum;
    /**
     * 创建时间
     */
    private String onlineTime;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 更新时间
     */
    private String updateTime;
    /**
     * 时间格式化 yyyy-mm-dd
     */
    private String createTimeOrder;
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
     * 删除
     */
    private String delFlag;


    /******  每个用户针对每条动态的基础属性 这个是代码里面赋值上去的  ****/
    /**
     * 是否点赞 0 没有点赞  1点赞过
     */
    private Integer isPraise = 0;
    /**
     * 是否收藏 0 没有收藏 1收藏过
     */
    private Integer isCollect = 0;
    /**
     * 是否关注过 0没有关注 1关注过
     */
    private Integer isAttention = 0;
    /**
     * 是否是广告 0不是 1是
     */
    private Integer isAdv = 0;
    /**
     * 是否是官方账号
     */
    private Integer isOfficial = 0;
    /**
     * 跳转链接
     */
    private String jumpUrl;

    /**
     * 每个用户曝光次数
     */
    private Integer exposureTimes;

    /**
     * 动态的神评
     */
    private CommentVo commentFrontVo;
}

