package com.euler.community.controller;

import cn.hutool.core.convert.Convert;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdDto;
import com.euler.common.core.domain.dto.IdTypeDto;
import com.euler.common.core.utils.JsonHelper;
import com.euler.common.core.validate.AddGroup;
import com.euler.common.core.web.controller.BaseController;
import com.euler.common.log.annotation.Log;
import com.euler.common.log.enums.BusinessType;
import com.euler.common.satoken.utils.LoginHelper;
import com.euler.community.domain.bo.ShieldBo;
import com.euler.community.domain.dto.ShieldPageDto;
import com.euler.community.domain.vo.MemberShieldVo;
import com.euler.community.domain.vo.ShieldVo;
import com.euler.community.service.IShieldService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * 屏蔽信息Controller
 * 前端访问路由地址为:/community/shield
 *
 * @author euler
 * @date 2022-09-15
 */
@Validated
@Api(value = "屏蔽信息控制器", tags = {"屏蔽信息管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/shield")
public class ShieldController extends BaseController {

    @Autowired
    private IShieldService shieldService;

    /**
     * 查询屏蔽信息列表
     */
    @ApiOperation("查询屏蔽信息列表")
    @PostMapping("/memberList")
    public R<List<MemberShieldVo>> memberList(@RequestBody IdTypeDto<Integer, Integer> idTypeDto) {
        long memberId = LoginHelper.getUserId();
        ShieldPageDto pageDto = new ShieldPageDto();
        pageDto.setMemberId(memberId);
        pageDto.setBusinessType(idTypeDto.getType());
        var list = shieldService.queryList(pageDto);
        var resList = JsonHelper.copyList(list, MemberShieldVo.class);
        return R.ok(resList);
    }


    /**
     * 获取屏蔽信息详细信息
     */
    @ApiOperation("获取屏蔽信息详细信息")
    @PostMapping("/getInfo")
    public R<ShieldVo> getInfo(@RequestBody IdDto<Integer> idDto) {
        return R.ok(shieldService.queryById(idDto.getId()));
    }

    /**
     * 新增屏蔽信息
     */
    @ApiOperation("新增屏蔽信息")
    @Log(title = "屏蔽信息", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public R add(@Validated(AddGroup.class) @RequestBody ShieldBo bo) {
        bo.setMemberId(LoginHelper.getUserId());
        return toAjax(shieldService.insertByBo(bo));
    }


    /**
     * 删除屏蔽信息
     */
    @ApiOperation("删除屏蔽信息")
    @PostMapping("/remove")
    public R remove(@RequestBody IdDto<String> idDto) {
        String[] strArr = idDto.getId().split(",");
        //主键为其他类型的时候，修改这个数组类型
        Integer[] ids = Convert.toIntArray(strArr);
        return toAjax(shieldService.deleteWithValidByIds(Arrays.asList(ids)));

    }
}
