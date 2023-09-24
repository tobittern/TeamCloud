package com.euler.community.controller;

import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdDto;
import com.euler.common.core.enums.UserTypeEnum;
import com.euler.common.core.validate.AddGroup;
import com.euler.common.core.web.controller.BaseController;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.common.satoken.utils.LoginHelper;
import com.euler.community.domain.bo.ReportBo;
import com.euler.community.domain.dto.ReportDto;
import com.euler.community.domain.vo.ReportVo;
import com.euler.community.service.IReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 举报Controller
 * 前端访问路由地址为:/community/report
 * @author euler
 * @date 2022-06-09
 */
@Validated
@Api(value = "举报控制器", tags = {"举报管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/report")
public class ReportController extends BaseController {

    private final IReportService iReportService;

    /**
     * 查询举报列表
     */
    @ApiOperation("查询举报列表")
    @PostMapping("/list")
    public TableDataInfo<ReportVo> list(@RequestBody ReportDto dto) {
        // 系统用户不设置用户ID
        if (!UserTypeEnum.SYS_USER.equals(LoginHelper.getUserType())) {
            dto.setMemberId(LoginHelper.getUserId());
        }
        return iReportService.queryPageList(dto);
    }

    /**
     * 获取举报详细信息
     */
    @ApiOperation("获取举报详细信息")
    @PostMapping("/getInfo")
    public R<ReportVo> getInfo(IdDto<Long> idDto) {
        return R.ok(iReportService.queryById(idDto.getId()));
    }

    /**
     * 举报
     */
    @ApiOperation("举报")
    @PostMapping("/add")
    public R add(@Validated(AddGroup.class) @RequestBody ReportBo bo) {
        bo.setMemberId(LoginHelper.getUserId());
        return iReportService.insertByBo(bo);
    }

}
