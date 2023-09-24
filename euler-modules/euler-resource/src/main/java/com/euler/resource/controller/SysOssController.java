package com.euler.resource.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpException;
import cn.hutool.http.HttpUtil;
import com.euler.common.core.utils.JsonUtils;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.oss.constant.OssConstant;
import com.euler.common.oss.enumd.OssEnumd;
import com.euler.common.oss.properties.OssProperties;
import com.euler.common.redis.utils.RedisUtils;
import com.euler.resource.domain.SysOss;
import com.euler.resource.domain.bo.OssSignBo;
import com.euler.resource.domain.bo.SysOssBo;
import com.euler.resource.domain.vo.OssSignVo;
import com.euler.resource.domain.vo.SysOssVo;
import com.euler.resource.service.ISysOssConfigService;
import com.euler.resource.service.ISysOssService;
import com.euler.common.core.domain.R;
import com.euler.common.core.exception.ServiceException;
import com.euler.common.core.utils.file.FileUtils;
import com.euler.common.core.validate.QueryGroup;
import com.euler.common.core.web.controller.BaseController;
import com.euler.common.log.annotation.Log;
import com.euler.common.log.enums.BusinessType;
import com.euler.common.mybatis.core.page.PageQuery;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.upyun.Base64Coder;
import com.upyun.Params;
import com.upyun.RestManager;
import com.upyun.UpYunUtils;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import okhttp3.HttpUrl;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件上传 控制层
 *
 * @author euler
 */
@Validated
@Api(value = "对象存储控制器", tags = {"对象存储管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/oss")
public class SysOssController extends BaseController {

    private final ISysOssService ossService;
    private final ISysOssConfigService ossConfigService;

    /**
     * 查询OSS对象存储列表
     */
    @ApiOperation("查询OSS对象存储列表")
    @SaCheckPermission("system:oss:list")
    @GetMapping("/list")
    public TableDataInfo<SysOssVo> list(@Validated(QueryGroup.class) SysOssBo bo, PageQuery pageQuery) {
        return ossService.queryPageList(bo, pageQuery);
    }

    /**
     * 上传OSS对象存储
     */
    @ApiOperation("上传OSS对象存储")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "file", value = "文件", paramType = "query", dataTypeClass = File.class, required = true)
    })
    @SaCheckPermission("system:oss:upload")
    @PostMapping("/upload")
    public R<Map<String, String>> upload(@RequestPart("file") MultipartFile file, String type) {
        if (ObjectUtil.isNull(file)) {
            throw new ServiceException("上传文件不能为空");
        }
        SysOss oss = ossService.upload(file, type);
        Map<String, String> map = new HashMap<>(4);
        map.put("url", oss.getUrl());
        map.put("fileName", oss.getFileName());
        map.put("originalName", oss.getOriginalName());
        map.put("ossId", oss.getOssId() == null ? null : oss.getOssId().toString());
        return R.ok(map);
    }

    @ApiOperation("下载OSS对象存储")
    @SaCheckPermission("system:oss:download")
    @GetMapping("/download/{ossId}")
    public void download(@ApiParam("OSS对象ID") @PathVariable Long ossId, HttpServletResponse response) throws IOException {
        SysOss sysOss = ossService.getById(ossId);
        if (ObjectUtil.isNull(sysOss)) {
            throw new ServiceException("文件数据不存在!");
        }
        response.reset();
        FileUtils.setAttachmentResponseHeader(response, sysOss.getOriginalName());
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE + "; charset=UTF-8");
        long data;
        try {
            data = HttpUtil.download(sysOss.getUrl(), response.getOutputStream(), false);
        } catch (HttpException e) {
            if (e.getMessage().contains("403")) {
                throw new ServiceException("无读取权限, 请在对应的OSS开启'公有读'权限!");
            } else {
                throw new ServiceException(e.getMessage());
            }
        }
        response.setContentLength(Convert.toInt(data));
    }

    /**
     * 删除OSS对象存储
     */
    @ApiOperation("删除OSS对象存储")
    @SaCheckPermission("system:oss:remove")
    @Log(title = "OSS对象存储", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ossIds}")
    public R<Void> remove(@ApiParam("OSS对象ID串")
                          @NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ossIds) {
        return toAjax(ossService.deleteWithValidByIds(Arrays.asList(ossIds), true) ? 1 : 0);
    }

    @SneakyThrows
    @ApiOperation("获取OSS上传签名")
    @PostMapping("/getUpyunSign")
    public R<OssSignVo> getUpyunSign(@RequestBody OssSignBo ossSignBo) {
        String ossType = ossSignBo.getOssType();
        if (StringUtils.isEmpty(ossType))
            ossType = OssEnumd.UPYUN.getValue();

        OssProperties properties = ossConfigService.getOssPropertiesByKey(ossType);
        if (properties == null)
            return R.fail("上传配置不存在，请联系客服处理");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(Params.BUCKET, properties.getBucketName());
        params.put(Params.SAVE_KEY, ossSignBo.getFilePath());
        int expiration = 1800;
        params.put(Params.EXPIRATION, System.currentTimeMillis() / 1000 + expiration);

        StringBuilder sb = new StringBuilder();
        String sp = "&";

        sb.append("POST&/");
        sb.append(properties.getBucketName());

        String date = UpYunUtils.getGMTDate();
        params.put(Params.DATE, date);
        sb.append(sp);
        sb.append(date);

        String policy = UpYunUtils.getPolicy(params);
        sb.append(sp);
        sb.append(policy);

        byte[] hmac = UpYunUtils.calculateRFC2104HMACRaw(UpYunUtils.md5(properties.getSecretKey()), sb.toString().trim());

        String signature = "UPYUN " + properties.getAccessKey() + ":" + Base64Coder.encodeLines(hmac).trim();

        OssSignVo ossSignVo = new OssSignVo(signature, policy, properties.getBucketName(), properties.getEndpoint());
        return R.ok(ossSignVo);
    }

    @SneakyThrows
    @ApiOperation("获取OSS删除签名")
    @PostMapping("/getUpyunDelSign")
    public R<OssSignVo> getUpyunDelSign(@Validated @RequestBody OssSignBo ossSignBo) {
        String date = UpYunUtils.getGMTDate();
        Object json = RedisUtils.getCacheObject(OssConstant.SYS_OSS_KEY + OssEnumd.UPYUN.getValue());
        OssProperties properties = JsonUtils.parseObject(json.toString(), OssProperties.class);
        // 获取链接
        String url = RestManager.ED_AUTO + UpYunUtils.formatPath(properties.getBucketName(), ossSignBo.getFilePath());

        String signature = UpYunUtils.sign("DELETE", date, HttpUrl.get(url).encodedPath(), properties.getAccessKey(), UpYunUtils.md5(properties.getSecretKey()), null);
        OssSignVo ossSignVo = new OssSignVo(signature, "", properties.getBucketName(), properties.getEndpoint());
        return R.ok(ossSignVo);

    }

}
