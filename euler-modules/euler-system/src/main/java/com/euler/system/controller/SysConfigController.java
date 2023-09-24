package com.euler.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.convert.Convert;
import com.euler.common.core.constant.Constants;
import com.euler.common.core.domain.dto.IdDto;
import com.euler.common.core.enums.LoginPlatformEnum;
import com.euler.common.core.utils.JsonHelper;
import com.euler.common.core.utils.ServletUtils;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.redis.utils.RedisUtils;
import com.euler.common.satoken.utils.LoginHelper;
import com.euler.system.domain.SysConfig;
import com.euler.system.domain.dto.SysConfigDto;
import com.euler.system.domain.dto.SysConfigOutDto;
import com.euler.system.service.ISysConfigService;
import com.euler.common.core.constant.UserConstants;
import com.euler.common.core.domain.R;
import com.euler.common.core.web.controller.BaseController;
import com.euler.common.excel.utils.ExcelUtil;
import com.euler.common.log.annotation.Log;
import com.euler.common.log.enums.BusinessType;
import com.euler.common.mybatis.core.page.PageQuery;
import com.euler.common.mybatis.core.page.TableDataInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 参数配置 信息操作处理
 *
 * @author euler
 */
@Validated
@Api(value = "参数配置控制器", tags = {"参数配置管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/config")
public class SysConfigController extends BaseController {

    private final ISysConfigService configService;

    /**
     * 获取参数配置列表
     */
    @ApiOperation("获取参数配置列表")
    @SaCheckPermission("system:config:list")
    @GetMapping("/list")
    public TableDataInfo<SysConfig> list(SysConfig config, PageQuery pageQuery) {
        return configService.selectPageConfigList(config, pageQuery);
    }


    /**
     * 根据参数编号获取详细信息
     */
    @ApiOperation("根据参数编号获取详细信息")
    @GetMapping(value = "/{configId}")
    public R<SysConfig> getInfo(@PathVariable Long configId) {
        return R.ok(configService.selectConfigById(configId));
    }

    /**
     * 根据参数键名查询参数值
     */
    @ApiOperation("根据参数键名查询参数值")
    @GetMapping(value = "/configKey/{configKey}")
    public R getConfigKey(@PathVariable String configKey) {
        return R.ok(configService.selectConfigByKey(configKey));
    }

    /**
     * 新增参数配置
     */
    @ApiOperation("新增参数配置")
    @SaCheckPermission("system:config:add")
    @Log(title = "参数管理", businessType = BusinessType.INSERT)
    @PostMapping
    public R add(@Validated @RequestBody SysConfig config) {
        if (UserConstants.NOT_UNIQUE.equals(configService.checkConfigKeyUnique(config))) {
            return R.fail("新增参数'" + config.getConfigName() + "'失败，参数键名已存在");
        }
        return toAjax(configService.insertConfig(config));
    }

    /**
     * 修改参数配置
     */
    @ApiOperation("修改参数配置")
    @SaCheckPermission("system:config:edit")
    @Log(title = "参数管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public R edit(@Validated @RequestBody SysConfig config) {
        if (UserConstants.NOT_UNIQUE.equals(configService.checkConfigKeyUnique(config))) {
            return R.fail("修改参数'" + config.getConfigName() + "'失败，参数键名已存在");
        }
        return toAjax(configService.updateConfig(config));
    }

    /**
     * 根据参数键名修改参数配置
     */
    @ApiOperation("根据参数键名修改参数配置")
    @SaCheckPermission("system:config:edit")
    @Log(title = "参数管理", businessType = BusinessType.UPDATE)
    @PutMapping("/updateByKey")
    public R updateByKey(@RequestBody SysConfig config) {
        return toAjax(configService.updateConfig(config));
    }

    /**
     * 删除参数配置
     */
    @ApiOperation("删除参数配置")
    @SaCheckPermission("system:config:remove")
    @Log(title = "参数管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{configIds}")
    public R remove(@PathVariable Long[] configIds) {
        configService.deleteConfigByIds(configIds);
        return R.ok();
    }

    /**
     * 刷新参数缓存
     */
    @ApiOperation("刷新参数缓存")
    @SaCheckPermission("system:config:remove")
    @Log(title = "参数管理", businessType = BusinessType.CLEAN)
    @DeleteMapping("/refreshCache")
    public R refreshCache() {
        configService.resetConfigCache();
        return R.ok();
    }


    /**
     * 刷新参数缓存
     */
    @ApiOperation("删除指定缓存")
    @SaCheckPermission("system:config:remove")
    @PostMapping("/removeCache")
    public R removeCache(@RequestBody IdDto<String> idDto) {
        RedisUtils.deleteObject(idDto.getId());
        return R.ok();
    }


    /**
     * 获取配置信息
     */
    @ApiOperation("获取配置信息")
    @PostMapping("/getConfigList")
    public R<List<SysConfigOutDto>> getConfigList() {

        Integer platform = Convert.toInt(ServletUtils.getHeader(ServletUtils.getRequest(), "platform"), -1);
        Integer device = Convert.toInt(ServletUtils.getHeader(ServletUtils.getRequest(), "device"), -1);

        Integer gameId = 0;
        if (LoginHelper.isLogin()&& LoginPlatformEnum.SDK.getLoginPlatformNum().equals(platform))
            gameId = LoginHelper.getSdkChannelPackage() != null ? Convert.toInt(LoginHelper.getSdkChannelPackage().getGameId(), 0) : 0;
        String cacheKey = StringUtils.format("{}{}:{}:{}",Constants.SYS_CONFIG_KEY, platform, device, gameId);
        List<SysConfigOutDto> configOutDtos = RedisUtils.getCacheList(cacheKey);
        if (configOutDtos == null || configOutDtos.isEmpty()) {
            SysConfig sysConfig = new SysConfig();
            sysConfig.setOperationPlatform(platform);
            sysConfig.setGameId(gameId);
            sysConfig.setDevice(device);
            var list = configService.selectUserConfigList(sysConfig);
            var outList = JsonHelper.copyList(list, SysConfigOutDto.class);
            RedisUtils.setCacheList(cacheKey, outList);
            return R.ok(outList);
        }
        return R.ok(configOutDtos);
    }


}
