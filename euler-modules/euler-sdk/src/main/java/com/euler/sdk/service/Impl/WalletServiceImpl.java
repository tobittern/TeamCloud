package com.euler.sdk.service.Impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.euler.common.core.constant.Constants;
import com.euler.common.core.constant.UserConstants;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdTypeDto;
import com.euler.common.core.domain.dto.RequestHeaderDto;
import com.euler.common.core.exception.ServiceException;
import com.euler.common.core.utils.*;
import com.euler.common.redis.utils.LockHelper;
import com.euler.common.satoken.utils.LoginHelper;
import com.euler.payment.api.RemoteOrderService;
import com.euler.platform.api.RemoteGameManagerService;
import com.euler.platform.api.domain.OpenGameDubboVo;
import com.euler.sdk.api.domain.GameConfigVo;
import com.euler.sdk.api.domain.WalletVo;
import com.euler.sdk.api.enums.RechargeTypeEnum;
import com.euler.sdk.domain.dto.CashOutDto;
import com.euler.sdk.domain.dto.GetWalletDto;
import com.euler.sdk.domain.dto.WalletDto;
import com.euler.sdk.domain.entity.CashOutRule;
import com.euler.sdk.domain.entity.Wallet;
import com.euler.sdk.domain.entity.WalletLog;
import com.euler.sdk.domain.vo.CashOutRuleVo;
import com.euler.sdk.domain.vo.WalletLogVo;
import com.euler.sdk.mapper.WalletLogMapper;
import com.euler.sdk.mapper.WalletMapper;
import com.euler.sdk.service.*;
import com.euler.system.api.RemoteDictService;
import com.euler.system.api.domain.SysDictData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 钱包Service业务层处理
 *
 * @author euler
 * @date 2022-03-28
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class WalletServiceImpl extends ServiceImpl<WalletMapper, Wallet> implements IWalletService {

    private final WalletMapper baseMapper;
    @Autowired
    private LockHelper lockHelper;

    @Autowired
    private IWalletLogService walletLogService;

    @Autowired
    private ICashOutRuleService cashOutRuleService;
    @Autowired
    private IMemberService memberService;
    @Autowired
    private WalletLogMapper walletLogMapper;
    @Autowired
    private IGameConfigService gameConfigService;
    @DubboReference
    private RemoteOrderService remoteOrderService;
    @DubboReference
    private RemoteDictService remoteDictService;
    @DubboReference
    private RemoteGameManagerService remoteGameManagerService;

    /**
     * 查询个人钱包
     *
     * @param getWalletDto
     * @return
     */
    @Override
    public WalletVo queryByMemberId(GetWalletDto getWalletDto) {
        Integer gameId = Convert.toInt(getWalletDto.getGameId(), 0);
        if (getWalletDto.getType() == null || getWalletDto.getType() == 1)
            gameId = 0;

        if (gameId == 0 && getWalletDto.getType() == 2) {
            log.error("查询钱包信息失败:{}", JsonHelper.toJson(getWalletDto));
            throw new ServiceException("查询钱包信息失败");
        }
        String keys = StringUtils.format("{}lock:walletquery:{}:{}", Constants.BASE_KEY, getWalletDto.getType().toString() + ("_" + gameId), getWalletDto.getId());
        var lockInfo = lockHelper.lock(keys);

        if (lockInfo == null)
            throw new ServiceException("账户正在操作，请稍后重试");

        try {
            var queryWrapper = Wrappers.<Wallet>lambdaQuery().eq(Wallet::getMemberId, getWalletDto.getId()).eq(Wallet::getWalletType, getWalletDto.getType()).eq(Wallet::getGameId, gameId).last("limit 1");

            var wallet = baseMapper.selectVoOne(queryWrapper);
            if (wallet == null) {
                if (save(new Wallet(getWalletDto.getId(), getWalletDto.getType(), gameId))) {
                    wallet = baseMapper.selectVoOne(queryWrapper);
                }
            }

            if (getWalletDto.getType().equals(2)) {
                //获取规则
                List<CashOutRule> cashOutRules = cashOutRuleService.list(Wrappers.<CashOutRule>lambdaQuery().eq(CashOutRule::getRuleStatus, UserConstants.NORMAL));
                if (cashOutRules != null && !cashOutRules.isEmpty()) {
                    for (var rule : cashOutRules) {
                        if (rule.getRuleType().equals(RechargeTypeEnum.score.getCode()) && rule.getCashOutMoney().compareTo(Convert.toBigDecimal(wallet.getScore())) < 1) {
                            wallet.setScoreStatus(UserConstants.NORMAL);
                            continue;
                        }
                        if (rule.getRuleType().equals(RechargeTypeEnum.balance.getCode()) && rule.getCashOutMoney().compareTo(wallet.getBalance()) < 1) {
                            wallet.setBalanceStatus(UserConstants.NORMAL);
                            continue;
                        }
                        if (rule.getRuleType().equals(RechargeTypeEnum.platform_currency.getCode()) && rule.getCashOutMoney().compareTo(Convert.toBigDecimal(wallet.getPlatformCurrency())) < 1) {
                            wallet.setPlatformCurrencyStatus(UserConstants.NORMAL);
                            continue;
                        }
                        if (rule.getRuleType().equals(RechargeTypeEnum.growth_value.getCode()) && rule.getCashOutMoney().compareTo(Convert.toBigDecimal(wallet.getGrowthValue())) < 1) {
                            wallet.setGrowthValueStatus(UserConstants.NORMAL);
                            continue;
                        }
                    }
                }
            }
            return wallet;
        } finally {
            lockHelper.unLock(lockInfo);
        }
    }

    /**
     * 钱包金额变动
     *
     * @param memberId         会员id
     * @param walletType       钱包类型，1：正常钱包，2：虚拟钱包
     * @param numValue         变动金额，负数为减
     * @param rechargeTypeEnum 变动类型，1：积分，2：余额，3：平台币，4：成长值
     * @param isAdd            增减，1：增加，2：减少
     * @param modifyDesc       修改备注
     * @return
     */
    @Override
    public boolean modifyWallet(Long memberId, Integer gameId, Integer walletType, Number numValue, RechargeTypeEnum rechargeTypeEnum, Integer isAdd, String modifyDesc) {
        return modifyWallet(memberId, gameId, walletType, 1, numValue, rechargeTypeEnum, isAdd, modifyDesc);
    }

    /**
     * 钱包金额变动
     *
     * @param memberId         会员id
     * @param walletType       钱包类型，1：正常钱包，2：虚拟钱包
     * @param walletOpType     钱包操作类型，1：正常增减，2：提现
     * @param numValue         变动金额，负数为减
     * @param rechargeTypeEnum 变动类型，1：积分，2：余额，3：平台币，4：成长值
     * @param isAdd            增减，1：增加，2：减少
     * @param modifyDesc       修改备注
     * @return
     */
    private boolean modifyWallet(Long memberId, Integer gameId, Integer walletType, Integer walletOpType, Number numValue, RechargeTypeEnum rechargeTypeEnum, Integer isAdd, String modifyDesc) {
        WalletDto walletDto = new WalletDto();
        walletDto.setMemberId(memberId)
            .setNumValue(numValue)
            .setRechargeTypeEnum(rechargeTypeEnum)
            .setIsAdd(isAdd)
            .setModifyDesc(modifyDesc)
            .setWalletOpType(walletOpType)
            .setWalletType(walletType).setGameId(gameId);
        return modifyWallet(walletDto);
    }


    /**
     * 钱包金额变动
     *
     * @param walletDto
     * @return
     */
    @Override
    public boolean modifyWallet(WalletDto walletDto) {
        Integer gameId = walletDto.getWalletType() == 2 ? walletDto.getGameId() : 0;

        String keys = StringUtils.format("{}lock:wallet:{}:{}", Constants.BASE_KEY, walletDto.getWalletType().toString() + ("_" + gameId), walletDto.getMemberId());
        var lockInfo = lockHelper.lock(keys);

        if (lockInfo == null)
            throw new ServiceException("账户正在操作，请稍后重试");
        var wallet = queryByMemberId(new GetWalletDto(walletDto.getMemberId(), walletDto.getWalletType(), gameId));
        var member = memberService.getById(wallet.getMemberId());


        LambdaUpdateChainWrapper<Wallet> updateChainWrapper;
        try {
            if (member == null || !UserConstants.NORMAL.equals(member.getStatus()))
                throw new ServiceException("账户不存在或已锁定");

            if (wallet == null || !UserConstants.NORMAL.equals(wallet.getStatus()))
                throw new ServiceException("账户不存在或已锁定");
            updateChainWrapper = new LambdaUpdateChainWrapper<>(baseMapper);
            updateChainWrapper.eq(Wallet::getMemberId, walletDto.getMemberId()).eq(Wallet::getWalletType, walletDto.getWalletType()).set(Wallet::getUpdateTime, new Date()).eq(Wallet::getGameId, gameId).set(Wallet::getUpdateBy, member.getAccount());
            switch (walletDto.getRechargeTypeEnum()) {
                case score:
                    Long res = NumberUtil.add(wallet.getScore(), walletDto.getNumValue()).longValue();
                    updateChainWrapper.set(Wallet::getScore, res);
                    break;
                case balance:
                    BigDecimal banlance = NumberUtil.add(wallet.getBalance(), walletDto.getNumValue());
                    updateChainWrapper.set(Wallet::getBalance, banlance);
                    break;
                case platform_currency:
                    Long resc = NumberUtil.add(wallet.getPlatformCurrency(), walletDto.getNumValue()).longValue();

                    updateChainWrapper.set(Wallet::getPlatformCurrency, resc);
                    break;
                case growth_value:
                    Long resg = NumberUtil.add(wallet.getGrowthValue(), walletDto.getNumValue()).longValue();
                    updateChainWrapper.set(Wallet::getGrowthValue, resg);
                    break;
            }
        } finally {
            lockHelper.unLock(lockInfo);
        }
        boolean flag = updateChainWrapper.update();
        WalletLog walletLog = new WalletLog();
        walletLog.setWalletId(wallet.getId());
        walletLog.setMemberId(walletDto.getMemberId());
        walletLog.setChangeType(walletDto.getRechargeTypeEnum().getCode());
        walletLog.setChangeValue(new BigDecimal(walletDto.getNumValue().toString()));
        walletLog.setIsAdd(walletDto.getIsAdd());
        walletLog.setDescription(walletDto.getModifyDesc());
        walletLog.setWalletType(walletDto.getWalletType());
        walletLog.setWalletOpType(walletDto.getWalletOpType());
        walletLog.setGameId(gameId);
        // 钱包类型，1：正常钱包，2：虚拟钱包
        if(walletDto.getWalletType() == 1 && walletDto.getRechargeTypeEnum().getCode() == 2) {
            // 3:达到额度可提现
            walletLog.setBalanceType(3);
        } else {
            // 2:不可提现
            walletLog.setBalanceType(2);
        }
        walletLogService.save(walletLog);
        return flag;

    }

    /**
     * 提取虚拟钱包金额到真实钱包
     *
     * @param idTypeDto
     * @return
     */
    @Transactional
    @Override
    public R<Boolean> cashOutToWallet(IdTypeDto<Long, Integer> idTypeDto) {
        //获取虚拟钱包金额
        Integer gameId = LoginHelper.getSdkChannelPackage().getGameId();

        var wallet = queryByMemberId(new GetWalletDto(idTypeDto.getId(), 2, gameId));

        if (wallet == null && !UserConstants.NORMAL.equals(wallet.getStatus())) {
            return R.fail("钱包未启用");
        }

        RechargeTypeEnum rechargeTypeEnum = RechargeTypeEnum.find(idTypeDto.getType());
        BigDecimal cashOut = BigDecimal.ZERO;
        switch (rechargeTypeEnum) {
            case score:
                cashOut = Convert.toBigDecimal(wallet.getScore());
                break;
            case balance:
                cashOut = wallet.getBalance();
                break;
            case platform_currency:
                cashOut = Convert.toBigDecimal(wallet.getPlatformCurrency());
                break;
        }
        //提现金额简单判断
        if (cashOut == null || cashOut.compareTo(BigDecimal.ZERO) == 0)
            return R.fail("无待提现" + rechargeTypeEnum.getDesc());

        //获取规则
        List<CashOutRule> cashOutRules = cashOutRuleService.list(Wrappers.<CashOutRule>lambdaQuery().eq(CashOutRule::getRuleType, idTypeDto.getType()).eq(CashOutRule::getRuleStatus, UserConstants.NORMAL));
        if (cashOutRules != null && !cashOutRules.isEmpty()) {
            for (var rule : cashOutRules) {
                if (rule.getCashOutMoney().compareTo(cashOut) > 0)
                    return R.fail("未达到提现最低限额");

                Date today = new Date();
                //获取当日提现次数
                long count = walletLogService.count(Wrappers.<WalletLog>lambdaQuery().eq(WalletLog::getMemberId, idTypeDto.getId()).eq(WalletLog::getWalletType, 2).eq(WalletLog::getWalletOpType, 2).eq(WalletLog::getChangeType, idTypeDto.getType()).between(WalletLog::getCreateTime, DateUtils.getBeginOfDay(today), DateUtils.getEndOfDay(today)));
                if (count >= rule.getDayNum()) {
                    return R.fail("今日提现次数已达上限");
                }
            }
        }

        //提现操作
        //1、减少虚拟钱包
        boolean subFlag = modifyWallet(idTypeDto.getId(), gameId, 2, 2, cashOut.negate(), rechargeTypeEnum, 2, "提现");
        //2、增加真实钱包
        boolean addFlag = modifyWallet(idTypeDto.getId(), gameId, 1, 2, cashOut, rechargeTypeEnum, 1, "提现");

        if (subFlag && addFlag)
            return R.ok(true);
        return R.fail("提现失败，请稍后重试");
    }

    /**
     * 获取sdk钱包菜单
     *
     * @param walletMenuType 钱包菜单，sdk_wallet_menu:真实钱包，sdk_virtual_wallet_menu
     * @return
     */
    @Override
    public R getGamePayTypeList(String walletMenuType) {
        List<Map> newList = new ArrayList<>();
        boolean hasGameConfig = false;
        JSONObject jsonObject = new JSONObject();
        RequestHeaderDto headerDto = HttpRequestHeaderUtils.getFromHttpRequest();
        if (headerDto != null && headerDto.getAppId() != null) {
            // 通过appid查询游戏信息
            OpenGameDubboVo openGameDubboVo = remoteGameManagerService.selectOpenGameInfo(headerDto.getAppId());
            if (openGameDubboVo != null && openGameDubboVo.getId() > 0) {
                // 查询是否有单游戏配置
                GameConfigVo vo = new GameConfigVo();
                // 游戏配置类型 1:SDK菜单 2:SDK钱包菜单 3:SDK虚拟钱包菜单 4:游戏支付方式 5:苹果应用类支付条件
                if(StringUtils.equals("sdk_wallet_menu", walletMenuType)) {
                    vo = gameConfigService.selectGameConfigByParam(openGameDubboVo.getId(), "2", Convert.toStr(openGameDubboVo.getOperationPlatform()));
                } else {
                    vo = gameConfigService.selectGameConfigByParam(openGameDubboVo.getId(), "3", Convert.toStr(openGameDubboVo.getOperationPlatform()));
                }
                if (vo != null) {
                    hasGameConfig = true;
                    jsonObject = JSONUtil.parseObj(vo.getData());
                }
            }
        }

        if(StringUtils.equals("sdk_wallet_menu", walletMenuType)) {
            List<SysDictData> data = remoteDictService.selectDictDataByType("sdk_wallet_menu");
            // 如果有单个游戏配置，返回游戏配置信息
            if(hasGameConfig) {
                JSONObject finalJsonObject = jsonObject;
                List<SysDictData> first = data.stream().filter(c -> Convert.toBool(finalJsonObject.get(c.getDictLabel()))).collect(Collectors.toList());
                data =  first;
            }

            List<Map> list = getBalanceStatus(headerDto, data);
            newList = list;
        } else {
            List<SysDictData> data = remoteDictService.selectDictDataByType(walletMenuType);

            // 如果有单个游戏配置，返回游戏配置信息
            if(hasGameConfig) {
                JSONObject finalJsonObject = jsonObject;
                List<SysDictData> first = data.stream().filter(c -> Convert.toBool(finalJsonObject.get(c.getDictLabel()))).collect(Collectors.toList());
                data =  first;
            }

            List<Map> list = new ArrayList<>();
            if (data != null && !data.isEmpty()) {
                data.forEach(a -> {
                    if (StringUtils.isNotEmpty(a.getRemark())) {
                        Map sdkMenuMap = JsonUtils.parseMap(a.getRemark());
                        list.add(sdkMenuMap);
                    }
                });
                newList = list;
            }
        }
        return R.ok(newList);
    }

    /**
     * 获取钱包开关状态
     */
    private List<Map> getBalanceStatus(RequestHeaderDto headerDto, List<SysDictData> data) {
        List<Map> list = new ArrayList<>();
        if (headerDto != null && StringUtils.isNotBlank(headerDto.getPlatform())) {
            // 平台，1：sdk，2：开放平台，3：管理后台 4：九区玩家
            if (StringUtils.equals("4", headerDto.getPlatform())) {
                if (data != null && !data.isEmpty()) {
                    data.forEach(a -> {
                        if (!a.getDictValue().equals("balance")) {
                            Map map = JsonUtils.parseMap(a.getRemark());
                            list.add(map);
                        }
                    });
                }
            } else {
                if (data != null && !data.isEmpty()) {
                    data.forEach(a -> {
                        if (!a.getDictValue().equals("app_balance")) {
                            Map map = JsonUtils.parseMap(a.getRemark());
                            list.add(map);
                        }
                    });
                }
            }
        }
        return list;
    }

    /**
     * 获取提现按钮开关状态
     *
     * @param withdrawalType 提现按钮开关
     * @return
     */
    @Override
    public R getWithdrawalSwitch(String withdrawalType) {
        List<SysDictData> data = remoteDictService.selectDictDataByType(withdrawalType);
        List<Map> list = new ArrayList<>();

        RequestHeaderDto headerDto = HttpRequestHeaderUtils.getFromHttpRequest();
        if (headerDto != null) {
            String platform = headerDto.getPlatform();
            if (StringUtils.isNotBlank(platform)) {
                // 平台，1：sdk，2：开放平台，3：管理后台 4：九区玩家
                if (StringUtils.equals("4", platform)) {
                    if (data != null && !data.isEmpty()) {
                        data.forEach(a -> {
                            if (a.getDictValue().equals("app_switch")) {
                                Map map = JsonUtils.parseMap(a.getRemark());
                                list.add(map);
                            }
                        });
                    }
                } else if (StringUtils.equals(Constants.PLATFORM_TYPE_SDK, platform)){
                    if (data != null && !data.isEmpty()) {
                        data.forEach(a -> {
                            if (a.getDictValue().equals("sdk_switch")) {
                                Map map = JsonUtils.parseMap(a.getRemark());
                                list.add(map);
                            }
                        });
                    }
                }
            }
        }
        return R.ok(list);
    }

    /**
     * 查询钱包里可提现金额
     *
     * @param memberId
     */
    @Override
    public WalletLogVo getBalanceTotal(Long memberId) {
        return walletLogMapper.getBalanceTotal(memberId);
    }

    /**
     * 获取当日提现次数
     *
     * @param memberId 用户id
     * @param walletType 钱包类型，1：正常钱包，2：虚拟钱包
     * @param walletOpType 钱包操作类型，1：正常增减，2：提现
     * @param changeType 钱包值变动类型，1-积分，2-余额，3-平台币，4-成长值
     */
    @Override
    public long getCashOutCount(Long memberId, Integer walletType, Integer walletOpType, Integer changeType) {
        Date today = new Date();
        long count = walletLogService.count(Wrappers.<WalletLog>lambdaQuery()
            .eq(WalletLog::getMemberId, memberId).eq(WalletLog::getWalletType, walletType)
            .eq(WalletLog::getWalletOpType, walletOpType)
            .eq(WalletLog::getChangeType, changeType)
            // 余额类型 1:可提现 2:不可提现 3:达到额度可提现
            .and(a -> a.eq(WalletLog:: getBalanceType, 1)
                .or(b -> b.eq(WalletLog:: getBalanceType, 3)))
            .between(WalletLog::getCreateTime, DateUtils.getBeginOfDay(today), DateUtils.getEndOfDay(today)));
        return count;
    }

    /**
     * 提现到微信
     */
    @Transactional
    @Override
    public R<String> cashOutToWX(CashOutDto dto) {
        // 查询钱包里可提现金额
        WalletLogVo info = getBalanceTotal(dto.getMemberId());
        if (info == null || info.getChangeValue().compareTo(BigDecimal.ZERO) == 0) {
            return R.fail("没有可提现金额");
        }
        log.info("=====可提现金额:========"+ info.getChangeValue());
        // 提现金额的校验
        if(info.getChangeValue().compareTo(dto.getAmount()) != 0) {
            return R.fail("提现金额不正确");
        }

        // 提现限制：每天提现次数，单笔提现金额
        List<CashOutRuleVo> cashOutRules = cashOutRuleService.getCashOutList(4, UserConstants.NORMAL);
        if (cashOutRules != null && !cashOutRules.isEmpty()) {
            for (var rule : cashOutRules) {
                if (rule.getCashOutMoney().compareTo(info.getChangeValue()) > 0)
                    return R.fail("未达到提现最低限额");
                // 微信提醒，默认额度200
                if (info.getChangeValue().compareTo(new BigDecimal(200)) > 0)
                    return R.fail("超过微信提现额度");

                // 获取当日提现次数
                long count = getCashOutCount(dto.getMemberId(), 1, 2, 2);
                if (count >= rule.getDayNum()) {
                    return R.fail("今日提现次数已达上限");
                }
            }
        }

        // 提现到微信
        String resStr = remoteOrderService.cashOutToWX(dto.getAmount(), dto.getRemark(), dto.getOpenId());
        if(resStr == null) {
            return R.fail("提现失败，请稍后重试");
        }
        Dict dict = JsonUtils.parseMap(resStr);
        String code = dict.get("code").toString();
        String message = dict.get("message").toString();
        log.info("===返回code:=======" + code + "====message======" + message);
        // 转账成功code值为null，转账失败code、message值不为null
        if(code != null) {
            return R.fail(message);
        }
        // 钱包金额减少
        boolean subFlag = modifyWallet(dto.getMemberId(), null, 1, info.getChangeValue(), RechargeTypeEnum.balance, 2, "微信提现");
        if (subFlag)
            return R.ok("提现成功");
        return R.fail("提现失败");
    }

}
