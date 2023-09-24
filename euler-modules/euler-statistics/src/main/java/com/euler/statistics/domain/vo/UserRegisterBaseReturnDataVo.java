package com.euler.statistics.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;


/**
 *
 * @author euler
 * @date 2022-04-27
 */
@Data
@ApiModel("注册统计")
@ExcelIgnoreUnannotated
public class UserRegisterBaseReturnDataVo {

    private static final long serialVersionUID = 1L;

    private Long total;

    private Integer pageSize;

    private Integer pageNums;

    private List<UserRegisterBaseDataVo> rows;

}
