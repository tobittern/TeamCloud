package com.euler.risk.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;


/**
 * 封禁的全部关联信息
 *
 * @author euler
 * @date 2022-08-29
 */
@Data
@ApiModel("token关联用户基础信息视图对象")
@ExcelIgnoreUnannotated
public class BanRelationVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 按照用户ID查询到的封号信息
     */
    private BanlistVo userBanInfo;

    /**
     * 按照手机号查询到的封号信息
     */
    private BlacklistVo mobileBanInfo;

    /**
     * 按照身份证查询到的封号信息
     */
    private BlacklistVo idCardNoBanInfo;

    /**
     * 按照ip查询到的封号信息
     */
    private BlacklistVo ipBanInfo;

    /**
     * 按照设备查询到的封号信息
     */
    private BlacklistVo deviceBanInfo;

}
