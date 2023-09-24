package com.euler.system.domain;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.euler.common.core.web.domain.BaseEntity;
import com.euler.common.excel.annotation.ExcelDictFormat;
import com.euler.common.excel.convert.ExcelDictConvert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 参数配置表 sys_config
 *
 * @author euler
 */

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@TableName("sys_config")
@ExcelIgnoreUnannotated
@ApiModel("参数配置业务对象")
public class SysConfig extends BaseEntity {

    /**
     * 参数主键
     */
    @ApiModelProperty(value = "参数主键")
    @TableId(value = "config_id")
    private Long configId;

    /**
     * 参数名称
     */
    @ApiModelProperty(value = "参数名称")
    @NotBlank(message = "参数名称不能为空")
    @Size(min = 0, max = 100, message = "参数名称不能超过100个字符")
    private String configName;

    /**
     * 参数键名
     */
    @ApiModelProperty(value = "参数键名")
    @NotBlank(message = "参数键名长度不能为空")
    @Size(min = 0, max = 100, message = "参数键名长度不能超过100个字符")
    private String configKey;

    /**
     * 参数键值
     */
    @ApiModelProperty(value = "参数键值")
    @NotBlank(message = "参数键值不能为空")
    @Size(min = 0, max = 8000, message = "参数键值长度不能超过8000个字符")
    private String configValue;

    /**
     * 系统内置（Y是 N否）
     */
    @ApiModelProperty(value = "系统内置（Y是 N否）")
    private String configType;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;


    /**
     * 平台 ， -1：全局，1：sdk，2：开放平台，3：管理后台 4：九区玩家app
     */
    @ApiModelProperty(value = "平台 ， -1：全局，1：sdk，2：开放平台，3：管理后台 4：九区玩家app")
    private Integer operationPlatform;

    /**
     * 状态（0正常 1停用）
     */
    @ApiModelProperty(value = "状态（0正常 1停用）")
    private String status="0";


    @ApiModelProperty(value = "游戏Id")
    private Integer gameId;

    @ApiModelProperty(value = "设备，-1：不限设备，0：PC，1：安卓，2：ios，3：h5，4：小程序")
    private Integer device;



}
