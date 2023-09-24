package com.euler.community.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.euler.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 动态所有资源对象 resource
 *
 * @author euler
 * @date 2022-06-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("resource")
public class Resource extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 创建人id
     */
    private Long memberId;
    /**
     * 动态表主键id
     */
    private Long dynamicId;
    /**
     * 附件类型，1图片 2视频
     */
    private Integer fileType;
    /**
     * 附件名称
     */
    private String fileName;
    /**
     * 附件路径
     */
    private String filePath;
    /**
     * 审核状态 0 初始状态 1 待审中  2审核成功 3审核拒绝
     */
    private Integer auditStatus;
    /**
     * 审核原因
     */
    private String auditContent;
    /**
     * 是否删除，0未删除，2已删除
     */
    @TableLogic
    private String delFlag;

}
