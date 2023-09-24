package com.euler.sdk.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.euler.common.core.constant.MemberConstants;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdNameTypeDicDto;
import com.euler.common.core.enums.UserTypeEnum;
import com.euler.common.core.exception.ServiceException;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.common.satoken.utils.LoginHelper;
import com.euler.sdk.api.domain.GoodsVo;
import com.euler.sdk.domain.bo.GoodsBo;
import com.euler.sdk.domain.dto.GoodsDto;
import com.euler.sdk.domain.entity.Goods;
import com.euler.sdk.domain.entity.MemberRights;
import com.euler.sdk.api.domain.MemberDetailVo;
import com.euler.sdk.mapper.GoodsMapper;
import com.euler.sdk.service.IGoodsService;
import com.euler.sdk.service.IMemberProfileService;
import com.euler.sdk.service.IMemberRightsReceiveRecordService;
import com.euler.sdk.service.IMemberRightsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * 商品Service业务层处理
 *
 * @author euler
 * @date 2022-03-21
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class GoodsServiceImpl implements IGoodsService {

    private final GoodsMapper baseMapper;
    @Autowired
    private IMemberProfileService iMemberProfileService;
    @Autowired
    private IMemberRightsService memberRightsService;
    @Autowired
    private IMemberRightsReceiveRecordService iMemberRightsReceiveRecordService;

    /**
     * 根据id查询商品
     *
     * @param id 商品主键
     * @return 商品
     */
    @Override
    public GoodsVo queryById(Integer id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询商品
     *
     * @param id 商品主键
     * @return 商品
     */
    @Override
    public GoodsVo queryById(Integer id, Integer type, Long userId) {
        GoodsVo goodsVo = baseMapper.selectVoById(id);

        if (goodsVo == null)
            throw new ServiceException("当前商品不存在或已下架");


        // 判断商品类型 是否拥有指定权限
        if (goodsVo != null && goodsVo.getGoodsType().equals(1)) {
            // 校验商品 是否上线
            if (goodsVo.getIsUp().equals(2)) {
                throw new ServiceException("当前商品尚未上架");
            }
            // 如果商品是限时商品 判断当前商品是否在限时区间内
            if (goodsVo.getIsLimitTime().equals(1) && !DateUtil.isIn(new Date(), goodsVo.getLimitTimeStart(), goodsVo.getLimitTimeEnd())) {
                throw new ServiceException("当前商品不在购买时间范围之内");
            }
            // 判断获取的商品类型 如果获取的商品是年费类型的商品 我们需要判断当前用户是否购买过当前年费商品
            MemberDetailVo memberDetailByMemberId = iMemberProfileService.getMemberDetailByMemberId(userId);
            if (memberDetailByMemberId != null && memberDetailByMemberId.getMemberRightsLevel() != null && memberDetailByMemberId.getMemberRightsLevel().equals(goodsVo.getGoodsLevel())) {
                goodsVo.setMemberRightsHave(1);
            }
            // 查询当前用户是否已经存在年费会员了 如果存在我们需要从新计算一下商品的价格 获取最新的一条数据

            MemberRights memberRightsDetailVo = memberRightsService.getById(userId);
            // 只要下单时候查询商品才需要判断 是否存在降级操作

            // 下单商品，有效期内，只能购买当前或者更高一级的会员
            if (type.equals(2) && memberRightsDetailVo != null && Convert.toInt(goodsVo.getGoodsLevel()) < (Convert.toInt(memberRightsDetailVo.getLever())) && MemberConstants.MEMBER_STATUS_NORMAL.equals(memberRightsDetailVo.getStatus())) {
                throw new ServiceException("只能购买当前或者更高一级的会员");
            }

            // 判断用户是否有会员权益
            if (memberRightsDetailVo != null
                && memberRightsDetailVo.getLever().equals(goodsVo.getGoodsLevel()) && MemberConstants.MEMBER_STATUS_NORMAL.equals(memberRightsDetailVo.getStatus())) {
                // 当前时进行升级操作
                // 原会员剩余月份
                long diffMonth = 12 - DateUtil.betweenMonth(memberRightsDetailVo.getValidateStartTime(), new Date(), false);
                //原会员每月价格
                var oldMonthPrice = NumberUtil.div(memberRightsDetailVo.getGoodsPrice(), 12);
                //原会员剩余金额
                var oldPrice = NumberUtil.mul(oldMonthPrice, diffMonth);
                // 升级价格
                BigDecimal diffPrice = NumberUtil.sub(goodsVo.getGoodsPrice(), oldPrice);

                if (diffPrice.compareTo(new BigDecimal(0)) == -1) {
                    throw new ServiceException("当前商品错误");
                }
                goodsVo.setGoodsDiscountPrice(diffPrice);
                // 查询当前用户是否已领领取过年费会员的每月给与东西
                Integer year = DateUtil.year(new Date());
                Integer month = DateUtil.month(new Date());
                Boolean aBoolean = iMemberRightsReceiveRecordService.selectUserIsReceive(userId,  year, month, 0);
                if (aBoolean) {
                    goodsVo.setRightsIsReceive(1);
                }
            } else {
                // 用户没有购买过年费商品我们将商品价格给与优惠价格
                goodsVo.setGoodsDiscountPrice(goodsVo.getGoodsPrice());
            }
        }
        return goodsVo;
    }

    /**
     * 查询商品列表
     *
     * @return 商品
     */
    @Override
    public TableDataInfo<GoodsVo> queryPageList(GoodsDto goodsDto) {
        LambdaQueryWrapper<Goods> lqw = buildQueryWrapper(goodsDto);
        Page<GoodsVo> result = baseMapper.selectVoPage(goodsDto.build(), lqw);
        if (goodsDto.getGoodsType().equals(1) && UserTypeEnum.SDK_USER.equals(LoginHelper.getUserType())) {
            // 判断获取的商品类型 如果获取的商品是年费类型的商品 我们需要判断当前用户是否购买过当前年费商品
            Long loginUserId = LoginHelper.getUserId();
            MemberDetailVo memberDetailByMemberId = iMemberProfileService.getMemberDetailByMemberId(loginUserId);
            if (memberDetailByMemberId != null && StringUtils.isNotEmpty(memberDetailByMemberId.getMemberRightsLevel())) {
                result.getRecords().forEach(a -> {
                    if (a.getGoodsLevel().equals(memberDetailByMemberId.getMemberRightsLevel())) {
                        a.setMemberRightsHave(1);
                    }
                });
            }
        }
        return TableDataInfo.build(result);
    }


    private LambdaQueryWrapper<Goods> buildQueryWrapper(GoodsDto dto) {
        LambdaQueryWrapper<Goods> lqw = Wrappers.lambdaQuery();
        lqw.eq(dto.getUserId() != null, Goods::getUserId, dto.getUserId());
        lqw.eq(dto.getGetType() != null, Goods::getGetType, dto.getGetType());
        lqw.eq(dto.getGoodsType() != null, Goods::getGoodsType, dto.getGoodsType());
        lqw.likeRight(StringUtils.isNotBlank(dto.getGoodsName()), Goods::getGoodsName, dto.getGoodsName());
        lqw.eq(dto.getIsLimitTime() != null, Goods::getIsLimitTime, dto.getIsLimitTime());
        lqw.eq(dto.getIsUp() != null, Goods::getIsUp, dto.getIsUp());
        lqw.orderByDesc(Goods::getSort);
        return lqw;
    }

    /**
     * 新增商品
     *
     * @param bo 商品
     * @return 结果
     */
    @Override
    public R insertByBo(GoodsBo bo) {
        Goods add = BeanUtil.toBean(bo, Goods.class);
        String s = validEntityBeforeSave(add);
        if (!s.equals("success")) {
            return R.fail(s);
        }
        // 判断当前添加的是否是年费商品  年费商品只能允许有3条 同事年费商品默认添加的时候是下架状态
        if (add.getGoodsType().equals(1)) {
            LambdaQueryWrapper<Goods> eq = Wrappers.<Goods>lambdaQuery().eq(Goods::getGoodsType, 1);
            Long aLong = baseMapper.selectCount(eq);
            if (aLong >= 3) {
                return R.fail("年费商品只能允许三条数据");
            }
            // 设置年费商品添加的默认值 - 默认是下架状态
            add.setIsUp(2);
        }
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
            return R.ok();
        }
        return R.fail("添加失败");
    }

    /**
     * 修改商品
     *
     * @param bo 商品
     * @return 结果
     */
    @Override
    public R updateByBo(GoodsBo bo) {
        Goods update = BeanUtil.toBean(bo, Goods.class);
        String s = validEntityBeforeSave(update);
        if (!s.equals("success")) {
            return R.fail(s);
        }
        if (baseMapper.updateById(update) > 0) {
            return R.ok();
        }
        return R.fail("更新失败");
    }

    /**
     * 批量删除礼包活动管理
     *
     * @param ids 需要删除的礼包活动管理主键
     * @return 结果
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Integer> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        int i = baseMapper.deleteBatchIds(ids);
        if (i > 0) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * 保存前的数据校验
     *
     * @param entity 实体类数据
     */
    private String validEntityBeforeSave(Goods entity) {
        //TODO 做一些数据校验,如唯一约束
        // 如果商品是限时的 那么限时的开始时间和结束时间就必须填写
        if (entity.getIsLimitTime().equals(1) && (entity.getLimitTimeStart() == null || entity.getLimitTimeEnd() == null)) {
            return "限时的开始时间和结束时间就必须填写";
        }
        // 会员类型的商品 - 判断给的会员时长是多久
        if (entity.getGetType().equals(1) && entity.getDuration() > 10000) {
            return "年费商品给与的会员时长不能超过10000";
        }
        // 如果是年费商品的时候 商品背景图不能为空
        if (entity.getGoodsType().equals(1) && entity.getGoodsBackgroud() == null) {
            return "年费商品的背景图不能为空";
        }
        return "success";
    }

    /**
     * 操作上下架
     *
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
            .eq(Goods::getId, idNameTypeDicDto.getId())
           // .eq(Goods::getUserId, userId)
            .set(Goods::getIsUp, idNameTypeDicDto.getType());
        boolean update = updateChainWrapper.update();
        if (update) {
            return R.ok("修改成功");
        }
        return R.fail("数据修改失败");
    }

    /**
     * 查询已上架的年卡商品列表
     *
     * @return 商品集合
     */
    @Override
    public List<GoodsVo> queryCardList() {
        LambdaQueryWrapper<Goods> lqw = Wrappers.lambdaQuery();
        // 商品类型 （1年费商品 2充值商品）
        lqw.eq(Goods::getGoodsType, 1);
        // 是否上线   1上线  2下线
        lqw.eq(Goods::getIsUp, 1);
        return baseMapper.selectVoList(lqw);
    }

}
