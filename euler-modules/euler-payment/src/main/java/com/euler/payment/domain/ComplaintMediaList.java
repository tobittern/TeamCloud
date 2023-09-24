package com.euler.payment.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.euler.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 投诉资料列对象 complaint_media_list
 *
 * @author euler
 * @date 2022-09-13
 */
@Data
@TableName("complaint_media_list")
public class ComplaintMediaList {

private static final long serialVersionUID=1L;

    /**
     * 主键
     */
     @TableId(value = "id")
    private Long id;
    /**
     * 投诉详情ID
     */
    private Long complaintInfoId;
    /**
     * 媒体文件业务类型
     */
    private String mediaType;
    /**
     * 媒体文件请求url
     */
    private String mediaUrl;

}
