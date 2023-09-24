package com.euler.platform.domain.bo;

import com.euler.common.core.validate.AddGroup;
import com.euler.common.core.validate.EditGroup;
import com.euler.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
 * 文档业务对象 open_document
 *
 * @author open
 * @date 2022-02-21
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("文档业务对象")
public class OpenDocumentBo extends BaseEntity {

    /**
     * id
     */
    @ApiModelProperty(value = "id", required = true)
    @NotNull(message = "id不能为空", groups = { EditGroup.class })
    private Integer id;

    /**
     * 文档标题
     */
    @ApiModelProperty(value = "文档标题", required = true)
    @NotBlank(message = "文档标题不能为空", groups = { AddGroup.class, EditGroup.class })
    private String title;

    /**
     * 资源类型，1：链接，2：文本
     */
    @ApiModelProperty(value = "文档类型，1：链接，2：文本", required = true)
    @NotBlank(message = "文档类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private String type;



    /**
     * 上级id
     */
    @ApiModelProperty(value = "上级id")
    private Integer parentId=0;


    /**
     * 资源父路径,逗号分隔
     */
    @ApiModelProperty(value = "资源父路径,逗号分隔")
    private  String path;

    /**
     * 显示顺序
     */
    @ApiModelProperty(value = "显示顺序")
    private Integer orderNum=0;


    /**
     * 文档类型
     */
    @ApiModelProperty(value = "文档内容")
    private String content;


}
