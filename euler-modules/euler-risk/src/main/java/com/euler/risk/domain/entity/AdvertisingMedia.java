package com.euler.risk.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.euler.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 广告媒体对象 advertising_media
 *
 * @author euler
 * @date 2022-09-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("advertising_media")
public class AdvertisingMedia extends BaseEntity {

private static final long serialVersionUID=1L;

    /**
     * id
     */
     @TableId(value = "id")
    private Integer id;
    /**
     * 媒体名称
     */
    private String mediaName;

    /**
     * 广告平台
     */
    private String advertisingPlatform;
    /**
     * 返点比例
     */
    private Integer rebate;
    /**
     * 是否删除，0未删除，2已删除
     */
     @TableLogic
    private String delFlag;

}
