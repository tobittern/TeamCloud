package com.euler.community.controller;

import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdDto;
import com.euler.common.core.validate.AddGroup;
import com.euler.common.core.validate.EditGroup;
import com.euler.common.core.web.controller.BaseController;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.common.satoken.utils.LoginHelper;
import com.euler.community.domain.bo.TopicBo;
import com.euler.community.domain.dto.TopicDto;
import com.euler.community.domain.vo.TopicVo;
import com.euler.community.service.ITopicService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 话题Controller
 * 前端访问路由地址为:/community/topic
 * @author euler
 * @date 2022-06-06
 */
@Validated
@Api(value = "话题控制器", tags = {"话题管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/topic")
public class TopicController extends BaseController {

    private final ITopicService iTopicService;

    /**
     * 查询话题列表
     */
    @ApiOperation("查询话题列表")
    @PostMapping("/list")
    public TableDataInfo<TopicVo> list(@RequestBody TopicDto dto) {
        return iTopicService.queryPageList(dto);
    }

    /**
     * 获取话题详细信息
     */
    @ApiOperation("获取话题详细信息")
    @PostMapping("/getInfo")
    public R<TopicVo> getInfo(@RequestBody IdDto<Long> idDto) {
        return R.ok(iTopicService.queryById(idDto.getId()));
    }

    /**
     * 新增话题
     */
    @ApiOperation("新增话题")
    @PostMapping("/add")
    public R add(@Validated(AddGroup.class) @RequestBody TopicBo bo) {
        bo.setMemberId(LoginHelper.getUserId());
        return iTopicService.insertByBo(bo);
    }

    /**
     * 修改话题
     */
    @ApiOperation("修改话题")
    @PostMapping("/edit")
    public R edit(@Validated(EditGroup.class) @RequestBody TopicBo bo) {
        bo.setMemberId(LoginHelper.getUserId());
        return iTopicService.updateByBo(bo);
    }

    /**
     * 查询热门话题列表
     */
    @ApiOperation("查询热门话题列表")
    @PostMapping("/hotList")
    public TableDataInfo<TopicVo> hotList(@RequestBody TopicDto dto) {
        return iTopicService.queryList(dto);
    }

}
