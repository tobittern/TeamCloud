package com.euler.platform.domain.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("转移所属游戏权限")
public class OpenGameTransferDto {


    /**
     * id
     */
    private Integer id;

    /**
     * transferUserId
     */
    private Long transferUserId;

}
