package com.euler.sdk.service;

import com.euler.common.core.domain.R;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.sdk.api.domain.MyGameVo;
import com.euler.sdk.domain.bo.MyGameBo;
import com.euler.sdk.domain.dto.MyGameDto;

import java.util.Collection;

/**
 * 我的游戏Service接口
 *
 * @author euler
 * @date 2022-03-29
 */
public interface IMyGameService {

    /**
     * 查询我的游戏
     *
     * @param id 我的游戏主键
     * @return 我的游戏
     */
    MyGameVo queryById(Integer id);

    /**
     * 后台 - 查询用户游戏列表
     *
     * @param
     * @return 游戏集合
     */
    TableDataInfo<MyGameVo> backstagePageList(MyGameDto dto);

    /**
     * 查询我的游戏列表
     *
     * @param
     * @return 我的游戏集合
     */
    TableDataInfo<MyGameVo> queryPageList(MyGameDto dto);

    /**
     * 获取登录的游戏信息
     *
     * @param
     * @param
     * @param
     * @return
     */
    MyGameVo getCurrentGameInfo(MyGameDto dto);


    /**
     * 修改我的游戏
     *
     * @param
     * @return 结果
     */
    R insertByBo(MyGameBo bo);

    /**
     * 修改我的游戏
     *
     * @param
     * @return 结果
     */
    R updateByBo(MyGameBo bo);

    /**
     * 校验并批量删除我的游戏信息
     *
     * @param ids     需要删除的我的游戏主键集合
     * @param isValid 是否校验,true-删除前校验,false-不校验
     * @return 结果
     */
    Boolean deleteWithValidByIds(Collection<Integer> ids, Boolean isValid);

    /**
     * 查询游戏通过指定参数
     *
     * @param userId
     * @param packageCode
     */
    MyGameVo selectUserGameByParams(Long userId, Integer gameId, String packageCode);

}
