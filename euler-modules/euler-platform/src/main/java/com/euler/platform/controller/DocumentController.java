package com.euler.platform.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.convert.Convert;
import cn.hutool.http.HtmlUtil;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdDto;
import com.euler.common.core.validate.AddGroup;
import com.euler.common.core.validate.EditGroup;
import com.euler.common.core.web.controller.BaseController;
import com.euler.common.idempotent.annotation.RepeatSubmit;
import com.euler.common.log.annotation.Log;
import com.euler.common.log.enums.BusinessType;
import com.euler.platform.domain.bo.OpenDocumentBo;
import com.euler.platform.domain.vo.OpenDocumentListVo;
import com.euler.platform.domain.vo.OpenDocumentVo;
import com.euler.platform.service.IOpenDocumentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

/**
 * 文档资源中心Controller
 *
 * @author open
 * @date 2022-02-21
 */
@Validated
@Api(value = "文档资源控制器", tags = {"文档资源"})
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/document")
public class DocumentController extends BaseController {

    @Autowired
    private IOpenDocumentService openDocumentService;

    /**
     * 查询文档分类列表
     */
    @ApiOperation("查询文档分类列表")
    @PostMapping("/list")
    public R<List<OpenDocumentListVo>> list(@RequestBody IdDto<Integer> idDto) {
        var list = openDocumentService.documentList(idDto);

        return R.ok(list);

    }


    /**
     * 获取文档详细信息
     */
    @ApiOperation("获取文档详细信息")
    @PostMapping("/getInfo")
    public R<OpenDocumentVo> getInfo(@Validated @RequestBody IdDto<Long> idDto) {
        return R.ok(openDocumentService.queryById(idDto.getId()));
    }

    /**
     * 新增文档
     */
    @ApiOperation("新增文档")
    @SaCheckPermission("platform:document:add")
    @Log(title = "文档资源", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping("/add")
    public R<Void> add(@Validated(AddGroup.class) @RequestBody OpenDocumentBo bo) {
        bo.setContent(HtmlUtil.unescape(bo.getContent()));

        return toAjax(openDocumentService.insertByBo(bo));
    }

    /**
     * 修改文档
     */
    @ApiOperation("修改文档")
    @SaCheckPermission("platform:document:edit")
    @Log(title = "文档资源", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PostMapping("/edit")
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody OpenDocumentBo bo) {
        bo.setContent(HtmlUtil.unescape(bo.getContent()));
        return toAjax(openDocumentService.updateByBo(bo));
    }

    /**
     * 删除文档
     */
    @ApiOperation("删除文档")
    @SaCheckPermission("platform:document:remove")
    @Log(title = "文档资源", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public R<Void> remove(@Validated @RequestBody IdDto<String> idDto) {

        return toAjax(openDocumentService.deleteWithValidByIds(Convert.convert(Collection.class, idDto.getId()), true));
    }



}
