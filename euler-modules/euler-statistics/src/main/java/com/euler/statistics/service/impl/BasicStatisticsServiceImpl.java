package com.euler.statistics.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdDto;
import com.euler.common.core.domain.dto.IdNameTypeDicDto;
import com.euler.common.core.domain.dto.LoginUser;
import com.euler.common.core.domain.dto.RoleDTO;
import com.euler.common.core.utils.DateUtils;
import com.euler.common.core.utils.JsonUtils;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.satoken.utils.LoginHelper;
import com.euler.platform.api.RemoteGameManagerService;
import com.euler.platform.api.domain.OpenGame;
import com.euler.statistics.domain.dto.BasicStatisticsDto;
import com.euler.statistics.domain.entity.BasicStatistics;
import com.euler.statistics.domain.entity.Channel;
import com.euler.statistics.domain.vo.*;
import com.euler.statistics.mapper.BasicStatisticsMapper;
import com.euler.statistics.mapper.BusinessOrderMapper;
import com.euler.statistics.mapper.ChannelMapper;
import com.euler.statistics.mapper.OpenGameMapper;
import com.euler.statistics.service.IBasicStatisticsService;
import com.euler.statistics.utils.StatisticsUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * 数据统计 - 每日的ltv基础数据统计Service业务层处理
 *
 * @author euler
 * @date 2022-04-27
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class BasicStatisticsServiceImpl implements IBasicStatisticsService {

    @Autowired
    private BasicStatisticsMapper baseMapper;
    @Autowired
    private BusinessOrderMapper businessOrderMapper;
    @Autowired
    private OpenGameMapper openGameMapper;
    @Autowired
    private ChannelMapper channelMapper;
    @DubboReference
    private RemoteGameManagerService remoteGameManagerService;
    /**
     * 通过游戏名获取基础数据中的游戏列表
     *
     * @param dto
     * @return
     */
    @Override
    public R getGameListByName(IdNameTypeDicDto dto) {
        List<DropdownGameVo> gameVoList = new ArrayList<>();
        AtomicReference<List<OpenGame>> openGames = new AtomicReference<>();
        // 判断登录的用户是否是渠道管理员用户
        LoginUser user = LoginHelper.getLoginUser();
        List<RoleDTO> list = user.getRoles();
        Optional<RoleDTO> first = list.stream().filter(a -> a.getRoleKey().contains("channel")).findFirst();
        if(first.isPresent()) {
            // 根据渠道用户查询渠道信息
            LambdaQueryWrapper<Channel> eq = Wrappers.<Channel>lambdaQuery()
                .select(Channel::getId)
                .eq(Channel::getUserId, user.getUserId())
                .eq(Channel::getStatus, '0')
                .eq(Channel::getDelFlag, '0');

            List<ChannelVo> channelIds = channelMapper.selectVoList(eq);
            if(channelIds != null && channelIds.size() > 0) {
                // 渠道用户，显示该渠道内的游戏列表
                channelIds.forEach(a -> {
                    List<OpenGame> games = remoteGameManagerService.getGameList(a.getId());
                    openGames.set(games);
                });
            }
        } else {
            // 其他管理员权限用户，显示所有的游戏列表
            var wrapper = Wrappers.<OpenGame>
                query().select("id, game_name, operation_platform,icon_url")
                .orderByAsc("game_name");
            // 数据获取完毕之后我们返回给角色
            openGames.set(openGameMapper.selectList(wrapper));
        }

        if (openGames.get() != null && !openGames.get().isEmpty()) {
            openGames.get().forEach(a -> {
                DropdownGameVo gameVo = new DropdownGameVo();
                gameVo.setId(a.getId())
                    .setName(a.getGameName())
                    .setType(a.getOperationPlatform())
                    .setIcon(a.getIconUrl());
                gameVoList.add(gameVo);
            });
        }
        return R.ok(gameVoList);
    }

    /**
     * 汇总数据查询
     *
     * @param dto
     * @return
     */
    @SneakyThrows
    @Override
    public List<BasicStatisticsReturnVo> Summary(BasicStatisticsDto dto, Integer type) {
        // 如果前台没有传输时间 我们默认返回的是全部  开始时间从 2022-05-10 开始
        if (dto.getStartTime() == null || dto.getEndTime() == null) {
            dto.setStartTime("2022-05-10");
            dto.setEndTime(DateUtil.format(new Date(), "yyyy-MM-dd"));
        }
        // 查询需要获取几天的数据
        List<BasicStatisticsReturnVo> basicStatisticsVoList = new ArrayList<>();
        SearchDataSummaryVo searchDataSummaryVo = new SearchDataSummaryVo();
        // 根据时间区间获取出他们之间的年月日存放到一个List里面
        List<String> searchInDate = StatisticsUtils.getDate(dto.getStartTime(), dto.getEndTime());
        // 首先进行的是汇总数据的统计  角色列表 分包列表 游戏列表 还有这些人的充值金额
        QueryWrapper<BasicStatistics> getBasicStatisticsListLqw = Wrappers.query();
        getBasicStatisticsListLqw.in(true, "date_label", searchInDate)
            .eq((dto.getOperationPlatform() != null && dto.getOperationPlatform() != 0), "operation_platform", dto.getOperationPlatform())
            .eq(dto.getGameId() != null, "game_id", dto.getGameId())
            .eq(StringUtils.isNotBlank(dto.getGameName()), "game_name", dto.getGameName())
            .eq(dto.getChannelId() != null, "channel_id", dto.getChannelId())
            .eq(StringUtils.isNotBlank(dto.getChannelName()), "channel_name", dto.getChannelName())
            .eq(StringUtils.isNotBlank(dto.getPackageCode()), "package_code", dto.getPackageCode())
            // 如果是首日LTV的话需要去掉滚浮用户 （只有当日注册并且当日进行角色上报达特的才算）
            .apply(type == 2, "date_label = DATE_FORMAT(register_time, '%Y-%m-%d')");

        List<BasicStatisticsVo> basicStatistics = baseMapper.selectVoList(getBasicStatisticsListLqw);
//        log.info("从数据库中查询到了按照时间汇总出来的数据如下：{}", JsonUtils.toJsonString(basicStatistics));
        // 循环获取到我们想要的数据 我们需要将角色存档起来判断那些是新增角色那些不是
        List<RoleSummaryVo> getOtherRoleIds = basicStatistics.stream().map(e -> new RoleSummaryVo(e.getRoleId(), e.getServerId(), e.getGameId(), e.getPackageCode(), e.getLoginTime())).collect(Collectors.toList());
        if (getOtherRoleIds.size() > 0 && searchDataSummaryVo.getRole() != null) {
            searchDataSummaryVo.getRole().addAll(getOtherRoleIds);
            searchDataSummaryVo.setRole(searchDataSummaryVo.getRole());
        } else if (getOtherRoleIds.size() > 0 && searchDataSummaryVo.getRole() == null) {
            searchDataSummaryVo.setRole(getOtherRoleIds);
        }
        // 获取这批数据中的基础数据 角色集合  渠道包集合 游戏集合
        List<String> totalRoleServerMd5 = basicStatistics.stream().map(BasicStatisticsVo::getRoleServerMd5).distinct().collect(Collectors.toList());
        List<String> totalRoleIds = basicStatistics.stream().map(BasicStatisticsVo::getRoleId).distinct().collect(Collectors.toList());
        List<String> totalServerIds = basicStatistics.stream().map(BasicStatisticsVo::getServerId).distinct().collect(Collectors.toList());
        List<String> totalPackageCodes = basicStatistics.stream().map(BasicStatisticsVo::getPackageCode).distinct().collect(Collectors.toList());
        List<Integer> totalGameIds = basicStatistics.stream().map(BasicStatisticsVo::getGameId).distinct().collect(Collectors.toList());
        // 查询这些角色的
        // 我们需要将这批角色按照他们的注册时间进行数据分组 为了我们后面做汇总的ltv数据
        QueryWrapper<BusinessOrderSimpleVo> orderWrapper = Wrappers.query();

        String searchOrderStartTime = DateUtil.format(DateUtil.parse(dto.getStartTime()), "yyyy-MM-dd") + " 00:00:00";
        // 查询订单的结束时间是当前时间
        String searchOrderEndTime = DateUtil.format(new Date(), "yyyy-MM-dd") + " 23:59:59";
        orderWrapper.between("create_time", searchOrderStartTime, searchOrderEndTime)
            .eq("order_state", 2)
            .eq("order_type", "G")
            .in(totalGameIds.size() > 0, "game_id", totalGameIds)
            .in(totalRoleIds.size() > 0, "game_role_id", totalRoleIds)
            .in(totalServerIds.size() > 0, "game_server_id", totalServerIds);
//            .in(totalPackageCodes.size() > 0, "game_package_code", totalPackageCodes);
        List<BusinessOrderSimpleVo> businessOrderSimpleVos = businessOrderMapper.selectBusinessOrderSimpleData(orderWrapper);
//        log.info("首先获取的汇总数据：{}", JsonUtils.toJsonString(businessOrderSimpleVos));
        // 通过上面的数据我么你是可以算出每日新增角色 新增充值角色 新增充值金额 和单日的LTV数据
        TreeMap<String, BasicStatisticsReturnListVo> baseListVo = new TreeMap<>();
        // 循环获取的总角色 按照他们的创建时间(login_time)给他们进行分组 - 这个数据用于我们获取这些角色在其他天数的充值金额
        basicStatistics.forEach(row -> {
            String searchKey = DateUtil.format(row.getLoginTime(), "yyyy-MM-dd");
            BasicStatisticsReturnListVo basicStatisticsReturnListVo = baseListVo.get(searchKey);
            // 判断是否存储了当天的基础数据
            if (basicStatisticsReturnListVo == null) {
                BasicStatisticsReturnListVo tempBasicStatisticsVo = new BasicStatisticsReturnListVo();
                // 存放角色集合
                List<Long> userIds = new ArrayList<>();
                userIds.add(row.getMemberId());
                tempBasicStatisticsVo.setMemberIds(userIds);
                // 存放角色集合
                List<String> roleIds = new ArrayList<>();
                roleIds.add(row.getRoleId());
                tempBasicStatisticsVo.setRoleIds(roleIds);
                // 存放区服集合
                List<String> serverIds = new ArrayList<>();
                serverIds.add(row.getServerId());
                tempBasicStatisticsVo.setServerIds(serverIds);
                // 存放游戏集合
                List<Integer> gameIds = new ArrayList<>();
                gameIds.add(row.getGameId());
                tempBasicStatisticsVo.setGameIds(gameIds);
                // 存放分包集合
                List<String> packageCodes = new ArrayList<>();
                packageCodes.add(row.getPackageCode());
                tempBasicStatisticsVo.setPackageCodes(packageCodes);
                // 数据存入
                baseListVo.put(searchKey, tempBasicStatisticsVo);
            } else {
                // 存在我们需要进行数据累加
                BasicStatisticsReturnListVo getHaveBasic = baseListVo.get(searchKey);
                if (!getHaveBasic.getMemberIds().contains(row.getMemberId())) {
                    getHaveBasic.getMemberIds().add(row.getMemberId());
                }
                if (!getHaveBasic.getRoleIds().contains(row.getRoleId())) {
                    getHaveBasic.getRoleIds().add(row.getRoleId());
                }
                if (!getHaveBasic.getServerIds().contains(row.getServerId())) {
                    getHaveBasic.getServerIds().add(row.getServerId());
                }
                if (!getHaveBasic.getGameIds().contains(row.getGameId())) {
                    getHaveBasic.getGameIds().add(row.getGameId());
                }
                if (!getHaveBasic.getPackageCodes().contains(row.getPackageCode())) {
                    getHaveBasic.getPackageCodes().add(row.getPackageCode());
                }
                baseListVo.put(searchKey, getHaveBasic);
            }
        });
        String overEndTime = DateUtil.format(DateUtil.parse(dto.getEndTime()), "yyyy-MM-dd") + " 23:59:59";
        // 设置存储基础值
        List<String> newIncrPayNumList = new ArrayList<>();
        List<String> newIncrPayNumRoleServerList = new ArrayList<>();
        AtomicReference<BigDecimal> newIncrPayAmount = new AtomicReference<>(new BigDecimal(0));
        if (businessOrderSimpleVos.size() > 0) {
            businessOrderSimpleVos.forEach(a -> {
                // 首先判断 将我们多查出来的一条数据给过滤掉
                long createLongTime = a.getCreateTime().getTime(); // 订单的创建时间
                long overLongTime = DateUtil.parse(overEndTime).getTime(); // 搜索条件中的结束时间
                if (createLongTime <= overLongTime) { // 只获取搜索时间之内的数据
                    // 对这些订单进行统一的归类 key是天 把当天的充值角色的游戏渠道号汇总一下
                    // 获取到这批角色中那些是属于当天的新增充值角色数
                    if (searchDataSummaryVo != null && searchDataSummaryVo.getRole() != null) {
                        Optional<RoleSummaryVo> first = searchDataSummaryVo.getRole().stream().filter(e ->
                            e.getRoleId().equals(a.getGameRoleId())
                                && e.getServerId().equals(a.getGameServerId())
//                                && e.getPackageCode().equals(a.getGamePackageCode())
                                && e.getGameId().equals(a.getGameId())
                                && DateUtil.format(a.getCreateTime(), "yyyy/MM/dd").equals(DateUtil.format(e.getCreateTime(), "yyyy/MM/dd"))
                        ).findFirst();
                        // 判断那些角色属于新增的充值角色
                        if (first.isPresent()) {
                            // 将新增充值角色进行累加
                            if (!newIncrPayNumList.contains(first.get().getRoleId())) {
                                newIncrPayNumList.add(first.get().getRoleId());
                            }
                            // 由于会存在同样角色ID不同区服ID的情况 但是他们也算是新增充值角色 我们这边对角色+区服加密一下进行判断
                            String md5Value = SecureUtil.md5(first.get().getRoleId() + first.get().getServerId());
                            if (!newIncrPayNumRoleServerList.contains(md5Value)) {
                                newIncrPayNumRoleServerList.add(md5Value);
                            }
                            newIncrPayAmount.set(NumberUtil.add(newIncrPayAmount.get(), a.getOrderAmount()));
                        }
                    }
                }
            });
        }
        log.info("角色按照他们的注册时间汇总之后的数据:{}", JsonUtils.toJsonString(baseListVo));
        log.info("计算出的新增付费角色：{}", newIncrPayNumList);
        log.info("计算出的新增付费角色+区服加密之后的结果：{}", newIncrPayNumRoleServerList);
        log.info("计算出的新增付费金额：{}", newIncrPayAmount);
        // 分母 totalMemberIds的size
        // 分子是这个角色充值的金额
        // 数据输出到汇总中
        BasicStatisticsReturnVo basicStatisticsVo = new BasicStatisticsReturnVo();
        basicStatisticsVo.setDateLabel("汇总");
        basicStatisticsVo.setChannelId(0);
        basicStatisticsVo.setChannelName("all");
        basicStatisticsVo.setPackageCode("all");
        basicStatisticsVo.setGameId(0);
        basicStatisticsVo.setGameName("all");
        basicStatisticsVo.setOperationPlatform("0");
        basicStatisticsVo.setNewIncrUserNum(totalRoleServerMd5.size());
        basicStatisticsVo.setNewIncrPayNum(newIncrPayNumRoleServerList.size());
        // 设置参与LTV计算的分母
        int CalculateDenominator = totalRoleServerMd5.size();
        if (CalculateDenominator > 0) {
            basicStatisticsVo.setNewIncrPayLv((Math.round(newIncrPayNumRoleServerList.size() * 10000 / CalculateDenominator) / 100.0));
        }
        basicStatisticsVo.setNewIncrPayAmount(newIncrPayAmount.get());
        TreeMap<String, BigDecimal> storageDataBigDecimal = new TreeMap<>();
        storageDataBigDecimal.put("oneDay", new BigDecimal(0));
        storageDataBigDecimal.put("twoDay", new BigDecimal(0));
        storageDataBigDecimal.put("threeDay", new BigDecimal(0));
        storageDataBigDecimal.put("fourDay", new BigDecimal(0));
        storageDataBigDecimal.put("fiveDay", new BigDecimal(0));
        storageDataBigDecimal.put("sixDay", new BigDecimal(0));
        storageDataBigDecimal.put("sevenDay", new BigDecimal(0));
        storageDataBigDecimal.put("fifteenDay", new BigDecimal(0));
        storageDataBigDecimal.put("thirtyDay", new BigDecimal(0));
        storageDataBigDecimal.put("sixtyDay", new BigDecimal(0));
        storageDataBigDecimal.put("ninetyDay", new BigDecimal(0));
        // 设置首日的LTV金额
        storageDataBigDecimal.put("oneDay", newIncrPayAmount.get());
        if (CalculateDenominator > 0) {
            basicStatisticsVo.setOneDay(Math.round(Convert.toDouble(newIncrPayAmount) * 100 / CalculateDenominator) / 100.0);
        }
        // 这个时候其实我们已经获取到了比结束日期还有多一天的订单数据 依次计算获取 baseListVo 中的数据中这些人 针对于他们注册时间的第二天的充值信息  以此类推
        // 判断我们需要获取多少日的ltv数据
        DateTime parse1 = DateUtil.parse(searchOrderStartTime);
        DateTime parse2 = DateUtil.parse(searchOrderEndTime);
        // 由于列表我们用的是大于等于  汇总的是直接循环 所以这个地方我们加一一下
        long size = DateUtil.between(parse1, parse2, DateUnit.DAY) + 1;
        log.info("开始时间：{}, 结束时间：{}", parse1, parse2);
        log.info("汇总展示出相差多少天：{}", size);
        /**
         * {
         * 	"2022-03-28": {
         * 		"memberIds": [104, 155, 171, 153],
         * 		"packageCodes": ["test_5", "aaa_6"],
         * 		"gameIds": [18, 19]
         *        },
         * 	"2022-03-29": {
         * 		"memberIds": [180],
         * 		"packageCodes": ["test_10"],
         * 		"gameIds": [19]
         *    }
         * }
         */
        // 需要计算第二日之后的数据
        if (size >= 2 && type == 1) {
            // 这里代表着我们需要对  baseListVo中的每条数据循环多少次 也代表着我们需要针对baseListVo中的  String往后面推几天
            for (int j = 1; j < size; j++) {
                // 设置一个初始金额
                BigDecimal bigDecimal = new BigDecimal(0);

                for (Map.Entry<String, BasicStatisticsReturnListVo> entry : baseListVo.entrySet()) {
                    String key = entry.getKey();
                    BasicStatisticsReturnListVo value = entry.getValue();

                    // j的是就是我们需要将a的值往后推几天
                    DateTime foreachParse = DateUtil.parse(key + " 00:00:00");
                    String yyyyMMdd = DateUtil.format(DateUtil.offsetDay(foreachParse, j), "yyyyMMdd");
                    // 根据这个时间 携带者b中的角色 渠道 游戏限制条件 我们获取到当前的充值金额
                    // 筛选条件是  dateFormatNumbers相同 角色包含 游戏包含 渠道包含  这个计算的是 这个日期这些人第二天的充值金额  循环完毕之后汇总才能算是下一个ltv数据
                    BigDecimal reduce = businessOrderSimpleVos.stream().filter(lwq -> lwq.getDateFormatNumbers().equals(Convert.toInt(yyyyMMdd))
                        && value.getRoleIds().contains(lwq.getGameRoleId())
                        && value.getServerIds().contains(lwq.getGameServerId())
                        && value.getGameIds().contains(lwq.getGameId())
                        && value.getPackageCodes().contains(lwq.getGamePackageCode()))
                        .map(BusinessOrderSimpleVo::getOrderAmount)
                        .collect(Collectors.toList()).stream().reduce(BigDecimal.ZERO, BigDecimal::add);
                    bigDecimal = NumberUtil.add(bigDecimal, reduce);
                }
                // 一个外层计算完毕之后我们判断这条数据需要累加到第几天的LTV数据上
                if (j == 1) {
                    // 计算到第二天的LTV数据中
                    BigDecimal oneDay = storageDataBigDecimal.get("oneDay");
                    BigDecimal add = NumberUtil.add(oneDay, bigDecimal);
                    storageDataBigDecimal.put("twoDay", add);
                }
                if (j == 2) {
                    // 计算到第三天的LTV数据中
                    BigDecimal twoDay = storageDataBigDecimal.get("twoDay");
                    BigDecimal add = NumberUtil.add(twoDay, bigDecimal);
                    storageDataBigDecimal.put("threeDay", add);
                }
                if (j == 3) {
                    // 计算到第四天的LTV数据中
                    BigDecimal threeDay = storageDataBigDecimal.get("threeDay");
                    BigDecimal add = NumberUtil.add(threeDay, bigDecimal);
                    storageDataBigDecimal.put("fourDay", add);
                }
                if (j == 4) {
                    // 计算到第五天的LTV数据中
                    BigDecimal fourDay = storageDataBigDecimal.get("fourDay");
                    BigDecimal add = NumberUtil.add(fourDay, bigDecimal);
                    storageDataBigDecimal.put("fiveDay", add);
                }
                if (j == 5) {
                    // 计算到第六天的LTV数据中
                    BigDecimal fiveDay = storageDataBigDecimal.get("fiveDay");
                    BigDecimal add = NumberUtil.add(fiveDay, bigDecimal);
                    storageDataBigDecimal.put("sixDay", add);
                }
                if (j == 6) {
                    // 计算到第七天的LTV数据中
                    BigDecimal sixDay = storageDataBigDecimal.get("sixDay");
                    BigDecimal add = NumberUtil.add(sixDay, bigDecimal);
                    storageDataBigDecimal.put("sevenDay", add);
                }
                if (j == 14) {
                    // 计算到十五天的LTV数据中
                    // 首先判断十五天这个map中有数据没  没得话我们存 有的话 累加 反之新增
                    BigDecimal fifteenDay = storageDataBigDecimal.get("fifteenDay");
                    if (fifteenDay.equals(new BigDecimal(0))) {
                        BigDecimal sevenDay = storageDataBigDecimal.get("sevenDay");
                        BigDecimal add = NumberUtil.add(sevenDay, bigDecimal);
                        storageDataBigDecimal.put("fifteenDay", add);
                    } else {
                        BigDecimal forFifteenDay = storageDataBigDecimal.get("fifteenDay");
                        BigDecimal add = NumberUtil.add(forFifteenDay, bigDecimal);
                        storageDataBigDecimal.put("fifteenDay", add);
                    }
                }
                if (j == 29) {
                    // 计算到三十天的LTV数据中
                    BigDecimal thirtyDay = storageDataBigDecimal.get("thirtyDay");
                    if (thirtyDay.equals(new BigDecimal(0))) {
                        BigDecimal forFifteenDay = storageDataBigDecimal.get("fifteenDay");
                        BigDecimal add = NumberUtil.add(forFifteenDay, bigDecimal);
                        storageDataBigDecimal.put("thirtyDay", add);
                    } else {
                        BigDecimal forThirtyDay = storageDataBigDecimal.get("thirtyDay");
                        BigDecimal add = NumberUtil.add(forThirtyDay, bigDecimal);
                        storageDataBigDecimal.put("thirtyDay", add);
                    }
                }
                if (j == 59) {
                    // 计算到六十天的LTV数据中
                    BigDecimal sixtyDay = storageDataBigDecimal.get("sixtyDay");
                    if (sixtyDay.equals(new BigDecimal(0))) {
                        BigDecimal forThirtyDay = storageDataBigDecimal.get("thirtyDay");
                        BigDecimal add = NumberUtil.add(forThirtyDay, bigDecimal);
                        storageDataBigDecimal.put("sixtyDay", add);
                    } else {
                        BigDecimal forSixtyDay = storageDataBigDecimal.get("sixtyDay");
                        BigDecimal add = NumberUtil.add(forSixtyDay, bigDecimal);
                        storageDataBigDecimal.put("sixtyDay", add);
                    }
                }
                if (j == 89) {
                    // 计算到九十天的LTV数据中
                    BigDecimal ninetyDay = storageDataBigDecimal.get("ninetyDay");
                    if (ninetyDay.equals(new BigDecimal(0))) {
                        BigDecimal forSixtyDay = storageDataBigDecimal.get("sixtyDay");
                        BigDecimal add = NumberUtil.add(forSixtyDay, bigDecimal);
                        storageDataBigDecimal.put("ninetyDay", add);
                    } else {
                        BigDecimal forNinetyDay = storageDataBigDecimal.get("ninetyDay");
                        BigDecimal add = NumberUtil.add(forNinetyDay, bigDecimal);
                        storageDataBigDecimal.put("ninetyDay", add);
                    }
                }
            }
        }
        log.info("汇总LTV每日金额计算：{}", JsonUtils.toJsonString(storageDataBigDecimal));
        // 第二日
        if (!storageDataBigDecimal.get("twoDay").equals(new BigDecimal(0)) && totalGameIds.size() > 0) {
            basicStatisticsVo.setTwoDay(Math.round(Convert.toDouble(storageDataBigDecimal.get("twoDay")) * 100 / CalculateDenominator) / 100.0);
        }
        // 第三日
        if (!storageDataBigDecimal.get("threeDay").equals(new BigDecimal(0)) && totalGameIds.size() > 0) {
            basicStatisticsVo.setThreeDay(Math.round(Convert.toDouble(storageDataBigDecimal.get("threeDay")) * 100 / CalculateDenominator) / 100.0);
        }
        // 第四日
        if (!storageDataBigDecimal.get("fourDay").equals(new BigDecimal(0)) && totalGameIds.size() > 0) {
            basicStatisticsVo.setFourDay(Math.round(Convert.toDouble(storageDataBigDecimal.get("fourDay")) * 100 / CalculateDenominator) / 100.0);
        }
        // 第五日
        if (!storageDataBigDecimal.get("fiveDay").equals(new BigDecimal(0)) && totalGameIds.size() > 0) {
            basicStatisticsVo.setFiveDay(Math.round(Convert.toDouble(storageDataBigDecimal.get("fiveDay")) * 100 / CalculateDenominator) / 100.0);
        }
        // 第六日
        if (!storageDataBigDecimal.get("sixDay").equals(new BigDecimal(0)) && totalGameIds.size() > 0) {
            basicStatisticsVo.setSixDay(Math.round(Convert.toDouble(storageDataBigDecimal.get("sixDay")) * 100 / CalculateDenominator) / 100.0);
        }
        // 第七日
        if (!storageDataBigDecimal.get("sevenDay").equals(new BigDecimal(0)) && totalGameIds.size() > 0) {
            basicStatisticsVo.setSevenDay(Math.round(Convert.toDouble(storageDataBigDecimal.get("sevenDay")) * 100 / CalculateDenominator) / 100.0);
        }
        // 十五日
        if (!storageDataBigDecimal.get("fifteenDay").equals(new BigDecimal(0)) && totalGameIds.size() > 0) {
            basicStatisticsVo.setFifteenDay(Math.round(Convert.toDouble(storageDataBigDecimal.get("fifteenDay")) * 100 / CalculateDenominator) / 100.0);
        }
        // 三十日
        if (!storageDataBigDecimal.get("thirtyDay").equals(new BigDecimal(0)) && totalGameIds.size() > 0) {
            basicStatisticsVo.setThirtyDay(Math.round(Convert.toDouble(storageDataBigDecimal.get("thirtyDay")) * 100 / CalculateDenominator) / 100.0);
        }
        // 六十日
        if (!storageDataBigDecimal.get("sixtyDay").equals(new BigDecimal(0)) && totalGameIds.size() > 0) {
            basicStatisticsVo.setSixtyDay(Math.round(Convert.toDouble(storageDataBigDecimal.get("sixtyDay")) * 100 / CalculateDenominator) / 100.0);
        }
        // 九十日
        if (!storageDataBigDecimal.get("ninetyDay").equals(new BigDecimal(0)) && totalGameIds.size() > 0) {
            basicStatisticsVo.setNinetyDay(Math.round(Convert.toDouble(storageDataBigDecimal.get("ninetyDay")) * 100 / CalculateDenominator) / 100.0);
        }

        basicStatisticsVoList.add(basicStatisticsVo);
        return basicStatisticsVoList;
    }


    /**
     * 汇总列表数据  就是把汇总数据再按照天进行一下分割
     *
     * @param dto
     * @return
     */
    @SneakyThrows
    @Override
    public BasicStatisticsReturnDataVo summaryList(BasicStatisticsDto dto) {
        // 如果前台没有传输时间 我们默认返回的是全部  开始时间从 2022-05-10 开始
        if (dto.getStartTime() == null || dto.getEndTime() == null) {
            dto.setStartTime("2022-05-10");
            dto.setEndTime(DateUtil.format(new Date(), "yyyy-MM-dd"));
        }
        // 查询需要获取几天的数据
        List<BasicStatisticsReturnVo> basicStatisticsVoList = new ArrayList<>();
        // 根据时间区间获取出他们之间的年月日存放到一个List里面
        List<String> searchInDate = StatisticsUtils.getDate(dto.getStartTime(), dto.getEndTime());
        List<String> reverseDate = StatisticsUtils.getDate(dto.getStartTime(), dto.getEndTime());
        // 声明基础数据值
        BasicStatisticsReturnDataVo basicStatisticsReturnDataVo = new BasicStatisticsReturnDataVo();
        basicStatisticsReturnDataVo.setTotal(Convert.toLong(searchInDate.size()));
        basicStatisticsReturnDataVo.setPageSize(dto.getPageSize());
        basicStatisticsReturnDataVo.setPageNums(dto.getPageNum());
        // 我们按照筛选中来的时间的倒叙进行循环获取数据
        log.info("计算前的时间：{}", JsonUtils.toJsonString(searchInDate));
        Collections.reverse(reverseDate);
        log.info("计算后的时间：{}", JsonUtils.toJsonString(reverseDate));
        // 我们需要按照分页将数据进行一下分割
        reverseDate = reverseDate.subList((dto.getPageNum() - 1) * dto.getPageSize(), Math.min(dto.getPageNum() * dto.getPageSize(), reverseDate.size()));
        log.info("手动分页之后的结果：{}", JsonUtils.toJsonString(reverseDate));
        // 循环时间数据 我们需要按照时间进行获取想要的值
        if (reverseDate.size() > 0) {
            reverseDate.forEach(a -> {
                // 列表中获取的每个渠道的对应游戏的单独数据 我们需要获取当前渠道的新增角色 新增充值角色 新增充值金额 还有对应的ltv
                // 先获取当前日期 当前渠道的的角色全部数据 包括角色id 渠道号 游戏id
                LambdaQueryWrapper<BasicStatistics> foreachEveryEq = Wrappers.<BasicStatistics>lambdaQuery()
                    .select(BasicStatistics::getPackageCode, BasicStatistics::getRoleId, BasicStatistics::getServerId, BasicStatistics::getRoleServerMd5, BasicStatistics::getMemberId, BasicStatistics::getGameId, BasicStatistics::getLoginTime)
                    .eq(BasicStatistics::getDateLabel, a)
                    .eq(dto.getOperationPlatform() != null && dto.getOperationPlatform() != 0, BasicStatistics::getOperationPlatform, dto.getOperationPlatform())
                    .eq(dto.getGameId() != null, BasicStatistics::getGameId, dto.getGameId())
                    .eq(StringUtils.isNotBlank(dto.getGameName()), BasicStatistics::getGameName, dto.getGameName())
                    .eq(dto.getChannelId() != null, BasicStatistics::getChannelId, dto.getChannelId())
                    .eq(StringUtils.isNotBlank(dto.getChannelName()), BasicStatistics::getChannelName, dto.getChannelName())
                    .eq(StringUtils.isNotBlank(dto.getPackageCode()), BasicStatistics::getPackageCode, dto.getPackageCode());
                // 这个是获取当前的基础数据
                List<BasicStatisticsVo> foreachBasicStatisticsVos = baseMapper.selectVoList(foreachEveryEq);
                log.info("当天当前渠道的原始数据:{}", JsonUtils.toJsonString(foreachBasicStatisticsVos));
                // 当前渠道的数据获取完毕之后 我们先获取当前渠道的基础数据  就是出去ltv的数据
                BasicStatisticsReturnVo basicStatisticsForVo = new BasicStatisticsReturnVo();
                basicStatisticsForVo.setDateLabel(a);
                basicStatisticsForVo.setChannelId(dto.getChannelId() != null ? dto.getChannelId() : 0);
                basicStatisticsForVo.setChannelName(StringUtils.isNotBlank(dto.getChannelName()) ? dto.getChannelName() : "all");
                basicStatisticsForVo.setPackageCode(StringUtils.isNotBlank(dto.getPackageCode()) ? dto.getPackageCode() : "all");
                basicStatisticsForVo.setGameId((dto.getGameId() != null && dto.getGameId() != 0) ? dto.getGameId() : 0);
                basicStatisticsForVo.setGameName(StringUtils.isNotBlank(dto.getGameName()) ? dto.getGameName() : "all");
                basicStatisticsForVo.setOperationPlatform(Convert.toStr(dto.getOperationPlatform(), "0"));
                // 对新增角色进行一下去重操作
                List<String> totalRoleIds = foreachBasicStatisticsVos.stream().map(BasicStatisticsVo::getRoleId).distinct().collect(Collectors.toList());
                List<String> totalServerIds = foreachBasicStatisticsVos.stream().map(BasicStatisticsVo::getServerId).distinct().collect(Collectors.toList());
                List<String> totalRoleServerIds = foreachBasicStatisticsVos.stream().map(BasicStatisticsVo::getRoleServerMd5).distinct().collect(Collectors.toList());
                List<String> totalPackageCodes = foreachBasicStatisticsVos.stream().map(BasicStatisticsVo::getPackageCode).distinct().collect(Collectors.toList());
                List<Integer> totalGameIds = foreachBasicStatisticsVos.stream().map(BasicStatisticsVo::getGameId).distinct().collect(Collectors.toList());
                basicStatisticsForVo.setNewIncrUserNum(totalRoleIds.size());
                // 获取查询订单的时候需要的列表
                SearchDataSummaryVo searchDataSummaryVo = new SearchDataSummaryVo();
                // 当天角色相关数据存入到指定集合中
                List<RoleSummaryVo> getTodayRoleData = foreachBasicStatisticsVos.stream().map(e -> new RoleSummaryVo(e.getRoleId(), e.getServerId(), e.getGameId(), e.getPackageCode(), e.getLoginTime())).collect(Collectors.toList());
//                log.info("当天角色基础数据获取：{}", JsonUtils.toJsonString(getTodayMemberData));
                searchDataSummaryVo.setRole(getTodayRoleData);
                // 我们按照时间标签获取到我们的开始时间和结束时间
                String sTime = a + " 00:00:00";
                String eTime = a + " 23:59:59";
                BusinessOrderSummaryVo businessOrderSummaryVo = getBusinessOrderList(sTime, eTime, searchDataSummaryVo);
//                log.info("当天查询出的订单数据：{}", JsonUtils.toJsonString(businessOrderSummaryVo));
                // 设置新增付费订单数
                basicStatisticsForVo.setNewIncrPayOrderNum(businessOrderSummaryVo.getNewIncrPayOrderNum());
                // 设置新增付费人数 - 这也也是进行过去重操作的
                basicStatisticsForVo.setNewIncrPayNum(businessOrderSummaryVo.getNewIncrPayNum());
                // 设置参与计算的分母
                int CalculateDenominator = totalRoleServerIds.size();
                if (CalculateDenominator > 0) {
                    basicStatisticsForVo.setNewIncrPayLv((Math.round(businessOrderSummaryVo.getNewIncrPayNum() * 10000 / CalculateDenominator) / 100.0));
                }
                // 设置新增付费金额
                basicStatisticsForVo.setNewIncrPayAmount(businessOrderSummaryVo.getNewIncrPayAmount());
                /**
                 * 开始计算LTV数据  我们在businessOrderSummaryVo中的 storageData里面存储了我们当天当前渠道当前游戏新增的充值角色列表
                 * 我们需要查询一下当前写着角色在我们查询时间区间段了内的充值情况  按照日期进行归类处理 这样我们就知道今天新增角色在其他天的充值情况了
                 */
                // 我们直接依据最外层我们获取的角色id集合进行查询
                QueryWrapper<BusinessOrderSimpleVo> orderWrapper = Wrappers.query();
                String searchStartTime = DateUtil.format(DateUtil.parse(dto.getStartTime()), "yyyy-MM-dd") + " 00:00:00";
                // 这个结束时间可能需要获取当前时间
                String searchEndTime = DateUtil.format(new Date(), "yyyy-MM-dd") + " 23:59:59";
                orderWrapper.between("create_time", searchStartTime, searchEndTime)
                    .eq("order_state", 2)
                    .eq("order_type", "G")
                    .in(totalGameIds.size() > 0, "game_id", totalGameIds)
                    .in(totalRoleIds.size() > 0, "game_role_id", totalRoleIds)
                    .in(totalServerIds.size() > 0, "game_server_id", totalServerIds)
//                    .in(totalPackageCodes.size() > 0, "game_package_code", totalPackageCodes)
                    .groupBy("date_format");

                List<BusinessOrderSimpleDateVo> businessOrderSimpleDateVos = businessOrderMapper.selectBusinessOrderSimpleDataBySql(orderWrapper);
//                log.info("根据订单中每个游戏获取到的指定天数的充值金额数据:{}", JsonUtils.toJsonString(businessOrderSimpleDateVos));
                // 获取当前游戏 当前时间 当前渠道新增角色充值数
                TreeMap<String, Double> stringBigDecimalTreeMap = calculationLTV(businessOrderSimpleDateVos, a, searchEndTime, CalculateDenominator, 1);
                // ltv数据写入返回实体中
                basicStatisticsForVo.setOneDay(stringBigDecimalTreeMap.get("oneDay"));
                basicStatisticsForVo.setTwoDay(stringBigDecimalTreeMap.get("twoDay"));
                basicStatisticsForVo.setThreeDay(stringBigDecimalTreeMap.get("threeDay"));
                basicStatisticsForVo.setFourDay(stringBigDecimalTreeMap.get("fourDay"));
                basicStatisticsForVo.setFiveDay(stringBigDecimalTreeMap.get("fiveDay"));
                basicStatisticsForVo.setSixDay(stringBigDecimalTreeMap.get("sixDay"));
                basicStatisticsForVo.setSevenDay(stringBigDecimalTreeMap.get("sevenDay"));
                basicStatisticsForVo.setFifteenDay(stringBigDecimalTreeMap.get("fifteenDay"));
                basicStatisticsForVo.setThirtyDay(stringBigDecimalTreeMap.get("thirtyDay"));
                basicStatisticsForVo.setSixtyDay(stringBigDecimalTreeMap.get("sixtyDay"));
                basicStatisticsForVo.setNinetyDay(stringBigDecimalTreeMap.get("ninetyDay"));
                basicStatisticsVoList.add(basicStatisticsForVo);
            });
            basicStatisticsReturnDataVo.setRows(basicStatisticsVoList);
            return basicStatisticsReturnDataVo;
        }
        basicStatisticsReturnDataVo.setRows(basicStatisticsVoList);
        return basicStatisticsReturnDataVo;
    }


    /**
     * 查询数据统计 - 每日的ltv基础数据统计列表
     * 全部从基础表中获取数据
     * 基础表中的数据每个5分钟会执行一次将数据进行入库
     * <p>
     * 新增角色 新增充值角色全部都是按照 memberId计算 所以我们需要对角色进行去重
     *
     * @return 数据统计 - 每日的ltv基础数据统计
     */
    @Override
    public BasicStatisticsReturnDataVo queryPageList(BasicStatisticsDto dto, Integer type) {
        // 如果前台没有传输时间 我们默认返回的是全部  开始时间从 2022-05-10 开始
        if (dto.getStartTime() == null || dto.getEndTime() == null) {
            dto.setStartTime("2022-05-10");
            dto.setEndTime(DateUtil.format(new Date(), "yyyy-MM-dd"));
        }
        // 设置分页查询
        List<BasicStatisticsReturnVo> basicStatisticsVoList = new ArrayList<>();
        List<String> searchInDate = StatisticsUtils.getDate(dto.getStartTime(), dto.getEndTime());
        // 从数据库中获取到基础数据
        QueryWrapper<BasicStatistics> basicStatisticsLambdaQueryWrapper = Wrappers.query();
        basicStatisticsLambdaQueryWrapper.in(true, "date_label", searchInDate)
            .eq((dto.getOperationPlatform() != null && dto.getOperationPlatform() != 0), "operation_platform", dto.getOperationPlatform())
            .eq(dto.getGameId() != null, "game_id", dto.getGameId())
            .eq(StringUtils.isNotBlank(dto.getGameName()), "game_name", dto.getGameName())
            .eq(dto.getChannelId() != null, "channel_id", dto.getChannelId())
            .eq(StringUtils.isNotBlank(dto.getChannelName()), "channel_name", dto.getChannelName())
            .eq(StringUtils.isNotBlank(dto.getPackageCode()), "package_code", dto.getPackageCode())
            // 如果是首日LTV的话需要去掉滚浮用户 （只有当日注册并且当日进行角色上报达特的才算）
            .apply(type == 2, "date_label = DATE_FORMAT(register_time, '%Y-%m-%d')")
            .orderByDesc("date_label")
            .groupBy("date_label", "select_key");

        IPage<BasicStatisticsVo> basicStatisticsVoIPage = baseMapper.selectVoPage(dto.build(), basicStatisticsLambdaQueryWrapper);

        // log.info("从BasicStatistics查询出基础的数据:{}", JsonUtils.toJsonString(basicStatisticsVoIPage.getRecords()));
        BasicStatisticsReturnDataVo basicStatisticsReturnDataVo = new BasicStatisticsReturnDataVo();
        basicStatisticsReturnDataVo.setTotal(basicStatisticsVoIPage.getTotal());
        basicStatisticsReturnDataVo.setPageSize(dto.getPageSize());
        basicStatisticsReturnDataVo.setPageNums(dto.getPageNum());
        // 获取查询列表中的渠道ID  用于我们查询出渠道名称
        if (basicStatisticsVoIPage.getRecords() != null && basicStatisticsVoIPage.getRecords().size() > 0) {
            // 循环计算数据
            basicStatisticsVoIPage.getRecords().forEach(a -> {
                // 列表中获取的每个渠道的对应游戏的单独数据 我们需要获取当前渠道的新增角色 新增充值角色 新增充值金额 还有对应的ltv
                // 先获取当前日期 当前渠道的的角色全部数据 包括角色id 渠道号 游戏id
                QueryWrapper<BasicStatistics> foreachEveryEq = Wrappers.query();
                foreachEveryEq.eq("select_key", a.getSelectKey())
                    .eq("date_label", a.getDateLabel())
                    .apply(type == 2, "date_label = DATE_FORMAT(register_time, '%Y-%m-%d')");
                // 这个是获取当前的基础数据
                List<BasicStatisticsVo> foreachBasicStatisticsVos = baseMapper.selectVoList(foreachEveryEq);
//                log.info("当天当前渠道的原始数据:{}", JsonUtils.toJsonString(foreachBasicStatisticsVos));
                // 当前渠道的数据获取完毕之后 我们先获取当前渠道的基础数据  就是出去ltv的数据
                BasicStatisticsReturnVo basicStatisticsForVo = new BasicStatisticsReturnVo();
                basicStatisticsForVo.setDateLabel(a.getDateLabel());
                basicStatisticsForVo.setChannelId(a.getChannelId());
                basicStatisticsForVo.setChannelName(a.getChannelName());
                basicStatisticsForVo.setPackageCode(a.getPackageCode());
                basicStatisticsForVo.setGameId(a.getGameId());
                basicStatisticsForVo.setGameName(a.getGameName());
                basicStatisticsForVo.setOperationPlatform(Convert.toStr(a.getOperationPlatform()));
                // 对新增角色+区服进行一下去重操作
                List<String> incrRoleDistinctList = foreachBasicStatisticsVos.stream().map(BasicStatisticsVo::getRoleId).distinct().collect(Collectors.toList());
                List<String> incrServerDistinctList = foreachBasicStatisticsVos.stream().map(BasicStatisticsVo::getServerId).distinct().collect(Collectors.toList());
                List<String> incrRoleServerIdDistinctList = foreachBasicStatisticsVos.stream().map(BasicStatisticsVo::getRoleServerMd5).distinct().collect(Collectors.toList());
                basicStatisticsForVo.setNewIncrUserNum(incrRoleServerIdDistinctList.size());
                // 获取查询订单的时候需要的列表
                SearchDataSummaryVo searchDataSummaryVo = new SearchDataSummaryVo();
                // 当天角色相关数据存入到指定集合中
                List<RoleSummaryVo> getTodayRoleData = foreachBasicStatisticsVos.stream().map(e -> new RoleSummaryVo(e.getRoleId(), e.getServerId(), e.getGameId(), e.getPackageCode(), e.getLoginTime())).collect(Collectors.toList());
//                log.info("当天角色基础数据获取：{}", JsonUtils.toJsonString(getTodayRoleData));
                searchDataSummaryVo.setRole(getTodayRoleData);
                // 我们按照时间标签获取到我们的开始时间和结束时间
                String sTime = a.getDateLabel() + " 00:00:00";
                String eTime = a.getDateLabel() + " 23:59:59";

                BusinessOrderSummaryVo businessOrderSummaryVo = getBusinessOrderList(sTime, eTime, searchDataSummaryVo);
//                log.info("当天当前渠道当前角色当前游戏充值的基础数据：{}", JsonUtils.toJsonString(businessOrderSummaryVo));
                // 设置新增付费订单数
                basicStatisticsForVo.setNewIncrPayOrderNum(businessOrderSummaryVo.getNewIncrPayOrderNum());
                basicStatisticsForVo.setNewIncrPayNum(businessOrderSummaryVo.getNewIncrPayNum());
                // 设置参与计算的分母[新增注册人数]
                int CalculateDenominator = incrRoleServerIdDistinctList.size();
                if (CalculateDenominator > 0) {
                    basicStatisticsForVo.setNewIncrPayLv((Math.round(businessOrderSummaryVo.getNewIncrPayNum() * 10000 / CalculateDenominator) / 100.0));
                }
                // 设置新增付费金额
                basicStatisticsForVo.setNewIncrPayAmount(businessOrderSummaryVo.getNewIncrPayAmount());
                /**
                 * 开始计算LTV数据  我们在businessOrderSummaryVo中的 storageData里面存储了我们当天当前渠道当前游戏新增的充值角色列表
                 * 我们需要查询一下当前写着角色在我们查询时间区间段了内的充值情况  按照日期进行归类处理 这样我们就知道今天新增角色在其他天的充值情况了
                 */
                // 按照最外层的时间进行获取我们想要的渠道+游戏+角色的每日充值数据
                QueryWrapper<BusinessOrderSimpleVo> orderWrapper = Wrappers.query();
                String searchStartTime = DateUtil.format(DateUtil.parse(dto.getStartTime()), "yyyy-MM-dd") + " 00:00:00";
                // 这个结束时间可能需要获取当前时间
                String searchEndTime = DateUtil.format(new Date(), "yyyy-MM-dd") + " 23:59:59";
                orderWrapper.between("create_time", searchStartTime, searchEndTime)
                    .eq("order_state", 2)
                    .eq("order_type", "G")
                    .eq("game_id", a.getGameId())
                    .in(incrRoleDistinctList.size() > 0, "game_role_id", incrRoleDistinctList)
                    .in(incrServerDistinctList.size() > 0, "game_server_id", incrServerDistinctList)
//                    .eq("game_package_code", a.getPackageCode())
                    .groupBy("date_format");
                List<BusinessOrderSimpleDateVo> businessOrderSimpleDateVos = businessOrderMapper.selectBusinessOrderSimpleDataBySql(orderWrapper);
//                log.info("根据订单中每个渠道每个游戏获取到的指定天数的充值金额数据:{}", JsonUtils.toJsonString(businessOrderSimpleDateVos));
                // 获取当前游戏 当前时间 当前渠道新增角色充值数
                TreeMap<String, Double> stringBigDecimalTreeMap = calculationLTV(businessOrderSimpleDateVos, a.getDateLabel(), searchEndTime, CalculateDenominator, type);
                // ltv数据写入返回实体中
                basicStatisticsForVo.setOneDay(stringBigDecimalTreeMap.get("oneDay"));
                basicStatisticsForVo.setTwoDay(stringBigDecimalTreeMap.get("twoDay"));
                basicStatisticsForVo.setThreeDay(stringBigDecimalTreeMap.get("threeDay"));
                basicStatisticsForVo.setFourDay(stringBigDecimalTreeMap.get("fourDay"));
                basicStatisticsForVo.setFiveDay(stringBigDecimalTreeMap.get("fiveDay"));
                basicStatisticsForVo.setSixDay(stringBigDecimalTreeMap.get("sixDay"));
                basicStatisticsForVo.setSevenDay(stringBigDecimalTreeMap.get("sevenDay"));
                basicStatisticsForVo.setFifteenDay(stringBigDecimalTreeMap.get("fifteenDay"));
                basicStatisticsForVo.setThirtyDay(stringBigDecimalTreeMap.get("thirtyDay"));
                basicStatisticsForVo.setSixtyDay(stringBigDecimalTreeMap.get("sixtyDay"));
                basicStatisticsForVo.setNinetyDay(stringBigDecimalTreeMap.get("ninetyDay"));
                basicStatisticsVoList.add(basicStatisticsForVo);
            });
            basicStatisticsReturnDataVo.setRows(basicStatisticsVoList);
            return basicStatisticsReturnDataVo;
        }
        basicStatisticsReturnDataVo.setRows(basicStatisticsVoList);
        return basicStatisticsReturnDataVo;
    }

    /**
     * 获取完新增角色之后需要获取这批角色的订单基础数据
     *
     * @param startTime
     * @param endTime
     * @param searchVo
     * @return
     */
    private BusinessOrderSummaryVo getBusinessOrderList(String startTime, String endTime, SearchDataSummaryVo searchVo) {
        // 每个新增角色组中
        NewIncrOrderUserVo newIncrOrderUserVo = new NewIncrOrderUserVo();
        // 首先获取角色id集合 游戏集合 分包集合
        List<String> getRoleIds = searchVo.getRole().stream().map(RoleSummaryVo::getRoleId).distinct().collect(Collectors.toList());
        List<String> getServerIds = searchVo.getRole().stream().map(RoleSummaryVo::getServerId).distinct().collect(Collectors.toList());
        List<Integer> getGameIds = searchVo.getRole().stream().map(RoleSummaryVo::getGameId).distinct().collect(Collectors.toList());
        List<String> getPackageCodes = searchVo.getRole().stream().map(RoleSummaryVo::getPackageCode).distinct().collect(Collectors.toList());

        QueryWrapper<BusinessOrderSimpleVo> orderWrapper = Wrappers.query();
        orderWrapper.between("create_time", startTime, endTime)
            .eq("order_state", 2)
            .eq("order_type", "G")
            .in(getGameIds.size() > 0, "game_id", getGameIds)
            .in(getRoleIds.size() > 0, "game_role_id", getRoleIds)
            .in(getServerIds.size() > 0, "game_server_id", getServerIds);
//            .in(getPackageCodes.size() > 0, "game_package_code", getPackageCodes);

        List<BusinessOrderSimpleVo> businessOrderSimpleVos = businessOrderMapper.selectBusinessOrderSimpleData(orderWrapper);
        // 开始进行数据循环获取新增数据 按照订单来说  如果这笔订单的产生时间和角色的注册时间一致  同时渠道相同 游戏相同 游戏角色相同 我们认为当天这笔充值算是新增充值角色
        // 由于我们获取的SearchDataSummaryVo中数据没有进行去重  所以这个地方直接判断就行  （没有去重就是代表着一个角色 同一个渠道 同一款游戏 会有2条 是因为他们的角色id不通）
        List<String> newIncrPayRoleServerIdsList = new ArrayList<>();
        BusinessOrderSummaryVo businessOrderSummaryVo = new BusinessOrderSummaryVo();
        businessOrderSimpleVos.forEach(a -> {
            Optional<RoleSummaryVo> first = searchVo.getRole().stream().filter(e ->
                e.getRoleId().equals(a.getGameRoleId())
                    && e.getServerId().equals(a.getGameServerId())
//                    && e.getPackageCode().equals(a.getGamePackageCode())
                    && e.getGameId().equals(a.getGameId())
                    && DateUtil.format(a.getCreateTime(), "yyyy/MM/dd").equals(DateUtil.format(e.getCreateTime(), "yyyy/MM/dd"))
            ).findFirst();
            if (first.isPresent()) {
                // 今天这个时间的新充值角色存放到一个集合中 用于我们LTV数据的统计  dateLabel+packcode+game_id组成的字符串
                String searchKey = DateUtil.format(a.getCreateTime(), "yyyy-MM-dd") + "#" + a.getGamePackageCode() + "#" + a.getGameId();
                String storageKey = newIncrOrderUserVo.getStorageKey();
                String md5Value = SecureUtil.md5(first.get().getRoleId() + first.get().getServerId());
                // TODO 到时候可以删除一些代码
                if (storageKey == null) {
                    newIncrPayRoleServerIdsList.add(md5Value);
                    List<String> roleIds = new ArrayList<>();
                    roleIds.add(first.get().getRoleId());
                    List<String> serverIds = new ArrayList<>();
                    serverIds.add(first.get().getServerId());
                    newIncrOrderUserVo.setStorageKey(searchKey);
                    newIncrOrderUserVo.setStorageRoleIds(roleIds);
                    newIncrOrderUserVo.setStorageServerIds(serverIds);
                } else {
                    if (!newIncrPayRoleServerIdsList.contains(md5Value)) {
                        newIncrPayRoleServerIdsList.add(md5Value);
                    }
                    // 存在直接添加
                    newIncrOrderUserVo.getStorageRoleIds().add(first.get().getRoleId());
                    newIncrOrderUserVo.getStorageServerIds().add(first.get().getServerId());
                }
                // 如果角色存在 那么我们就进行基础数据的累加
                // 设置新增付费订单数
                businessOrderSummaryVo.setNewIncrPayOrderNum(businessOrderSummaryVo.getNewIncrPayOrderNum() + 1);
                // 设置 累计付费金额
                businessOrderSummaryVo.setNewIncrPayAmount(NumberUtil.add(businessOrderSummaryVo.getNewIncrPayAmount(), a.getOrderAmount()));
                // 记录这些付费的人数 在指定天指定渠道的基础数据
                businessOrderSummaryVo.setNewIncrOrderUserVos(newIncrOrderUserVo);
            }
        });
        // 设置新增付费人数
        businessOrderSummaryVo.setNewIncrPayNum(newIncrPayRoleServerIdsList.size());
//        if (newIncrOrderUserVo != null && newIncrOrderUserVo.getStorageRoleIds() != null && newIncrOrderUserVo.getStorageServiceIds() != null) {
//            List<String> payNumsListDistinct = newIncrOrderUserVo.getStorageRoleIds().stream().distinct().collect(Collectors.toList());
//            businessOrderSummaryVo.setNewIncrPayNum(payNumsListDistinct.size());
//        }
        log.info("用作LTV数据计算的数据汇总：{}", JsonUtils.toJsonString(newIncrOrderUserVo));
        log.info("订单查询出来的基础数据:{}", JsonUtils.toJsonString(businessOrderSimpleVos));
        log.info("数据循环之后的汇总:{}", JsonUtils.toJsonString(businessOrderSummaryVo));
        return businessOrderSummaryVo;
    }

    /**
     * 计算ltv
     *
     * @param businessOrderSimpleDateVos
     * @param startTime
     * @param peopleNums
     * @return
     */
    private TreeMap<String, Double> calculationLTV(List<BusinessOrderSimpleDateVo> businessOrderSimpleDateVos, String startTime, String searchEndTime, Integer peopleNums, Integer type) {
//        log.info("入参打印：businessOrderSimpleDateVos{}, startTime:{}, searchEndTime:{},peopleNums:{}", JsonUtils.toJsonString(businessOrderSimpleDateVos), startTime, searchEndTime, peopleNums);
        // 数据获取完毕之后我们我们根据时间来进行计算1-90日的LTV数据  记住我们按照时间正序进行处理  第一天就是第一个的LTV  第二日的LTV就是第一天加上第二天的 依次类推
        // 需要记住的是 我们循环的时候要一次记录上一天的数据 用作数据累加计算的分子  至于分母  就是我们 storageMemberIds.size() 这个是不会变化的 变化的是分母
        // 数据由上面我们获取完毕了  现在我们开始进行数据的计算了 我们根据 searchInDate里面的数据进行计算
        // 以角色的订单数据为循环主体进行数据的循环输出
        // 声明一个初始的金额数
        BigDecimal nowDecimal = new BigDecimal(0);
        // 获取一个当天传输过来的startTime和 当前时间相差多少
        DateTime parse1 = DateUtil.parse(startTime + " 00:00:00");
        DateTime parse2 = DateUtil.parse(searchEndTime, "yyyy-MM-dd");
        long between = DateUtil.between(parse1, parse2, DateUnit.DAY);
        log.info("开始时间：{}, 结束时间：{}", parse1, parse2);
        log.info("展示出相差多少天：{}", between);
        TreeMap<String, Double> storageData = new TreeMap<>();
        storageData.put("oneDay", new Double(0));
        storageData.put("twoDay", new Double(0));
        storageData.put("threeDay", new Double(0));
        storageData.put("fourDay", new Double(0));
        storageData.put("fiveDay", new Double(0));
        storageData.put("sixDay", new Double(0));
        storageData.put("sevenDay", new Double(0));
        storageData.put("fifteenDay", new Double(0));
        storageData.put("thirtyDay", new Double(0));
        storageData.put("sixtyDay", new Double(0));
        storageData.put("ninetyDay", new Double(0));

        TreeMap<String, BigDecimal> storageDataBigDecimal = new TreeMap<>();
        storageDataBigDecimal.put("oneDay", new BigDecimal(0));
        storageDataBigDecimal.put("twoDay", new BigDecimal(0));
        storageDataBigDecimal.put("threeDay", new BigDecimal(0));
        storageDataBigDecimal.put("fourDay", new BigDecimal(0));
        storageDataBigDecimal.put("fiveDay", new BigDecimal(0));
        storageDataBigDecimal.put("sixDay", new BigDecimal(0));
        storageDataBigDecimal.put("sevenDay", new BigDecimal(0));
        storageDataBigDecimal.put("fifteenDay", new BigDecimal(0));
        storageDataBigDecimal.put("thirtyDay", new BigDecimal(0));
        storageDataBigDecimal.put("sixtyDay", new BigDecimal(0));
        storageDataBigDecimal.put("ninetyDay", new BigDecimal(0));

        DateTime parse = DateUtil.parse(startTime + " 00:00:00");
        String yyyyMMdd1 = DateUtil.format(parse, "yyyyMMdd");
        Integer integerOneDay = Convert.toInt(yyyyMMdd1);
        // 第一日
        Optional<BigDecimal> first = businessOrderSimpleDateVos.stream().filter(lwq -> lwq.getDateFormatNumbers().equals(integerOneDay)).map(BusinessOrderSimpleDateVo::getOrderAmounts).findFirst();
        if (first.isPresent() && between >= 0 && peopleNums > 0) {
            double v = Math.round(Convert.toDouble(first.get()) * 100 / peopleNums) / 100.0;
            storageData.put("oneDay", v);
            storageDataBigDecimal.put("oneDay", first.get());
        }
        // 类型为2 代表着是首日LTV 其他日的不需要获取 所以这个地方直接返回就行
        if (type == 2) {
            return storageData;
        }
        // 第二日
        String yyyyMMdd2 = DateUtil.format(DateUtil.offsetDay(parse, 1), "yyyyMMdd");
        BigDecimal twoDay = businessOrderSimpleDateVos.stream().filter(lwq -> lwq.getDateFormatNumbers() >= integerOneDay && lwq.getDateFormatNumbers() <= Convert.toInt(yyyyMMdd2)).map(BusinessOrderSimpleDateVo::getOrderAmounts).collect(Collectors.toList()).stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        if (twoDay != null && between >= 1 && peopleNums > 0) {
            double v = Math.round(Convert.toDouble(twoDay) * 100 / peopleNums) / 100.0;
            storageData.put("twoDay", v);
            storageDataBigDecimal.put("twoDay", twoDay);
        }

        // 第三日
        String yyyyMMdd3 = DateUtil.format(DateUtil.offsetDay(parse, 2), "yyyyMMdd");
        BigDecimal threeDay = businessOrderSimpleDateVos.stream().filter(lwq -> lwq.getDateFormatNumbers() >= integerOneDay && lwq.getDateFormatNumbers() <= Convert.toInt(yyyyMMdd3)).map(BusinessOrderSimpleDateVo::getOrderAmounts).collect(Collectors.toList()).stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        if (threeDay != null && between >= 2 && peopleNums > 0) {
            double v = Math.round(Convert.toDouble(threeDay) * 100 / peopleNums) / 100.0;
            storageData.put("threeDay", v);
            storageDataBigDecimal.put("threeDay", threeDay);
        }
        // 第四日
        String yyyyMMdd4 = DateUtil.format(DateUtil.offsetDay(parse, 3), "yyyyMMdd");
        BigDecimal fourDay = businessOrderSimpleDateVos.stream().filter(lwq -> lwq.getDateFormatNumbers() >= integerOneDay && lwq.getDateFormatNumbers() <= Convert.toInt(yyyyMMdd4)).map(BusinessOrderSimpleDateVo::getOrderAmounts).collect(Collectors.toList()).stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        if (fourDay != null && between >= 3 && peopleNums > 0) {
            double v = Math.round(Convert.toDouble(fourDay) * 100 / peopleNums) / 100.0;
            storageData.put("fourDay", v);
            storageDataBigDecimal.put("fourDay", fourDay);
        }
        // 第五日
        String yyyyMMdd5 = DateUtil.format(DateUtil.offsetDay(parse, 4), "yyyyMMdd");
        BigDecimal fiveDay = businessOrderSimpleDateVos.stream().filter(lwq -> lwq.getDateFormatNumbers() >= integerOneDay && lwq.getDateFormatNumbers() <= Convert.toInt(yyyyMMdd5)).map(BusinessOrderSimpleDateVo::getOrderAmounts).collect(Collectors.toList()).stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        if (fiveDay != null && between >= 4 && peopleNums > 0) {
            double v = Math.round(Convert.toDouble(fiveDay) * 100 / peopleNums) / 100.0;
            storageData.put("fiveDay", v);
            storageDataBigDecimal.put("fiveDay", fiveDay);
        }
        // 第六日
        String yyyyMMdd6 = DateUtil.format(DateUtil.offsetDay(parse, 5), "yyyyMMdd");
        BigDecimal sixDay = businessOrderSimpleDateVos.stream().filter(lwq -> lwq.getDateFormatNumbers() >= integerOneDay && lwq.getDateFormatNumbers() <= Convert.toInt(yyyyMMdd6)).map(BusinessOrderSimpleDateVo::getOrderAmounts).collect(Collectors.toList()).stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        if (sixDay != null && between >= 5 && peopleNums > 0) {
            double v = Math.round(Convert.toDouble(sixDay) * 100 / peopleNums) / 100.0;
            storageData.put("sixDay", v);
            storageDataBigDecimal.put("sixDay", sixDay);
        }
        // 第七日
        String yyyyMMdd7 = DateUtil.format(DateUtil.offsetDay(parse, 6), "yyyyMMdd");
        BigDecimal sevenDay = businessOrderSimpleDateVos.stream().filter(lwq -> lwq.getDateFormatNumbers() >= integerOneDay && lwq.getDateFormatNumbers() <= Convert.toInt(yyyyMMdd7)).map(BusinessOrderSimpleDateVo::getOrderAmounts).collect(Collectors.toList()).stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        if (sevenDay != null && between >= 6 && peopleNums > 0) {
            double v = Math.round(Convert.toDouble(sevenDay) * 100 / peopleNums) / 100.0;
            storageData.put("sevenDay", v);
            storageDataBigDecimal.put("sevenDay", sevenDay);
        }
        // 第十五日
        String yyyyMMdd15 = DateUtil.format(DateUtil.offsetDay(parse, 14), "yyyyMMdd");
        BigDecimal fifteenDay = businessOrderSimpleDateVos.stream().filter(lwq -> lwq.getDateFormatNumbers() >= integerOneDay && lwq.getDateFormatNumbers() <= Convert.toInt(yyyyMMdd15)).map(BusinessOrderSimpleDateVo::getOrderAmounts).collect(Collectors.toList()).stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        if (fifteenDay != null && between >= 14 && peopleNums > 0) {
            double v = Math.round(Convert.toDouble(fifteenDay) * 100 / peopleNums) / 100.0;
            storageData.put("fifteenDay", v);
            storageDataBigDecimal.put("fifteenDay", fifteenDay);
        }
        // 第三十日
        String yyyyMMdd30 = DateUtil.format(DateUtil.offsetDay(parse, 29), "yyyyMMdd");
        BigDecimal thirtyDay = businessOrderSimpleDateVos.stream().filter(lwq -> lwq.getDateFormatNumbers() >= integerOneDay && lwq.getDateFormatNumbers() <= Convert.toInt(yyyyMMdd30)).map(BusinessOrderSimpleDateVo::getOrderAmounts).collect(Collectors.toList()).stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        if (thirtyDay != null && between >= 29 && peopleNums > 0) {
            double v = Math.round(Convert.toDouble(thirtyDay) * 100 / peopleNums) / 100.0;
            storageData.put("thirtyDay", v);
            storageDataBigDecimal.put("thirtyDay", thirtyDay);
        }
        // 第六十日
        String yyyyMMdd60 = DateUtil.format(DateUtil.offsetDay(parse, 59), "yyyyMMdd");
        BigDecimal sixtyDay = businessOrderSimpleDateVos.stream().filter(lwq -> lwq.getDateFormatNumbers() >= integerOneDay && lwq.getDateFormatNumbers() <= Convert.toInt(yyyyMMdd60)).map(BusinessOrderSimpleDateVo::getOrderAmounts).collect(Collectors.toList()).stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        if (sixtyDay != null && between >= 59 && peopleNums > 0) {
            double v = Math.round(Convert.toDouble(sixtyDay) * 100 / peopleNums) / 100.0;
            storageData.put("sixtyDay", v);
            storageDataBigDecimal.put("sixtyDay", sixtyDay);
        }
        // 第九十日
        String yyyyMMdd90 = DateUtil.format(DateUtil.offsetDay(parse, 89), "yyyyMMdd");
        BigDecimal ninetyDay = businessOrderSimpleDateVos.stream().filter(lwq -> lwq.getDateFormatNumbers() >= integerOneDay && lwq.getDateFormatNumbers() <= Convert.toInt(yyyyMMdd90)).map(BusinessOrderSimpleDateVo::getOrderAmounts).collect(Collectors.toList()).stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        if (ninetyDay != null && between >= 89 && peopleNums > 0) {
            double v = Math.round(Convert.toDouble(ninetyDay) * 100 / peopleNums) / 100.0;
            storageData.put("ninetyDay", v);
            storageDataBigDecimal.put("ninetyDay", ninetyDay);
        }
        log.info("计算完之后的基础LTV金钱数据:{}", JsonUtils.toJsonString(storageDataBigDecimal));
        log.info("计算完之后的基础LTV数据:{}", JsonUtils.toJsonString(storageData));
        return storageData;
    }


    /**
     * 注册数据
     *
     * @param dto
     * @return
     */
    @Override
    public UserRegisterBaseReturnDataVo registerList(BasicStatisticsDto dto) {
        // 如果前台没有传输时间 我们默认返回的是全部  开始时间从 2022-05-10 开始
        if (dto.getStartTime() == null || dto.getEndTime() == null) {
            dto.setStartTime("2022-05-10");
            dto.setEndTime(DateUtil.format(new Date(), "yyyy-MM-dd"));
        }
        // 首先按照指定要求查询出新增角色数
        QueryWrapper<BasicStatistics> wrapper = Wrappers.query();
        wrapper.between("mp.create_time", DateUtils.getBeginOfDay(dto.getStartTime()), DateUtils.getEndOfDay(dto.getEndTime()))
            .eq(dto.getOperationPlatform() != null && dto.getOperationPlatform() != 0, "og.operation_platform", dto.getOperationPlatform())
            .eq(dto.getGameId() != null, "mp.game_id", dto.getGameId())
            .eq(StringUtils.isNotBlank(dto.getGameName()), "mp.game_name", dto.getGameName())
            .eq(dto.getChannelId() != null, "mp.channel_id", dto.getChannelId())
            .eq(StringUtils.isNotBlank(dto.getChannelName()), "mp.channel_name", dto.getChannelName())
            .eq(StringUtils.isNotBlank(dto.getPackageCode()), "mp.package_code", dto.getPackageCode())
            .ne("mp.package_code", "default");

        // 首先根据这个条件查询总数据
        IdDto<Long> integerIdDto = baseMapper.selectBasisForRegisterByCount(wrapper);
        // 查询出汇总的这些角色那些进行了角色上报
        QueryWrapper<BasicStatistics> wrapperGame = Wrappers.query();
        wrapperGame.between("gum.create_time", DateUtils.getBeginOfDay(dto.getStartTime()), DateUtils.getEndOfDay(dto.getEndTime()))
            .eq(dto.getOperationPlatform() != null && dto.getOperationPlatform() != 0, "og.operation_platform", dto.getOperationPlatform())
            .eq(dto.getGameId() != null, "gum.game_id", dto.getGameId())
            .eq(StringUtils.isNotBlank(dto.getGameName()), "gum.game_name", dto.getGameName())
            .eq(dto.getChannelId() != null, "gum.channel_id", dto.getChannelId())
            .eq(StringUtils.isNotBlank(dto.getChannelName()), "gum.channel_name", dto.getChannelName())
            .eq(StringUtils.isNotBlank(dto.getPackageCode()), "gum.package_code", dto.getPackageCode())
            .apply("DATE_FORMAT(gum.create_time, '%Y-%m-%d') = DATE_FORMAT(gum.register_time, '%Y-%m-%d')")
            .ne("gum.package_code", "default");
        IdDto<Long> gumIdDto = baseMapper.selectBasisForGameUserManagerByCount(wrapperGame);
        log.info("member_profile:{}, game_user_management:{}", integerIdDto.getId(), gumIdDto.getId());
        List<UserRegisterBaseDataVo> returnList = new ArrayList<>();
        // 把今天的数据累加一下
        UserRegisterBaseDataVo userRegisterBaseDataVo = new UserRegisterBaseDataVo();
        userRegisterBaseDataVo.setSelectKey("all");
        userRegisterBaseDataVo.setDateLabel("汇总");
        userRegisterBaseDataVo.setChannelId(0);
        userRegisterBaseDataVo.setChannelName("all");
        userRegisterBaseDataVo.setGameId(0);
        userRegisterBaseDataVo.setPackageCode("all");
        userRegisterBaseDataVo.setGameName("all");
        userRegisterBaseDataVo.setOperationPlatform("0");
        userRegisterBaseDataVo.setIncrNums(Convert.toInt(integerIdDto.getId()));
        userRegisterBaseDataVo.setIncrReportNums(Convert.toInt(gumIdDto.getId()));
        returnList.add(userRegisterBaseDataVo);
        // 设置排序分组
        wrapper.orderByDesc("date_label")
            .groupBy("date_label, select_key");
        wrapperGame.orderByDesc("date_label")
            .groupBy("date_label, select_key");
        // 获取列表的分页总条数
        // 开始进行每个渠道每个游戏的数据统计
        UserRegisterBaseReturnDataVo userRegisterBaseReturnDataVo = new UserRegisterBaseReturnDataVo();
        Page<UserRegisterBaseDataVo> userRegisterBaseDataVoPage = baseMapper.selectBasisForRegister(dto.build(), wrapper);
        Page<UserRegisterBaseDataVo> gameUserManagementBaseDataVoPage = baseMapper.selectBasisForGameUserManagement(dto.build(), wrapperGame);
        log.info("汇总出来的数据:{}", JsonUtils.toJsonString(userRegisterBaseDataVoPage));
        userRegisterBaseReturnDataVo.setTotal(userRegisterBaseDataVoPage.getTotal());
        userRegisterBaseReturnDataVo.setPageSize(dto.getPageSize());
        userRegisterBaseReturnDataVo.setPageNums(dto.getPageNum());
        // 循环数据进行累加
        userRegisterBaseDataVoPage.getRecords().forEach(a -> {
            Optional<UserRegisterBaseDataVo> first = gameUserManagementBaseDataVoPage.getRecords().stream().filter(c -> c.getDateLabel().equals(a.getDateLabel())
                && c.getSelectKey().equals(a.getSelectKey())).findFirst();
            if (!first.isPresent()) {
                a.setIncrReportNums(0);
            } else {
                a.setIncrReportNums(first.get().getIncrReportNums());
            }
        });
        returnList.addAll(userRegisterBaseDataVoPage.getRecords());
        userRegisterBaseReturnDataVo.setRows(returnList);
        // 数据融合到一块
        return userRegisterBaseReturnDataVo;
    }


    /**
     * 每日数据入库到数据库中
     * 每五分钟执行一次 将新数据入库
     * 不按照时间来 就按照基础表中最后一条数据的值来判断跑到那个地步了
     *
     * @return
     */
    @Override
    public void getDataIntoMysql() {
        // 查询出基础表中的最后一条数据
        LambdaQueryWrapper<BasicStatistics> last = Wrappers.<BasicStatistics>lambdaQuery().orderByDesc(BasicStatistics::getId).last("limit 1");
        BasicStatistics lastBasicStatisticsData = baseMapper.selectOne(last);
        Integer startNums = 0;
        if (lastBasicStatisticsData != null) {
            startNums = lastBasicStatisticsData.getGumId();
        }
        // 拼接基础查询条件
        QueryWrapper<GameUserBaseDataVo> wrapper = Wrappers.query();
        wrapper.gt("gum.id", startNums);
        wrapper.last("limit 5000");
        List<GameUserBaseDataVo> gameUserBaseDataVos = baseMapper.selectPointTimeList(wrapper);
        log.info("定时查询出的数据结果：{}", JsonUtils.toJsonString(gameUserBaseDataVos));
        // 判断再次运行的时候是否存在数据
        if (gameUserBaseDataVos != null && gameUserBaseDataVos.size() > 0) {
            // 判断数据库中是否已经存在昨天的数据
            // 数据入库
            List<BasicStatistics> basicStatisticsList = new ArrayList<>();
            // 分批次数据数据
            for (GameUserBaseDataVo gameUserBaseDataVo : gameUserBaseDataVos) {
                Optional<BasicStatistics> first = basicStatisticsList.stream().filter(a -> a.getGumId().equals(gameUserBaseDataVo.getId())).findFirst();
                if (first.isPresent()) {
                    continue;
                }
                BasicStatistics basicStatistics = new BasicStatistics();
                basicStatistics.setSelectKey(gameUserBaseDataVo.getSelectKey());
                basicStatistics.setGumId(gameUserBaseDataVo.getId());
                basicStatistics.setRoleId(gameUserBaseDataVo.getRoleId());
                basicStatistics.setServerId(gameUserBaseDataVo.getServerId());
                basicStatistics.setRoleServerMd5(SecureUtil.md5(gameUserBaseDataVo.getRoleId() + gameUserBaseDataVo.getServerId()));
                basicStatistics.setChannelId(gameUserBaseDataVo.getChannelId());
                basicStatistics.setChannelName(gameUserBaseDataVo.getChannelName());
                basicStatistics.setPackageCode(gameUserBaseDataVo.getPackageCode());
                basicStatistics.setGameId(gameUserBaseDataVo.getGameId());
                basicStatistics.setGameName(gameUserBaseDataVo.getGameName());
                basicStatistics.setMemberId(gameUserBaseDataVo.getMemberId());
                basicStatistics.setOperationPlatform(gameUserBaseDataVo.getOperationPlatform());
                basicStatistics.setDateLabel(DateUtil.format(gameUserBaseDataVo.getCreateTime(), "yyyy-MM-dd"));
                basicStatistics.setLoginTime(gameUserBaseDataVo.getCreateTime());
                basicStatistics.setRegisterTime(gameUserBaseDataVo.getRegisterTime());
                basicStatistics.setCreateBy("定时服务");
                basicStatistics.setCreateTime(new Date());
                basicStatistics.setUpdateBy("定时服务");
                basicStatistics.setUpdateTime(new Date());
                basicStatistics.setDelFlag("0");
                basicStatisticsList.add(basicStatistics);
            }
            baseMapper.insertBatch(basicStatisticsList);
            log.info("数据入库成功");
        }
        log.info("当前查询出来的数据为空");
    }


}
