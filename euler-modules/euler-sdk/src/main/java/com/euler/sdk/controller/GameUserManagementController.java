package com.euler.sdk.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.convert.Convert;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdDto;
import com.euler.common.core.domain.dto.LoginUser;
import com.euler.common.core.domain.dto.SdkChannelPackageDto;
import com.euler.common.core.utils.HttpRequestHeaderUtils;
import com.euler.common.core.utils.ServletUtils;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.core.validate.AddGroup;
import com.euler.common.core.web.controller.BaseController;
import com.euler.common.idempotent.annotation.RepeatSubmit;
import com.euler.common.rabbitmq.RabbitMqHelper;
import com.euler.common.satoken.utils.LoginHelper;
import com.euler.sdk.api.domain.GameUserManagement;
import com.euler.sdk.config.WebConfig;
import com.euler.sdk.domain.dto.GameUserAddDto;
import com.euler.sdk.domain.dto.GameUserManagementDto;
import com.euler.system.api.RemoteUserService;
import com.euler.system.api.domain.SysUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.euler.sdk.api.domain.GameUserManagementVo;
import com.euler.sdk.domain.bo.GameUserManagementBo;
import com.euler.sdk.service.IGameUserManagementService;
import com.euler.common.mybatis.core.page.TableDataInfo;

/**
 * 游戏用户管理Controller
 * 前端访问路由地址为:/sdk/gameUserManagement
 *
 * @author euler
 * @date 2022-04-02
 */
@Validated
@Api(value = "游戏用户管理控制器", tags = {"游戏用户管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/gameUserManagement")
public class GameUserManagementController extends BaseController {

    private final IGameUserManagementService iGameUserManagementService;

    @DubboReference
    private RemoteUserService remoteUserService;
    @Autowired
    private WebConfig webConfig;

    @Autowired
    private RabbitMqHelper rabbitMqHelper;

    private Integer getChannelIdByUser() {
        // 根据用户判断是否需要给他设置查询指定渠道的数据
        Long userId = LoginHelper.getUserId();
        SysUser sysUser = remoteUserService.selectUserById(userId);
        if (sysUser != null && sysUser.getRegisterChannelId() != null && sysUser.getRegisterChannelId() != 0) {
            return sysUser.getRegisterChannelId();
        }
        return 0;
    }

    /**
     * 查询游戏用户列表
     */
    @ApiOperation("查询游戏用户列表")
    @PostMapping("/list")
    @SaCheckPermission("sdk:gameUserManagement:list")
    public TableDataInfo<GameUserManagementVo> list(@RequestBody GameUserManagementDto dto) {
        Integer channelIdByUser = getChannelIdByUser();
        dto.setChannelId(channelIdByUser);
        return iGameUserManagementService.getGameUserDetailPageList(dto);
    }

    /**
     * 获取游戏用户详细信息
     */
    @ApiOperation("获取游戏用户详细信息")
    @PostMapping("/getInfo")
    public R<GameUserManagement> getInfo(@RequestBody IdDto<Integer> dto) {
        return R.ok(iGameUserManagementService.getById(dto.getId()));
    }

    /**
     * 新增游戏用户
     */
    @ApiOperation("新增游戏用户")
    @PostMapping("/add")
    @RepeatSubmit
    public R add(@Validated(AddGroup.class) @RequestBody GameUserManagementBo bo) {
        if (StringUtils.isBlank(bo.getRoleId()) || StringUtils.isBlank(bo.getServerId()))
            return R.fail("角色或区服不能为空");

        LoginUser loginUser = LoginHelper.getLoginUser();
        SdkChannelPackageDto channelPackage = loginUser.getSdkChannelPackage();
        Integer gameId = Convert.toInt(channelPackage.getGameId(), 0);
        if (gameId <= 0) {
            return R.fail("游戏不能为空");
        }

        GameUserAddDto gameUserAddDto = new GameUserAddDto();
        bo.setMemberId(loginUser.getUserId());

        if (channelPackage != null) {
            channelPackage.setGameRoleId(bo.getRoleId());
            channelPackage.setGameRoleName(bo.getRoleName());
            channelPackage.setGameServerId(bo.getServerId());
            channelPackage.setGameServerName(bo.getServerName());
            loginUser.setSdkChannelPackage(channelPackage);
            LoginHelper.setLoginUser(loginUser);
            bo.setGameId(channelPackage.getGameId());
            bo.setGameName(channelPackage.getGameName());
            bo.setPackageCode(channelPackage.getPackageCode());
            bo.setChannelId(channelPackage.getChannelId());
            bo.setChannelName(channelPackage.getChannelName());
        }
        gameUserAddDto.setGameUserManagementBo(bo);
        //游戏上报添加header信息 2022-08-30
        gameUserAddDto.setHeaderDto(HttpRequestHeaderUtils.getFromHttpRequest());
        String msgId = rabbitMqHelper.createMsgId("GU");
        gameUserAddDto.setIp(ServletUtils.getClientIP());

        rabbitMqHelper.sendObj(webConfig.getGameUserExchange(), webConfig.getGameUserRoutingkey(), msgId, gameUserAddDto);
        return R.ok();
    }

}
