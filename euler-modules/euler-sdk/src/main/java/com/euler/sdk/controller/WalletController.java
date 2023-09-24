package com.euler.sdk.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdTypeDto;
import com.euler.common.core.web.controller.BaseController;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.common.satoken.utils.LoginHelper;
import com.euler.sdk.api.domain.WalletVo;
import com.euler.sdk.domain.dto.CashOutDto;
import com.euler.sdk.domain.dto.GetWalletDto;
import com.euler.sdk.domain.dto.WalletLogPageDto;
import com.euler.sdk.domain.vo.WalletLogVo;
import com.euler.sdk.service.IWalletLogService;
import com.euler.sdk.service.IWalletService;
import com.euler.system.api.RemoteDictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 钱包Controller
 * 前端访问路由地址为:/sdk/wallet
 *
 * @author euler
 * @date 2022-03-28
 */
@Validated
@Api(value = "钱包控制器", tags = {"钱包管理"})
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/wallet")
public class WalletController extends BaseController {
    @Autowired
    private IWalletService walletService;
    @DubboReference
    private RemoteDictService remoteDictService;
    @Autowired
    private IWalletLogService walletLogService;

    /**
     * 获取钱包详细信息
     */
    @ApiOperation("获取钱包详细信息")
    @PostMapping("/getInfo")
    public R<WalletVo> getInfo(@RequestBody(required = false) GetWalletDto getWalletDto) {
        if (getWalletDto == null)
            getWalletDto = new GetWalletDto(LoginHelper.getUserId(), 1, null);

        if (getWalletDto.getType() == null) {
            getWalletDto.setType(1).setGameId(null);
        }
        Integer gameId = LoginHelper.getSdkChannelPackage() == null ? 0 : LoginHelper.getSdkChannelPackage().getGameId();
        getWalletDto.setId(LoginHelper.getUserId()).setGameId(gameId);
        return R.ok(walletService.queryByMemberId(getWalletDto));
    }

    /**
     * 获取钱包详细信息
     */
    @SaCheckPermission("sdk:wallet:getinfo")
    @ApiOperation("获取钱包详细信息")
    @PostMapping("/getWalletInfo")
    public R<WalletVo> getWalletInfo(@RequestBody(required = false) GetWalletDto getWalletDto) {
        if (getWalletDto == null)
            getWalletDto = new GetWalletDto(LoginHelper.getUserId(), 1, null);

        if (getWalletDto.getType() == null) {
            getWalletDto.setType(1).setGameId(null);
        }
        return R.ok(walletService.queryByMemberId(getWalletDto));
    }

    @ApiOperation("提取虚拟钱包金额到钱包")
    @PostMapping("/cashOut")
    public R<Boolean> cashOut(@RequestBody IdTypeDto<Long, Integer> idTypeDto) {
        idTypeDto.setId(LoginHelper.getUserId());
        return walletService.cashOutToWallet(idTypeDto);
    }

    /**
     * 查询我的游戏列表
     */
    @ApiOperation("查询我的钱包变动记录")
    @PostMapping("/log")
    public TableDataInfo<WalletLogVo> list(@RequestBody WalletLogPageDto dto) {
        dto.setMemberId(LoginHelper.getUserId());
        dto.setGameId(LoginHelper.getSdkChannelPackage().getGameId());
        return walletLogService.queryPageList(dto);
    }

    /**
     * 获取sdk钱包菜单
     *
     * @param walletMenuType 钱包菜单，sdk_wallet_menu:真实钱包，sdk_virtual_wallet_menu
     * @return
     */
    @ApiOperation("获取钱包菜单")
    @PostMapping(value = "/getWalletMenu/{walletMenuType}")
    public R getGamePayTypeList(@PathVariable String walletMenuType) {
        return walletService.getGamePayTypeList(walletMenuType);
    }

    /**
     * 获取提现按钮开关状态
     *
     * @param withdrawalType 提现按钮开关
     * @return
     */
    @ApiOperation("获取提现按钮开关状态")
    @PostMapping(value = "/dict/{withdrawalType}")
    public R getWithdrawalSwitch(@PathVariable String withdrawalType) {
        return walletService.getWithdrawalSwitch(withdrawalType);
    }

    /**
     * 将余额提现到微信
     */
    @ApiOperation("提现到微信")
    @PostMapping("/cashOutToWX")
    public R cashOutToWX(@RequestBody CashOutDto dto) {
        dto.setMemberId(LoginHelper.getUserId());
        return walletService.cashOutToWX(dto);
    }

}
