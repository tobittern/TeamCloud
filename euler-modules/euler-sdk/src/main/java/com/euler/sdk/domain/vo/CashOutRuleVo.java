package com.euler.sdk.domain.vo;


import java.math.BigDecimal;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.euler.common.excel.annotation.ExcelDictFormat;
import com.euler.common.excel.convert.ExcelDictConvert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 提现规则视图对象 cash_out_rule
 *
 * @author euler
 * @date 2022-05-26
 */
@Data
@ApiModel("提现规则视图对象")
@ExcelIgnoreUnannotated
public class CashOutRuleVo {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty("id")
    private Integer id;

    /**
     * 提现规则名称
     */
    @ApiModelProperty("提现规则名称")
    private String ruleName;

    /**
     * 提现规则类型，1-积分，2-余额，3-平台币
     */
    @ApiModelProperty("提现规则类型，1-积分，2-余额，3-平台币")
    private Integer ruleType;

    /**
     * 每天提现限制次数
     */
    @ApiModelProperty("每天提现限制次数")
    private Integer dayNum;

    /**
     * 规则状态
     */
    @ApiModelProperty("规则状态")
    private Integer ruleStatus;

    /**
     * 提现限制金额
     */
    @ApiModelProperty("提现限制金额")
    private BigDecimal cashOutMoney;


}
