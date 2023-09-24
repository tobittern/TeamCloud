package com.euler.community.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.euler.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 礼包配置对象 gift_bag
 *
 * @author euler
 * @date 2022-06-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("gift_bag")
public class GiftBag extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 游戏id
     */
    private Long gameId;
    /**
     * 游戏名称
     */
    private String gameName;
    /**
     * 礼包名称
     */
    private String giftName;
    /**
     * 激活码
     */
    private String activationCode;
    /**
     * 礼包图片名称
     */
    private String picName;
    /**
     * 礼包图片路径
     */
    private String picPath;
    /**
     * 礼包内容
     */
    private String content;
    /**
     * 使用方式
     */
    private String useMethod;
    /**
     * 有效期-开始时间
     */
    private Date startTime;
    /**
     * 有效期-结束时间
     */
    private Date endTime;
    /**
     * 礼包总数
     */
    private Integer totalNum;
    /**
     * 礼包领取数目
     */
    private Integer drawNum;
    /**
     * 礼包兑换数目
     */
    private Integer exchangeNum;
    /**
     * 状态，0待上架，1已上架，2已下架
     */
    private Integer status;
    /**
     * 资源表主键id,用于附件下载
     */
    private Long ossId;
    /**
     * cdk礼包文件名称
     */
    private String cdkFileName;
    /**
     * cdk礼包文件路径
     */
    private String cdkFilePath;
    /**
     * 应用平台 1：android 2：ios 3：h5
     */
    private String applicationType;
    /**
     * ios下载地址
     */
    private String iosDownloadUrl;
    /**
     * android下载地址
     */
    private String androidDownloadUrl;
    /**
     * h5下载地址
     */
    private String h5DownloadUrl;
    /**
     * 是否删除，0未删除，2已删除
     */
    @TableLogic
    private String delFlag;

}
