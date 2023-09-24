package com.euler.community.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 动态所有资源视图对象 resource
 *
 * @author euler
 * @date 2022-06-09
 */
@Data
@ApiModel("动态所有资源视图对象")
@ExcelIgnoreUnannotated
public class ResourceVo {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    @ApiModelProperty("主键")
    private Long id;

    /**
     * 创建人id
     */
    @ExcelProperty(value = "创建人id")
    @ApiModelProperty("创建人id")
    private Long memberId;

    /**
     * 动态表主键id
     */
    @ExcelProperty(value = "动态表主键id")
    @ApiModelProperty("动态表主键id")
    private Long dynamicId;

    /**
     * 附件类型，1图片 2视频
     */
    @ExcelProperty(value = "附件类型，1图片 2视频")
    @ApiModelProperty("附件类型，1图片 2视频")
    private Integer fileType;

    /**
     * 附件名称
     */
    @ExcelProperty(value = "附件名称")
    @ApiModelProperty("附件名称")
    private String fileName;

    /**
     * 附件路径
     */
    @ExcelProperty(value = "附件路径")
    @ApiModelProperty("附件路径")
    private String filePath;

    /**
     * 审核状态 0 初始状态 1 待审中  2审核成功 3审核拒绝
     */
    @ExcelProperty(value = "审核状态 0 初始状态 1 待审中  2审核成功 3审核拒绝")
    @ApiModelProperty("审核状态 0 初始状态 1 待审中  2审核成功 3审核拒绝")
    private Integer auditStatus;

    /**
     * 审核原因
     */
    @ExcelProperty(value = "审核原因")
    @ApiModelProperty("审核原因")
    private String auditContent;


}
