package com.euler.risk.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.euler.common.excel.annotation.ExcelDictFormat;
import com.euler.common.excel.convert.ExcelDictConvert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;


/**
 * ip汇总视图对象 tf_ip_summary
 *
 * @author euler
 * @date 2022-08-24
 */
@Data
@ApiModel("ip汇总视图对象")
@ExcelIgnoreUnannotated
public class TfIpSummaryVo  implements Serializable  {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ExcelProperty(value = "id")
    @ApiModelProperty("id")
    private Long id;

    /**
     * 汇总日期
     */
    @ExcelProperty(value = "汇总日期")
    @ApiModelProperty("汇总日期")
    private Date dateId;

    /**
     * ip地址
     */
    @ExcelProperty(value = "ip地址")
    @ApiModelProperty("ip地址")
    private String ip;

    /**
     * ip区域地址
     */
    @ExcelProperty(value = "ip区域地址")
    @ApiModelProperty("ip区域地址")
    private String ipAddress;

    /**
     * 注册次数
     */
    @ExcelProperty(value = "注册次数")
    @ApiModelProperty("注册次数")
    private Integer registerNum;

    /**
     * 登录次数
     */
    @ExcelProperty(value = "登录次数")
    @ApiModelProperty("登录次数")
    private Integer loginNum;

    /**
     * 角色创建次数
     */
    @ExcelProperty(value = "角色创建次数")
    @ApiModelProperty("角色创建次数")
    private Integer roleCreateNum;


}
