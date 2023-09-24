package com.euler.statistics.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.euler.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 主渠道对象 channel
 *
 * @author euler
 * @date 2022-04-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("channel")
public class Channel extends BaseEntity {

private static final long serialVersionUID=1L;

    /**
     * 主键
     */
     @TableId(value = "id")
    private Integer id;
    /**
     * user_id
     */
    private Long userId;
    /**
     * 渠道名
     */
    private String channelName;
    /**
     * 渠道主账号名称
     */
    private String adminName;
    /**
     * 渠道游戏数量
     */
    private Integer gameNum;
    /**
     * 渠道状态
     */
    private Integer status;
    /**
     * 删除状态 0正常 2删除
     */
     @TableLogic
    private String delFlag;

}
