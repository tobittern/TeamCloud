package com.euler.sdk.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;


/**
 * 使用使用到不通地方的记录对象 game_use_record
 *
 * @author euler
 * @date 2022-03-30
 */
@Data
@ApiModel("游戏关联")
@ExcelIgnoreUnannotated
public class GameUseRecordVo {

    private static final long serialVersionUID = 1L;

    /**
     * 甲方ID 代表着是使用方
     */
    private Integer partyAId;
    /**
     * 乙方ID 被使用的ID
     */
    private Integer partyBId;
    /**
     * 类型 1等级礼包 2活动礼包 3活动创建 4渠道关联
     */
    private Integer type;


}
