package com.euler.sdk.service;

import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdNameTypeDicDto;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.sdk.api.domain.ChannelPackageVo;
import com.euler.sdk.domain.bo.ChannelBo;
import com.euler.sdk.domain.bo.ChannelPackageBo;
import com.euler.sdk.domain.dto.ChannelDto;
import com.euler.sdk.domain.dto.ChannelPackageDto;
import com.euler.sdk.domain.vo.ChannelVo;

import java.util.List;

/**
 * 主渠道Service接口
 *
 * @author euler
 * @date 2022-04-01
 */
public interface IChannelService {

    /**
     * 查询主渠道
     *
     * @param id 主渠道主键
     * @return 主渠道
     */
    ChannelVo queryById(Integer id);

    /**
     * 查询主渠道列表
     *
     * @return 主渠道集合
     */
    TableDataInfo<ChannelVo> queryPageList(ChannelDto dto);

    /**
     * 查询主渠道列表, 不分页
     *
     * @return 主渠道集合
     */
    List<ChannelVo> queryList(ChannelDto dto);

    /**
     * 修改主渠道
     *
     * @return 结果
     */
    R insertByBo(ChannelBo bo);

    /**
     * 修改主渠道
     *
     * @return 结果
     */
    R updateByBo(ChannelBo bo);

    /**
     * 渠道开启停用
     */
    R operation(IdNameTypeDicDto dto, Long userId);

    /**
     * 分包列表
     */
    TableDataInfo<ChannelPackageVo> groupList(ChannelPackageDto dto);

    /**
     * 导出渠道分组列表
     */
    List<ChannelPackageVo> queryExportList(ChannelPackageDto dto);


    /**
     * 添加渠道分包
     *
     * @return
     */
    R addGroup(ChannelPackageBo bo);


    /**
     * 修改渠道分包
     *
     * @return
     */
    R editGroup(ChannelPackageBo bo);

    /**
     * 根据渠道code查询出分包渠道的基础信息
     *
     * @param codeKey
     * @return
     */
    ChannelPackageVo selectChannelPackageByCode(String codeKey, String appId);

//    /**
//     * 根据渠道code查询出分包渠道的基础信息
//     * @param gameName
//     * @param gameId
//     * @return
//     */
//    Boolean createChannelPackageDefault(String gameName, Integer gameId);

    /**
     * 获取一批游戏的分包基础信息
     * @param gameIds
     * @return
     */
    List<ChannelPackageVo> getGamePackageInfoByIds(List<Integer> gameIds);

    /**
     * 根据游戏名查询出指定游戏列表
     * @return
     */
    List<ChannelPackageVo> getGameListByName();

}
