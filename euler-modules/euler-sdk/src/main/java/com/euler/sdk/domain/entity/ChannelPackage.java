package com.euler.sdk.domain.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.euler.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 渠道分组对象 channel_group
 *
 * @author euler
 * @date 2022-04-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("channel_package")
public class ChannelPackage extends BaseEntity {

private static final long serialVersionUID=1L;

    /**
     * 主键
     */
     @TableId(value = "id")
    private Integer id;
    /**
     * 主渠道ID
     */
    private Integer channelId;
    /**
     * 对应游戏ID
     */
    private Integer gameId;
    /**
     * 分包任务id
     */
    private Integer packageTaskId;
    /**
     * 渠道名
     */
    private String newGameName;
    /**
     * 分包的icon
     */
    private String icon;
    /**
     * 分包的前缀名
     */
    private String packagePrefixName;
    /**
     * 分包的名称
     */
    private String packageCode;
    /**
     * 分包的标签
     */
    private String label;
    /**
     * 版本id
     */
    private Integer versionId;
    /**
     * 版本
     */
    private String version;
    /**
     * 分保渠道版本 其实指的得就是次数
     */
    private String edition;
    /**
     * 包地址
     */
    private String packageAddress;

    /**
     * 下载地址
     */
    private String realDownAddress;

    /**
     * 是否进行分包 0需要进行分包 1分包完毕
     */
    private Integer status;

    /**
     * 删除状态 0正常 2删除
     */
     @TableLogic
    private String delFlag;

    /**
     * 打包类型  1APP
     */
    private Integer packageType;

    /**
     * 包名
     */
    private String packageName;

    /**
     * 启动类
     */
    private String startupClass;

}
