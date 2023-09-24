package com.euler.community.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.euler.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 版本管理对象 version
 *
 * @author euler
 * @date 2022-06-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("version")
public class Version extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 应用系统，'1': android  '2': ios
     */
    private String systemType;

    /**
     * 版本号
     */
    private String versionNo;

    /**
     * ios的时候，下载地址
     */
    private String downloadUrl;

    /**
     * 附件名称
     */
    private String fileName;

    /**
     * 附件路径
     */
    private String filePath;

    /**
     * 上传时间
     */
    private Date uploadTime;

    /**
     * 发布时间
     */
    private Date publishTime;

    /**
     * 更新方式，'1':推荐更新 '2':强制更新
     */
    private String updateType;

    /**
     * 版本状态，'1':待发布 '2':已发布 '3':已下架
     */
    private String versionStatus;

    /**
     * 是否删除，0未删除，2已删除
     */
    @TableLogic
    private String delFlag;
    /**
     * 发现页开关：1：开，0：关
     */
    private String discoverSwitch;

}
