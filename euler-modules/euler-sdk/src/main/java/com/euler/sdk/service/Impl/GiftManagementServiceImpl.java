package com.euler.sdk.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.euler.common.core.constant.Constants;
import com.euler.common.core.constant.UserConstants;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdNameTypeDicDto;
import com.euler.common.core.domain.dto.KeyValueDto;
import com.euler.common.core.exception.ServiceException;
import com.euler.common.core.utils.JsonUtils;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.common.satoken.utils.LoginHelper;
import com.euler.platform.api.domain.OpenGameDubboVo;
import com.euler.sdk.domain.bo.GameUseRecordBo;
import com.euler.sdk.domain.bo.GiftManagementBo;
import com.euler.sdk.domain.dto.GiftManagementDetailDto;
import com.euler.sdk.domain.dto.GiftManagementDto;
import com.euler.sdk.domain.entity.GameUseRecord;
import com.euler.sdk.domain.entity.GiftInfo;
import com.euler.sdk.domain.entity.GiftManagement;
import com.euler.sdk.domain.vo.GiftInfoVo;
import com.euler.sdk.domain.vo.GiftManagementVo;
import com.euler.sdk.enums.GameUseRecordTypeEnum;
import com.euler.sdk.mapper.GiftInfoMapper;
import com.euler.sdk.mapper.GiftManagementMapper;
import com.euler.sdk.service.IGameUseRecordService;
import com.euler.sdk.service.IGiftManagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 礼包组管理Service业务层处理
 *
 * @author euler
 * @date 2022-03-22
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class GiftManagementServiceImpl implements IGiftManagementService {

    private final GiftManagementMapper baseMapper;

    private final GiftInfoMapper infoMapper;

    private final IGameUseRecordService iGameUseRecordService;

    /**
     * 查询礼包组管理
     *
     * @param id 礼包组管理主键
     * @return 礼包管理
     */
    @Override
    public GiftManagementVo queryById(Integer id) {

        GiftManagementVo vo = baseMapper.selectVoById(id);
        List<Integer> ids = new ArrayList<>();
        ids.add(id);
        List<OpenGameDubboVo> openGameDubboVos = iGameUseRecordService.selectGameNameByIds(ids, GameUseRecordTypeEnum.GRADE_GIFTG.getCode());
        if (openGameDubboVos != null && openGameDubboVos.size() > 0) {
            // 判断哪一个包含了这款游戏
            List<KeyValueDto> newList = openGameDubboVos.stream().filter(e -> e.getRelationList().contains(id)).map(e -> new KeyValueDto(e.getId(), e.getGameName())).collect(Collectors.toList());
            // 添加一个过滤 将获取的游戏列表按照id进行一下归类
            vo.setGameOriented(newList);
        }
        return vo;
    }

    /**
     * 查询礼包组管理列表
     *
     * @param dto 礼包组管理
     * @return 礼包管理
     */
    @Override
    public TableDataInfo<GiftManagementVo> queryPageList(GiftManagementDto dto) {
        LambdaQueryWrapper<GiftManagement> lqw = buildQueryWrapper(dto);
        Page<GiftManagementVo> result = baseMapper.selectVoPage(dto.build(), lqw);
        List<Integer> ids = result.getRecords().stream().map(GiftManagementVo::getId).collect(Collectors.toList());
        List<OpenGameDubboVo> openGameDubboVos = iGameUseRecordService.selectGameNameByIds(ids, GameUseRecordTypeEnum.GRADE_GIFTG.getCode());
        // 循环获取数据之后将游戏名累加到元数据中
        result.getRecords().forEach(row -> {
            if (openGameDubboVos != null && openGameDubboVos.size() > 0) {
                // 判断哪一个包含了这款游戏
                List<KeyValueDto> newList = openGameDubboVos.stream().filter(e -> e.getRelationList().contains(row.getId())).map(e -> new KeyValueDto(e.getId(), e.getGameName())).collect(Collectors.toList());
                // 添加一个过滤 将获取的游戏列表按照id进行一下归类
                row.setGameOriented(newList);
            }
        });
        return TableDataInfo.build(result);
    }

    private LambdaQueryWrapper<GiftManagement> buildQueryWrapper(GiftManagementDto dto) {
        LambdaQueryWrapper<GiftManagement> lqw = Wrappers.lambdaQuery();
        lqw.likeRight(StringUtils.isNotBlank(dto.getGiftGroupName()), GiftManagement::getGiftGroupName, dto.getGiftGroupName());
        lqw.eq(StringUtils.isNotBlank(dto.getIsUp()), GiftManagement::getIsUp, dto.getIsUp());
        return lqw;
    }

    /**
     * 根据渠道code获取礼包组管理详细信息
     *
     * @return 礼包组管理详细信息
     */
    @Override
    public GiftManagementVo getInfoByCodeKey() {

        Integer gameId = LoginHelper.getSdkChannelPackage().getGameId();
        String gameName = LoginHelper.getSdkChannelPackage().getGameName();
        if (gameId == null) {
            return new GiftManagementVo();
        }

        GameUseRecordBo recordBo = new GameUseRecordBo();
        recordBo.setPartyBId(gameId.toString());
        recordBo.setType(GameUseRecordTypeEnum.GRADE_GIFTG.getCode());
        List<GameUseRecord> recordList = iGameUseRecordService.selectInfoByParams(recordBo);
        if (recordList != null && recordList.size() > 0) {
            Integer groupId = recordList.get(0).getPartyAId();
            GiftManagementVo vo = baseMapper.selectVoById(groupId);
            if (ObjectUtil.isNull(vo)) {
                return new GiftManagementVo();
            }
            // 设置游戏ID
            vo.setGameId(gameId);
            // 面向游戏名称
            vo.setGameName(gameName);

            return vo;
        } else {
            return new GiftManagementVo();
        }
    }

    /**
     * 新增礼包组
     *
     * @param bo 礼包管理
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R insertByBo(GiftManagementBo bo) {
        GiftManagement add = BeanUtil.toBean(bo, GiftManagement.class);
        log.info("添加的字段列表->" + JsonUtils.toJsonString(add));
        String result = validEntityBeforeSave(add, true);
        if (!result.equals("success")) {
            return R.fail(result);
        }

        // 新增礼包组
        boolean flag = baseMapper.insert(add) > 0;

        if (flag) {
            bo.setId(add.getId());
            // 游戏关联数据添加
            GameUseRecordBo gameUseRecordBo = new GameUseRecordBo();
            gameUseRecordBo.setPartyAId(add.getId());
            gameUseRecordBo.setPartyBId(bo.getGameId());
            gameUseRecordBo.setType(GameUseRecordTypeEnum.GRADE_GIFTG.getCode());
            iGameUseRecordService.insertGameUseRecord(gameUseRecordBo);
            return R.ok();
        }
        return R.fail("新增礼包失败！");
    }

    /**
     * 修改礼包组管理
     *
     * @param bo 礼包管理
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R updateByBo(GiftManagementBo bo) {
        // 验证是否传输过来主键
        if (bo.getId() == null || bo.getId() <= 0) {
            return R.fail("参数缺失");
        }
        GiftManagement update = BeanUtil.toBean(bo, GiftManagement.class);
        String result = validEntityBeforeSave(update, false);
        if (!result.equals("success")) {
            return R.fail(result);
        }
        // 根据礼包组id查询礼包的数量
        int giftAmount = infoMapper.giftInfoListCount(bo.getId());

        // 查询礼包列表
        LambdaQueryWrapper<GiftInfo> lqw = Wrappers.lambdaQuery();
        lqw.eq(GiftInfo::getGiftGroupId, bo.getId());
        lqw.eq(GiftInfo::getDelFlag, Constants.COMMON_STATUS_NO);
        List<GiftInfoVo> list = infoMapper.selectVoList(lqw);

        List gradeList = new ArrayList();
        String receiveGrade = null;
        if (ObjectUtil.isNotNull(list) && !list.isEmpty() && list.size() > 0) {
            for (GiftInfoVo vo : list) {
                int grade = vo.getReceiveGrade();
                gradeList.add(grade);
            }

            // 可领取的最小等级和最大等级
            String gradeMin = Convert.toStr(Collections.min(gradeList));
            String gradeMax = Convert.toStr(Collections.max(gradeList));
            // 礼包可领取等级
            if (StringUtils.equals(gradeMin, gradeMax)) {
                receiveGrade = gradeMin;
            } else {
                receiveGrade = gradeMin + "-" + gradeMax;
            }
        }
        update.setGiftAmount(giftAmount);
        update.setReceiveGrade(receiveGrade);

        int i = baseMapper.updateById(update);
        if (i > 0) {
            // 游戏关联数据更新
            GameUseRecordBo gameUseRecordBo = new GameUseRecordBo();
            gameUseRecordBo.setPartyAId(update.getId());
            gameUseRecordBo.setPartyBId(bo.getGameId());
            gameUseRecordBo.setType(GameUseRecordTypeEnum.GRADE_GIFTG.getCode());
            iGameUseRecordService.updateRGameUseRecord(gameUseRecordBo);
            return R.ok();
        }
        return R.fail("更新失败");
    }

    /**
     * 保存前的数据校验
     *
     * @param entity 实体类数据
     * @param isAdd  true:是新增的验证 false:是编辑时的验证
     * @return 校验结果
     */
    private String validEntityBeforeSave(GiftManagement entity, Boolean isAdd) {
        // 添加校验，一款游戏只能用于一个礼包组，一个礼包组可能有多款游戏
        String[] strArr = entity.getGameId().split(",");
        String[] gameIds = Convert.toStrArray(strArr);

        for(String gameId : gameIds) {
            GameUseRecordBo recordBo = new GameUseRecordBo();
            recordBo.setPartyBId(gameId);
            recordBo.setType(GameUseRecordTypeEnum.GRADE_GIFTG.getCode());
            List<GameUseRecord> recordList = iGameUseRecordService.selectInfoByParams(recordBo);
            if(isAdd) {
                if (recordList != null && recordList.size() > 0) {
                    return "游戏id=" + gameId + "的礼包组已添加，请换一款游戏";
                }
            } else{
                if (recordList != null && recordList.size() > 0) {
                    GameUseRecordBo newBo = new GameUseRecordBo();
                    newBo.setPartyAId(entity.getId());
                    newBo.setPartyBId(gameId);
                    newBo.setType(GameUseRecordTypeEnum.GRADE_GIFTG.getCode());
                    List<GameUseRecord> newList = iGameUseRecordService.selectInfoByParams(newBo);

                    for(GameUseRecord record : recordList){
                        if (newList != null && newList.size() > 0) {
                            if (record.getPartyAId() != newList.get(0).getPartyAId()) {
                                return "游戏id=" + gameId + "的礼包组已添加，请换一款游戏";
                            }
                        } else {
                            return "游戏id=" + gameId + "的礼包组已添加，请换一款游戏";
                        }
                    }
                }
            }
        }
        return "success";
    }

    /**
     * 操作上下架
     *
     * @param idNameTypeDicDto 字典Dto
     * @param userId           用户id
     * @return 结果
     */
    @Override
    public R operation(IdNameTypeDicDto idNameTypeDicDto, Long userId) {
        // 数据存在 我们开始进行数据更新
        Integer[] typeList = new Integer[]{1, 2};
        Optional<Integer> operationPlatformAny = Arrays.stream(typeList).filter(a -> a.equals(idNameTypeDicDto.getType())).findAny();
        if (!operationPlatformAny.isPresent()) {
            return R.fail("参数错误");
        }
        // 执行更新操作
        var updateChainWrapper = new LambdaUpdateChainWrapper<>(baseMapper)
            .eq(GiftManagement::getId, idNameTypeDicDto.getId())
            .set(GiftManagement::getIsUp, idNameTypeDicDto.getType());
        boolean update = updateChainWrapper.update();
        if (update) {
            return R.ok("修改成功");
        }
        return R.fail("数据修改失败");
    }

    /**
     * 查看礼包内容详情
     *
     * @param dto 礼包详情管理
     * @return 礼包内容详情
     */
    @Override
    public TableDataInfo<GiftInfoVo> getGiftContentsList(GiftManagementDetailDto dto) {
        QueryWrapper<GiftInfoVo> lqw = buildQueryContentsWrapper(dto);
        IPage<GiftInfoVo> result = baseMapper.getGiftContentsList(dto.build(), lqw);
        return TableDataInfo.build(result);
    }

    private QueryWrapper<GiftInfoVo> buildQueryContentsWrapper(GiftManagementDetailDto dto) {
        QueryWrapper<GiftInfoVo> lqw = Wrappers.query();
        lqw.eq("i.del_flag", UserConstants.USER_NORMAL).eq("m.del_flag", UserConstants.USER_NORMAL)
            .eq(dto.getGiftGroupId() != null, "m.id", dto.getGiftGroupId())
            .eq(StringUtils.isNotBlank(dto.getGiftGroupName()), "m.gift_group_name", dto.getGiftGroupName());

        return lqw;
    }

    /**
     * 批量删除礼包组管理的同时，删除对应的礼包详情和游戏关联表
     *
     * @param ids 需要删除的礼包管理主键
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R deleteWithValidByIds(Collection<Integer> ids, Boolean isValid) {
        // 删除礼包组
        List<GiftManagementVo> list = baseMapper.selectVoById(ids);

        int i = baseMapper.deleteBatchIds(ids);

        for (GiftManagementVo vo : list) {
            // 删除礼包
            int j = infoMapper.delete(new LambdaQueryWrapper<GiftInfo>().eq(GiftInfo::getGiftGroupName, vo.getGiftGroupName()));
        }

        // 删除游戏关联表的信息
        for(Integer id : ids) {
            iGameUseRecordService.deleteWithValidByIdAndType(id, GameUseRecordTypeEnum.GRADE_GIFTG.getCode());
        }
        return R.ok("删除成功");
    }
}
