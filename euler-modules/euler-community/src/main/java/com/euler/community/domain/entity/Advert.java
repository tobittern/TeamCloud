package com.euler.community.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.euler.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 广告对象 advert
 *
 * @author euler
 * @date 2022-06-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("advert")
public class Advert extends BaseEntity {

private static final long serialVersionUID=1L;

    /**
     * 主键
     */
     @TableId(value = "id")
    private Long id;
    /**
     * 用户id
     */
    private Long memberId;
    /**
     * 推广素材，0视频，1图片
     */
    private Integer type;
    /**
     * 状态，0待启用，1已启用，2已停用
     */
    private Integer status;
    /**
     * 广告在第几页
     */
    private Integer page;
    /**
     * 广告在第几行
     */
    private Integer row;
    /**
     * 每个用户的曝光次数
     */
    private Integer exposureTimes;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 头像附件名称
     */
    private String headFileName;
    /**
     * 头像url
     */
    private String headUrl;
    /**
     * 封面附件名称
     */
    private String coverFileName;
    /**
     * 封面附件路径
     */
    private String coverUrl;
    /**
     * 封面--宽
     */
    private Integer coverWidth;
    /**
     * 封面-高度
     */
    private Integer coverHeight;
    /**
     * 内容
     */
    private String content;
    /**
     * 话题
     */
    private String topics;
    /**
     * 游戏id
     */
    private Long gameId;
    /**
     * 游戏名字
     */
    private String gameName;
    /**
     * 跳转链接
     */
    private String jumpUrl;
    /**
     * 开始时间
     */
    private Date startTime;
    /**
     * 结束时间
     */
    private Date endTime;
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
     * 被举报次数
     */
    private Integer reportNum;
    /**
     * 收藏量
     */
    private Integer collectNum;
    /**
     * 点击量
     */
    private Integer hitNum;
    /**
     * 查看广告总量
     */
    private Integer viewNum;
    /**
     * 附件内容，json格式存储 [{"fileName":"a.jpg","filePath":""},{}]
     */
    private String fileContent;
    /**
     * 是否删除，0未删除，2已删除
     */
     @TableLogic
    private String delFlag;

}
