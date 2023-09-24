package com.euler.platform.domain.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.euler.common.core.annotation.Sensitive;
import com.euler.common.core.enums.SensitiveStrategy;
import com.euler.common.excel.annotation.ExcelDictFormat;
import com.euler.common.excel.convert.ExcelDictConvert;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户对象导出VO
 *
 * @author euler
 */

@Data
@NoArgsConstructor
public class UserVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @ExcelProperty(value = "用户序号")
    private Long userId;

    /**
     * 用户账号
     */
    @ExcelProperty(value = "登录名称")
    private String userName;

    /**
     * 用户昵称
     */
    @ExcelProperty(value = "用户名称")
    private String nickName;

    /**
     * 用户真实姓名
     */
    @ExcelProperty(value = "用户真实姓名")
    private String realName;

    /**
     * 用户邮箱
     */
    @ExcelProperty(value = "用户邮箱")
    private String email;

    /**
     * 手机号码
     */
    @ExcelProperty(value = "手机号码")
    @Sensitive(strategy = SensitiveStrategy.PHONE)
    private String phonenumber;

    /**
     * 用户性别
     */
    @ExcelProperty(value = "用户性别", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "sys_user_sex")
    private String sex;

    /**
     * 帐号状态（0正常 1停用）
     */
    @ExcelProperty(value = "帐号状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "sys_common_status")
    private String status;

    /**
     * 公司名称
     */
    @ExcelProperty(value = "公司名称")
    private String companyName;


    /**
     * 认证类型
     */
    @ExcelProperty(value = "认证类型")
    private String authType;

    /**
     * 审核状态
     */
    @ExcelProperty(value = "审核状态(0初始状态 1待审核 2审核通过 3审核拒绝)")
    private Integer auditStatus = 0;

    /**
     * 审核时间
     */
    @ExcelProperty(value = "审核时间")
    private Date auditTime;

    /**
     * 审核原因
     */
    @ExcelProperty(value = "审核原因")
    private String auditRecord;

    /**
     * 最后登录时间
     */
    @ExcelProperty(value = "最后登录时间")
    private Date loginDate;

    /**
     * 提交时间
     */
    @ExcelProperty(value = "提交时间")
    private Date createTime;

}
