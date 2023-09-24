package com.euler.statistics.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 游戏用户管理视图对象 game_user_management
 *
 * @author euler
 * @date 2022-04-02
 */
@Data
@ApiModel("游戏用户管理视图对象")
@ExcelIgnoreUnannotated
public class MemberSummaryVo implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * memberIds
     */
    private Long memberId;


    /**
     * gameId
     */
    private Integer gameId;


    /**
     * packageCode
     */
    private String packageCode;

    /**
     * createTime
     */
    private Date createTime;

    public MemberSummaryVo(Long memberId, Integer gameId, String packageCode, Date createTime) {
        this.setMemberId(memberId);
        this.setGameId(gameId);
        this.setPackageCode(packageCode);
        this.setCreateTime(createTime);
    }
}
