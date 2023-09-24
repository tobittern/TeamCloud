package com.euler.sdk.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel("result")
@ExcelIgnoreUnannotated
public class AuthenticationResultVo {

    private static final long serialVersionUID = 1L;

    /**
     * result
     */
    private AuthenticationResultStatusVo result;

}
