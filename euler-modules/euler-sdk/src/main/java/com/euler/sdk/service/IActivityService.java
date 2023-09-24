package com.euler.sdk.service;


import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdNameTypeDicDto;
import com.euler.common.mybatis.core.page.PageQuery;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.sdk.domain.bo.ActivityBo;
import com.euler.sdk.domain.dto.ActivityDto;
import com.euler.sdk.domain.vo.ActivityVo;

import java.util.Collection;
import java.util.List;

/**
 * 活动Service接口
 *
 * @author euler
 * @date 2022-03-29
 */
public interface IActivityService {

    /**
     * 查询活动
     *
     * @param id 活动主键
     * @return 活动
     */
    ActivityVo queryById(Integer id);

    /**
     * 查询活动列表
     *
     * @param
     * @return 活动集合
     */
    TableDataInfo<ActivityVo> queryPageList(ActivityDto dto);

    /**
     * 修改活动
     *
     * @param
     * @return 结果
     */
    R insertByBo(ActivityBo bo);

    /**
     * 修改活动
     *
     * @param
     * @return 结果
     */
    R updateByBo(ActivityBo bo);

    /**
     * 校验并批量删除活动信息
     *
     * @param ids     需要删除的活动主键集合
     * @param isValid 是否校验,true-删除前校验,false-不校验
     * @return 结果
     */
    Boolean deleteWithValidByIds(Collection<Integer> ids, Boolean isValid);

    /**
     * 操作上下架
     */
    R operation(IdNameTypeDicDto dto, Long userId);
}
