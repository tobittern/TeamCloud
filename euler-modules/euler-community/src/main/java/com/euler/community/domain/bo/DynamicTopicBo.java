package com.euler.community.domain.bo;

import io.swagger.annotations.ApiModel;
import lombok.Data;


/**
 * 动态关联话题对象 dynamic_topic
 *
 * @author euler
 * @date 2022-06-01
 */
@Data
@ApiModel("动态关联话题业务对象")
public class DynamicTopicBo {

    private static final long serialVersionUID = 1L;

    /**
     * 动态表主键id
     */
    private Long dynamicId;
    /**
     * 话题id集合
     */
    private String topicIds;

}
