package com.euler.sdk.service;

import com.euler.common.core.domain.R;
import com.euler.sdk.api.domain.GameConfigVo;
import com.euler.sdk.domain.entity.GameConfig;
import com.euler.sdk.domain.bo.GameConfigBo;
import com.euler.sdk.domain.dto.GameConfigDto;
import com.baomidou.mybatisplus.extension.service.IService;
import com.euler.common.mybatis.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 游戏配置Service接口
 *
 * @author euler
 * @date 2023-03-23
 */
public interface IGameConfigService extends IService<GameConfig> {

    /**
     * 查询游戏配置
     *
     * @param id 游戏配置主键
     * @return 游戏配置
     */
    GameConfigVo queryById(Integer id);

    /**
     * 根据类条件查询游戏配置信息
     *
     * @return 游戏配置信息
     */
    GameConfigVo selectGameConfigByParam(Integer gameId, String type, String platform);

    /**
     * 查询游戏配置列表
     *
     * @param dto 游戏配置
     * @return 游戏配置集合
     */
    TableDataInfo<GameConfigVo> queryPageList(GameConfigDto dto);

    /**
     * 查询游戏配置列表
     *
     * @param dto 游戏配置
     * @return 游戏配置集合
     */
    List<GameConfigVo> queryList(GameConfigDto dto);

    /**
     * 修改游戏配置
     *
     * @param bo 游戏配置
     * @return 结果
     */
    R insertByBo(GameConfigBo bo);

    /**
     * 修改游戏配置
     *
     * @param bo 游戏配置
     * @return 结果
     */
    R updateByBo(GameConfigBo bo);

    /**
     * 校验并批量删除游戏配置信息
     *
     * @param ids 需要删除的游戏配置主键集合
     * @return 结果
     */
    R deleteWithValidByIds(Collection<Integer> ids);

}

