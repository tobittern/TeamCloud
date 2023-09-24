package com.euler.sdk.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdDto;
import com.euler.common.core.exception.ServiceException;
import com.euler.common.core.validate.AddGroup;
import com.euler.common.core.web.controller.BaseController;
import com.euler.common.log.annotation.Log;
import com.euler.common.log.enums.BusinessType;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.common.satoken.utils.LoginHelper;
import com.euler.sdk.domain.bo.MemberRightsReceiveRecordBo;
import com.euler.sdk.domain.dto.MemberRightsDto;
import com.euler.sdk.domain.vo.MemberRightsVo;
import com.euler.sdk.service.IMemberRightsReceiveRecordService;
import com.euler.sdk.service.IMemberRightsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Arrays;

/**
 * 会员权益Controller
 * 前端访问路由地址为:/sdk/memberRights
 *
 * @author euler
 * @date 2022-03-21
 */
@Validated
@Api(value = "会员权益控制器", tags = {"会员权益管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/memberRights")
public class MemberRightsController extends BaseController {

    private final IMemberRightsService iMemberRightsService;
    private final IMemberRightsReceiveRecordService iReceiveRecordService;

    /**
     * 查询会员权益列表
     */
    @ApiOperation("查询会员权益列表")
    @SaCheckPermission("sdk:memberRights:list")
    @PostMapping("/list")
    public TableDataInfo<MemberRightsVo> list(@RequestBody MemberRightsDto dto) {
        // 查询设置用户
        Long userId = LoginHelper.getUserId();
        dto.setId(userId);
        return iMemberRightsService.queryPageList(dto);
    }

    /**
     * 获取会员权益详细信息
     */
    @ApiOperation("获取会员权益详细信息")
    @SaCheckPermission("sdk:memberRights:getInfo")
    @PostMapping("/getInfo")
    public R<MemberRightsVo> getInfo(@RequestBody IdDto<Long> idDto) {
        return R.ok(iMemberRightsService.queryById(idDto));
    }

    /**
     * 前端获取会员权益详细信息
     */
    @ApiOperation("前端获取会员权益详细信息")
    @PostMapping("/info")
    public R<MemberRightsVo> info() {
        Long userId = LoginHelper.getUserId();
        IdDto<Long> idDto = new IdDto<>();
        idDto.setId(userId);
        return R.ok(iMemberRightsService.queryById(idDto));
    }

    /**
     * 删除会员权益
     */
    @ApiOperation("删除会员权益")
    @Log(title = "会员权益", businessType = BusinessType.DELETE)
    @SaCheckPermission("sdk:memberRights:remove")
    @PostMapping("/remove")
    public R remove(@RequestBody IdDto<String> idDto) {
        // 判断传输过来的数据是否包含多个
        String[] strArr = idDto.getId().split(",");
        Long[] ids = Convert.toLongArray(strArr);
        return iMemberRightsService.deleteWithValidByIds(Arrays.asList(ids), true);
    }

    /**
     * 上传年费会员协议
     */
    @ApiOperation("上传年费会员协议")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "file", value = "文件", paramType = "query", dataTypeClass = File.class, required = true)
    })
    @Log(title = "会员权益", businessType = BusinessType.INSERT)
    @PostMapping("/upload")
    public R upload(@RequestPart("file") MultipartFile multipartFile) {
        if (ObjectUtil.isNull(multipartFile)) {
            throw new ServiceException("上传文件不能为空");
        }
        return iMemberRightsService.upload(multipartFile);
    }

    /**
     * 新增会员权益领取记录
     */
    @ApiOperation("新增会员权益领取记录")
    @Log(title = "会员权益领取记录", businessType = BusinessType.INSERT)
    @PostMapping("/insertReceiveRecord")
    public R insertReceiveRecord(@Validated(AddGroup.class) @RequestBody MemberRightsReceiveRecordBo bo) {
        // 查询设置用户
        Long userId = LoginHelper.getUserId();
        bo.setMemberId(userId);
        return iReceiveRecordService.insertByBo(bo);
    }
}
