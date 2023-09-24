package com.euler.community.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * banner组视图对象 banner_group
 *
 * @author euler
 * @date 2022-06-06
 */
@Data
@ApiModel("banner组对应的banner列表")
@ExcelIgnoreUnannotated
public class BannerGroupListVo {

    private static final long serialVersionUID = 1L;

    /**
     * banner组ID
     */
    @ExcelProperty(value = "banner组ID")
    @ApiModelProperty("banner组ID")
    private Long id;

    /**
     * banner组名
     */
    @ExcelProperty(value = "banner组名")
    @ApiModelProperty("banner组名")
    private String groupName;

    /**
     * banner组列表信息
     */
    @ExcelProperty(value = "banner组列表信息")
    @ApiModelProperty("banner组列表信息")
    private List<BannerVo> bannerVoList;

}
