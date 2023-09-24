package com.euler.community.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdTypeDto;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.community.domain.bo.HistorySearchBo;
import com.euler.community.domain.dto.HistorySearchDto;
import com.euler.community.domain.entity.HistorySearch;
import com.euler.community.domain.vo.HistorySearchVo;

import java.util.Collection;
import java.util.List;

/**
 * 搜索历史Service接口
 *
 * @author euler
 *  2022-06-07
 */
public interface IHistorySearchService extends IService<HistorySearch> {

    /**
     * 查询搜索历史
     *
     * @param id 搜索历史主键
     * @return 搜索历史
     */
    HistorySearchVo queryById(Long id);

    /**
     * 查询搜索历史列表
     *
     * @param historySearchDto 搜索历史
     * @return 搜索历史集合
     */
    TableDataInfo<HistorySearchVo> queryPageList(HistorySearchDto historySearchDto);

    /**
     * 查询搜索历史列表
     *
     * @param historySearchDto 搜索历史
     * @return 搜索历史集合
     */
    List<HistorySearchVo> queryList(HistorySearchDto historySearchDto);

    /**
     * 修改搜索历史
     *
     * @param bo 搜索历史
     * @return 结果
     */
    Boolean insertByBo(HistorySearchBo bo);

    /**
     * 修改搜索历史
     *
     * @param bo 搜索历史
     * @return 结果
     */
    Boolean updateByBo(HistorySearchBo bo);

    /**
     * 逻辑删除
     *
     * @param ids
     * @return
     */
    R deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 逻辑清空
     *
     * @return
     */
    Integer clearByUserId(IdTypeDto<Integer, Integer> dto);

    /**
     * 搜索内容（首页、发现页功能）
     * @param bo 搜索
     * @return 结果
     */
    R<Object> search(HistorySearchDto bo);
}
