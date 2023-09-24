package com.euler.platform.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.euler.common.core.web.domain.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 用户的资质认证记录对象 open_user_auth
 *
 * @author open
 * @date 2022-02-23
 */
@Data
@TableName("open_user_certification")
public class OpenUserCertification extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 主键自增
     */
    @TableId(value = "id")
    private Integer id;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 审核状态(0初始状态 1待审核 2审核通过 3审核拒绝)
     */
    private Integer auditStatus;
    /**
     * 功能名称
     */
    private String companyName;
    /**
     * 营业执照
     */
    private String businessLicense;
    /**
     * 公司法人
     */
    private String legalPerson;
    /**
     * 税号
     */
    private String dutyParagraph;
    /**
     * 注册地址
     */
    private String registeredAddress;
    /**
     * 经营期限 - 开始时间
     */
    private Date operatingPeriodStart;
    /**
     * 经营期限 - 结束时间
     */
    private Date operatingPeriodEnd;
    /**
     * 是否长期有效
     */
    private Integer isLongEffective;
    /**
     * 经营范围
     */
    private String natureOfBusiness;
    /**
     * 联系人姓名
     */
    private String contactName;
    /**
     * 联系人身份证
     */
    private String contactIdCard;
    /**
     * 联系人电话
     */
    private String contactPhone;
    /**
     * 审核时间
     */
    private Date auditTime;
    /**
     * 认证主题类型
     */
    private String authType;
    /**
     * 删除状态 （0正常  1删除）
     */
    @TableLogic
    private String delFlag;

}
