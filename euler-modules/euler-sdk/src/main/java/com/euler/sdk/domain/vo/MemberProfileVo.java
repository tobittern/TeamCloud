package com.euler.sdk.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.euler.common.excel.annotation.ExcelDictFormat;
import com.euler.common.excel.convert.ExcelDictConvert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


/**
 * 会员详细信息对象 member_profile
 *
 * @author euler
 * @date 2022-04-06
 */
@Data
@ApiModel("会员详细信息对象")
@ExcelIgnoreUnannotated
public class MemberProfileVo {

    private static final long serialVersionUID = 1L;

    /**
     * 会员详情id
     */
    @ExcelProperty(value = "会员详情id")
    @ApiModelProperty("会员详情id")
    private Long id;

    /**
     * 会员id
     */
    @ExcelProperty(value = "会员id")
    @ApiModelProperty("会员id")
    private Long memberId;

    /**
     * 用户昵称
     */
    @ExcelProperty(value = "用户昵称")
    @ApiModelProperty("用户昵称")
    private String nickName;

    /**
     * 真实姓名
     */
    @ExcelProperty(value = "真实姓名")
    @ApiModelProperty("真实姓名")
    private String realName;

    /**
     * 身份证号
     */
    @ExcelProperty(value = "身份证号")
    @ApiModelProperty("身份证号")
    private String idCardNo;

    /**
     * 实名认证状态，0：未认证，1：已认证
     */
    @ExcelProperty(value = "实名认证状态，0：未认证，1：已认证")
    @ApiModelProperty("实名认证状态，0：未认证，1：已认证")
    private String verifyStatus;

    /**
     * 是否可以玩 0 不可以 1可以玩
     */
    @ExcelProperty(value = "是否可以玩 0 不可以 1可以玩")
    @ApiModelProperty("是否可以玩 0 不可以 1可以玩")
    private String isPlay;

    /**
     * 头像地址
     */
    @ExcelProperty(value = "头像地址")
    @ApiModelProperty("头像地址")
    private String avatar;

    /**
     * 用户性别（0男 1女 2未知）
     */
    @ExcelProperty(value = "用户性别", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "1=男,0=女,2=未知")
    @ApiModelProperty("用户性别（1男 0女 2未知）")
    private String sex;

    /**
     * 用户邮箱
     */
    @ExcelProperty(value = "用户邮箱")
    @ApiModelProperty("用户邮箱")
    private String email;

    /**
     * 手机号码
     */
    @ExcelProperty(value = "手机号码")
    @ApiModelProperty("手机号码")
    private String mobile;

    /**
     * 生日
     */
    @ExcelProperty(value = "生日")
    @ApiModelProperty("生日")
    private Date birthday;

    /**
     * 个人简介
     */
    @ExcelProperty(value = "个人简介")
    @ApiModelProperty("个人简介")
    private String description;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    @ApiModelProperty("备注")
    private String remark;

    /**
     * 注册ip地址
     */
    @ExcelProperty(value = "注册ip地址")
    @ApiModelProperty("注册ip地址")
    private String registerIp;

    /**
     * 最后登录IP
     */
    @ExcelProperty(value = "最后登录IP")
    @ApiModelProperty("最后登录IP")
    private String loginIp;


    /**
     * 最后登录设备Id
     */
    @ExcelProperty(value = "最后登录设备Id")
    @ApiModelProperty("最后登录设备Id")
    private String loginDeviceId;

    /**
     * 最后登录时间
     */
    @ExcelProperty(value = "最后登录时间")
    @ApiModelProperty("最后登录时间")
    private Date loginDate;

    /**
     * 签到时间
     */
    @ExcelProperty(value = "签到时间")
    @ApiModelProperty("签到时间")
    private Date signDate;

    /**
     * 来源渠道id
     */
    @ExcelProperty(value = "来源渠道id")
    @ApiModelProperty("来源渠道id")
    private Integer channelId;

    /**
     * 来源渠道名称
     */
    @ExcelProperty(value = "来源渠道名称")
    @ApiModelProperty("来源渠道名称")
    private String channelName;

    /**
     * 渠道号，分包号
     */
    @ExcelProperty(value = "渠道号，分包号")
    @ApiModelProperty("渠道号，分包号")
    private String packageCode;

    /**
     * 来源游戏id
     */
    @ExcelProperty(value = "来源游戏id")
    @ApiModelProperty("来源游戏id")
    private Integer gameId;

    /**
     * 来源游戏名称
     */
    @ExcelProperty(value = "来源游戏名称")
    @ApiModelProperty("来源游戏名称")
    private String gameName;

    /**
     * 是否是官方账号 0不是  1是
     */
    @ExcelProperty(value = "是否是官方账号 0不是  1是")
    @ApiModelProperty("是否是官方账号 0不是  1是")
    private Integer isOfficial;

    /**
     * 省份id
     */
    @ExcelProperty(value = "省份id")
    @ApiModelProperty("省份id")
    private Integer provinceId;

    /**
     * 省份名称
     */
    @ExcelProperty(value = "省份名称")
    @ApiModelProperty("省份名称")
    private String province;

    /**
     * 城市id
     */
    @ExcelProperty(value = "城市id")
    @ApiModelProperty("城市id")
    private Integer cityId;

    /**
     * 城市名称
     */
    @ExcelProperty(value = "城市名称")
    @ApiModelProperty("城市名称")
    private String city;

    /**
     * 区县id
     */
    @ExcelProperty(value = "区县id")
    @ApiModelProperty("区县id")
    private Integer areaId;

    /**
     * 区县名称
     */
    @ExcelProperty(value = "区县名称")
    @ApiModelProperty("区县名称")
    private String area;
}
