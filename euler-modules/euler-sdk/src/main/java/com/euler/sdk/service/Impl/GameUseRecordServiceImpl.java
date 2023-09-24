package com.euler.sdk.service.Impl;


import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.euler.common.core.domain.dto.KeyValueDto;
import com.euler.common.core.domain.dto.SelectGameDto;
import com.euler.common.core.domain.dto.TableDataInfoCoreDto;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.platform.api.RemoteGameManagerService;
import com.euler.platform.api.domain.OpenGameDubboVo;
import com.euler.sdk.domain.bo.GameUseRecordBo;
import com.euler.sdk.domain.dto.CommonIdPageDto;
import com.euler.sdk.domain.entity.GameUseRecord;
import com.euler.sdk.mapper.ChannelPackageMapper;
import com.euler.sdk.mapper.GameUseRecordMapper;
import com.euler.sdk.service.IGameUseRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 活动Service接口
 *
 * @author euler
 * @date 2022-03-29
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class GameUseRecordServiceImpl implements IGameUseRecordService {

    @Autowired
    private GameUseRecordMapper baseMapper;
    @DubboReference
    private RemoteGameManagerService remoteGameManagerService;
    @Autowired
    private ChannelPackageMapper channelPackageMapper;

    /**
     * 游戏关联数据添加
     */
    @Override
    public int insertGameUseRecord(GameUseRecordBo bo) {
        int rows = 1;
        // 新增关联数据
        if (bo.getPartyBId() == null) {
            return rows;
        }
        Integer[] insertData = Convert.toIntArray(bo.getPartyBId().split(","));
        // 这个地方就需要调一下远程的dubbo服务  查看一下那些游戏是可以正常使用的
        List<Integer> searchIds = Arrays.asList(insertData);
        if (searchIds.size() > 0) {
            List<OpenGameDubboVo> openGameDubboVos = remoteGameManagerService.selectByIds(searchIds);
            List<GameUseRecord> list = new ArrayList<GameUseRecord>();
            for (var x : openGameDubboVos) {
                // 过滤掉没有上线的不能使用的游戏
                GameUseRecord rm = new GameUseRecord();
                rm.setPartyAId(bo.getPartyAId());
                rm.setPartyBId(x.getId());
                rm.setType(bo.getType());
                list.add(rm);
            }
            if (list.size() > 0) {
                rows = baseMapper.insertBatch(list) ? list.size() : 0;
            }
        }
        return rows;
    }


    /**
     * 游戏关联数据修改
     */
    @Override
    public int updateRGameUseRecord(GameUseRecordBo bo) {
        // 删除相对应的关联数据
        baseMapper.delete(new LambdaQueryWrapper<GameUseRecord>()
            .eq(GameUseRecord::getPartyAId, bo.getPartyAId())
            .eq(GameUseRecord::getType, bo.getType()));
        // 新增
        return insertGameUseRecord(bo);
    }

    /**
     * 判断指定查询条件是否存在数据
     *
     * @return
     */
    @Override
    public List<GameUseRecord> selectInfoByParams(GameUseRecordBo bo) {
        LambdaQueryWrapper<GameUseRecord> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getPartyAId() != null, GameUseRecord::getPartyAId, bo.getPartyAId());
        lqw.eq(StringUtils.isNotBlank(bo.getPartyBId()), GameUseRecord::getPartyBId, Convert.toInt(bo.getPartyBId()));
        lqw.eq(GameUseRecord::getType, bo.getType());
        List<GameUseRecord> gameUseRecords = baseMapper.selectVoList(lqw);
        return gameUseRecords;
    }

    /**
     * 根据ids获取到对应的游戏名称
     *
     * @param ids
     * @return
     */
    @Override
    public List<OpenGameDubboVo> selectGameNameByIds(List<Integer> ids, Integer type) {
        if (ids != null && ids.size() > 0) {
            // 查询出需要查询的游戏id集合
            LambdaQueryWrapper<GameUseRecord> in = Wrappers.<GameUseRecord>lambdaQuery()
                .eq(GameUseRecord::getType, type)
                .in(GameUseRecord::getPartyAId, ids);
            List<GameUseRecord> gameUseRecords = baseMapper.selectList(in);
            List<Integer> useIds = new ArrayList<>();
            gameUseRecords.forEach(a -> {
                useIds.add(a.getPartyBId());
            });
            // 获取到了指定id绑定的游戏id集合
            List<OpenGameDubboVo> openGameDubboVos = remoteGameManagerService.selectByIds(useIds);
            // 循环我们获取的partyAId集合进行一下归类
            openGameDubboVos.forEach(a -> {
                List<Integer> collect = gameUseRecords.stream().filter(b -> b.getPartyBId().equals(a.getId())).map(GameUseRecord::getPartyAId).collect(Collectors.toList());
                a.setRelationList(collect);
            });
            return openGameDubboVos;
        } else {
            return null;
        }
    }

    /**
     * 根据ids获取到对应的游戏名称
     *
     * @return
     */
    @Override
    public TableDataInfo<OpenGameDubboVo> selectGameNameById(CommonIdPageDto dto, Integer type) {
        if (dto.getId() != null) {
            // 查询出需要查询的游戏id集合
            LambdaQueryWrapper<GameUseRecord> lqw = Wrappers.lambdaQuery();
            lqw.eq(GameUseRecord::getType, type);
            lqw.eq(GameUseRecord::getPartyAId, dto.getId());
            IPage<GameUseRecord> gameUseRecordIPage = baseMapper.selectVoPage(dto.build(), lqw);
            List<Integer> useIds = new ArrayList<>();
            gameUseRecordIPage.getRecords().forEach(a -> {
                useIds.add(a.getPartyBId());
            });
            List<OpenGameDubboVo> openGameDubboVos = remoteGameManagerService.selectByIds(useIds);
            TableDataInfo<OpenGameDubboVo> build = TableDataInfo.build(openGameDubboVos);
            // 设置一下每款游戏当前的分包数量
            build.setTotal(gameUseRecordIPage.getTotal());
            return build;
        } else {
            return null;
        }
    }

    /**
     * 根据ids获取到对应的游戏名称
     *
     * @return
     */
    @Override
    public TableDataInfoCoreDto<OpenGameDubboVo> selectGameNameByParams(SelectGameDto dto) {
        // 查询出需要查询的游戏id集合
        LambdaQueryWrapper<GameUseRecord> eq = Wrappers.<GameUseRecord>lambdaQuery()
            .eq(GameUseRecord::getType, dto.getType())
            .eq(GameUseRecord::getPartyAId, dto.getId());
        List<GameUseRecord> gameUseRecords = baseMapper.selectList(eq);
        List<Integer> useIds = new ArrayList<>();
        gameUseRecords.forEach(a -> {
            useIds.add(a.getPartyBId());
        });
        // 获取到了指定id绑定的游戏id集合
        TableDataInfoCoreDto<OpenGameDubboVo> openGameDubboVos = remoteGameManagerService.selectByParams(useIds, dto);
        // 获取当前渠道下面关联的游戏
        List<KeyValueDto> keyValueDtos = channelPackageMapper.selectGameIdHavePackageNums(dto.getId());
        // 循环我们获取的partyAId集合进行一下归类
        openGameDubboVos.getRows().forEach(a -> {
            List<Integer> collect = gameUseRecords.stream().filter(b -> b.getPartyBId().equals(a.getId())).map(GameUseRecord::getPartyAId).collect(Collectors.toList());
            a.setRelationList(collect);
            // 获取当前游戏下面创建了多少分包
            Optional<KeyValueDto> first = keyValueDtos.stream().filter(c -> c.getValue().equals(a.getId().toString())).findFirst();
            if (first.isPresent()) {
                a.setExtend(first.get().getKey().toString());
            } else {
                a.setExtend("0");
            }
        });
        return openGameDubboVos;
    }

    /**
     * 渠道对应的游戏列表, 不分页
     */
    @Override
    public List<OpenGameDubboVo> selectGameNameByChannel(SelectGameDto dto) {
        // 查询出需要查询的游戏id集合
        LambdaQueryWrapper<GameUseRecord> eq = Wrappers.<GameUseRecord>lambdaQuery()
            .eq(GameUseRecord::getType, dto.getType())
            .eq(GameUseRecord::getPartyAId, dto.getId());
        List<GameUseRecord> gameUseRecords = baseMapper.selectList(eq);
        List<Integer> useIds = new ArrayList<>();
        gameUseRecords.forEach(a -> {
            useIds.add(a.getPartyBId());
        });
        // 获取到了指定id绑定的游戏id集合
        List<OpenGameDubboVo> openGameDubboVos = remoteGameManagerService.selectByChannel(useIds, dto);
        // 获取当前渠道下面关联的游戏
        List<KeyValueDto> keyValueDtos = channelPackageMapper.selectGameIdHavePackageNums(dto.getId());
        // 循环我们获取的partyAId集合进行一下归类
        openGameDubboVos.forEach(a -> {
            List<Integer> collect = gameUseRecords.stream().filter(b -> b.getPartyBId().equals(a.getId())).map(GameUseRecord::getPartyAId).collect(Collectors.toList());
            a.setRelationList(collect);
            // 获取当前游戏下面创建了多少分包
            Optional<KeyValueDto> first = keyValueDtos.stream().filter(c -> c.getValue().equals(a.getId().toString())).findFirst();
            if (first.isPresent()) {
                a.setExtend(first.get().getKey().toString());
            } else {
                a.setExtend("0");
            }
        });
        return openGameDubboVos;
    }

    /**
     * 批量删除我的游戏
     *
     * @param id   需要删除主键
     * @param type 需要删除类型 主键
     * @return 结果
     */
    @Override
    public Boolean deleteWithValidByIdAndType(Integer id, Integer type) {
        int i = baseMapper.delete(Wrappers.<GameUseRecord>lambdaQuery().eq(GameUseRecord::getPartyAId, id).eq(GameUseRecord::getType, type));
        if (i > 0) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

}
