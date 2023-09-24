package com.euler.community.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.euler.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 对象 hot_game
 *
 * @author euler
 * @date 2022-06-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("hot_game")
public class HotGame extends BaseEntity {

private static final long serialVersionUID=1L;

    /**
     * 主键
     */
     @TableId(value = "id")
    private Long id;

    /**
     * 用户id
     */
    private Long memberId;

    /**
     * 游戏id
     */
    private Long gameId;

    /**
     * 游戏名称
     */
    private String gameName;

    /**
     * 游戏图片
     */
    private String gamePic;

    /**
     * 查看游戏次数
     */
    private Integer num;

    /**
     * 平台
     */
    private Integer operationPlatform;

    /**
     * 是否删除，0未删除，2已删除
     */
     @TableLogic
    private String delFlag;

}
