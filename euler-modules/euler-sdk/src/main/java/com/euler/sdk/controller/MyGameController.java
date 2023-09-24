package com.euler.sdk.controller;

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.session.SaSession;
import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdDto;
import com.euler.common.core.domain.dto.LoginUser;
import com.euler.common.core.enums.UserTypeEnum;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.core.validate.AddGroup;
import com.euler.common.core.validate.EditGroup;
import com.euler.common.core.web.controller.BaseController;
import com.euler.common.log.annotation.Log;
import com.euler.common.log.enums.BusinessType;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.common.redis.utils.RedisUtils;
import com.euler.common.satoken.utils.LoginHelper;
import com.euler.sdk.api.domain.GameUserManagement;
import com.euler.sdk.api.domain.MyGameVo;
import com.euler.sdk.domain.bo.MyGameBo;
import com.euler.sdk.domain.dto.MyGameDto;
import com.euler.sdk.service.IGameUserManagementService;
import com.euler.sdk.service.IMemberService;
import com.euler.sdk.service.IMyGameService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 我的游戏Controller
 * 前端访问路由地址为:/sdk/myGame
 *
 * @author euler
 * @date 2022-03-29
 */
@Validated
@Api(value = "我的游戏控制器", tags = {"我的游戏管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/myGame")
@Slf4j
public class MyGameController extends BaseController {

    private final IMyGameService iMyGameService;

    @Autowired
    private IGameUserManagementService gameUserManagementService;

    @Autowired
    private IMemberService memberService;

    /**
     * 后台 - 查询用户游戏列表
     */
    @ApiOperation("后台 - 查询用户游戏列表")
    @PostMapping("/backstageList")
    public TableDataInfo<MyGameVo> backstageList(@RequestBody MyGameDto dto) {
        return iMyGameService.backstagePageList(dto);
    }

    /**
     * 查询我的游戏列表
     */
    @ApiOperation("查询我的游戏列表")
    @PostMapping("/list")
    public TableDataInfo<MyGameVo> list(@RequestBody MyGameDto dto) {
        Long userId = LoginHelper.getUserId();
        dto.setUserId(userId);
        return iMyGameService.queryPageList(dto);
    }

    /**
     * 获取我的游戏详细信息
     */
    @ApiOperation("获取我的游戏详细信息")
    @PostMapping("/getInfo")
    public R<MyGameVo> getInfo(@RequestBody IdDto<Integer> idDto) {
        return R.ok(iMyGameService.queryById(idDto.getId()));
    }

    /**
     * 新增我的游戏
     */
    @ApiOperation("新增我的游戏")
    @Log(title = "新增我的游戏", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public R add(@Validated(AddGroup.class) @RequestBody MyGameBo bo) {
        Long userId = LoginHelper.getUserId();
        bo.setUserId(userId);
        return iMyGameService.insertByBo(bo);
    }

    /**
     * 修改我的游戏
     */
    @ApiOperation("修改我的游戏")
    @Log(title = "修改我的游戏", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public R edit(@Validated(EditGroup.class) @RequestBody MyGameBo bo) {
        return iMyGameService.updateByBo(bo);
    }

    /**
     * 删除我的游戏
     */
    @ApiOperation("删除我的游戏")
    @Log(title = "删除我的游戏", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public R remove(@RequestBody IdDto<String> idDto) {
        String[] strArr = idDto.getId().split(",");
        Integer[] ids = Convert.toIntArray(strArr);
        return toAjax(iMyGameService.deleteWithValidByIds(Arrays.asList(ids), true) ? 1 : 0);
    }



    @PostMapping("/downline")
    @SaCheckPermission("sdk:game:downline")
    public R downLine(@RequestBody IdDto<Integer> idDto) {
        //1、获取指定游戏的用户id
        List<GameUserManagement> list = gameUserManagementService.list(Wrappers.<GameUserManagement>lambdaQuery().eq(GameUserManagement::getGameId, idDto.getId()));
Integer num=0;
        if (list != null && !list.isEmpty()) {
            for (var gameUser : list) {
                num+= memberService.downLineUser(gameUser.getMemberId(),idDto.getId(),0);
            }
            String result =
                StringUtils.format("下线游戏用户--游戏id：{}，共有{}个用户，下线处理用户：{}个", idDto.getId(), list.size(), num);
            log.info(result);
            return R.ok(result);
        }
        return R.ok("暂无需要处理的用户");


    }


}
