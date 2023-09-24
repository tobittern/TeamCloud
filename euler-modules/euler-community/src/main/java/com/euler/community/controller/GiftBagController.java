package com.euler.community.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.convert.Convert;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdDto;
import com.euler.common.core.validate.AddGroup;
import com.euler.common.core.validate.EditGroup;
import com.euler.common.core.web.controller.BaseController;
import com.euler.common.excel.utils.ExcelUtil;
import com.euler.common.log.annotation.Log;
import com.euler.common.log.enums.BusinessType;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.common.satoken.utils.LoginHelper;
import com.euler.community.domain.bo.GiftBagBo;
import com.euler.community.domain.bo.GiftBagCdkBo;
import com.euler.community.domain.dto.GiftBagDto;
import com.euler.community.domain.vo.GiftBagVo;
import com.euler.community.service.IGiftBagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * 礼包配置Controller
 * 前端访问路由地址为:/community/bag
 * @author euler
 *  2022-06-02
 */
@Validated
@Api(value = "礼包配置控制器", tags = {"礼包配置管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/bag")
public class GiftBagController extends BaseController {

    private final IGiftBagService iGiftBagService;

    /**
     * 查询礼包配置列表
     */
    @ApiOperation("查询礼包配置列表")
    @SaCheckPermission("community:bag:list")
    @PostMapping("/list")
    public TableDataInfo<GiftBagVo> list(@RequestBody GiftBagDto giftBagDto) {
        return iGiftBagService.queryPageList(giftBagDto);
    }

    /**
     * 导出礼包配置列表
     */
    @ApiOperation("导出礼包配置列表")
    @SaCheckPermission("community:bag:export")
    @Log(title = "礼包配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(@Validated GiftBagDto giftBagDto, HttpServletResponse response) {
        List<GiftBagVo> list = iGiftBagService.queryList(giftBagDto);
        ExcelUtil.exportExcel(list, "礼包配置", GiftBagVo.class, response);
    }

    /**
     * 获取礼包配置详细信息
     */
    @ApiOperation("获取礼包配置详细信息")
    @PostMapping("/getInfo")
    public R<GiftBagVo> getInfo(@RequestBody IdDto<Long> idDto) {
        return R.ok(iGiftBagService.queryById(idDto.getId()));
    }

    /**
     * 新增礼包配置
     */
    @ApiOperation("新增礼包配置")
    @SaCheckPermission("community:bag:add")
    @Log(title = "礼包配置", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public R add(@Validated(AddGroup.class) @RequestBody GiftBagBo bo) {
        return iGiftBagService.insertByBo(bo);
    }

    /**
     * 修改礼包配置
     */
    @ApiOperation("修改礼包配置")
    @SaCheckPermission("community:bag:edit")
    @Log(title = "礼包配置", businessType = BusinessType.UPDATE)
    @PostMapping("/update")
    public R edit(@Validated(EditGroup.class) @RequestBody GiftBagBo bo) {
        return iGiftBagService.updateByBo(bo);
    }

    /**
     * 删除礼包
     */
    @ApiOperation("删除礼包")
    @SaCheckPermission("community:bag:remove")
    @Log(title = "礼包配置", businessType = BusinessType.DELETE)
    @PostMapping("/delete")
    public R delete(@RequestBody IdDto<String> idDto) {
        // 判断传输过来的数据是否包含多个
        String[] strArr = idDto.getId().split(",");
        Long[] ids = Convert.toLongArray(strArr);
        return iGiftBagService.deleteWithValidByIds(Arrays.asList(ids));
    }

    /**
     * 已领取的礼包
     * @param giftBagCdkBo cdk
     * @return R
     */
    @ApiOperation("获取已领取的礼包信息")
    @PostMapping("/receivedBags")
    public R<Object> receivedBags(@RequestBody GiftBagCdkBo giftBagCdkBo) {
        giftBagCdkBo.setMemberId(LoginHelper.getUserId());
        return iGiftBagService.receivedBags(giftBagCdkBo);
    }


    /**
     * 待领取的礼包
     * @param bo cdk
     * @return R
     */
    @ApiOperation("获取待领取的礼包信息")
    @PostMapping("/waitBags")
    public R<Object> waitBags(@RequestBody GiftBagCdkBo bo ){
        return iGiftBagService.waitBags(bo);
    }

    /**
     * 领取礼包
     * @param bo cdk
     * @return R
     */
    @ApiOperation("领取礼包")
    @PostMapping("/pickBag")
    public R pickBag(@RequestBody GiftBagCdkBo bo) {
        bo.setMemberId(LoginHelper.getUserId());
        return iGiftBagService.pickBag(bo);
    }

    /**
     * 礼包兑换
     * @param bo cdk
     * @return R
     */
    @ApiOperation("兑换礼包")
    @PostMapping("/exchangeBag")
    public R exchangeBag(@RequestBody GiftBagCdkBo bo) {
        bo.setMemberId(LoginHelper.getUserId());
        return iGiftBagService.exchangeBag(bo);
    }

    /**
     * 修改礼包状态
     * @param bo cdk
     * @return R
     */
    @ApiOperation("修改礼包状态")
    @SaCheckPermission("community:bag:updateStatus")
    @PostMapping("/updateStatus")
    public R updateStatus(@RequestBody GiftBagBo bo) {
        return iGiftBagService.updateStatus(bo);
    }


}
