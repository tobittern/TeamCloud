package com.euler.community.domain.dto;

import com.euler.common.core.validate.AddGroup;
import com.euler.common.core.validate.EditGroup;
import com.euler.common.mybatis.core.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * 关注业务对象 attention
 *
 * @author euler
 * @date 2022-06-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("关注业务对象")
public class VersionDto extends PageQuery {

    /**
     * 版本号
     */
    @ApiModelProperty(value = "版本号")
    private String versionNo;

    /**
     * 应用系统，'1': android '2': ios
     */
    @ApiModelProperty(value = "应用系统，'1': android '2': ios")
    private String systemType;

    /**
     * 版本状态，'1':待发布 '2':已发布 '3':已下架
     */
    @ApiModelProperty(value = "版本状态，'1':待发布 '2':已发布 '3':已下架", required = true)
    @NotBlank(message = "版本状态，'1':待发布 '2':已发布 '3':已下架不能为空", groups = { AddGroup.class, EditGroup.class })
    private String versionStatus;

}
