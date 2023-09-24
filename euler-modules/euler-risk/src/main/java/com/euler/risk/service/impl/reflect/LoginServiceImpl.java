package com.euler.risk.service.impl.reflect;

import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson.JSONObject;
import com.euler.common.core.enums.LoginTypeEnum;
import com.euler.common.core.utils.StringUtils;
import com.euler.risk.domain.dto.BehaviorMqMsgDto;
import com.euler.risk.domain.dto.BehaviorUserDto;
import com.euler.risk.service.reflect.IBehaviorUserService;
import com.euler.sdk.api.RemoteMemberService;
import com.euler.sdk.api.domain.LoginMemberVo;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
@Slf4j
public class LoginServiceImpl implements IBehaviorUserService {


    @DubboReference
    private RemoteMemberService remoteMemberService;

    /**
     * 从用户行为中获取用户信息,拦截/auth/login方法
     *
     * @param msgDto
     * @return
     */
    @Override
    public BehaviorUserDto getUserInfo(BehaviorMqMsgDto msgDto) {

        if (msgDto == null)
            return null;
        BehaviorUserDto userDto = new BehaviorUserDto();
        //获取登录参数
        JSONObject jsonObject = JSONObject.parseObject(msgDto.getRequestData());
        //登录类型，只拦截新版登录
        String loginType = jsonObject.getString("loginType");


        if (LoginTypeEnum.IDLOGIN.getLoginTypeNum().equals(loginType)) {
            //登录用户Id
            Long userId = Convert.toLong(jsonObject.getString("userName"), 0L);


            if (userId > 0L) {
                userDto.setUserId(userId);
                LoginMemberVo member = remoteMemberService.getMemberByIdNoException(userId);

                if (member != null)
                    userDto.setMobile(member.getMobile()).setAccount(member.getAccount());
                return userDto;

            }

        } else {
            String userName = jsonObject.getString("userName");
            var requestHeader = msgDto.getRequestHeader();
            if (StringUtils.isNotBlank(requestHeader.getDevice()) && requestHeader.getDeviceInfo() != null && StringUtils.isNotBlank(userName)) {
                LoginMemberVo member = remoteMemberService.loginByUserNameNoEx(userName);

                if (member != null)
                    userDto.setMobile(member.getMobile()).setAccount(member.getAccount()).setUserId(member.getId());
                return userDto;

            }
        }


        return userDto;
    }
}
