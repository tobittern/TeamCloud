package com.euler.sdk.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.payment.api.RemoteOrderService;
import com.euler.sdk.domain.bo.StatisticsChargeBo;
import com.euler.sdk.domain.dto.DayValueDto;
import com.euler.sdk.domain.dto.StatisticsChargeDto;
import com.euler.sdk.domain.dto.StatisticsDto;
import com.euler.sdk.domain.entity.Channel;
import com.euler.sdk.domain.entity.StatisticsCharge;
import com.euler.sdk.domain.vo.ChannelVo;
import com.euler.sdk.domain.vo.StatisticsChargeVo;
import com.euler.sdk.mapper.ChannelMapper;
import com.euler.sdk.mapper.MemberProfileMapper;
import com.euler.sdk.mapper.StatisticsChargeMapper;
import com.euler.sdk.service.IStatisticsChargeService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Service业务层处理
 *
 * @author euler
 * 2022-07-06
 */
@RequiredArgsConstructor
@Service
public class StatisticsChargeServiceImpl extends ServiceImpl<StatisticsChargeMapper, StatisticsCharge> implements IStatisticsChargeService {

    private final StatisticsChargeMapper baseMapper;

    @Resource
    private MemberProfileMapper memberProfileMapper;

    @DubboReference
    private RemoteOrderService remoteOrderService;

    @Resource
    private ChannelMapper channelMapper;


