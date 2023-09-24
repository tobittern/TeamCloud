package com.euler.sdk.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.convert.Convert;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdDto;
import com.euler.common.core.domain.dto.IdNameTypeDicDto;
import com.euler.common.core.validate.AddGroup;
import com.euler.common.core.validate.EditGroup;
import com.euler.common.core.web.controller.BaseController;
import com.euler.common.idempotent.annotation.RepeatSubmit;
import com.euler.common.log.annotation.Log;
import com.euler.common.log.enums.BusinessType;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.common.satoken.utils.LoginHelper;
import com.euler.sdk.api.domain.GoodsVo;
import com.euler.sdk.domain.bo.GoodsBo;
import com.euler.sdk.domain.dto.GoodsDto;
import com.euler.sdk.service.IGoodsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * 商品Controller
 * 前端访问路由地址为:/sdk/goods
 *
 * @author euler
 * @date 2022-03-21
 */
@Validated
@Api(value = "商品控制器", tags = {"商品管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/goods")
public class GoodsController extends BaseController {

    private final IGoodsService iGoodsService;

    /**
     * 后台查询商品列表
     */
    @ApiOperation("后台查询商品列表")
    @PostMapping("/backstageList")
    public TableDataInfo<GoodsVo> backstageList(@RequestBody GoodsDto goodsDto) {
        return iGoodsService.queryPageList(goodsDto);
    }

    /**
     * 查询商品列表
     */
    @ApiOperation("查询商品列表")
    @PostMapping("/list")
    public TableDataInfo<GoodsVo> list(@RequestBody GoodsDto goodsDto) {
        goodsDto.setIsUp(1);
        return iGoodsService.queryPageList(goodsDto);
    }

    /**
     * 获取商品详细信息
     */
    @ApiOperation("获取商品详细信息")
    @PostMapping("/getInfo")
    public R<GoodsVo> getInfo(@RequestBody IdDto<Integer> idDto) {
        return R.ok(iGoodsService.queryById(idDto.getId(), 1, LoginHelper.getUserId()));
    }

    /**
     * 新增商品
     */
    @ApiOperation("新增商品")
    @SaCheckPermission("sdk:goods:add")
    @Log(title = "商品", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping("/add")
    public R add(@Validated(AddGroup.class) @RequestBody GoodsBo bo) {
        Long userId = LoginHelper.getUserId();
        bo.setUserId(userId);
        return iGoodsService.insertByBo(bo);
    }

    /**
     * 修改商品
     */
    @ApiOperation("修改商品")
    @SaCheckPermission("sdk:goods:edit")
    @Log(title = "商品", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PostMapping("/edit")
    public R edit(@Validated(EditGroup.class) @RequestBody GoodsBo bo) {
        Long userId = LoginHelper.getUserId();
        bo.setUserId(userId);
        return iGoodsService.updateByBo(bo);
    }

    /**
     * 删除商品
     */
    @ApiOperation("删除商品")
    @SaCheckPermission("sdk:goods:remove")
    @Log(title = "删除商品", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public R remove(@RequestBody IdDto<String> idDto) {
        String[] strArr = idDto.getId().split(",");
        Integer[] ids = Convert.toIntArray(strArr);
        return toAjax(iGoodsService.deleteWithValidByIds(Arrays.asList(ids), true) ? 1 : 0);
    }

    /**
     * 操作上下架
     */
    @ApiOperation("操作上下架")
    @SaCheckPermission("sdk:goods:operation")
    @Log(title = "上下架")
    @PostMapping("/operation")
    public R operation(@RequestBody IdNameTypeDicDto idNameTypeDicDto) {
        Long userId = LoginHelper.getUserId();
        return iGoodsService.operation(idNameTypeDicDto, userId);
    }

    /**
     * 查询已上架的年卡商品列表
     */
    @ApiOperation("查询商品列表")
    @PostMapping("/cardList")
    public List<GoodsVo> cardList() {
        return iGoodsService.queryCardList();
    }

}
