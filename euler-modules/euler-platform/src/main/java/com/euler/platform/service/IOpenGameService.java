package com.euler.platform.service;

import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.*;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.platform.api.domain.OpenGame;
import com.euler.platform.api.domain.OpenGameDubboVo;
import com.euler.platform.api.domain.OpenGameVo;
import com.euler.platform.domain.bo.OpenGameAuditRecordBo;
import com.euler.platform.domain.bo.OpenGameBo;
import com.euler.platform.domain.bo.OpenGameVersionHistoryBo;
import com.euler.platform.domain.dto.OpenGamePageDto;
import com.euler.platform.domain.dto.OpenGameTransferDto;
import com.euler.platform.domain.dto.OpenGameTransferLogDto;
import com.euler.platform.domain.dto.OpenGameVersionHistoryDto;
import com.euler.platform.domain.vo.OpenGameDataCountVo;
import com.euler.platform.domain.vo.OpenGameTransferLogVo;
import com.euler.platform.domain.vo.OpenGameVersionHistoryVo;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 【游戏管理】Service接口
 *
 * @author open
 * @date 2022-02-18
 */
public interface IOpenGameService {

    /**
     * 游戏申请记录数据查询
     */
    OpenGameDataCountVo selectCount();

    /**
     * 查询【游戏管理】
     *
     * @return 【游戏管理】
     */
    OpenGameVo selectInfo(IdDto idDto, Long userId);

    /**
     * 查询【游戏管理】列表
     *
     * @param 【游戏管理】
     * @return 【游戏管理】集合
     */
    TableDataInfo<OpenGameVo> queryPageList(OpenGamePageDto openGamePageDto);

    /**
     * 查询【游戏管理】列表
     *
     * @param 【游戏管理】
     * @return 【游戏管理】集合
     */
    List<OpenGameVo> queryList(OpenGameBo bo);

    /**
     * 添加【游戏管理】
     *
     * @param 【游戏管理】
     * @return 结果
     */
    R insertByBo(OpenGameBo bo);

    /**
     * 修改【游戏管理】
     *
     * @param 【游戏管理】
     * @return 结果
     */
    R updateByBo(OpenGameBo bo);

    /**
     * 校验并批量删除
     *
     * @param ids     需要删除的渠道分组主键集合
     * @param isValid 是否校验,true-删除前校验,false-不校验
     * @return 结果
     */
    Boolean deleteWithValidByIds(Collection<Integer> ids, Boolean isValid);

    /**
     * 撤销审核
     *
     * @param idDto
     * @param userId
     * @return
     */
    R revokeApproval(IdDto<Integer> idDto, Long userId);

    /**
     * 游戏的上下线操作
     *
     * @param idNameTypeDicDTO
     * @return
     */
    R operation(IdNameTypeDicDto idNameTypeDicDTO, Long userId);

    /**
     * 游戏的审核
     */
    R auditGame(OpenGameAuditRecordBo bo, Long userId);

    /**
     * 通过id查询游戏基础信息
     *
     * @return
     */
    List<OpenGameDubboVo> selectByIds(List<Integer> collect);

    /**
     * 通过id查询游戏基础信息
     *
     * @return
     */
    TableDataInfoCoreDto<OpenGameDubboVo> selectByParams(List<Integer> collect, SelectGameDto dto);

    /**
     * 通过id查询游戏基础信息, 不分页
     *
     * @return
     */
    List<OpenGameDubboVo> selectByChannel(List<Integer> collect, SelectGameDto dto);

    /**
     * 通过appId查询游戏基础信息
     *
     * @return
     */
    OpenGameDubboVo selectInfoByAppId(String appId);

    /**
     * 添加【游戏版本历史】
     *
     * @param 【游戏版本历史】
     * @return 结果
     */
    R addVersion(OpenGameVersionHistoryBo bo);

    /**
     * 游戏版本历史列表
     *
     * @param 【游戏版本历史】
     * @return 结果
     */
    TableDataInfo<OpenGameVersionHistoryVo> gameVersionList(OpenGameVersionHistoryDto dto);

    /**
     * 不同版本的操作行为
     */
    R operationVersion(IdNameTypeDicDto dto, Long userId);

    /**
     * 审核添加的游戏版本
     */
    R auditVersion(IdNameTypeDicDto dto, Long userId);

    /**
     * 校验并批量删除
     *
     * @return 结果
     */
    R removeVersionByIds(KeyValueDto<String> keyValueDto);

    /**
     * 根据条件查询openGame
     */
    TableDataInfoCoreDto<OpenGameDubboVo> selectGameByParam(Map<String,Object> map);

    /**
     * app端查询游戏详情
     * @return 游戏列表集合
     */
    OpenGameVo getGameInfo(IdDto<Integer> idDto);

    /**
     * 游戏拥有权转移
     *
     * @return 结果
     */
    R transfer(OpenGameTransferDto dto);

    /**
     * 列表
     * @param dto
     * @return
     */
    TableDataInfo<OpenGameTransferLogVo> transferList(OpenGameTransferLogDto dto);

    /**
     * 根据用户id查询出审核过且在线的游戏列表
     * @return
     */
    List<OpenGameVo> queryGameListByUserId(IdDto<Long> idDto);

    /**
     * 根据渠道id查询游戏列表
     */
    List<OpenGame> getGameList(Integer channelId);

    /**
     * 获取支付方式
     *
     * @param keyValueDto
     * @return
     */
    List getPayTypeData(KeyValueDto<String> keyValueDto);

    /**
     * 通过游戏名称和平台查询游戏信息
     */
    OpenGameVo getGameInfo(String gameName, String platform);

}
