package com.euler.resource.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.euler.common.core.utils.JsonHelper;
import com.euler.common.oss.enumd.OssEnumd;
import com.euler.common.oss.properties.OssProperties;
import com.euler.resource.domain.Patch;
import com.euler.resource.domain.dto.PatchDto;
import com.euler.resource.domain.dto.Result;
import com.euler.resource.service.ISysOssConfigService;
import com.euler.resource.service.ISysOssService;
import com.euler.resource.service.PatchService;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 *
 */
@RestController
@RequestMapping("/patch")
@Slf4j
@CrossOrigin
public class PatchController {
    @Autowired
    private PatchService patchService;

    @Autowired
    public Validator validator;
    @Autowired
    private   ISysOssConfigService ossConfigService;

    @Autowired
    private ISysOssService ossService;



    /**
     * 补丁包添加
     */
    @PostMapping("/add")
    public Result add(HttpServletRequest httpServletRequest, @RequestPart("patchFile") MultipartFile file, PatchDto patchDto) {
        if (ObjectUtil.isNull(file)|| file.isEmpty()) {
            return Result.failed("上传文件不能为空");
        }
        String params = JsonHelper.toJson(patchDto);
        log.info("请求路径：{}，请求参数：{}", httpServletRequest.getRequestURI(), params);
        String msg = validate(patchDto);
        if (StringUtils.isNotEmpty(msg)){
            return Result.failed(msg);
        }

        Patch patch = JsonHelper.copyObj(patchDto, Patch.class);
        var sysOss = ossService.upload(file,OssEnumd.UPYUNPATCH.getValue(),false);
        patch.setPatchFile(sysOss.getUrl());
        patch.setIsDelete(0);
        boolean insert = patchService.save(patch);
        return Result.restResult(null, insert, insert ? "" : "添加失败");
    }


    private <T> String validate(T t) {
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(t);
        String message = constraintViolations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));

        return message;
    }

    private String getEndpointLink(String url) {
        if (!url.contains("patch.eulertu.cn")) {
            OssProperties properties = ossConfigService.getOssPropertiesByKey(OssEnumd.UPYUNPATCH.getValue());

            String endpoint = properties.getEndpoint();
            StringBuilder sb = new StringBuilder(endpoint);
            sb.append("/");
            sb.append(url);
            return sb.toString();
        } else {
            return url;
        }
    }


    /**
     * 补丁包列表
     */
    @PostMapping("/list")
    public Result list() {
        LambdaQueryWrapper<Patch> patchLambdaQueryWrapper = Wrappers.<Patch>lambdaQuery().orderByDesc(Patch::getId).last("limit 100");
        List<Patch> patches = patchService.list(patchLambdaQueryWrapper);
        if (patches != null && !patches.isEmpty()) {
            patches.forEach(a -> {
                a.setPatchFile(getEndpointLink(a.getPatchFile()));
            });
        }
        return Result.ok(patches);
    }

}
