package com.euler.platform.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.euler.common.core.web.domain.BaseEntity;
import lombok.Data;


/**
 * 文档资源中心对象 open_document
 *
 * @author open
 * @date 2022-02-21
 */
@Data
@TableName("open_document")
public class OpenDocument extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 资源id
     */
    @TableId(value = "id")
    private Integer id;
    /**
     * 资源标题
     */
    private String title;
    /**
     * 资源类型，1：链接，2：文本
     */
    private String type;
    /**
     * 上级id
     */
    private Integer parentId;

    /**
     * 资源父路径,逗号分隔
     */
    private  String path;

    /**
     * 显示顺序
     */
    private Integer orderNum;
    /**
     * 资源类型
     */
    private String content;
    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @TableLogic
    private String delFlag;

}
