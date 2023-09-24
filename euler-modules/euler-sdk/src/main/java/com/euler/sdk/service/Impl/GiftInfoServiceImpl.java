package com.euler.sdk.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.euler.common.core.constant.Constants;
import com.euler.common.core.domain.R;
import com.euler.common.core.exception.ServiceException;
import com.euler.common.core.utils.JsonUtils;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.euler.common.satoken.utils.LoginHelper;
import com.euler.sdk.api.domain.GameUserManagement;
import com.euler.sdk.domain.dto.GiftInfoDto;
import com.euler.sdk.domain.entity.*;
import com.euler.sdk.api.domain.GameUserManagementVo;
import com.euler.sdk.mapper.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.stereotype.Service;
import com.euler.sdk.domain.bo.GiftInfoBo;
import com.euler.sdk.domain.vo.GiftInfoVo;
import com.euler.sdk.service.IGiftInfoService;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 礼包Service业务层处理
 *
 * @author euler
 * @date 2022-03-24
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class GiftInfoServiceImpl implements IGiftInfoService {

    private final GiftInfoMapper baseMapper;

    private final GiftReceiveRecordMapper recordMapper;

    private final GameUserManagementMapper gameUserManagementMapper;

    private final GiftManagementMapper managementMapper;

    /**
     * 查询礼包详情
     *
     * @param id 礼包主键
     * @return 礼包详情
     */
    @Override
    public GiftInfoVo queryById(Integer id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询礼包列表
     *
     * @param dto 礼包dto
     * @return 礼包列表
     */
    @Override
    public TableDataInfo<GiftInfoVo> queryPageList(GiftInfoDto dto) {
        if(ObjectUtil.isNull(dto.getGiftGroupId())){
            throw new ServiceException("礼包组Id参数缺失");
        }
        // 游戏id
        Integer gameId = LoginHelper.getSdkChannelPackage().getGameId();
        if (ObjectUtil.isNull(gameId)) {
            throw new ServiceException("该游戏不存在");
        }
        // 游戏角色
        String gameRoleId = LoginHelper.getSdkChannelPackage().getGameRoleId();
        // 游戏区服
        String gameServerId = LoginHelper.getSdkChannelPackage().getGameServerId();
        if (ObjectUtil.isNull(gameRoleId) || ObjectUtil.isNull(gameServerId)) {
            throw new ServiceException("该游戏的角色信息未上报，请先上报角色信息");
        }
        LambdaQueryWrapper<GiftInfo> lqw = buildQueryWrapper(dto);
        Page<GiftInfoVo> result = baseMapper.selectVoPage(dto.build(), lqw);
        log.info("礼包列表信息，{}", JsonUtils.toJsonString(result));

        result.getRecords().forEach(row -> {
            // 设置礼包领取状态
           boolean existsRecord = recordMapper.exists(new LambdaQueryWrapper<GiftReceiveRecord>()
                .eq(GiftReceiveRecord::getGiftType, Constants.GIFT_TYPE_GRADE)
                .eq(GiftReceiveRecord::getMemberId, LoginHelper.getUserId())
                .eq(GiftReceiveRecord::getGiftId, row.getId()));

            if (existsRecord) {
                row.setReceiveStatus(Constants.RECEIVE_STATUS_YES);
            } else {
                // 获取礼包等级信息
//                GiftInfoVo giftInfoVo = baseMapper.selectVoById(row.getId());
//                if (ObjectUtil.isNull(giftInfoVo)) {
//                    throw new ServiceException("该礼包不存在");
//                }

                // 根据游戏id查询游戏信息
                LambdaQueryWrapper<GameUserManagement> gameLqw = Wrappers.lambdaQuery();
                gameLqw.eq(GameUserManagement::getMemberId, LoginHelper.getUserId());
                gameLqw.eq(GameUserManagement::getGameId, gameId);
                gameLqw.eq(StringUtils.isNotBlank(gameRoleId), GameUserManagement::getRoleId, gameRoleId);
                gameLqw.eq(StringUtils.isNotBlank(gameServerId), GameUserManagement::getServerId, gameServerId);
                gameLqw.orderByDesc(GameUserManagement::getId).last("limit 1");
                GameUserManagementVo gameUserManagementVo = gameUserManagementMapper.selectVoOne(gameLqw);

                if (ObjectUtil.isNull(gameUserManagementVo)) {
                    throw new ServiceException("该游戏不存在");
                }

                // 判断游戏玩家等级是否达到可领取等级
                if (Convert.toInt(gameUserManagementVo.getRoleLevel()) < row.getReceiveGrade()) {
                    row.setReceiveStatus(Constants.RECEIVE_STATUS_NOREACH);
                } else {
                    row.setReceiveStatus(Constants.RECEIVE_STATUS_NO);
                }
            }
         });

        return TableDataInfo.build(result);
    }

    private LambdaQueryWrapper<GiftInfo> buildQueryWrapper(GiftInfoDto dto) {
        LambdaQueryWrapper<GiftInfo> lqw = Wrappers.lambdaQuery();
        lqw.likeRight(StringUtils.isNotBlank(dto.getGiftName()), GiftInfo::getGiftName, dto.getGiftName());
        lqw.eq(dto.getGiftGroupId() != null, GiftInfo::getGiftGroupId, dto.getGiftGroupId());
        lqw.eq(dto.getReceiveGrade() != null, GiftInfo::getReceiveGrade, dto.getReceiveGrade());
        lqw.eq(StringUtils.isNotBlank(dto.getType()), GiftInfo::getType, dto.getType());
        return lqw;
    }

    /**
     * 新增礼包
     *
     * @param bo 礼包bo
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R insertByBo(GiftInfoBo bo) {
        GiftInfo add = BeanUtil.toBean(bo, GiftInfo.class);
        log.info("添加的字段列表->" + JsonUtils.toJsonString(add));
        String result = validEntityBeforeSave(add);
        if (!result.equals("success")) {
            return R.fail(result);
        }

        // 新增礼包
        boolean flag = baseMapper.insert(add) > 0;

        if (flag) {
            // 更新礼包组信息
            updateGiftGroupInfo(add.getGiftGroupId());

            return R.ok("新增礼包成功");
        }
        return R.fail("新增礼包失败！");
    }

    /**
     * 更新礼包组信息
     * @param giftGroupId 礼包组id
     */
    private boolean updateGiftGroupInfo(Integer giftGroupId){
        // 根据礼包组id查询礼包的数量
        int giftAmount = baseMapper.giftInfoListCount(giftGroupId);

        // 查询礼包列表
        LambdaQueryWrapper<GiftInfo> lqw = Wrappers.lambdaQuery();
        lqw.eq(GiftInfo::getGiftGroupId, giftGroupId);
        lqw.eq(GiftInfo::getDelFlag, Constants.COMMON_STATUS_NO);
        List<GiftInfoVo> list = baseMapper.selectVoList(lqw);

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
        var updateChainWrapper = new LambdaUpdateChainWrapper<>(managementMapper)
            .eq(GiftManagement::getId, giftGroupId)
            .set(GiftManagement::getGiftAmount, giftAmount)
            .set(GiftManagement::getReceiveGrade, receiveGrade);
        // 更新礼包组信息
        return updateChainWrapper.update();
    }

    /**
     * 修改礼包
     *
     * @param bo 礼包bo
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R updateByBo(GiftInfoBo bo) {
        // 验证是否传输过来主键
        if (bo.getId() == null || bo.getId() <= 0) {
            return R.fail("参数缺失");
        }
        GiftInfo update = BeanUtil.toBean(bo, GiftInfo.class);
        String result = validEntityBeforeSave(update);
        if (!result.equals("success")) {
            return R.fail(result);
        }

        int i = baseMapper.updateById(update);
        if (i > 0) {
            // 更新礼包组信息
            updateGiftGroupInfo(update.getGiftGroupId());

            return R.ok();
        }
        return R.fail("修改失败");
    }

    /**
     * 保存前的数据校验
     *
     * @param entity 实体类数据
     */
    private String validEntityBeforeSave(GiftInfo entity){
        if(ObjectUtil.isNull(entity.getGiftGroupName())){
            throw new ServiceException("礼包组名不能为空");
        }
        return "success";
    }

    /**
     * 批量删除礼包
     *
     * @param ids 需要删除的礼包主键
     * @param isValid 是否校验,true-删除前校验,false-不校验
     * @param giftGroupId 礼包组id
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R deleteWithValidByIds(Collection<Integer> ids, Boolean isValid, Integer giftGroupId) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        int i = baseMapper.deleteBatchIds(ids);
        if (i > 0) {
            // 更新礼包组数据
            updateGiftGroupInfo(giftGroupId);
            return R.ok("删除成功");
        }
        return R.fail("删除失败");
    }

}
