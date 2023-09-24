package com.euler.collection.service;

import com.euler.collection.domain.bo.BehaviorBo;
import com.euler.collection.domain.dto.BehaviorDto;
import com.euler.collection.domain.vo.BehaviorVo;
import com.euler.common.core.domain.R;
import com.euler.common.mybatis.core.page.TableDataInfo;

/**
 * Service接口
 *
 * @author euler
 * @date 2022-03-22
 */
public interface IBehaviorService {

    /**
     * 查询
     *
     * @param id 主键
     * @return
     */
    BehaviorVo queryById(Integer id);

    /**
     * 查询列表
     *
     * @return 集合
     */
    TableDataInfo<BehaviorVo> queryPageList(BehaviorDto dto);

    /**
     * 修改
     *
     * @return 结果
     */
    R insertByBo(BehaviorBo bo);

}
