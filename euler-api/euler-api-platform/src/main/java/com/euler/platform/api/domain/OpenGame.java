package com.euler.platform.api.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.euler.common.core.web.domain.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 【游戏管理】对象 open_game
 *
 * @author open
 * @date 2022-02-18
 */
@Data
@TableName("open_game")
public class OpenGame extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键自增
     */
    @TableId(value = "id")
    private Integer id;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 对应版本的ID
     */
    private Integer versionId;
    /**
     * 游戏状态(0 初始状态 1审核中  2审核成功  3审核失败 4归档)
     */
    private Integer gameStatus;
    /**
     * 游戏上架状态(0待上线  1上线  2下线)
     */
    private Integer onlineStatus;
    /**
     * 游戏名称
     */
    private String gameName;
    /**
     * 游戏类目
     */
    private String gameCategory;
    /**
     * 运行平台 (1 安卓  2 ios  3 h5)
     */
    private Integer operationPlatform;
    /**
     * 充值回调
     */
    private String rechargeCallback;
    /**
     * 回调秘钥
     */
    private String callbackSecretKey;
    /**
     * icon图片地址
     */
    private String iconUrl;
    /**
     * 游戏的图片列表
     */
    private String pictureUrl;
    /**
     * 游戏简介
     */
    private String gameIntroduction;
    /**
     * 付费类型(1 有付费 2无付费)
     */
    private Integer payType;
    /**
     * 游戏标签 多个用逗号隔开
     */
    private String gameTags;
    /**
     * 游戏的安装包(运行平台安卓 ios的时候必须存在)
     */
    private String gameInstallPackage;
    /**
     * 游戏的版本号
     */
    private String versionNumberName;

    /**
     * 游戏包名
     */
    private String packageName;
    /**
     * 签名
     */
    private String autograph;
    /**
     * 上架时间
     */
    private Date onTime;
    /**
     * 下架时间
     */
    private Date offTime;
    /**
     * ISBN核发单
     */
    private String isbnIssuanceOrder;
    /**
     * ISBN号
     */
    private String isbnNumber;
    /**
     * 著作权证
     */
    private String copyrightUrl;
    /**
     * 授权链
     */
    private String authorizationChain;
    /**
     * 其他资质
     */
    private String otherQualifications;
    /**
     * 审核时间
     */
    private Date auditTime;
    /**
     * app_id
     */
    private String appId;
    /**
     * app_secret
     */
    private String appSecret;
    /**
     * 删除状态(0 正常  1删除)
     */
    @TableLogic
    private String delFlag;


    /**
     * universal_link
     */
    private String universalLink;

    /**
     * url_scheme
     */
    private String urlScheme;

    /**
     * 版本上线时间
     */
    private Date versionOnlineTime;

}
