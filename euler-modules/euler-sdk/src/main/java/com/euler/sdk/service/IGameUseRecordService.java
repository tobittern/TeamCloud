package com.euler.sdk.service;

import com.euler.common.core.domain.dto.SelectGameDto;
import com.euler.common.core.domain.dto.TableDataInfoCoreDto;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.platform.api.domain.OpenGameDubboVo;
import com.euler.sdk.domain.bo.GameUseRecordBo;
import com.euler.sdk.domain.dto.CommonIdPageDto;
import com.euler.sdk.domain.entity.GameUseRecord;

import java.util.List;

/**
 * 活动Service接口
 *
 * @author euler
 * @date 2022-03-29
 */
public interface IGameUseRecordService {

    /**
     * 游戏关联数据添加
     */
    int insertGameUseRecord(GameUseRecordBo bo);

    /**
     * 游戏关联数据修改
     */
    int updateRGameUseRecord(GameUseRecordBo bo);

    /**
     * 判断指定查询条件是否存在数据
     *
     * @return
     */
    List<GameUseRecord> selectInfoByParams(GameUseRecordBo bo);

    /**
     * 获取游戏列表 通过关联id
     *
     * @param ids
     * @param type
     * @return
     */
    List<OpenGameDubboVo> selectGameNameByIds(List<Integer> ids, Integer type);

    /**
     * 分页获取游戏列表 通过关联id
     */
    TableDataInfo<OpenGameDubboVo> selectGameNameById(CommonIdPageDto dto, Integer type);

    /**
     * 分页获取游戏列表
     */
    TableDataInfoCoreDto<OpenGameDubboVo> selectGameNameByParams(SelectGameDto dto);

    /**
     * 渠道对应的游戏列表, 不分页
     */
    List<OpenGameDubboVo> selectGameNameByChannel(SelectGameDto dto);

    /**
     * 校验并批量删除我的游戏信息
     *
     * @param id     需要删除的我的游戏主键集合
     * @param type 是否校验,true-删除前校验,false-不校验
     * @return 结果
     */
    Boolean deleteWithValidByIdAndType(Integer id, Integer type);

}
