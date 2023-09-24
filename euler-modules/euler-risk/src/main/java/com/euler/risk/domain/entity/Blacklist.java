package com.euler.risk.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.euler.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 黑名单对象 blacklist
 *
 * @author euler
 * @date 2022-08-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("blacklist")
public class Blacklist extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 自增主键
     */
    @TableId(value = "id")
    private Integer id;
    /**
     * 类型（1:手机号 2:身份证 3:ip 4:mac 5:oaid 6:imei 7:android 8:uuid 9:idfa 10:pushId）
     */
    private String type;

    /**
     * 类型对应的目标内容
     */
    private String target;

    /**
     * 封号类型（1:使用外挂 2:发布广告信息 3:发布淫秽色情信息 4:发布违法违规信息 5:对他人谩骂侮辱 6:发布虚假信息 7:违反九区玩家服务协议 8:其他）
     */
    private String banType;

    /**
     * 封号开始时间
     */
    private Date startTime;

    /**
     * 封号截止时间
     */
    private Date endTime;

    /**
     * 封号原因
     */
    private String reason;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @TableLogic
    private String delFlag;

}
