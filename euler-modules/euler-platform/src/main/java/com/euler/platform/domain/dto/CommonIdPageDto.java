package com.euler.platform.domain.dto;

import com.euler.common.mybatis.core.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("只传输一个ID的分页获取方式")
public class CommonIdPageDto extends PageQuery {


    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * useId
     */
    @ApiModelProperty(value = "useId")
    private Integer useId;

}
