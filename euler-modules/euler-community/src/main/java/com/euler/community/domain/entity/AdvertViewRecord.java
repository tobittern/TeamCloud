package com.euler.community.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.euler.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 对象 advert_view_record
 *
 * @author euler
 * @date 2022-06-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("advert_view_record")
public class AdvertViewRecord extends BaseEntity {

private static final long serialVersionUID=1L;

    /**
     * 主键
     */
     @TableId(value = "id")
    private Long id;
    /**
     * 广告表主键id
     */
    private Long advertId;
    /**
     * 查看广告的用户id
     */
    private Long memberId;
    /**
     * 查看广告的次数
     */
    private Integer viewNum;
    /**
     * 是否删除，0未删除，2已删除
     */
     @TableLogic
    private String delFlag;

}
