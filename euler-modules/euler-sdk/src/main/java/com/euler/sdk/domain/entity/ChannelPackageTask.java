package com.euler.sdk.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.euler.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 渠道分组对象 channel_packge_task
 *
 * @author euler
 * @date 2022-04-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("channel_package_task")
public class ChannelPackageTask extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Integer id;
    /**
     * 主渠道ID
     */
    private Integer channelId;
    /**
     * 对应游戏ID
     */
    private Integer gameId;
    /**
     * 状态 0 待执行  1执行完毕 2执行中断报错
     */
    private Integer status;

    /**
     * 删除状态 0正常 2删除
     */
    @TableLogic
    private String delFlag;

}
