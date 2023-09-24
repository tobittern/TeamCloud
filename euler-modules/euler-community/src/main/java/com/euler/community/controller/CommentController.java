package com.euler.community.controller;

import cn.hutool.core.convert.Convert;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdDto;
import com.euler.common.core.validate.AddGroup;
import com.euler.common.core.web.controller.BaseController;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.common.satoken.utils.LoginHelper;
import com.euler.community.domain.bo.CommentBo;
import com.euler.community.domain.dto.CommentDto;
import com.euler.community.domain.vo.CommentFrontVo;
import com.euler.community.domain.vo.NewCommentFrontVo;
import com.euler.community.service.ICommentService;
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
 * 评论Controller
 * 前端访问路由地址为:/community/comment
 *
 * @author euler
 * @date 2022-06-07
 */
@Validated
@Api(value = "评论控制器", tags = {"评论管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/comment")
public class CommentController extends BaseController {

    private final ICommentService iCommentService;

    /**
     * 查询评论列表
     */
    @ApiOperation("查询评论列表")
    @PostMapping("/list")
    public TableDataInfo<CommentFrontVo> list(@RequestBody CommentDto dto) {
        return iCommentService.queryPageList(dto);
    }

    /**
     * 新增评论
     */
    @ApiOperation("新增评论")
    @PostMapping("/add")
    public R add(@Validated(AddGroup.class) @RequestBody CommentBo bo) {
        Long userId = LoginHelper.getUserId();
        bo.setMemberId(userId);
        return iCommentService.insertByBo(bo);
    }

    /**
     * 删除评论
     */
    @ApiOperation("删除评论")
    @PostMapping("/remove")
    public R remove(@RequestBody IdDto<String> idDto) {
        String[] strArr = idDto.getId().split(",");
        Long[] ids = Convert.toLongArray(strArr);
        return toAjax(iCommentService.deleteWithValidByIds(Arrays.asList(ids), true) ? 1 : 0);
    }

    /**
     * 查询新评论消息列表
     */
    @ApiOperation("查询新评论消息列表")
    @PostMapping("/newList")
    public TableDataInfo<NewCommentFrontVo> newList(@RequestBody CommentDto dto) {
        return iCommentService.queryNewPageList(dto);
    }

}
