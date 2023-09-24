package com.euler.system.api.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.euler.common.core.web.domain.BaseEntity;
import lombok.Data;

@Data
@TableName(value = "app_config")
public class AppConfig extends BaseEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String appName;

    private String appId;

    private String appSecret;

    private Integer parentId;

    private Integer isShow;

}
