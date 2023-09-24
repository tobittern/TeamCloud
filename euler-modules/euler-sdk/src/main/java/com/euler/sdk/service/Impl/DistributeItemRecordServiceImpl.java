package com.euler.sdk.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import com.euler.common.core.constant.MemberConstants;
import com.euler.common.core.domain.R;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.euler.sdk.api.domain.GoodsVo;
import com.euler.sdk.api.domain.MemberDetailVo;
import com.euler.sdk.domain.dto.DistributeItemRecordDto;
import com.euler.sdk.api.enums.RechargeTypeEnum;
import com.euler.sdk.domain.entity.MemberRights;
import com.euler.sdk.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.euler.sdk.domain.bo.DistributeItemRecordBo;
import com.euler.sdk.domain.vo.DistributeItemRecordVo;
import com.euler.sdk.domain.entity.DistributeItemRecord;
import com.euler.sdk.mapper.DistributeItemRecordMapper;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 派发物品记录Service业务层处理
 *
 * @author euler
 * @date 2022-04-09
 */
@RequiredArgsConstructor
@Service
public class DistributeItemRecordServiceImpl implements IDistributeItemRecordService {

    private final DistributeItemRecordMapper baseMapper;
    @Autowired
    private IWalletService walletService;
    @Autowired
    private IMemberProfileService memberProfileService;
    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private IMemberRightsService memberRightsService;

    /**
     * 查询派发物品记录
     *
     * @param id 派发物品记录主键
     * @return 派发物品记录
     */
    @Override
    public DistributeItemRecordVo queryById(Integer id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询派发物品记录列表
     *
     * @param dto 派发物品记录
     * @return 派发物品记录
     */
    @Override
    public TableDataInfo<DistributeItemRecordVo> queryPageList(DistributeItemRecordDto dto) {
        LambdaQueryWrapper<DistributeItemRecord> lqw = buildQueryWrapper(dto);
        Page<DistributeItemRecordVo> result = baseMapper.selectVoPage(dto.build(), lqw);
        return TableDataInfo.build(result);
    }

    private LambdaQueryWrapper<DistributeItemRecord> buildQueryWrapper(DistributeItemRecordDto dto) {
        LambdaQueryWrapper<DistributeItemRecord> lqw = Wrappers.lambdaQuery();
        lqw.eq(dto.getMemberId() != null, DistributeItemRecord::getMemberId, dto.getMemberId());
        lqw.eq(StringUtils.isNotBlank(dto.getDistributeType()), DistributeItemRecord::getDistributeType, dto.getDistributeType());
        return lqw;
    }

    /**
     * 新增派发物品记录
     *
     * @param bo 派发物品记录
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R insertByBo(DistributeItemRecordBo bo) {
        DistributeItemRecord add = BeanUtil.toBean(bo, DistributeItemRecord.class);
        if (baseMapper.insert(add) > 0) {
            if (updateDistributeData(add)) {
                bo.setId(add.getId());
                return R.ok("派发物品成功");
            }
        }
        return R.fail("派发物品失败");
    }

    /**
     * 修改派发物品记录
     *
     * @param bo 派发物品记录
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R updateByBo(DistributeItemRecordBo bo) {
        DistributeItemRecord update = BeanUtil.toBean(bo, DistributeItemRecord.class);
        if (baseMapper.updateById(update) > 0) {
            if (updateDistributeData(update)) {
                return R.ok("修改成功");
            }
        }
        return R.fail("修改失败");
    }

    /**
     * 更新派发物品
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateDistributeData(DistributeItemRecord entity) {
        Boolean retBFlag= false;
        // 派发类型是年卡以外的，更新钱包(派发类型 1:积分 2:余额 3:平台币 4:成长值)
        if (entity.getDistributeType() != null && !StringUtils.equals("5", entity.getDistributeType())) {
            Boolean walletFlag = updateWallet(entity);
            if (walletFlag) {
                retBFlag = true;
            }
        } else if (StringUtils.equals("5", entity.getDistributeType())) {
            // 获取当前用户详细信息
            MemberDetailVo memberDetailVo = memberProfileService.getMemberDetailByMemberId(entity.getMemberId());
            // 获取年卡商品信息
            GoodsVo goodsVo = goodsService.queryById(entity.getGoodsId());
            if (memberDetailVo != null && goodsVo != null) {
                // 相同级别的年卡增加日期，不同级别的年卡，比原等级高的直接升，比原等级低的不处理
                if(memberDetailVo.getMemberRightsLevel() == null || (goodsVo.getGoodsLevel() != null && Convert.toInt(memberDetailVo.getMemberRightsLevel()) <= Convert.toInt(goodsVo.getGoodsLevel()))) {
                    // 更新会员权益信息
                    MemberRights memberRights = BeanUtil.toBean(memberDetailVo, MemberRights.class);
                    memberRights.setId(memberDetailVo.getMemberId());
                    memberRights.setGoodsId(entity.getGoodsId());
                    memberRights.setName(goodsVo.getGoodsName());
                    // 会员等级
                    memberRights.setLever(goodsVo.getGoodsLevel());
                    // 会员权益状态 0:未开通，1：已生效，2：已失效
                    memberRights.setStatus(MemberConstants.MEMBER_STATUS_NORMAL);
                    if(memberDetailVo.getMemberRightsLevel() == null || Convert.toInt(memberDetailVo.getMemberRightsLevel()) < Convert.toInt(goodsVo.getGoodsLevel())) {
                        memberRights.setIsUpgrade(MemberConstants.IS_UPGRADE_YES);
                    } else if(Convert.toInt(memberDetailVo.getMemberRightsLevel()) == Convert.toInt(goodsVo.getGoodsLevel())) {
                        memberRights.setIsUpgrade(MemberConstants.IS_UPGRADE_NO);
                    }
                    // 会员有效期：派发日到下一年
                    memberRights.setValidateStartTime(new Date());
                    memberRights.setValidateEndTime(DateUtil.offset(new Date(), DateField.YEAR, 1));
                    memberRights.setGoodsPrice(goodsVo.getGoodsPrice());
                    // 免费派发
                    memberRights.setPayPrice(new BigDecimal(0));
                    // 判断是否已开通会员
                    if(memberDetailVo.getMemberRightsLevel() == null || StringUtils.equals("0", memberDetailVo.getMemberRightsLevel())) {
                        // 开通会员
                        R ret = memberRightsService.openMember(memberRights);
                        if(ret.getCode() == 200) {
                            retBFlag = true;
                        }
                    } else {
                        // 更新会员权益
                        Boolean flag = memberRightsService.updateMemberRightsDetail(memberRights);
                        if(flag) {
                            retBFlag = true;
                        }
                    }
                }
            }
        }
        return retBFlag;
    }

    /**
     * 更新钱包
     *
     * @param entity 实体类数据
     */
    private Boolean updateWallet(DistributeItemRecord entity) {

        Number num =Convert.toNumber(entity.getDistributeAmount(),0);
        // 获取钱包详情
        // 验证派发物品的数量，不能超过钱包里的数量
        RechargeTypeEnum rechargeTypeEnum = RechargeTypeEnum.find(Convert.toInt(entity.getDistributeType()));
        Boolean walletFlag = walletService.modifyWallet(entity.getMemberId(),null,1, num, rechargeTypeEnum, 1, "管理员派发物品");

        return walletFlag;
    }

}
