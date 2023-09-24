package com.euler.sdk.service;

import com.euler.common.core.domain.R;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.sdk.api.domain.ScoreSystemBo;
import com.euler.sdk.domain.dto.ScoreSystemDto;
import com.euler.sdk.domain.vo.ScoreSystemVo;

/**
 * 积分体系Service接口
 *
 * @author euler
 * @date 2022-03-22
 */
public interface IScoreSystemService {

    /**
     * 查询积分体系
     *
     * @param id 积分体系主键
     * @return 积分体系
     */
    ScoreSystemVo queryById(Long id);

    /**
     * 查询积分体系列表
     *
     * @param dto 积分体系
     * @return 积分体系集合
     */
    TableDataInfo<ScoreSystemVo> queryPageList(ScoreSystemDto dto);

    /**
     * 计算积分
     *
     * @param bo 积分体系
     * @return 结果
     */
    R calculateScore(ScoreSystemBo bo);

    /**
     * 修改积分体系
     *
     * @param bo 积分体系
     * @return 结果
     */
    R updateByBo(ScoreSystemBo bo);

}
