package com.euler.community.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.euler.common.excel.annotation.ExcelDictFormat;
import com.euler.common.excel.convert.ExcelDictConvert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


/**
 * 地区视图对象 region
 *
 * @author euler
 * @date 2022-06-21
 */
@Data
@ApiModel("地区视图对象")
@ExcelIgnoreUnannotated
public class RegionVo {

    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @ExcelProperty(value = "")
    @ApiModelProperty("")
    private Integer id;

    /**
     * 名称
     */
    @ExcelProperty(value = "名称")
    @ApiModelProperty("名称")
    private String name;

    /**
     * 父ID
     */
    @ExcelProperty(value = "父ID")
    @ApiModelProperty("父ID")
    private Integer parentId;

    /**
     * 缩写名称
     */
    @ExcelProperty(value = "缩写名称")
    @ApiModelProperty("缩写名称")
    private String shortName;

    /**
     * 级数
     */
    @ExcelProperty(value = "级数")
    @ApiModelProperty("级数")
    private String level;

    /**
     * 城市代码
     */
    @ExcelProperty(value = "城市代码")
    @ApiModelProperty("城市代码")
    private String cityCode;

    /**
     * 邮编
     */
    @ExcelProperty(value = "邮编")
    @ApiModelProperty("邮编")
    private String zipCode;

    /**
     * 合并名称
     */
    @ExcelProperty(value = "合并名称")
    @ApiModelProperty("合并名称")
    private String mergerName;

    /**
     * 经度
     */
    @ExcelProperty(value = "经度")
    @ApiModelProperty("经度")
    private String lng;

    /**
     * 纬度
     */
    @ExcelProperty(value = "纬度")
    @ApiModelProperty("纬度")
    private String lat;

    /**
     * 拼音缩写
     */
    @ExcelProperty(value = "拼音缩写")
    @ApiModelProperty("拼音缩写")
    private String pinyin;


    private List<RegionVo> children;


}
