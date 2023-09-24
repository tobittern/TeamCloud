package com.euler.sdk.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 弹窗关联游戏对象 popup_game_relation
 *
 * @author euler
 * @date 2022-09-05
 */
@Data
@TableName("popup_game_relation")
public class PopupGameRelation {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Integer id;
    /**
     * 弹框ID
     */
    private Integer popupId;
    /**
     * 游戏ID
     */
    private Integer gameId;

}
