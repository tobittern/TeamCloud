package com.euler.sdk.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import io.swagger.annotations.ApiModel;
import lombok.Data;


@Data
@ApiModel("status")
@ExcelIgnoreUnannotated
public class AuthenticationResultStatusVo {

    private static final long serialVersionUID = 1L;

    /**
     * 状态
     */
    private Integer status;

    private String pi;


}
