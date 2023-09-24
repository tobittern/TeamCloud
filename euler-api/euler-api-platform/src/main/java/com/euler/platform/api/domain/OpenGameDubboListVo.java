package com.euler.platform.api.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


/**
 * dubbo调用时候的实体
 *
 * @author open
 * @date 2022-02-18
 */
@Data
@ApiModel("dubbo调用时候的实体")
public class OpenGameDubboListVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键自增
     */
    @ExcelProperty(value = "主键自增")
    @ApiModelProperty("主键自增")
    private long id;

    /**
     * 用户id
     */
    @ExcelProperty(value = "用户id")
    @ApiModelProperty("用户id")
    private Long userId;

    /**
     * 游戏状态(0 初始状态 1审核中  2审核成功  3审核失败 4归档)
     */
    @ExcelProperty(value = "游戏状态(0 初始状态 1审核中  2审核成功  3审核失败 4归档)")
    @ApiModelProperty("游戏状态(0 初始状态 1审核中  2审核成功  3审核失败 4归档)")
    private Integer gameStatus;

    /**
     * 游戏上架状态(0待上线  1上线  2下线)
     */
    @ExcelProperty(value = "游戏上架状态(0待上线  1上线  2下线)")
    @ApiModelProperty(value = "游戏上架状态(0待上线  1上线  2下线)")
    private Integer onlineStatus;

    /**
     * 游戏名称
     */
    @ExcelProperty(value = "游戏名称")
    @ApiModelProperty("游戏名称")
    private String gameName;

    /**
     * 游戏类目
     */
    @ExcelProperty(value = "游戏类目")
    @ApiModelProperty("游戏类目")
    private String gameCategory;

    /**
     * 游戏类目名称
     */
    @ExcelProperty(value = "游戏类目名称")
    @ApiModelProperty("游戏类目名称")
    private String gameCategoryName;

    /**
     * 运行平台 (1 安卓  2 ios  3 h5)
     */
    @ExcelProperty(value = "运行平台 (1 安卓  2 ios  3 h5)")
    @ApiModelProperty("运行平台 (1 安卓  2 ios  3 h5)")
    private Integer operationPlatform;

    /**
     * icon图片地址
     */
    @ExcelProperty(value = "icon图片地址")
    @ApiModelProperty("icon图片地址")
    private String iconUrl;

    /**
     * 游戏的当前版本
     */
    @ExcelProperty(value = "游戏的当前版本")
    @ApiModelProperty("游戏的当前版本")
    private String versionNumberName;

    /**
     * 回调地址
     */
    @ExcelProperty(value = "回调地址")
    @ApiModelProperty("回调地址")
    private String rechargeCallback;

    /**
     * 回调秘钥
     */
    @ExcelProperty(value = "回调秘钥")
    @ApiModelProperty("回调秘钥")
    private String callbackSecretKey;

    /**
     * 扩展
     */
    @ExcelProperty(value = "扩展")
    @ApiModelProperty("扩展")
    private String extend;

    /**
     * 信息的关联字段
     */
    private List<Integer> relationList;

}
