package com.euler.sdk.controller;

import com.euler.common.core.domain.R;
import com.euler.common.core.utils.BeanCopyUtils;
import com.euler.common.core.utils.DateUtils;
import com.euler.sdk.api.domain.MemberBanVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Validated
@Api(value = "公用方法", tags = {"异公用方法管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/common")
public class CommonController {

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
