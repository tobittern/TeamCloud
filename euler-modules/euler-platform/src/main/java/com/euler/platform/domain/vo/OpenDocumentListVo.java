package com.euler.platform.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


/**
 * 文档资源中心视图对象 open_document
 *
 * @author open
 * @date 2022-02-21
 */
@Data
@ApiModel("文档分类视图对象")
public class OpenDocumentListVo {

    private static final long serialVersionUID = 1L;

    /**
     * 资源id
     */
    @ApiModelProperty("资源id")
    private Integer id;

    /**
     * 资源标题
     */
    @ApiModelProperty("资源标题")
    private String title;

    /**
     * 资源类型，1：链接，2：文本
     */
    @ApiModelProperty("资源类型，1：链接，2：文本")
    private String type;


    /**
     * 上级id
     */
    @ApiModelProperty(value = "上级id")
    private Integer parentId = 0;

    /**
     * 资源父路径,逗号分隔
     */
    @ApiModelProperty(value = "资源父路径,逗号分隔")
    private  String path;

    /**
     * 显示顺序
     */
    @ApiModelProperty(value = "显示顺序")
    private Integer orderNum = 0;

    @ApiModelProperty(value = "子菜单")

    private List<OpenDocumentListVo> children;


}
