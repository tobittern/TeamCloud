package com.euler.community.domain.bo;

import com.euler.common.core.validate.AddGroup;
import com.euler.common.core.validate.EditGroup;
import com.euler.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;

/**
 * 发现配置业务对象 discover
 *
 * @author euler
 * @date 2022-06-06
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("发现配置业务对象")
public class DiscoverBo extends BaseEntity {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键", required = true)
    @NotNull(message = "主键不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private Long memberId;

    /**
     * 模块标题
     */
    @ApiModelProperty(value = "模块标题", required = true)
    @NotBlank(message = "模块标题不能为空", groups = { AddGroup.class, EditGroup.class })
    private String title;

    /**
     * 模块介绍
     */
    @ApiModelProperty(value = "模块介绍", required = true)
    @NotBlank(message = "模块介绍不能为空", groups = { AddGroup.class, EditGroup.class })
    private String introduce;

    /**
     * 模块类型, 1纵向编组，2横向编组，3banner编组
     */
    @ApiModelProperty(value = "模块类型, 1纵向编组，2横向编组，3banner编组", required = true)
    @NotBlank(message = "模块类型, 1纵向编组，2横向编组，3bannner编组不能为空", groups = { AddGroup.class, EditGroup.class })
    private String moduleType;

    /**
     * 状态，0待启用，1已启用，2已停用
     */
    @ApiModelProperty(value = "状态，0待启用，1已启用，2已停用")
    private String status;

    /**
     * 应用系统，1android，2ios,3h5
     */
    @ApiModelProperty(value = "应用系统，1android，2ios,3h5", required = true)
    @NotBlank(message = "应用系统，1 android，2 ios,3h5", groups = { AddGroup.class, EditGroup.class })
    private String applicationSystem;

    /**
     * 显示优先级，默认值0，数字越小，显示级别越高
     */
    @ApiModelProperty(value = "显示优先级，默认值0，数字越小，显示级别越高", required = true)
    @NotNull(message = "优先级不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer level;

    /**
     * 具体内容，格式为json格式
     * 类型是1和2的时候，[{"gameId":"101","tag":"热门游戏1"},{"gameId":"102","tag":"热门游戏2"}]
     * 类型是3的时候，[{"bannerGroupId":"201"}]
     */
    @ApiModelProperty(value = "具体内容，格式为json格式")
    @NotBlank(message = "具体内容，格式为json格式", groups = { AddGroup.class, EditGroup.class })
    private String content;

}
