package com.euler.resource.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;


/**
 * @auther CodeGenerator
 * @create 2022-03-10 17:21:17
 * @description 补丁表实体类
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName(value = "patch")
public class Patch implements Serializable {

    private static final long serialVersionUID = 1L;

    @JSONField(serialize = false)
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 版本描述信息
     */
    @TableField("bundle_describe")
    private String bundleDescribe;

    /**
     * 版本名称
     */
    @TableField("bundle_name")
    private String bundleName;

    /**
     * 版本Url
     */
    @TableField("bundle_url")
    private String bundleUrl;

    /**
     * from_data
     */
    @TableField("from_date")
    private Long fromDate;

    /**
     * 系统版本号
     */
    @TableField("from_system")
    private String fromSystem;

    /**
     * pKey
     */
    @TableField("p_key")
    @JSONField(name = "pKey")
    private String pKey;

    @JsonProperty("pKey")
    public String getpKey() {
        return pKey;
    }

    /**
     * 包名
     */
    @TableField("package_name")
    private String packageName;

    /**
     * 运行平台
     */
    @TableField("platform")
    private String platform;

    /**
     * 大小
     */
    @TableField("size")
    private Long size;

    /**
     * to_data
     */
    @TableField("to_date")
    private Long toDate;

    /**
     * version
     */
    @TableField("version")
    private String version;

    /**
     * 补丁文件地址
     */
    @TableField("patch_file")
    private String patchFile;

    /**
     * 创建时间
     */
    @JSONField(serialize = false)

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 状态
     */
    @JSONField(serialize = false)
    @TableField(fill = FieldFill.INSERT)
    @TableLogic
    private Integer isDelete;


}
