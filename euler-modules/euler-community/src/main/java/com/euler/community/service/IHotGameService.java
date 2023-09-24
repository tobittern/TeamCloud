package com.euler.community.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.euler.common.core.domain.R;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.community.domain.bo.HotGameBo;
import com.euler.community.domain.dto.HotGameDto;
import com.euler.community.domain.entity.HotGame;
import com.euler.community.domain.vo.HotGameVo;

import java.util.Collection;
import java.util.List;

/**
 * Service接口
 *
 * @author euler
 * @date 2022-06-17
 */
public interface IHotGameService extends IService<HotGame> {

    /**
     * 查询
     *
     * @param id 主键
     * @return
     */
    HotGameVo queryById(Long id);

    /**
     * 查询列表
     *
     * @param bo
     * @return 集合
     */
    TableDataInfo<HotGameVo> queryPageList(HotGameDto bo);

    /**
     * 查询列表
     *
     * @param bo
     * @return 集合
     */
    List<HotGameVo> queryList(HotGameDto bo);

    /**
     * 修改
     *
     * @param bo
     * @return 结果
     */
    Boolean insertByBo(HotGameBo bo);

    /**
     * 修改
     *
     * @param bo
     * @return 结果
     */
    Boolean updateByBo(HotGameBo bo);

    /**
     * 校验并批量删除信息
     *
     * @param ids 需要删除的主键集合
     * @param isValid 是否校验,true-删除前校验,false-不校验
     * @return 结果
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 获取热门游戏
     * 取出10条数据
     * @return
     */
    R getHotGames(HotGameBo hotGameBo);
}
