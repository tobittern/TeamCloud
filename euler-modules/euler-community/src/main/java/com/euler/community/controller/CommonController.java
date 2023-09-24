package com.euler.community.controller;

import cn.hutool.core.lang.Dict;
import com.euler.common.core.domain.R;
import com.euler.common.core.utils.BeanCopyUtils;
import com.euler.common.core.utils.DateUtils;
import com.euler.common.core.utils.JsonUtils;
import com.euler.common.log.annotation.Log;
import com.euler.common.log.enums.BusinessType;
import com.euler.community.config.CommonCommunityConfig;
import com.euler.sdk.api.domain.MemberBanVo;
import com.euler.system.api.RemoteDictService;
import com.euler.system.api.domain.SysDictData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 公告配置
 */
@Slf4j
@Validated
@Api(value = "公共配置控制器", tags = {"公共配置管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/common")
public class CommonController {

    @DubboReference
    private RemoteDictService remoteDictService;

    @Resource
    private CommonCommunityConfig commonCommunityConfig;

    /**
     * 获取云的前缀
     *
     * @return
     */
    @ApiOperation("获取有拍云前缀")
    @PostMapping("/getYunPrefix")
    @Log(title = "有拍云前缀", businessType = BusinessType.OTHER)
    public R<Void> getYunPrefix() {
        return R.ok(commonCommunityConfig.getYunDomain());
    }


    /**
     * 社区配置获取
     *
     * @return
     */
    @ApiOperation("社区配置获取")
    @PostMapping(value = "/dict/{configName}")
    public R getDynamicOperationType(@PathVariable String configName) {
        List<SysDictData> data = remoteDictService.selectDictDataByType(configName);
        List<Map> list = new ArrayList<>();
        if (data != null && !data.isEmpty()) {
            data.forEach(a -> {
                Dict dict = JsonUtils.parseMap(a.getRemark());
                list.add(dict);
            });
        }
        return R.ok(list);
    }

    /**
     * 用户封禁之后的重定向位置
     */
    @ApiOperation("用户封禁之后的重定向位置")
    @PostMapping("/userBlockingDispatcher")
    public R userBlockingDispatcher(HttpServletRequest request, HttpServletResponse response) {
        Object returnMessage = request.getAttribute("returnMessage");
        MemberBanVo copy = BeanCopyUtils.copy(returnMessage, MemberBanVo.class);
        R<MemberBanVo> r = new R<>();
        r.setCode(4003);
        String msg = "你被封禁了";
        if (copy != null) {
            String memberName = "";
            if (copy.getMemberName() != null) {
                memberName = copy.getMemberName();
            }
            if (copy.getIsPermanent() != null && copy.getIsPermanent().equals(1)) {
                msg = memberName + ",永久封号," + copy.getRecord();
            } else {
                msg = memberName + "," + DateUtils.parseDateToStr("yyyy-MM-dd HH:mm:ss", copy.getEndTime()) + "," + copy.getRecord();
            }
        }
        r.setMsg(msg);
        return r;
    }

}
