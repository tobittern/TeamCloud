package com.euler.statistics.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * 游戏用户管理视图对象 game_user_management
 *
 * @author euler
 * @date 2022-04-02
 */
@Data
@ApiModel("游戏用户管理视图对象")
@ExcelIgnoreUnannotated
public class SearchDataSummaryVo {

    private static final long serialVersionUID = 1L;

    /**
     * memberIds
     */
    private List<MemberSummaryVo> member;
    private List<RoleSummaryVo> role;
}
