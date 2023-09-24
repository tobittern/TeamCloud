package com.euler.sdk.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdDto;
import com.euler.common.core.validate.AddGroup;
import com.euler.common.core.validate.QueryGroup;
import com.euler.common.core.web.controller.BaseController;
import com.euler.common.log.annotation.Log;
import com.euler.common.log.enums.BusinessType;
import com.euler.common.satoken.utils.LoginHelper;
import com.euler.sdk.domain.dto.GiftReceiveRecordDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.euler.sdk.domain.vo.GiftReceiveRecordVo;
import com.euler.sdk.domain.bo.GiftReceiveRecordBo;
import com.euler.sdk.service.IGiftReceiveRecordService;
import com.euler.common.mybatis.core.page.TableDataInfo;

/**
 * 礼包领取记录Controller
 * 前端访问路由地址为:/sdk/giftReceiveRecord
 * @author euler
 * @date 2022-04-13
 */
@Validated
@Api(value = "礼包领取记录控制器", tags = {"礼包领取记录管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/giftReceiveRecord")
public class GiftReceiveRecordController extends BaseController {

    private final IGiftReceiveRecordService iGiftReceiveRecordService;

    /**
     * 查询礼包领取记录列表
     */
    @ApiOperation("查询礼包领取记录列表")
    @SaCheckPermission("sdk:giftReceiveRecord:list")
    @PostMapping("/list")
    public TableDataInfo<GiftReceiveRecordVo> list(@Validated(QueryGroup.class) GiftReceiveRecordDto dto) {
        return iGiftReceiveRecordService.queryPageList(dto);
    }

    /**
     * 获取礼包领取记录详细信息
     */
    @ApiOperation("获取礼包领取记录详细信息")
    @PostMapping("/getInfo")
    public R<GiftReceiveRecordVo> getInfo(@RequestBody IdDto<Integer> idDto) {
        return R.ok(iGiftReceiveRecordService.queryById(idDto.getId()));
    }

    /**
     * 新增礼包领取记录
     */
    @ApiOperation("新增礼包领取记录")
    @Log(title = "礼包领取记录", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public R add(@Validated(AddGroup.class) @RequestBody GiftReceiveRecordBo bo) {
        // 查询设置用户
        Long userId = LoginHelper.getUserId();
        bo.setMemberId(userId);
        return iGiftReceiveRecordService.insertByBo(bo);
    }

}
