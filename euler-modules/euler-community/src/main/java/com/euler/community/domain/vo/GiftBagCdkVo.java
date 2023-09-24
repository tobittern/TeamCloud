package com.euler.community.domain.vo;

import java.util.Date;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.euler.common.excel.annotation.ExcelDictFormat;
import com.euler.common.excel.convert.ExcelDictConvert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 礼包SDk数据视图对象 gift_bag_cdk
 *
 * @author euler
 * @date 2022-06-07
 */
@Data
@ApiModel("礼包SDk数据视图对象")
@ExcelIgnoreUnannotated
public class GiftBagCdkVo {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @ExcelProperty(value = "主键id")
    @ApiModelProperty("主键id")
    private Long id;

    /**
     * 礼包表id
     */
    @ExcelProperty(value = "礼包表id")
    @ApiModelProperty("礼包表id")
    private Long giftBagId;

    /**
     * 领取礼包的用户id
     */
    @ExcelProperty(value = "领取礼包的用户id")
    @ApiModelProperty("领取礼包的用户id")
    private Long memberId;

    /**
     * 礼包码
     */
    @ExcelProperty(value = "礼包码")
    @ApiModelProperty("礼包码")
    private String code;

    /**
     * 礼包状态，0：未使用，1：已使用
     */
    @ExcelProperty(value = "礼包状态，0：未使用，1：已使用")
    @ApiModelProperty("礼包状态，0：未使用，1：已使用")
    private Integer status;

    /**
     * 领取时间
     */
    @ExcelProperty(value = "领取时间")
    @ApiModelProperty("领取时间")
    private Date receiveTime;

    /**
     * 兑换时间
     */
    @ExcelProperty(value = "兑换时间")
    @ApiModelProperty("兑换时间")
    private Date exchangeTime;


}
