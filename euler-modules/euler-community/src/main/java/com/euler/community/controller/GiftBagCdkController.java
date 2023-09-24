package com.euler.community.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.convert.Convert;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdDto;
import com.euler.common.core.validate.AddGroup;
import com.euler.common.core.validate.EditGroup;
import com.euler.common.core.validate.QueryGroup;
import com.euler.common.core.web.controller.BaseController;
import com.euler.common.excel.utils.ExcelUtil;
import com.euler.common.log.annotation.Log;
import com.euler.common.log.enums.BusinessType;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.community.domain.bo.GiftBagCdkBo;
import com.euler.community.domain.dto.GiftBagCdkDto;
import com.euler.community.domain.vo.GiftBagCdkVo;
import com.euler.community.service.IGiftBagCdkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * 礼包兑换码数据Controller
 * 前端访问路由地址为:/community/cdk
 * @author euler
 *  2022-06-07
 */
@Validated
@Api(value = "礼包兑换码数据控制器", tags = {"礼包兑换码数据管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/cdk")
public class GiftBagCdkController extends BaseController {

    private final IGiftBagCdkService iGiftBagCdkService;

    /**
     * 查询礼包兑换码数据列表
     */
    @ApiOperation("查询礼包兑换码数据列表")
    @SaCheckPermission("community:cdk:list")
    @PostMapping("/list")
    public TableDataInfo<GiftBagCdkVo> list(@Validated(QueryGroup.class) @RequestBody  GiftBagCdkDto giftBagCdkDto) {
        return iGiftBagCdkService.queryPageList(giftBagCdkDto);
    }

    /**
     * 导出礼包兑换码数据列表
     */
    @ApiOperation("导出礼包兑换码数据列表")
    @SaCheckPermission("community:cdk:export")
    @Log(title = "礼包兑换码数据", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(@Validated GiftBagCdkDto giftBagCdkDto, HttpServletResponse response) {
        List<GiftBagCdkVo> list = iGiftBagCdkService.queryList(giftBagCdkDto);
        ExcelUtil.exportExcel(list, "礼包SDk数据", GiftBagCdkVo.class, response);
    }

    /**
     * 获取礼包兑换码数据详细信息
     */
    @ApiOperation("获取礼包兑换码数据详细信息")
    @SaCheckPermission("community:cdk:query")
    @PostMapping("/getInfo")
    public R<GiftBagCdkVo> getInfo(@RequestBody GiftBagCdkDto giftBagCdkDto) {
        return R.ok(iGiftBagCdkService.queryById(giftBagCdkDto.getId()));
    }

    /**
     * 新增礼包兑换码数据
     */
    @ApiOperation("新增礼包兑换码数据")
    @SaCheckPermission("community:cdk:add")
    @Log(title = "礼包SDk数据", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public R add(@Validated(AddGroup.class) @RequestBody GiftBagCdkBo bo) {
         return iGiftBagCdkService.insertByBo(bo);
    }

    /**
     * 修改礼包兑换码数据
     */
    @ApiOperation("修改礼包兑换码数据")
    @SaCheckPermission("community:cdk:edit")
    @Log(title = "礼包SDk数据", businessType = BusinessType.UPDATE)
    @PostMapping("/update")
    public R edit(@Validated(EditGroup.class) @RequestBody GiftBagCdkBo bo) {
        return iGiftBagCdkService.updateByBo(bo);
    }

    /**
     * 删除礼包兑换码数据
     */
    @ApiOperation("删除礼包兑换码数据")
    @SaCheckPermission("community:cdk:remove")
    @Log(title = "礼包SDk数据", businessType = BusinessType.DELETE)
    @PostMapping("/delete")
    public R delete( @RequestBody IdDto<String> idDto) {
        // 判断传输过来的数据是否包含多个
        String[] strArr = idDto.getId().split(",");
        Long[] ids = Convert.toLongArray(strArr);
        return iGiftBagCdkService.deleteWithValidByIds(Arrays.asList(ids), true);
    }

}
