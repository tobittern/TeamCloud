package com.euler.sdk.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.euler.common.core.constant.Constants;
import com.euler.common.core.domain.R;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.common.satoken.utils.LoginHelper;
import com.euler.sdk.api.domain.GameUserManagement;
import com.euler.sdk.api.domain.GameUserManagementVo;
import com.euler.sdk.api.enums.RechargeTypeEnum;
import com.euler.sdk.domain.bo.GiftReceiveRecordBo;
import com.euler.sdk.domain.dto.GiftReceiveRecordDto;
import com.euler.sdk.domain.entity.GiftReceiveRecord;
import com.euler.sdk.domain.vo.*;
import com.euler.sdk.mapper.*;
import com.euler.sdk.service.IGiftActivityService;
import com.euler.sdk.service.IGiftReceiveRecordService;
import com.euler.sdk.service.IWalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 礼包领取记录Service业务层处理
 *
 * @author euler
 * @date 2022-04-13
 */
@RequiredArgsConstructor
@Service
public class GiftReceiveRecordServiceImpl implements IGiftReceiveRecordService {

    private final GiftReceiveRecordMapper baseMapper;

    private final GiftInfoMapper giftInfoMapper;

    private final GiftActivityMapper giftActivityMapper;

    private final GameUserManagementMapper gameUserManagementMapper;

    @Autowired
    private IGiftActivityService iGiftActivityService;

    @Autowired
    private final IWalletService walletService;

    /**
     * 查询礼包领取记录
     *
     * @param id 礼包领取记录主键
     * @return 礼包领取记录
     */
    @Override
    public GiftReceiveRecordVo queryById(Integer id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询礼包领取记录列表
     *
     * @param dto 礼包领取记录
     * @return 礼包领取记录
     */
    @Override
    public TableDataInfo<GiftReceiveRecordVo> queryPageList(GiftReceiveRecordDto dto) {
        LambdaQueryWrapper<GiftReceiveRecord> lqw = buildQueryWrapper(dto);
        Page<GiftReceiveRecordVo> result = baseMapper.selectVoPage(dto.build(), lqw);
        return TableDataInfo.build(result);
    }

    private LambdaQueryWrapper<GiftReceiveRecord> buildQueryWrapper(GiftReceiveRecordDto dto) {
        LambdaQueryWrapper<GiftReceiveRecord> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(dto.getGiftType()), GiftReceiveRecord::getGiftType, dto.getGiftType());
        lqw.eq(dto.getMemberId() != null, GiftReceiveRecord::getMemberId, dto.getMemberId());
        lqw.eq(StringUtils.isNotBlank(dto.getReceiveStatus()), GiftReceiveRecord::getReceiveStatus, dto.getReceiveStatus());
        lqw.eq(dto.getGiftId() != null, GiftReceiveRecord::getGiftId, dto.getGiftId());
        return lqw;
    }