    /**
     * 查询
     *
     * @param id 主键
     * @return 实体
     */
    @Override
    public StatisticsChargeVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询列表
     *
     * @param bo 数据
     * @return 分页
     */
    @Override
    public TableDataInfo<StatisticsChargeVo> queryPageList(StatisticsChargeDto bo) {
        LambdaQueryWrapper<StatisticsCharge> lqw = buildQueryWrapper(bo);
        Page<StatisticsChargeVo> result = baseMapper.selectVoPage(bo.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询列表
     *
     * @param bo 实体
     * @return 集合
     */
    @Override
    public List<StatisticsChargeVo> queryList(StatisticsChargeDto bo) {
        LambdaQueryWrapper<StatisticsCharge> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<StatisticsCharge> buildQueryWrapper(StatisticsChargeDto bo) {
        LambdaQueryWrapper<StatisticsCharge> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getChannelId() != null, StatisticsCharge::getChannelId, bo.getChannelId());
        lqw.like(StringUtils.isNotBlank(bo.getChannelName()), StatisticsCharge::getChannelName, bo.getChannelName());
        lqw.eq(bo.getUserTotal() != null, StatisticsCharge::getUserTotal, bo.getUserTotal());
        lqw.eq(bo.getChargeTotal() != null, StatisticsCharge::getChargeTotal, bo.getChargeTotal());
        lqw.eq(bo.getOrderTotal() != null, StatisticsCharge::getOrderTotal, bo.getOrderTotal());
        lqw.eq(bo.getUserNum() != null, StatisticsCharge::getUserNum, bo.getUserNum());
        lqw.eq(bo.getChargeNum() != null, StatisticsCharge::getChargeNum, bo.getChargeNum());
        lqw.eq(bo.getOrderNum() != null, StatisticsCharge::getOrderNum, bo.getOrderNum());
        lqw.eq(bo.getDay() != null, StatisticsCharge::getDay, bo.getDay());
        return lqw;
    }

    /**
     * 新增
     *
     * @param bo 数据
     * @return 结果
     */
    @Override
    public Boolean insertByBo(StatisticsChargeBo bo) {
        StatisticsCharge add = BeanUtil.toBean(bo, StatisticsCharge.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改
     *
     * @param bo 数据
     * @return 结果
     */
    @Override
    public Boolean updateByBo(StatisticsChargeBo bo) {
        StatisticsCharge update = BeanUtil.toBean(bo, StatisticsCharge.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     *
     * @param entity 实体类数据
     */
    private void validEntityBeforeSave(StatisticsCharge entity) {

    }

    /**
     * 批量删除
     *
     * @param ids 需要删除的主键
     * @return 结果
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public void insertDataSummary(Date date) {
        //这么做是为了留接口获取指定日期数据
        Date currentDate = null;
        if (date == null) {
            currentDate = new Date();
        } else {
            currentDate = date;
        }
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        //获取前一天日期
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        //拿到前一天
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date preDate = calendar.getTime();
        //1.查询用户相关的数据
        //1.1.查询截止到前一天23:59:59 分的历史汇总人数
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("delFlag", 0);
        userMap.put("endTime", sdf1.format(preDate) + " 23:59:59");
        List<StatisticsChargeVo> userTotalList = memberProfileMapper.selectUserNumByParam(userMap);
        //1.2.查询昨日新增的用户
        userMap.put("startTime", sdf1.format(preDate) + " 00:00:00");
        List<StatisticsChargeVo> userList = memberProfileMapper.selectUserNumByParam(userMap);
        //2.下面查询订单相关的数据
        //2.1.查询截止到前一天23:59:59 分的历史汇总订单数据
        Map<String, Object> orderMap = new HashMap<>();
        List<Integer> orderStateList = new ArrayList<>();
        orderStateList.add(2);
        orderMap.put("delFlag", 0);
        orderMap.put("endTime", sdf1.format(preDate) + " 23:59:59");
        orderMap.put("orderStateList", orderStateList);
        //2.1.调用订单远程服务获取订单汇总数据
        Object orderTotalObject = remoteOrderService.getOrderDataByparam(orderMap);
        List<StatisticsChargeVo> orderTotalList = orderTotalObject == null ? null : JSONArray.parseArray(JSONArray.toJSONString(orderTotalObject), StatisticsChargeVo.class);
        //2.2.调用订单远程服务获取订单昨日数据
        orderMap.put("startTime", sdf1.format(preDate) + " 00:00:00");
        Object orderObject = remoteOrderService.getOrderDataByparam(orderMap);
        List<StatisticsChargeVo> orderList = orderObject == null ? null : JSONArray.parseArray(JSONArray.toJSONString(orderObject), StatisticsChargeVo.class);
        //3.查询所有的渠道信息,对渠道数据进行封装
        LambdaQueryWrapper<Channel> channelLqw = Wrappers.lambdaQuery();
        List<ChannelVo> channelVos = channelMapper.selectVoList(channelLqw);
        List<StatisticsChargeVo> rList = new ArrayList<>();
        for (ChannelVo v : channelVos) {
            Integer channelId = v.getId();
            StatisticsChargeVo sc = new StatisticsChargeVo();
            sc.setDay(preDate);
            sc.setChannelName(v.getChannelName());
            sc.setChannelId(channelId);
            //处理用户总量
            for (StatisticsChargeVo u : userTotalList) {
                if (channelId.equals(u.getChannelId())) {
                    sc.setUserTotal(BigDecimal.valueOf(u.getNum()));
                }
            }
            //处理昨日用户数目
            for (StatisticsChargeVo u : userList) {
                if (channelId.equals(u.getChannelId())) {
                    sc.setUserNum(BigDecimal.valueOf(u.getNum()));
                }
            }
            //处理订单总数
            if (orderTotalList != null && !orderTotalList.isEmpty()) {
                for (StatisticsChargeVo u : orderTotalList) {
                    if (channelId.equals(u.getChannelId())) {
                        sc.setOrderTotal(BigDecimal.valueOf(u.getNum()));
                        sc.setChargeTotal(u.getMoney());
                    }
                }
            }
            //处理昨日订单
            if (orderList != null && !orderList.isEmpty()) {
                for (StatisticsChargeVo u : orderList) {
                    if (channelId.equals(u.getChannelId())) {
                        sc.setOrderNum(BigDecimal.valueOf(u.getNum()));
                        sc.setChargeNum(u.getMoney());
                    }
                }
            }
            sc.setUserTotal(sc.getUserTotal() == null ? BigDecimal.ZERO : sc.getUserTotal());
            sc.setChargeTotal(sc.getChargeTotal() == null ? BigDecimal.ZERO : sc.getChargeTotal());
            sc.setOrderTotal(sc.getOrderTotal() == null ? BigDecimal.ZERO : sc.getOrderTotal());
            sc.setUserNum(sc.getUserNum() == null ? BigDecimal.ZERO : sc.getUserNum());
            sc.setChargeNum(sc.getChargeNum() == null ? BigDecimal.ZERO : sc.getChargeNum());
            sc.setOrderNum(sc.getOrderNum() == null ? BigDecimal.ZERO : sc.getOrderNum());
            rList.add(sc);
        }
        //4.对数据进行插入，插入前先查询表里是否已经统计过当天的数据
        LambdaQueryWrapper<StatisticsCharge> statisticsChargeLqw = Wrappers.lambdaQuery();
        statisticsChargeLqw.ge(true, StatisticsCharge::getDay, sdf1.format(preDate) + " 00:00:00");
        statisticsChargeLqw.le(true, StatisticsCharge::getDay, sdf1.format(preDate) + " 23:59:59");
        List<StatisticsCharge> statisticsCharges = baseMapper.selectList(statisticsChargeLqw);
        if (statisticsCharges != null && !statisticsCharges.isEmpty()) {
            for (StatisticsCharge s : statisticsCharges) {
                baseMapper.deleteTrue(s);
            }
        }
        //保存数据
        for (StatisticsChargeVo v : rList) {
            StatisticsCharge entity = BeanUtil.toBean(v, StatisticsCharge.class);
            baseMapper.insert(entity);
        }
    }


    @Override
    public Object init30DayData() {
        Date currentDate = new Date();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        //获取30天的日期数据
        List<Date> dateList = new ArrayList<>();
        for (int i = 1; i <= 30; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(currentDate);
            calendar.add(Calendar.DAY_OF_MONTH, -i);
            Date date = calendar.getTime();
            dateList.add(date);
        }
        for(Date date:dateList){
            //1.查询用户相关的数据
            //1.1.查询截止到前一天23:59:59 分的历史汇总人数
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("delFlag", 0);
            userMap.put("endTime", sdf1.format(date) + " 23:59:59");
            List<StatisticsChargeVo> userTotalList = memberProfileMapper.selectUserNumByParam(userMap);
            //1.2.查询昨日新增的用户
            userMap.put("startTime", sdf1.format(date) + " 00:00:00");
            List<StatisticsChargeVo> userList = memberProfileMapper.selectUserNumByParam(userMap);
            //2.下面查询订单相关的数据
            //2.1.查询截止到前一天23:59:59 分的历史汇总订单数据
            Map<String, Object> orderMap = new HashMap<>();
            List<Integer> orderStateList = new ArrayList<>();
            orderStateList.add(2);
            orderMap.put("delFlag", 0);
            orderMap.put("endTime", sdf1.format(date) + " 23:59:59");
            orderMap.put("orderStateList", orderStateList);
            //2.1.调用订单远程服务获取订单汇总数据
            Object orderTotalObject = remoteOrderService.getOrderDataByparam(orderMap);
            List<StatisticsChargeVo> orderTotalList = orderTotalObject == null ? null : JSONArray.parseArray(JSONArray.toJSONString(orderTotalObject), StatisticsChargeVo.class);
            //2.2.调用订单远程服务获取订单昨日数据
            orderMap.put("startTime", sdf1.format(date) + " 00:00:00");
            Object orderObject = remoteOrderService.getOrderDataByparam(orderMap);
            List<StatisticsChargeVo> orderList = orderObject == null ? null : JSONArray.parseArray(JSONArray.toJSONString(orderObject), StatisticsChargeVo.class);
            //3.查询所有的渠道信息,对渠道数据进行封装
            LambdaQueryWrapper<Channel> channelLqw = Wrappers.lambdaQuery();
            List<ChannelVo> channelVos = channelMapper.selectVoList(channelLqw);
            List<StatisticsChargeVo> rList = new ArrayList<>();
            for (ChannelVo v : channelVos) {
                Integer channelId = v.getId();
                StatisticsChargeVo sc = new StatisticsChargeVo();
                sc.setDay(date);
                sc.setChannelName(v.getChannelName());
                sc.setChannelId(channelId);
                //处理用户总量
                for (StatisticsChargeVo u : userTotalList) {
                    if (channelId.equals(u.getChannelId())) {
                        sc.setUserTotal(BigDecimal.valueOf(u.getNum()));
                    }
                }
                //处理昨日用户数目
                for (StatisticsChargeVo u : userList) {
                    if (channelId.equals(u.getChannelId())) {
                        sc.setUserNum(BigDecimal.valueOf(u.getNum()));
                    }
                }
                //处理订单总数
                if (orderTotalList != null && !orderTotalList.isEmpty()) {
                    for (StatisticsChargeVo u : orderTotalList) {
                        if (channelId.equals(u.getChannelId())) {
                            sc.setOrderTotal(BigDecimal.valueOf(u.getNum()));
                            sc.setChargeTotal(u.getMoney());
                        }
                    }
                }
                //处理昨日订单
                if (orderList != null && !orderList.isEmpty()) {
                    for (StatisticsChargeVo u : orderList) {
                        if (channelId.equals(u.getChannelId())) {
                            sc.setOrderNum(BigDecimal.valueOf(u.getNum()));
                            sc.setChargeNum(u.getMoney());
                        }
                    }
                }
                sc.setUserTotal(sc.getUserTotal() == null ? BigDecimal.ZERO : sc.getUserTotal());
                sc.setChargeTotal(sc.getChargeTotal() == null ? BigDecimal.ZERO : sc.getChargeTotal());
                sc.setOrderTotal(sc.getOrderTotal() == null ? BigDecimal.ZERO : sc.getOrderTotal());
                sc.setUserNum(sc.getUserNum() == null ? BigDecimal.ZERO : sc.getUserNum());
                sc.setChargeNum(sc.getChargeNum() == null ? BigDecimal.ZERO : sc.getChargeNum());
                sc.setOrderNum(sc.getOrderNum() == null ? BigDecimal.ZERO : sc.getOrderNum());
                rList.add(sc);
            }
            //4.对数据进行插入，插入前先查询表里是否已经统计过当天的数据
            LambdaQueryWrapper<StatisticsCharge> statisticsChargeLqw = Wrappers.lambdaQuery();
            statisticsChargeLqw.ge(true, StatisticsCharge::getDay, sdf1.format(date) + " 00:00:00");
            statisticsChargeLqw.le(true, StatisticsCharge::getDay, sdf1.format(date) + " 23:59:59");
            List<StatisticsCharge> statisticsCharges = baseMapper.selectList(statisticsChargeLqw);
            if (statisticsCharges != null && !statisticsCharges.isEmpty()) {
                for (StatisticsCharge s : statisticsCharges) {
                    baseMapper.deleteTrue(s);
                }
            }
            //保存数据
            for (StatisticsChargeVo v : rList) {
                StatisticsCharge entity = BeanUtil.toBean(v, StatisticsCharge.class);
                baseMapper.insert(entity);
            }
        }
        return null;
    }

    @Override
    public Object getStatisticsInfo(Integer channelId) {
        Map<String, Object> rMap = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("MM-dd");
        //根据当前日期，查询出昨日数据、前日数据
        Date currentDate = new Date();
        //获取30天的日期数据
        List<Date> dateList = new ArrayList<>();
        for (int i = 1; i <= 30; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(currentDate);
            calendar.add(Calendar.DAY_OF_MONTH, -i);
            Date date = calendar.getTime();
            dateList.add(date);
        }
        //获取前一天日期
        Date preDate = dateList.get(0);
        //获取前两天日期
        Date preTwoDate = dateList.get(1);

        List<List<StatisticsCharge>> dataList = new ArrayList<>();
        for (int i = 0; i < dateList.size(); i++) {
            LambdaQueryWrapper<StatisticsCharge> lqw = Wrappers.lambdaQuery();
            lqw.ge(true, StatisticsCharge::getDay, sdf.format(dateList.get(i)) + " 00:00:00");
            lqw.le(true, StatisticsCharge::getDay, sdf.format(dateList.get(i)) + " 23:59:59");
            if (channelId != 0) {
                lqw.eq(true, StatisticsCharge::getChannelId, channelId);
            }
            List<StatisticsCharge> data = baseMapper.selectList(lqw);
            if (data == null || data.isEmpty()) {
                List<StatisticsCharge> list=new ArrayList<>();
                StatisticsCharge s = new StatisticsCharge();
                s.setDay(dateList.get(i));
                s.setUserTotal(BigDecimal.ZERO);
                s.setChargeTotal(BigDecimal.ZERO);
                s.setOrderTotal(BigDecimal.ZERO);
                s.setUserNum(BigDecimal.ZERO);
                s.setChargeNum(BigDecimal.ZERO);
                s.setOrderNum(BigDecimal.ZERO);
                list.add(s);
                dataList.add(list);
            }else{
                dataList.add(data);
            }
        }
        List<StatisticsCharge> list1 = dataList.get(0);//昨日数据
        List<StatisticsCharge> list2 = dataList.get(1);//前日数据

        BigDecimal userTotal1 = BigDecimal.ZERO;//截止到昨日各个渠道总人数
        BigDecimal chargeTotal1 = BigDecimal.ZERO;//截止到昨日各个渠道总充值
        BigDecimal orderTotal1 = BigDecimal.ZERO;//截止到昨日各个渠道总订单数

        BigDecimal userNum1 = BigDecimal.ZERO;//昨日各个渠道总人数
        BigDecimal chargeNum1 = BigDecimal.ZERO;//昨日各个渠道总充值
        BigDecimal orderNum1 = BigDecimal.ZERO;//昨日各个渠道总订单数

        for (StatisticsCharge sc : list1) {
            userTotal1 = userTotal1.add(sc.getUserTotal());
            chargeTotal1 = chargeTotal1.add(sc.getChargeTotal());
            orderTotal1 = orderTotal1.add(sc.getOrderTotal());

            userNum1 = userNum1.add(sc.getUserNum());
            chargeNum1 = chargeNum1.add(sc.getChargeNum());
            orderNum1 = orderNum1.add(sc.getOrderNum());
        }

        BigDecimal userNum2 = BigDecimal.ZERO;//前日各个渠道总人数
        BigDecimal chargeNum2 = BigDecimal.ZERO;//前日各个渠道总充值
        BigDecimal orderNum2 = BigDecimal.ZERO;//前日各个渠道总订单数
        for (StatisticsCharge sc : list2) {
            userNum2 = userNum2.add(sc.getUserNum());
            chargeNum2 = chargeNum2.add(sc.getChargeNum());
            orderNum2 = orderNum2.add(sc.getOrderNum());
        }
        StatisticsDto scd = new StatisticsDto();
        //截至至昨日的汇总数据
        scd.setUserTotal(userTotal1);
        scd.setChargeTotal(chargeTotal1);
        scd.setOrderTotal(orderTotal1);

        //昨日数据
        scd.setYesterdayUser(userNum1);
        scd.setYesterdayCharge(chargeNum1);
        scd.setYesterdayOrder(orderNum1);

        if (userNum1.compareTo(userNum2) > 0) {
            scd.setUserChange(userNum1.subtract(userNum2));
            scd.setUserFlag("1");
        } else if (userNum1.compareTo(userNum2) == 0) {
            scd.setUserChange(userNum1.subtract(userNum2));
            scd.setUserFlag("0");
        } else {
            scd.setUserChange(userNum2.subtract(userNum1));
            scd.setUserFlag("-1");
        }

        if (chargeNum1.compareTo(chargeNum2) > 0) {
            scd.setChargeChange(chargeNum1.subtract(chargeNum2));
            scd.setChargeFlag("1");
        } else if (chargeNum1.compareTo(chargeNum2) == 0) {
            scd.setChargeChange(chargeNum1.subtract(chargeNum2));
            scd.setChargeFlag("0");
        } else {
            scd.setChargeChange(chargeNum2.subtract(chargeNum1));
            scd.setChargeFlag("-1");
        }

        if (orderNum1.compareTo(orderNum2) > 0) {
            scd.setOrderChange(orderNum1.subtract(orderNum2));
            scd.setOrderFlag("1");
        } else if (orderNum1.compareTo(orderNum2) == 0) {
            scd.setOrderChange(orderNum1.subtract(orderNum2));
            scd.setOrderFlag("0");
        } else {
            scd.setOrderChange(orderNum2.subtract(orderNum1));
            scd.setOrderFlag("-1");
        }

        List<DayValueDto> kList = new ArrayList<>();
        //下面处理折线图
        //处理每一天的数据,倒序来
        for (int i = dataList.size() - 1; i >= 0; i--) {
            DayValueDto k = new DayValueDto();
            List<StatisticsCharge> data = dataList.get(i);
            //说明库里没有数据
            if (data == null) {
                continue;
            }
            BigDecimal user = BigDecimal.ZERO;//人数
            Date date = data.get(0).getDay();//日期
            for (StatisticsCharge sc : data) {
                user = user.add(sc.getUserNum());
            }

            k.setKey(sdf.format(date));
            k.setValue(user);
            kList.add(k);
        }
        //对数据进行比较处理,处理增长还是下降标志
        for (int i = 0; i < kList.size(); i++) {
            DayValueDto k1 = kList.get(i);
            if (i == 0) {
                k1.setChange("-");
                k1.setFlag("0");
            } else if (i <= kList.size() - 2) {
                DayValueDto k2 = kList.get(i + 1);
                if (k2.getValue().compareTo(k1.getValue()) > 0) {
                    if(k1.getValue().compareTo(BigDecimal.ZERO)==0){//前一天是0
                        k1.setChange(k2.getValue().subtract(k1.getValue()).toString()+"人");
                    }else{
                        BigDecimal k3=k2.getValue().subtract(k1.getValue());
                        BigDecimal k4=k3.divide(k1.getValue(),2,RoundingMode.HALF_UP);
                        k1.setChange(k4.multiply(new BigDecimal("100")).toString()+"%");
                    }
                    k1.setFlag("1");
                } else if (k2.getValue().compareTo(k1.getValue()) == 0) {
                    if(k2.getValue().compareTo(BigDecimal.ZERO)==0){
                        //说明k1也是0
                        k1.setChange("0人");
                    }else{
                        k1.setChange("0%");
                    }
                    k1.setFlag("0");
                } else {
                    BigDecimal k3=k1.getValue().subtract(k2.getValue());
                    BigDecimal k4=k3.divide(k1.getValue(),2,RoundingMode.HALF_UP);
                    k1.setChange(k4.multiply(new BigDecimal("100")).toString()+"%");
                    k1.setFlag("-1");
                }
            } else {
                DayValueDto k0 = kList.get(i - 1);
                if (k1.getValue().compareTo(k0.getValue()) > 0) {
                    if(k0.getValue().compareTo(BigDecimal.ZERO)==0){
                        k1.setChange(k1.getValue().subtract(k0.getValue()).toString()+"人");
                    }else{
                        BigDecimal k3=k1.getValue().subtract(k0.getValue());
                        BigDecimal k4=k3.divide(k0.getValue(),2,RoundingMode.HALF_UP);
                        k1.setChange(k4.multiply(new BigDecimal("100")).toString()+"%");
                    }
                    k1.setFlag("1");
                } else if (k1.getValue().compareTo(k0.getValue()) == 0) {
                    if(k1.getValue().compareTo(BigDecimal.ZERO)==0){
                        //说明k0也是0
                        k1.setChange("0人");
                    }else{
                        k1.setChange("0%");
                    }
                    k1.setFlag("0");
                } else {
                    BigDecimal k3=k1.getValue().subtract(k0.getValue());
                    BigDecimal k4=k3.divide(k0.getValue(),2,RoundingMode.HALF_UP);
                    k1.setChange(k4.multiply(new BigDecimal("100")).toString()+"%");
                    k1.setFlag("-1");
                }
            }
            try {
                k1.setKey(sdf2.format(sdf.parse(k1.getKey())));
            }catch (Exception e){
                log.error("格式化时间异常:"+e.getMessage());
            }
        }
        rMap.put("data1", scd);
        rMap.put("data2", kList);
        return rMap;
    }
}
