package com.euler.sdk.domain.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


/**
 * 弹窗关联游戏业务对象 popup_game_relation
 *
 * @author euler
 * @date 2022-09-05
 */

@Data
@ApiModel("弹窗关联游戏业务对象")
public class PopupGameRelationBo {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键", required = true)
    private Integer id;

    /**
     * 弹框ID
     */
    @ApiModelProperty(value = "弹框ID", required = true)
    private Integer popupId;

    /**
     * 游戏ID
     */
    @ApiModelProperty(value = "游戏ID", required = true)
    private String gameIds;


    /**
     * 弹框ID
     */
    @ApiModelProperty(value = "弹框ID", required = true)
    private List<Integer> popupIds;


}
