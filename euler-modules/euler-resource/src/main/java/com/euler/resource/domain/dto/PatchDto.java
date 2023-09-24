package com.euler.resource.domain.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * @auther CodeGenerator
 * @create 2022-03-10 17:21:17
 * @description 补丁表实体类
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName(value = "patch")
public class PatchDto implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 版本描述信息
     */
    @NotBlank(message = "bundledescribe不能为空")
    private String bundledescribe;

    /**
     * 版本名称
     */
    @NotBlank(message = "bundlename不能为空")
    private String bundlename;

    /**
     * 版本Url
     */
    @NotBlank(message = "bundleurl不能为空")
    private String bundleurl;

    /**
     * from_data
     */
    @NotNull(message = "fromDate不能为空")
    private Long fromDate;

    /**
     * 系统版本号
     */
    @NotBlank(message = "fromSystem不能为空")
    private String fromSystem;

    /**
     * pKey
     */
    @JSONField(name = "pKey")

    @NotBlank(message = "pKey不能为空")
    private String pKey;

    /**
     * 包名
     */
    @NotBlank(message = "packagename不能为空")
    private String packagename;

    /**
     * 运行平台
     */
    @NotBlank(message = "platform不能为空")
    private String platform;

    /**
     * 大小
     */
    @NotNull(message = "size不能为空")
    private Long size;

    /**
     * to_data
     */
    @NotNull(message = "toDate不能为空")
    private Long toDate;

    /**
     * version
     */
    @NotBlank(message = "version不能为空")
    private String version;



}
