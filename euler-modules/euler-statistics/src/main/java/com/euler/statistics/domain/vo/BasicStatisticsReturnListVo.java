package com.euler.statistics.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;


/**
 * 数据统计 - 每日的ltv基础数据统计视图对象 statistics_everyday_ltv_basedata
 *
 * @author euler
 * @date 2022-04-27
 */
@Data
@ApiModel("数据统计 - 每日的ltv基础数据统计视图对象")
@ExcelIgnoreUnannotated
public class BasicStatisticsReturnListVo implements Serializable {

    private static final long serialVersionUID = 1L;


    private List<Long> memberIds;
    private List<String> roleIds;
    private List<String> serverIds;
    private List<String> packageCodes;
    private List<Integer> gameIds;

    private BigDecimal newIncrAmounts;
}
