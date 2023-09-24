package com.euler.community.controller;

import com.euler.common.core.domain.R;
import com.euler.common.core.validate.AddGroup;
import com.euler.common.core.validate.EditGroup;
import com.euler.common.core.web.controller.BaseController;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.common.satoken.utils.LoginHelper;
import com.euler.community.domain.bo.AttentionBo;
import com.euler.community.domain.dto.AttentionDto;
import com.euler.community.domain.vo.AttentionVo;
import com.euler.community.service.IAttentionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 关注Controller
 * 前端访问路由地址为:/community/attention
 * @author euler
 * @date 2022-06-01
 */
@Validated
@Api(value = "关注控制器", tags = {"关注管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/attention")
public class AttentionController extends BaseController {

    private final IAttentionService iAttentionService;

    /**
     * 查询我的关注列表
     */
    @ApiOperation("查询我的关注列表")
    @PostMapping("/attentionList")
    public TableDataInfo<AttentionVo> attentionList(AttentionDto dto) {
        dto.setMemberId(LoginHelper.getUserId());
        return iAttentionService.queryMyAttentionList(dto);
    }

    /**
     * 查询我的粉丝列表
     */
    @ApiOperation("查询我的粉丝列表")
    @PostMapping("/fansList")
    public TableDataInfo<AttentionVo> fansList(AttentionDto dto) {
        dto.setAttentionUserId(LoginHelper.getUserId());
        return iAttentionService.queryMyFansList(dto);
    }

    /**
     * 查询他人的关注列表
     */
    @ApiOperation("查询他人的关注列表")
    @PostMapping("/othersAttentionList")
    public TableDataInfo<AttentionVo> othersAttentionList(@RequestBody AttentionDto dto) {
        return iAttentionService.queryOthersAttentionList(dto);
    }

    /**
     * 查询他人的粉丝列表
     */
    @ApiOperation("查询他人的粉丝列表")
    @PostMapping("/othersFansList")
    public TableDataInfo<AttentionVo> othersFansList(@RequestBody AttentionDto dto) {
        return iAttentionService.queryOthersFansList(dto);
    }

    /**
     * 查询我的新粉丝列表
     */
    @ApiOperation("查询我的新粉丝列表")
    @PostMapping("/newFansList")
    public TableDataInfo<AttentionVo> newFansList(AttentionDto dto) {
        dto.setAttentionUserId(LoginHelper.getUserId());
        return iAttentionService.queryMyNewFansList(dto);
    }

    /**
     * 新增关注
     */
    @ApiOperation("新增关注")
    @PostMapping("/add")
    public R add(@Validated(AddGroup.class) @RequestBody AttentionBo bo) {
        Long userId = LoginHelper.getUserId();
        bo.setMemberId(userId);
        return iAttentionService.insertByBo(bo);
    }

    /**
     * 取消关注
     */
    @ApiOperation("取消关注")
    @PostMapping("/cancel")
    public R cancel(@Validated(EditGroup.class) @RequestBody AttentionBo bo) {
        Long userId = LoginHelper.getUserId();
        bo.setMemberId(userId);
        return iAttentionService.updateByBo(bo);
    }

    /**
     * 获取关注数量
     */
    @ApiOperation("获取关注数量")
    @PostMapping("/count")
    public R count() {
        return R.ok(iAttentionService.queryAttentionCountById(LoginHelper.getUserId()));
    }

    /**
     * 获取关注数量
     */
    @ApiOperation("获取粉丝数量")
    @PostMapping("/fanCount")
    public R fanCount() {
        return R.ok(iAttentionService.queryFansCountById(LoginHelper.getUserId()));
    }

    /**
     * 根据昵称检索我的关注列表
     */
    @ApiOperation("根据昵称检索我的关注列表")
    @PostMapping("/getAttentionListByName")
    public TableDataInfo<AttentionVo> getAttentionListByName(@RequestBody AttentionDto dto) {
        dto.setMemberId(LoginHelper.getUserId());
        return iAttentionService.queryMyAttentionListByName(dto);
    }

    /**
     * 根据昵称检索我的粉丝列表
     */
    @ApiOperation("根据昵称检索我的粉丝列表")
    @PostMapping("/getFansListByName")
    public TableDataInfo<AttentionVo> getFansListByName(@RequestBody AttentionDto dto) {
        dto.setAttentionUserId(LoginHelper.getUserId());
        return iAttentionService.queryMyFansListByName(dto);
    }

    /**
     * 根据昵称检索他人的关注列表
     */
    @ApiOperation("根据昵称检索他人的关注列表")
    @PostMapping("/getOthersAttentionListByName")
    public TableDataInfo<AttentionVo> getOthersAttentionListByName(@RequestBody AttentionDto dto) {
        return iAttentionService.queryOthersAttentionListByName(dto);
    }

    /**
     * 根据昵称检索他人的粉丝列表
     */
    @ApiOperation("根据昵称检索他人的粉丝列表")
    @PostMapping("/getOthersFansListByName")
    public TableDataInfo<AttentionVo> getOthersFansListByName(@RequestBody AttentionDto dto) {
        return iAttentionService.queryOthersFansListByName(dto);
    }

}
