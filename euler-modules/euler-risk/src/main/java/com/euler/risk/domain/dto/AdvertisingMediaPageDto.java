package com.euler.risk.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.euler.common.mybatis.core.page.PageQuery;
import java.util.Date;


/**
 * 广告媒体分页业务对象 advertising_media
 *
 * @author euler
 * @date 2022-09-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("广告媒体分页业务对象")
public class AdvertisingMediaPageDto extends PageQuery {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Integer id;

    /**
     * 媒体名称
     */
    @ApiModelProperty(value = "媒体名称")
    private String mediaName;


    /**
     * 广告平台
     */
    @ApiModelProperty(value = "广告平台 ")
    private String advertisingPlatform;

    /**
     * 返点比例
     */
    @ApiModelProperty(value = "返点比例")
    private Integer rebate;

    /**
    * 开始时间
    */
    @ApiModelProperty("开始时间")
    private String beginTime;

    /**
     * 结束时间
     */
    @ApiModelProperty("结束时间")
    private String endTime;

}
