package com.euler.risk.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.convert.Convert;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdDto;
import com.euler.common.core.domain.dto.RequestHeaderDto;
import com.euler.common.core.utils.HttpRequestHeaderUtils;
import com.euler.common.core.utils.JsonHelper;
import com.euler.common.core.validate.AddGroup;
import com.euler.common.core.validate.EditGroup;
import com.euler.common.core.web.controller.BaseController;
import com.euler.common.log.annotation.Log;
import com.euler.common.log.enums.BusinessType;
import com.euler.platform.api.RemoteGameManagerService;
import com.euler.platform.api.domain.OpenGameDubboVo;
import com.euler.risk.api.domain.LoginConfigVo;
import com.euler.risk.domain.dto.LoginConfigDto;
import com.euler.risk.domain.dto.LoginConfigResDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.euler.risk.domain.bo.LoginConfigBo;
import com.euler.risk.service.ILoginConfigService;
import com.euler.common.mybatis.core.page.TableDataInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 登录配置Controller
 * 前端访问路由地址为:/risk/loginConfig
 *
 * @author euler
 * @date 2022-08-23
 */
@Validated
@Api(value = "登录配置控制器", tags = {"登录配置管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/loginConfig")
public class LoginConfigController extends BaseController {

    private final ILoginConfigService loginConfigService;
    @DubboReference
    private RemoteGameManagerService remoteGameManagerService;

    /**
     * 查询SDK全局登录配置列表
     */
    @ApiOperation("查询SDK全局登录配置列表")
    @SaCheckPermission("risk:loginConfig:sdkGlobalList")
    @PostMapping("/sdkGlobalList")
    public List<LoginConfigVo> sdkGlobalList() {
        LoginConfigDto dto = new LoginConfigDto();
        // SDK全局登录配置列表
        return loginConfigService.querySdkGlobalList(dto);
    }

    /**
     * 查询APP全局登录配置列表
     */
    @ApiOperation("查询APP全局登录配置列表")
    @SaCheckPermission("risk:loginConfig:appGlobalList")
    @PostMapping("/appGlobalList")
    public List<LoginConfigVo> appGlobalList() {
        List<LoginConfigVo> list = new ArrayList<>();
        LoginConfigDto dto = new LoginConfigDto();
        // APP全局登录配置列表
        List<LoginConfigVo> appGlobalList = loginConfigService.queryAppGlobalList(dto);
        if (appGlobalList != null && appGlobalList.size() == 3) {
            list.addAll(appGlobalList);
        }
        return list;
    }

    /**
     * 查询SDK单个游戏的登录配置列表
     */
    @ApiOperation("查询SDK单个游戏的登录配置列表")
    @SaCheckPermission("risk:loginConfig:sdkList")
    @PostMapping("/sdkList")
    public TableDataInfo<LoginConfigVo> sdkList(@RequestBody LoginConfigDto dto) {
        return loginConfigService.queryPageList(dto);
    }

    /**
     * 获取SDK/APP的登录配置详细信息
     * <p>
     * APP, 根据平台信息（1:Android 2:ios 3:h5），查询登录配置详情
     * SDK单游戏配置, 需要和全局的配置信息取交集；没有配置的话，默认取SDK全局配置
     */
    @ApiOperation("获取登录配置详细信息")
    @PostMapping("/getInfo")
    public R<LoginConfigResDto> getInfo() {
        RequestHeaderDto headerDto = HttpRequestHeaderUtils.getFromHttpRequest();
        Integer gameId = 0;
        String gameName = "";
        OpenGameDubboVo openGameDubboVo = remoteGameManagerService.selectOpenGameInfo(headerDto.getAppId());
        if (openGameDubboVo != null) {
            gameId = openGameDubboVo.getId();
            gameName = openGameDubboVo.getGameName();
        }
//        if (LoginHelper.isLogin() && LoginPlatformEnum.SDK.getLoginPlatformNum().equals(headerDto.getPlatform())) {
//            SdkChannelPackageDto channelPackage = LoginHelper.getSdkChannelPackage();
//            if (channelPackage != null && Convert.toInt(channelPackage.getGameId(), 0) > 0) {
//                gameId = channelPackage.getGameId();
//                gameName = channelPackage.getGameName();
//            }
//        }
        LoginConfigDto dto = new LoginConfigDto();
        dto.setGameId(gameId);
        dto.setGameName(gameName);
        dto.setPlatform(headerDto.getDevice());
        dto.setPlatformType(headerDto.getPlatform());

        var loginConfig = loginConfigService.queryInfo(dto);
        LoginConfigResDto loginConfigResDto = JsonHelper.copyObj(loginConfig, LoginConfigResDto.class);
        return R.ok(loginConfigResDto);
    }

    /**
     * 新增SDK单个游戏的登录配置
     */
    @ApiOperation("新增SDK单个游戏的登录配置")
    @SaCheckPermission("risk:loginConfig:add")
    @Log(title = "登录配置", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public R add(@Validated(AddGroup.class) @RequestBody LoginConfigBo bo) {
        return loginConfigService.insertByBo(bo);
    }

    /**
     * 修改SDK全局的登录配置
     */
    @ApiOperation("修改SDK全局的登录配置")
    @SaCheckPermission("risk:loginConfig:editSdkGlobalInfo")
    @Log(title = "登录配置", businessType = BusinessType.UPDATE)
    @PostMapping("/editSdkGlobalInfo")
    public R editSdkGlobalInfo(@Validated(EditGroup.class) @RequestBody LoginConfigBo bo) {
        bo.setPlatformType("1");
        bo.setGlobalConfig("1");
        return loginConfigService.updateByBo(bo);
    }

    /**
     * 修改APP列表的登录配置
     */
    @ApiOperation("修改APP列表的登录配置")
    @SaCheckPermission("risk:loginConfig:editAppList")
    @Log(title = "登录配置", businessType = BusinessType.UPDATE)
    @PostMapping("/editAppList")
    public R editAppList(@Validated(EditGroup.class) @RequestBody LoginConfigBo bo) {
        bo.setPlatformType("4");
        bo.setGlobalConfig("1");
        return loginConfigService.updateByBo(bo);
    }

    /**
     * 修改sdk单个游戏的登录配置
     */
    @ApiOperation("修改sdk单个游戏的登录配置")
    @SaCheckPermission("risk:loginConfig:editSdkInfo")
    @Log(title = "登录配置", businessType = BusinessType.UPDATE)
    @PostMapping("/editSdkInfo")
    public R editSdkInfo(@Validated(EditGroup.class) @RequestBody LoginConfigBo bo) {
        bo.setPlatformType("1");
        bo.setGlobalConfig("0");
        return loginConfigService.updateByBo(bo);
    }

    /**
     * 删除SDK单个游戏的登录配置
     */
    @ApiOperation("删除SDK单个游戏的登录配置")
    @SaCheckPermission("risk:loginConfig:remove")
    @Log(title = "登录配置", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public R remove(@RequestBody IdDto<String> idDto) {
        String[] strArr = idDto.getId().split(",");
        //主键为其他类型的时候，修改这个数组类型
        Integer[] ids = Convert.toIntArray(strArr);
        return loginConfigService.deleteWithValidByIds(Arrays.asList(ids), true);
    }

}