    /**
     * 新增礼包领取记录
     *
     * @param bo 礼包领取记录
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R insertByBo(GiftReceiveRecordBo bo) {
        String result = validEntityBeforeSave(bo);
        if (!result.equals("success")) {
            return R.fail(result);
        }
        GiftReceiveRecord add = BeanUtil.toBean(bo, GiftReceiveRecord.class);
        // 设置状态为已领取
        add.setReceiveStatus(Constants.RECEIVE_STATUS_YES);
        // 礼包领取后，把领取记录新增到表里
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());

            // 礼包类型
            Integer type = 0;
            // 领取数量
            Long num = 0L;
            // 将领取的平台币/余额/积分更新到钱包里面
            if (bo.getGiftType().equals(Constants.GIFT_TYPE_GRADE)) {
                GiftInfoVo giftInfoVo = giftInfoMapper.selectVoById(bo.getGiftId());
                type = Convert.toInt(giftInfoVo.getType());
                num = Convert.toLong(giftInfoVo.getRewardAmount());
            } else {
                GiftActivityVo giftActivityVo = giftActivityMapper.selectVoById(bo.getGiftId());
                type = giftActivityVo.getGiftRewardType();
                num = Convert.toLong(giftActivityVo.getGiftRewardNums());
            }

            // 礼包类型 1 平台币 2余额  3积分
            int rechargeType = 0;
            switch (type) {
                case 1:
                    rechargeType = 3;
                    break;
                case 2:
                    rechargeType = 2;
                    break;
                case 3:
                    rechargeType = 1;
                    break;
                default:
                    rechargeType = 4;
                    break;
            }
            RechargeTypeEnum rechargeTypeEnum = RechargeTypeEnum.find(Convert.toInt(rechargeType));
            walletService.modifyWallet(add.getMemberId(),LoginHelper.getSdkChannelPackage().getGameId(), 2, num, rechargeTypeEnum, 1, "礼包领取");

            return R.ok("领取成功");
        }
        return R.fail("领取失败");
    }

    /**
     * 保存前的数据校验
     *
     * @param bo 实体类数据
     */
    private String validEntityBeforeSave(GiftReceiveRecordBo bo) {
        Integer gameId = LoginHelper.getSdkChannelPackage().getGameId();
        if (gameId == null) {
            return "该游戏不存在";
        }
        // 游戏角色
        String gameRoleId = LoginHelper.getSdkChannelPackage().getGameRoleId();
        // 游戏区服
        String gameServerId = LoginHelper.getSdkChannelPackage().getGameServerId();
        if (ObjectUtil.isNull(gameRoleId) || ObjectUtil.isNull(gameServerId)) {
            return "角色信息未上报，请先上报角色信息";
        }
        // 根据游戏id查询游戏信息
        LambdaQueryWrapper<GameUserManagement> lqw = Wrappers.lambdaQuery();
        lqw.eq(GameUserManagement::getMemberId, LoginHelper.getUserId());
        lqw.eq(GameUserManagement::getGameId, gameId);
        lqw.eq(StringUtils.isNotBlank(gameRoleId), GameUserManagement::getRoleId, gameRoleId);
        lqw.eq(StringUtils.isNotBlank(gameServerId), GameUserManagement::getServerId, gameServerId);
        lqw.orderByDesc(GameUserManagement::getId).last("limit 1");
        GameUserManagementVo gameUserManagementVo = gameUserManagementMapper.selectVoOne(lqw);
        // 如果是等级礼包的话，判断是否达到游戏玩家等级
        if (bo.getGiftType().equals(Constants.GIFT_TYPE_GRADE)) {

            // 获取礼包的领取状态信息
            LambdaQueryWrapper<GiftReceiveRecord> recordLqw = Wrappers.lambdaQuery();
            recordLqw.eq(GiftReceiveRecord::getGiftType, Constants.GIFT_TYPE_GRADE);
            recordLqw.eq(GiftReceiveRecord::getGiftId, bo.getGiftId());
            recordLqw.eq(GiftReceiveRecord::getMemberId, LoginHelper.getUserId());
            recordLqw.orderByDesc(GiftReceiveRecord::getId).last("limit 1");
            GiftReceiveRecordVo recordVo = baseMapper.selectVoOne(recordLqw);
            // 判断等级礼包是否已领取
            if (ObjectUtil.isNotNull(recordVo)) {
                if (StringUtils.equals(Constants.RECEIVE_STATUS_YES, recordVo.getReceiveStatus())) {
                    return "该等级礼包已领取";
                }
            }
            // 获取礼包等级信息
            GiftInfoVo giftInfoVo = giftInfoMapper.selectVoById(bo.getGiftId());
            // 判断游戏玩家等级是否达到可领取等级
            if(giftInfoVo != null) {
                if (Convert.toInt(gameUserManagementVo.getRoleLevel()) < giftInfoVo.getReceiveGrade()) {
                    return "游戏玩家等级未达到礼包可领取等级, 不可领取礼包";
                }
            }
        } else {
            // 判断活动礼包是否满足领取条件
            GiftActivityVo giftActivityVo = giftActivityMapper.selectVoById(bo.getGiftId());
            // 获取相关数据
            String packageCode = LoginHelper.getSdkChannelPackage().getPackageCode();
            // TODO 判断是否达到领取资格 我们有可能需要查询到同一款游戏  不同角色 的时候 都达到了一个领取条件  可能都能进行领取 现在这个地方是只能领取一个
            // 如果都能领取 那么 可能需要从游戏上报中获取数据
            Long userId = LoginHelper.getUserId();
            GiftActivityMiddlerVo giftNeedData = iGiftActivityService.getGiftNeedData(userId, gameId, packageCode);
            // 检查是否拥有领取资格
            Boolean checkIsReceive = iGiftActivityService.checkIsReceive(giftActivityVo, giftNeedData.getMyGameVo(), gameId);
            if (!checkIsReceive) {
                return "你当前未达到领取的资格";
            }
            // 检查是否领取过了
            Boolean checkIsAlreadyReceive = iGiftActivityService.checkIsAlreadyReceive(bo.getGiftId(), giftNeedData.getIds());
            if (checkIsAlreadyReceive) {
                return "当前礼包您已经领取过了";
            }
        }

        return "success";
    }

    /**
     * 获取指定用户获取指定类型礼包的列表
     *
     * @param type
     * @param userId
     * @return
     */
    public ArrayList<Integer> selectUserAlreadyList(String type, Long userId) {
        LambdaQueryWrapper<GiftReceiveRecord> eq = Wrappers.<GiftReceiveRecord>lambdaQuery()
            .select(GiftReceiveRecord::getId, GiftReceiveRecord::getGiftId)
            .eq(GiftReceiveRecord::getMemberId, userId)
            .eq(GiftReceiveRecord::getGiftType, type);
        List<GiftReceiveRecord> giftReceiveRecords = baseMapper.selectList(eq);
        // 循环进行赋值操作
        ArrayList<Integer> returnList = new ArrayList<>();
        giftReceiveRecords.forEach(a -> {
            returnList.add(a.getGiftId());
        });
        return returnList;
    }


}
