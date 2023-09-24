package com.euler.community.domain.dto;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.euler.common.mybatis.core.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 视图对象 advert_view_record
 *
 * @author euler
 * @date 2022-06-17
 */
@Data
@ApiModel("视图对象")
@ExcelIgnoreUnannotated
public class AdvertViewRecordDto extends PageQuery {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty("主键")
    private Long id;

    /**
     * 广告表主键id
     */
    @ApiModelProperty("广告表主键id")
    private Long advertId;

    /**
     * 查看广告的用户id
     */
    @ApiModelProperty("查看广告的用户id")
    private Long memberId;

    /**
     * 查看广告的次数
     */
    @ApiModelProperty("查看广告的次数")
    private Integer viewNum;

    /**
     * 主键集合id
     */
    private Long[] ids;


}
