package com.euler.sdk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdNameDto;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.sdk.api.domain.GameUserManagement;
import com.euler.sdk.domain.bo.GameUserManagementBo;
import com.euler.sdk.domain.dto.GameUserAddDto;
import com.euler.sdk.domain.dto.GameUserManagementDto;
import com.euler.sdk.api.domain.GameUserManagementVo;

import java.util.List;

/**
 * 游戏用户管理Service接口
 *
 * @author euler
 * @date 2022-04-02
 */
public interface IGameUserManagementService extends IService<GameUserManagement> {


    /**
     * 查询游戏用户管理列表
     *
     * @param dto 游戏用户管理
     * @return 游戏用户管理集合
     */
    TableDataInfo<GameUserManagementVo> getGameUserDetailPageList(GameUserManagementDto dto);

    /**
     * 修改游戏用户管理
     *
     * @param gu 游戏用户管理
     * @return 结果
     */
    R insertByBo(GameUserAddDto gu);


    /**
     * 获取游戏用户数据
     *
     * @param gameUserManagement
     * @return
     */
    GameUserManagement getGameUserInfo(GameUserManagement gameUserManagement);


    /**
     * 获取游戏用户数据列表
     *
     * @param memberIds
     * @param gameId
     * @return
     */
    List<GameUserManagement> getGameUserInfoList(List<Long> memberIds, Integer gameId);

    /**
     * 获取所有的区服信息
     *
     * @return 区服信息数据
     */
    List<GameUserManagementVo> getAllGameService();

    String getGameUserMsgKey(GameUserManagementBo bo);

    /**
     * 根据区服名称查询游戏区服列表
     *
     * @param idNameDto
     * @return
     */
    List<GameUserManagementVo> getServerListByName(IdNameDto<Long> idNameDto);

}
