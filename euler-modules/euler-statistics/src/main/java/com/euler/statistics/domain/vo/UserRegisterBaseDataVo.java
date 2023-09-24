package com.euler.statistics.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.euler.common.excel.annotation.ExcelDictFormat;
import com.euler.common.excel.convert.ExcelDictConvert;
import io.swagger.annotations.ApiModel;
import lombok.Data;


/**
 *
 * @author euler
 * @date 2022-04-27
 */
@Data
@ApiModel("注册统计")
@ExcelIgnoreUnannotated
public class UserRegisterBaseDataVo {

    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "渠道号+游戏ID")
    private String selectKey;

    @ExcelProperty(value = "日期")
    private String dateLabel;

    @ExcelProperty(value = "渠道ID")
    private Integer channelId;

    @ExcelProperty(value = "渠道名")
    private String channelName;

    @ExcelProperty(value = "渠道号")
    private String packageCode;

    @ExcelProperty(value = "游戏ID")
    private Integer gameId;

    @ExcelProperty(value = "游戏名")
    private String gameName;

    @ExcelProperty(value = "游戏运行平台", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "game_operation_platform")
    private String operationPlatform;

    @ExcelProperty(value = "新增用户量")
    private Integer incrNums;

    @ExcelProperty(value = "新增角色上报用户量")
    private Integer incrReportNums;

}
