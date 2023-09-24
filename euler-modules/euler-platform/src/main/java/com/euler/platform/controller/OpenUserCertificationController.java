package com.euler.platform.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdDto;
import com.euler.common.core.validate.AddGroup;
import com.euler.common.core.validate.EditGroup;
import com.euler.common.core.validate.QueryGroup;
import com.euler.common.core.web.controller.BaseController;
import com.euler.common.idempotent.annotation.RepeatSubmit;
import com.euler.common.log.annotation.Log;
import com.euler.common.log.enums.BusinessType;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.common.satoken.utils.LoginHelper;
import com.euler.platform.domain.bo.OpenUserCertificationAuditRecordBo;
import com.euler.platform.domain.bo.OpenUserCertificationBo;
import com.euler.platform.domain.dto.CommonIdPageDto;
import com.euler.platform.domain.dto.OpenUserCertificationPageDto;
import com.euler.platform.domain.vo.OpenUserCertificationAuditRecordVo;
import com.euler.platform.domain.vo.OpenUserCertificationDataCountVo;
import com.euler.platform.domain.vo.OpenUserCertificationVo;
import com.euler.platform.service.IOpenUserCertificationAuditRecordService;
import com.euler.platform.service.IOpenUserCertificationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用户的资质认证记录Controller
 *
 * @author open
 * @date 2022-02-23
 */
@Validated
@Api(value = "用户的资质认证记录控制器", tags = {"用户的资质认证记录管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/userCertification")
public class OpenUserCertificationController extends BaseController {

    @Autowired
    private IOpenUserCertificationService iOpenUserCertificationService;

    @Autowired
    private IOpenUserCertificationAuditRecordService iOpenUserCertificationAuditRecordService;


    /**
     * 资质申请记录数据查询
     */
    @ApiOperation("资质申请记录数据查询")
    @SaCheckPermission("platform:userCertification:count")
    @PostMapping("/count")
    public OpenUserCertificationDataCountVo count() {
        return iOpenUserCertificationService.selectCount();
    }

    /**
     * 查询用户的资质认证记录列表
     */
    @ApiOperation("查询用户的资质认证记录列表")
    @SaCheckPermission("platform:userCertification:list")
    @PostMapping("/list")
    public TableDataInfo<OpenUserCertificationVo> list(@RequestBody OpenUserCertificationPageDto openUserCertificationPageDto) {
        Long userId = LoginHelper.getUserId();
        openUserCertificationPageDto.setUserId(userId);
        return iOpenUserCertificationService.queryPageList(openUserCertificationPageDto);
    }

    /**
     * 获取用户的资质认证记录详细信息
     */
    @ApiOperation("获取用户的资质认证记录详细信息")
    @SaCheckPermission("platform:userCertification:getInfo")
    @PostMapping("/getInfo")
    public R<OpenUserCertificationVo> getInfo(@RequestBody IdDto<Integer> idDto) {
        Long userId = LoginHelper.getUserId();
        return R.ok(iOpenUserCertificationService.selectInfo(idDto.getId(), userId));
    }

    /**
     * 新增用户的资质认证记录
     */
    @ApiOperation("新增用户的资质认证记录")
    @SaCheckPermission("platform:userCertification:add")
    @Log(title = "用户的资质认证记录", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @RepeatSubmit()
    public R add(@Validated(AddGroup.class) @RequestBody OpenUserCertificationBo bo) {
        // 查询设置用户
        Long userId = LoginHelper.getUserId();
        bo.setUserId(userId);
        return iOpenUserCertificationService.insertByBo(bo);
    }

    /**
     * 修改用户的资质认证记录
     */
    @ApiOperation("修改用户的资质认证记录")
    @SaCheckPermission("platform:userCertification:edit")
    @Log(title = "用户的资质认证记录", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @RepeatSubmit()
    public R edit(@Validated(EditGroup.class) @RequestBody OpenUserCertificationBo bo) {
        // 查询设置用户
        Long userId = LoginHelper.getUserId();
        bo.setUserId(userId);
        return iOpenUserCertificationService.updateByBo(bo);
    }


    /**
     * 游戏管理 撤销审核
     */
    @ApiOperation("撤销审核")
    @SaCheckPermission("platform:userCertification:revokeApproval")
    @Log(title = "撤销审核", businessType = BusinessType.UPDATE)
    @PostMapping("/revokeApproval")
    public R revokeApproval() {
        // 查询设置用户
        Long userId = LoginHelper.getUserId();
        return iOpenUserCertificationService.revokeApproval(userId);
    }

    /**
     * 审核列表
     */
    @ApiOperation("审核用户认证列表")
    @SaCheckPermission("platform:userCertification:auditUserCertificationList")
    @Log(title = "审核用户认证列表")
    @PostMapping("/auditUserCertificationList")
    public TableDataInfo<OpenUserCertificationAuditRecordVo> auditUserCertificationList(@RequestBody CommonIdPageDto dto) {
        // 审核审核人的用户ID
        return iOpenUserCertificationAuditRecordService.auditUserCertificationList(dto);
    }

    /**
     * 审核用户的认证
     */
    @ApiOperation("审核用户认证")
    @SaCheckPermission("platform:userCertification:auditUserCertification")
    @Log(title = "审核用户认证")
    @PostMapping("/auditUserCertification")
    public R auditUserCertification(@Validated(QueryGroup.class) @RequestBody OpenUserCertificationAuditRecordBo bo) {
        // 审核审核人的用户ID
        Long userId = LoginHelper.getUserId();
        bo.setAuditUserId(userId);
        return iOpenUserCertificationService.auditUserCertification(bo);
    }

    /**
     * 用户查询
     */
    @ApiOperation("用户查询")
    @SaCheckPermission("platform:userCertification:search")
    @PostMapping("/search")
    public R search(@RequestBody OpenUserCertificationPageDto dto) {
        return iOpenUserCertificationService.search(dto);
    }

}
