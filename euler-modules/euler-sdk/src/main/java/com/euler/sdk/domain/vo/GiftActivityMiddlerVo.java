package com.euler.sdk.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.euler.common.excel.annotation.ExcelDictFormat;
import com.euler.common.excel.convert.ExcelDictConvert;
import com.euler.sdk.api.domain.MyGameVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;


/**
 * 礼包活动管理视图对象 gift_activity
 *
 * @author euler
 * @date 2022-03-29
 */
@Data
@ApiModel("活动管理视图对象")
@ExcelIgnoreUnannotated
public class GiftActivityMiddlerVo {


    /**
     * 主键
     */
    private MyGameVo myGameVo;

    /**
     * 用户ID
     */
    private ArrayList<Integer> ids;


}
