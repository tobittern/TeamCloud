package com.euler.sdk.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.euler.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * sdk版本管理对象 version
 *
 * @author euler
 * @date 2022-07-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("version")
public class Version extends BaseEntity {

private static final long serialVersionUID=1L;

    /**
     * 主键
     */
     @TableId(value = "id")
    private Long id;
    /**
     * 版本号
     */
    private String number;
    /**
     * 运行平台 (1 安卓  2 ios  3 h5)
     */
    private Integer platform;
    /**
     * 版本类型，0稳定版，1最新版
     */
    private Integer type;
    /**
     * 是否是新版本，0是新版本，1历史版本
     */
    private Integer isNew;
    /**
     * 更新内容
     */
    private String content;
    /**
     * 文件路径
     */
    private String fileUrl;
    /**
     * 删除状态 0正常 2删除
     */
     @TableLogic
    private String delFlag;

}
