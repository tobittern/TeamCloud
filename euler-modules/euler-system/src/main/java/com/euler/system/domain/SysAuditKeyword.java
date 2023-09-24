package com.euler.system.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.euler.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 审核关键词 - 敏感词对象 audit_keyword
 *
 * @author euler
 * @date 2022-03-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_audit_keyword")
public class SysAuditKeyword extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Integer id;
    /**
     * 添加用户id
     */
    private Long userId;
    /**
     * 类型
     */
    private Integer type;
    /**
     * 敏感词
     */
    private String keywords;
    /**
     * 是否有效状态 0正常 1删除
     */
    private Integer  isItValid;

}
