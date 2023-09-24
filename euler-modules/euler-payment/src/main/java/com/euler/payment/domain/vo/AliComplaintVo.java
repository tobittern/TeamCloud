package com.euler.payment.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * 投诉详情视图对象 complaint_info
 *
 * @author euler
 * @date 2022-09-13
 */
@Data
@ApiModel("投诉详情视图对象")
@ExcelIgnoreUnannotated
public class AliComplaintVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 网关返回码
     */
    private String code;

    /**
     * 网关返回码描
     */
    private String msg;

    /**
     * 业务返回码，参见具体的API接口文档
     */
    private String subCode;

    /**
     * 业务返回码描述，参见具体的API接口文档
     */
    private String subMsg;

}
