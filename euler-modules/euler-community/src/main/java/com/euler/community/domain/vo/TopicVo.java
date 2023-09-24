package com.euler.community.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.euler.common.excel.annotation.ExcelDictFormat;
import com.euler.common.excel.convert.ExcelDictConvert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 话题视图对象 topic
 *
 * @author euler
 * @date 2022-06-06
 */
@Data
@ApiModel("话题视图对象")
@ExcelIgnoreUnannotated
public class TopicVo {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    @ApiModelProperty("主键")
    private Long id;

    /**
     * 用户id
     */
    @ExcelProperty(value = "用户id")
    @ApiModelProperty("用户id")
    private Long memberId;

    /**
     * 话题名称
     */
    @ExcelProperty(value = "话题名称")
    @ApiModelProperty("话题名称")
    private String topicName;

    /**
     * 话题访问次数
     */
    @ExcelProperty(value = "话题访问次数")
    @ApiModelProperty("话题访问次数")
    private Long searchCount;

    /**
     * 话题曝光次数
     */
    @ExcelProperty(value = "话题曝光次数")
    @ApiModelProperty("话题曝光次数")
    private Long exposureCount;

    /**
     * 状态：0:未发布 1:已发布
     */
    @ExcelProperty(value = "状态：0:未发布 1:已发布")
    @ApiModelProperty("状态：0:未发布 1:已发布")
    private String status;

    /**
     * 是否需要添加：1:是，0：否
     */
    @ExcelProperty(value = "是否需要添加")
    @ApiModelProperty("是否需要添加,1:是，0：否")
    private Integer needAdd = 0;

}
