package com.euler.sdk.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.euler.common.core.constant.Constants;
import com.euler.common.core.domain.R;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.mybatis.core.page.PageQuery;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.common.satoken.utils.LoginHelper;
import com.euler.sdk.api.domain.GoodsVo;
import com.euler.sdk.api.enums.RechargeTypeEnum;
import com.euler.sdk.domain.bo.MemberRightsReceiveRecordBo;
import com.euler.sdk.domain.entity.Goods;
import com.euler.sdk.domain.entity.MemberRightsReceiveRecord;
import com.euler.sdk.domain.vo.MemberRightsReceiveRecordVo;
import com.euler.sdk.mapper.GoodsMapper;
import com.euler.sdk.mapper.MemberRightsReceiveRecordMapper;
import com.euler.sdk.service.IMemberRightsReceiveRecordService;
import com.euler.sdk.service.IWalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 会员权益领取记录Service业务层处理
 *
 * @author euler
 * @date 2022-04-08
 */
@RequiredArgsConstructor
@Service
public class MemberRightsReceiveRecordServiceImpl implements IMemberRightsReceiveRecordService {

    private final MemberRightsReceiveRecordMapper baseMapper;

    private final GoodsMapper goodsMapper;

    @Autowired
    private final IWalletService walletService;

    /**
     * 查询会员权益领取记录
     *
     * @param id 会员权益领取记录主键
     * @return 会员权益领取记录
     */
    @Override
    public MemberRightsReceiveRecordVo queryById(Integer id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询会员权益领取记录列表
     *
     * @param bo 会员权益领取记录
     * @return 会员权益领取记录
     */
    @Override
    public TableDataInfo<MemberRightsReceiveRecordVo> queryPageList(MemberRightsReceiveRecordBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<MemberRightsReceiveRecord> lqw = buildQueryWrapper(bo);
        Page<MemberRightsReceiveRecordVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询会员权益领取记录列表
     *
     * @param bo 会员权益领取记录
     * @return 会员权益领取记录
     */
    @Override
    public List<MemberRightsReceiveRecordVo> queryList(MemberRightsReceiveRecordBo bo) {
        LambdaQueryWrapper<MemberRightsReceiveRecord> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<MemberRightsReceiveRecord> buildQueryWrapper(MemberRightsReceiveRecordBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<MemberRightsReceiveRecord> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getType()), MemberRightsReceiveRecord::getType, bo.getType());
        return lqw;
    }

    /**
     * 新增会员权益领取记录
     *
     * @param bo 会员权益领取记录
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R insertByBo(MemberRightsReceiveRecordBo bo) {
        MemberRightsReceiveRecord add = BeanUtil.toBean(bo, MemberRightsReceiveRecord.class);
        // 查询商品里可领取的平台币信息
        LambdaQueryWrapper<Goods> goodLqw = Wrappers.lambdaQuery();
        goodLqw.eq(Goods::getGoodsType, Constants.GOODS_TYPE_1);
        goodLqw.eq(StringUtils.isNotBlank(bo.getMemberLevel()), Goods::getGoodsLevel, bo.getMemberLevel());
        goodLqw.eq(Goods::getIsUp, Constants.GOODS_TYPE_1);
        goodLqw.last("limit 1");
        GoodsVo goodsvo = goodsMapper.selectVoOne(goodLqw);

        if (ObjectUtil.isNull(goodsvo)) {
            return R.fail("该年费商品已下架");
        }

        Integer year = DateUtil.year(new Date());
        Integer month = DateUtil.month(new Date());
        // 获取礼包的领取状态信息
        LambdaQueryWrapper<MemberRightsReceiveRecord> lqw = Wrappers.lambdaQuery();
        lqw.eq(MemberRightsReceiveRecord::getMemberId, LoginHelper.getUserId());
        lqw.eq(MemberRightsReceiveRecord::getType, bo.getType());
        lqw.eq(MemberRightsReceiveRecord::getCurrentYear, year);
        lqw.eq(StringUtils.equals(add.getType(), "1"), MemberRightsReceiveRecord::getCurrentMonth, month);
        lqw.last("limit 1");

        MemberRightsReceiveRecordVo recordVo = baseMapper.selectVoOne(lqw);
        // 判断前一年是否领取过立即领取平台币的福利
        if (StringUtils.equals(add.getType(), "2")) {
            LambdaQueryWrapper<MemberRightsReceiveRecord> lqw2 = Wrappers.lambdaQuery();
            lqw2.eq(MemberRightsReceiveRecord::getMemberId, LoginHelper.getUserId());
            lqw2.eq(MemberRightsReceiveRecord::getType, bo.getType());
            lqw2.eq(MemberRightsReceiveRecord::getCurrentYear, year - 1);
            lqw2.last("limit 1");

            MemberRightsReceiveRecordVo beforeRecordVo = baseMapper.selectVoOne(lqw2);
            // 判断会员权益礼包是否已领取
            if (ObjectUtil.isNotNull(recordVo) && ObjectUtil.isNotNull(beforeRecordVo)) {
                return R.fail("该会员权益礼包已领取");
            } else {
                if (ObjectUtil.isNotNull(beforeRecordVo)) {
                    if(beforeRecordVo.getCurrentMonth() < month){
                        return R.fail("该会员权益礼包已领取");
                    }
                }
                if (ObjectUtil.isNotNull(recordVo)) {
                    return R.fail("该会员权益礼包已领取");
                }
            }
        } else {
            // 判断会员权益礼包是否已领取
            if (ObjectUtil.isNotNull(recordVo)) {
                return R.fail("该会员权益礼包已领取");
            }
        }

        String result = validEntityBeforeSave(add);
        if (!result.equals("success")) {
            return R.fail(result);
        }

        // 是否更新的flag
        Boolean updateFlag = true;
        Integer num = 0;
        // 平台币
        if (StringUtils.equals(add.getType(), "1") || StringUtils.equals(add.getType(), "2")) {
            // 当月领取平台币
            if (StringUtils.equals(add.getType(), "1")) {
                num = goodsvo.getEveryMonthPlatformCurrency();
                add.setReceivePlatformCurrency(num);
            } else {
                // 立即领取的平台币
                num = goodsvo.getPlatformCurrency();
                add.setReceivePlatformCurrency(num);
            }

            add.setCurrentYear(year);
            add.setCurrentMonth(month);
            Boolean flag = baseMapper.insert(add) > 0;

            if (flag && updateFlag) {
                bo.setId(add.getId());
                // 将领取的平台币和积分，更新到钱包
                RechargeTypeEnum rechargeTypeEnum = RechargeTypeEnum.find(3);
                walletService.modifyWallet(LoginHelper.getUserId(),LoginHelper.getSdkChannelPackage().getGameId(), 1,Convert.toLong(num), rechargeTypeEnum, 1, "会员权益领取");
            }
            return R.ok("新增成功");
        }
        return R.fail("新增失败");
    }

    /**
     * 保存前的数据校验
     *
     * @param entity 实体类数据
     */
    private String validEntityBeforeSave(MemberRightsReceiveRecord entity) {
        //TODO 做一些数据校验,如唯一约束
        return "success";
    }


    /**
     * 查询当前用户是否已经领取过
     *
     * @param userId
     * @param type
     * @param year
     * @param month
     * @return
     */
    public Boolean selectUserIsReceive(Long userId, Integer year, Integer month, Integer type) {
        LambdaQueryWrapper<MemberRightsReceiveRecord> eq = Wrappers.<MemberRightsReceiveRecord>lambdaQuery()
            .eq(MemberRightsReceiveRecord::getMemberId, userId)
            .eq(!type.equals(0), MemberRightsReceiveRecord::getType, type)
            .eq(!year.equals(0), MemberRightsReceiveRecord::getCurrentYear, year)
            .eq(!month.equals(0), MemberRightsReceiveRecord::getCurrentMonth, month);

        return baseMapper.exists(eq);
    }

}
