package com.euler.platform.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.euler.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;


/**
 * 游戏版本历史对象 open_game_version_history
 *
 * @author euler
 * @date 2022-03-31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("open_game_version_history")
public class OpenGameVersionHistory extends BaseEntity {

private static final long serialVersionUID=1L;

    /**
     * 主键
     */
     @TableId(value = "id")
    private Integer id;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 游戏id
     */
    private Integer gameId;
    /**
     * 审核状态 (0 初始状态 1审核中  2审核成功  3审核失败)
     */
    private Integer auditStatus;
    /**
     * 版本号
     */
    private Integer versionNumber;
    /**
     * 版本号名称
     */
    private String versionNumberName;
    /**
     * 版本说明
     */
    private String versionDescription;
    /**
     * 游戏的图片列表
     */
    private String pictureUrl;
    /**
     * 游戏安装包地址
     */
    private String gameInstallPackage;
    /**
     * 审核时间
     */
    private Date auditTime;
    /**
     * 删除状态 0正常 2删除
     */
     @TableLogic
    private String delFlag;

}
