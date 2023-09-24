package com.euler.statistics.domain.vo;

import cn.hutool.http.HttpStatus;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.euler.common.mybatis.core.page.TableDataInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
public class BasicStatisticsReturnDataVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long total;

    private Integer pageSize;

    private Integer pageNums;

    private List<BasicStatisticsReturnVo> rows;

}
