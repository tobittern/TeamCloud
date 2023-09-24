package com.euler.community.controller;

import cn.hutool.core.convert.Convert;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdDto;
import com.euler.common.core.domain.dto.IdNameTypeDicDto;
import com.euler.common.core.web.controller.BaseController;
import com.euler.common.log.annotation.Log;
import com.euler.common.log.enums.BusinessType;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.common.satoken.utils.LoginHelper;
import com.euler.community.domain.dto.CollectDto;
import com.euler.community.domain.vo.CollectVo;
import com.euler.community.service.ICollectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

/**
 * 动态收藏Controller
 * 前端访问路由地址为:/system/collect
 *
 * @author euler
 * @date 2022-06-06
 */
@Validated
@Api(value = "动态收藏控制器", tags = {"动态收藏管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/collect")
public class CollectController extends BaseController {

    private final ICollectService iCollectService;

    /**
     * 查询动态收藏列表
     */
    @ApiOperation("查询动态收藏列表")
    @PostMapping("/list")
    public TableDataInfo<CollectVo> list(@RequestBody CollectDto dto) {
        Long userId = LoginHelper.getUserId();
        dto.setMemberId(userId);
        return iCollectService.queryPageList(dto);
    }


    /**
     * 新增动态收藏
     */
    @ApiOperation("新增动态收藏")
    @PostMapping("/click")
    public R add(@RequestBody IdNameTypeDicDto<Long> dto) {
        return iCollectService.insertByBo(dto);
    }

    /**
     * 删除动态收藏
     */
    @ApiOperation("删除动态收藏")
    @PostMapping("/remove")
    public R remove(@RequestBody IdDto<String> idDto) {
        String[] strArr = idDto.getId().split(",");
        Long[] ids = Convert.toLongArray(strArr);
        return toAjax(iCollectService.deleteWithValidByIds(Arrays.asList(ids), true) ? 1 : 0);
    }


    /**
     * 获取用户收藏数量
     */
    @ApiOperation("获取用户收藏数量")
    @PostMapping("/count")
    public R count() {
        return R.ok(iCollectService.count(LoginHelper.getUserId()));
    }

}
