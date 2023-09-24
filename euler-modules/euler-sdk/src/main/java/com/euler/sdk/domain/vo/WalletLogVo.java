package com.euler.sdk.domain.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 钱包变动记录视图对象 wallet_log
 *
 * @author euler
 * @date 2022-04-15
 */
@Data
@ApiModel("钱包变动记录视图对象")
@ExcelIgnoreUnannotated
public class WalletLogVo {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ExcelProperty(value = "id")
    @ApiModelProperty("id")
    private Long id;

    /**
     * 会员id
     */
    @ExcelProperty(value = "会员id")
    @ApiModelProperty("会员id")
    private Long memberId;

    /**
     * 钱包id
     */
    @ExcelProperty(value = "钱包id")
    @ApiModelProperty("钱包id")
    private Integer walletId;

    /**
     * 钱包值变动类型，1-积分，2-余额，3-平台币，4-成长值
     */
    @ExcelProperty(value = "钱包值变动类型，1-积分，2-余额，3-平台币，4-成长值")
    @ApiModelProperty("钱包值变动类型，1-积分，2-余额，3-平台币，4-成长值")
    private Integer changeType;

    /**
     * 1：增加，2：减少
     */
    @ExcelProperty(value = "1：增加，2：减少")
    @ApiModelProperty("1：增加，2：减少")
    private Integer isAdd;

    /**
     * 描述
     */
    @ExcelProperty(value = "描述")
    @ApiModelProperty("描述")
    private String description;

    /**
     * 变动数额
     */
    @ExcelProperty(value = "变动数额")
    @ApiModelProperty("变动数额")
    private BigDecimal changeValue;

    /**
     * 钱包操作类型，1：正常增减，2：提现
     */
    @ApiModelProperty("钱包操作类型，1：正常增减，2：提现")
    private Integer walletOpType;

    /**
     * 钱包类型，1：正常钱包，2：虚拟钱包
     */
    @ApiModelProperty("钱包类型，1：正常钱包，2：虚拟钱包")
    private Integer walletType;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

}
