package com.euler.sdk.api.domain;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.euler.common.core.constant.UserConstants;
import com.euler.common.excel.annotation.ExcelDictFormat;
import com.euler.common.excel.convert.ExcelDictConvert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * 钱包视图对象 wallet
 *
 * @author euler
 * @date 2022-03-28
 */
@Data
@ApiModel("钱包视图对象")
@ExcelIgnoreUnannotated
public class WalletVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty("id")
    private Integer id;

    /**
     * 会员id
     */
    @ApiModelProperty("会员id")
    private Long memberId;

    /**
     * 状态（0正常 1停用）
     */
    @ApiModelProperty("状态（0正常 1停用）")
    private String status;

    /**
     * 余额，分
     */
    @ApiModelProperty("余额")
    private BigDecimal balance;

    /**
     * 积分
     */
    @ApiModelProperty("积分")
    private Long score;

    /**
     * 平台币
     */
    @ApiModelProperty("平台币")
    private Long platformCurrency;

    /**
     * 成长值
     */
    @ApiModelProperty("成长值")
    private Long growthValue;


    /**
     * 余额状态
     */
    @ApiModelProperty("余额状态")
    private String balanceStatus= UserConstants.EXCEPTION;

    /**
     * 积分状态
     */
    @ApiModelProperty("积分状态")
    private String scoreStatus= UserConstants.EXCEPTION;

    /**
     * 平台币状态
     */
    @ApiModelProperty("平台币状态")
    private String platformCurrencyStatus= UserConstants.EXCEPTION;

    /**
     * 成长值状态
     */
    @ApiModelProperty("成长值状态")
    private String growthValueStatus= UserConstants.EXCEPTION;

}
