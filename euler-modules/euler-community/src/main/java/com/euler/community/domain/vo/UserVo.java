package com.euler.community.domain.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.euler.common.core.annotation.Sensitive;
import com.euler.common.core.enums.SensitiveStrategy;
import com.euler.common.excel.annotation.ExcelDictFormat;
import com.euler.common.excel.convert.ExcelDictConvert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 上传用户名单VO
 *
 * @author euler
 */
@ApiModel("app用户信息视图")
@Data
@NoArgsConstructor
public class UserVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID")
    private Long userId;
    // 用户数据相关数据
    /**
     * 动态数量
     */
    @ApiModelProperty("动态数量")
    private Integer dynamicCount = 0;
    /**
     * 关注数
     */
    @ApiModelProperty("关注数")
    private Integer attentionCount = 0;
    /**
     * 粉丝数
     */
    @ApiModelProperty("粉丝数")
    private Integer fanCount = 0;
    /**
     * 收藏数
     */
    @ApiModelProperty("收藏数")
    private Integer collectCount = 0;

    /**
     * 用户昵称
     */
    @ApiModelProperty("用户昵称")
    private String nickName;

    /**
     * 头像地址
     */
    @ApiModelProperty("头像地址")
    private String avatar;

    /**
     * 用户性别（0男 1女 2未知）
     */
    @ApiModelProperty("用户性别（0男 1女 2未知）")
    private String sex;
    /**
     * 个人简介
     */
    @ApiModelProperty("个人简介")
    private String description;
    /**
     * 是否是官方账号 0不是  1是
     */
    @ApiModelProperty("是否是官方账号 0不是  1是")
    private Integer isOfficial;

    /**
     * 是否已关注
     */
    @ApiModelProperty("是否已关注 0不是  1是")
    private Integer isAttention = 0;
    /**
     * 省份id
     */
    @ApiModelProperty("省份id")
    private Integer provinceId;

    /**
     * 省份名称
     */
    @ApiModelProperty("省份名称")
    private String province;

    /**
     * 城市id
     */
    @ApiModelProperty("城市id")
    private Integer cityId;

    /**
     * 城市名称
     */
    @ApiModelProperty("城市名称")
    private String city;

    /**
     * 区县id
     */
    @ApiModelProperty("区县id")
    private Integer areaId;

    /**
     * 区县名称
     */
    @ApiModelProperty("区县名称")
    private String area;

    /**
     * 手机号码
     */
    @ApiModelProperty("手机号码")
    private String mobile;

    /**
     * 身份证号
     */
    @ApiModelProperty("身份证号")
    @Sensitive(strategy = SensitiveStrategy.ID_CARD)
    private String idCardNo;

    /**
     * 实名认证状态
     */
    @ApiModelProperty("实名认证状态，0：未认证，1：已认证")
    private String verifyStatus;


}
