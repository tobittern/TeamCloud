package com.euler.community.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.convert.Convert;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdDto;
import com.euler.common.core.domain.dto.IdNameTypeDicDto;
import com.euler.common.core.validate.AddGroup;
import com.euler.common.core.web.controller.BaseController;
import com.euler.common.log.annotation.Log;
import com.euler.common.log.enums.BusinessType;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.common.satoken.utils.LoginHelper;
import com.euler.community.config.DynamicRabbitMqProducer;
import com.euler.community.domain.bo.DynamicBo;
import com.euler.community.domain.dto.DynamicDto;
import com.euler.community.domain.dto.IdTypeCommunityDto;
import com.euler.community.domain.vo.DynamicVo;
import com.euler.community.esMapper.DynamicElasticsearch;
import com.euler.community.service.IDynamicService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

/**
 * 动态Controller
 * 前端访问路由地址为:/community/dynamic
 *
 * @author euler
 * @date 2022-06-01
 */
@Slf4j
@Validated
@Api(value = "动态控制器", tags = {"动态管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/dynamic")
public class DynamicController extends BaseController {

    private final IDynamicService iDynamicService;
    @Autowired
    private DynamicElasticsearch dynamicElasticsearch;
    @Autowired
    private DynamicRabbitMqProducer dynamicRabbitMqProducer;

    /**
     * 后台 - 查询动态列表
     */
    @ApiOperation("查询动态列表")
    @SaCheckPermission("community:dynamic:list")
    @PostMapping("/list")
    public TableDataInfo<DynamicVo> list(@RequestBody DynamicDto dto) {
        return iDynamicService.queryPageList(dto);
    }

    /**
     * 后台 - 获取动态详细信息
     */
    @ApiOperation("获取动态详细信息")
    @SaCheckPermission("community:dynamic:query")
    @PostMapping("/getInfo")
    public R<DynamicVo> getInfo(@RequestBody IdDto<Long> idDto) {
        return R.ok(iDynamicService.queryById(idDto.getId()));
    }

    /**
     * 后台 - 获取动态详细信息
     */
    @ApiOperation("动态审核")
    @SaCheckPermission("community:dynamic:auditDynamic")
    @Log(title = "动态审核", businessType = BusinessType.UPDATE)
    @PostMapping("/auditDynamic")
    public R auditDynamic(@RequestBody IdNameTypeDicDto<Long> dto) {
        return iDynamicService.auditDynamic(dto);
    }

    /**
     * 后台 - 操作动态
     */
    @ApiOperation("操作动态")
    @SaCheckPermission("community:dynamic:operation")
    @PostMapping("/operation")
    public R operation(@RequestBody IdNameTypeDicDto<String> dto) {
        String[] strArr = dto.getId().split(",");
        Long[] ids = Convert.toLongArray(strArr);
        // 首先修改ES中的状态
        try {
            for (Long id : ids) {
                IdNameTypeDicDto<Long> idNameTypeDicDto = new IdNameTypeDicDto();
                idNameTypeDicDto.setId(id);
                idNameTypeDicDto.setType(dto.getType());
                dynamicElasticsearch.operationDataToElasticSearch(idNameTypeDicDto);
            }
        } catch (Exception e) {
        }
        return toAjax(iDynamicService.operationWithValidByIds(Arrays.asList(ids), 0L, dto.getType(), dto.getName()) ? 1 : 0);
    }

    /**
     * 新增动态
     */
    @ApiOperation("新增动态")
    @PostMapping("/add")
    public R add(@Validated(AddGroup.class) @RequestBody DynamicBo bo) {
        Long userId = LoginHelper.getUserId();
        bo.setMemberId(userId);
        return iDynamicService.insertByBo(bo);
    }


    /**
     * 获取动态数量
     */
    @ApiOperation("获取动态数量")
    @PostMapping("/count")
    public R count() {
        return R.ok(iDynamicService.count(LoginHelper.getUserId()));
    }

    /**
     * 删除动态
     */
    @ApiOperation("删除动态")
    @PostMapping("/remove")
    public R remove(@RequestBody IdDto<String> idDto) {
        return iDynamicService.deleteWithValidByIds(idDto);
    }


    /**
     * 指定动态审核通过并入库
     */
    @ApiOperation("指定动态审核通过并入库")
    @PostMapping("/testOpening")
    public R testOpening(@RequestBody IdDto<Long> idDto) {
        // 将数据添加到消息队列里面 先存储到ES中
        String msgId = "add_dynamic_into_es_" + idDto.getId();
        IdTypeCommunityDto idTypeCommunityDto = new IdTypeCommunityDto();
        idTypeCommunityDto.setId(idDto.getId().toString());
        // 数据发送到消息队列中
        dynamicRabbitMqProducer.dynamiInsertIntoEs(idTypeCommunityDto, msgId);
        return R.ok();
    }


}
