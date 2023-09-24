package com.euler.community.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 地区对象 region
 *
 * @author euler
 * @date 2022-06-21
 */
@Data
@TableName("region")
public class Region   {

private static final long serialVersionUID=1L;

    /**
     *
     */
     @TableId(value = "id")
    private Integer id;
    /**
     * 名称
     */
    private String name;
    /**
     * 父ID
     */
    private Integer parentId;
    /**
     * 缩写名称
     */
    private String shortName;
    /**
     * 级数
     */
    private String level;
    /**
     * 城市代码
     */
    private String cityCode;
    /**
     * 邮编
     */
    private String zipCode;
    /**
     * 合并名称
     */
    private String mergerName;
    /**
     * 经度
     */
    private String lng;
    /**
     * 纬度
     */
    private String lat;
    /**
     * 拼音缩写
     */
    private String pinyin;

}
