package com.euler.community.controller;

import cn.hutool.core.convert.Convert;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdDto;
import com.euler.common.core.web.controller.BaseController;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.common.satoken.utils.LoginHelper;
import com.euler.community.domain.bo.PraiseBo;
import com.euler.community.domain.dto.PraiseDto;
import com.euler.community.domain.vo.NewPraiseVo;
import com.euler.community.domain.vo.PraiseVo;
import com.euler.community.service.IPraiseService;
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
 * 点赞Controller
 * 前端访问路由地址为:/community/praise
 *
 * @author euler
 * @date 2022-06-06
 */
@Validated
@Api(value = "点赞控制器", tags = {"点赞管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/praise")
public class PraiseController extends BaseController {

    private final IPraiseService iPraiseService;

    /**
     * 查询点赞列表
     */
    @ApiOperation("查询点赞列表")
    @PostMapping("/list")
    public TableDataInfo<PraiseVo> list(@RequestBody PraiseDto dto) {
        Long userId = LoginHelper.getUserId();
        dto.setMemberId(userId);
        return iPraiseService.queryPageList(dto);
    }

    /**
     * 新增点赞
     */
    @ApiOperation("新增点赞")
    @PostMapping("/click")
    public R add(@RequestBody PraiseBo bo) {
        return iPraiseService.insertByBo(bo);
    }


    /**
     * 删除点赞
     */
    @ApiOperation("删除点赞")
    @PostMapping("/remove")
    public R remove(@RequestBody IdDto<String> idDto) {
        String[] strArr = idDto.getId().split(",");
        Long[] ids = Convert.toLongArray(strArr);
        return toAjax(iPraiseService.deleteWithValidByIds(Arrays.asList(ids), true) ? 1 : 0);
    }

    /**
     * 查询新点赞消息列表
     */
    @ApiOperation("查询新点赞消息列表")
    @PostMapping("/newList")
    public TableDataInfo<NewPraiseVo> newList(@RequestBody PraiseDto dto) {
        Long userId = LoginHelper.getUserId();
        dto.setMemberId(userId);
        return iPraiseService.queryNewPageList(dto);
    }

}
