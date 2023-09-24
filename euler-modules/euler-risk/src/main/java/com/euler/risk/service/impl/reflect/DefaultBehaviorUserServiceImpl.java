package com.euler.risk.service.impl.reflect;

import com.euler.risk.domain.dto.BehaviorMqMsgDto;
import com.euler.risk.domain.dto.BehaviorUserDto;
import com.euler.risk.service.reflect.IBehaviorUserService;
import com.euler.sdk.api.RemoteMemberService;
import com.euler.sdk.api.domain.LoginMemberVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DefaultBehaviorUserServiceImpl implements IBehaviorUserService {

    @DubboReference
    private RemoteMemberService remoteMemberService;

    /**
     * 从用户行为中获取用户信息
     *
     * @param msgDto
     * @return
     */
    @Override
    public BehaviorUserDto getUserInfo(BehaviorMqMsgDto msgDto) {

        if (msgDto == null)
            return null;
        BehaviorUserDto userDto = new BehaviorUserDto();
        if (msgDto.getUserId() != null && msgDto.getUserId() > 0L) {
            userDto
                .setUserId(msgDto.getUserId());
            LoginMemberVo member = remoteMemberService.getMemberByIdNoException(userDto.getUserId());

            if (member != null)
                userDto.setMobile(member.getMobile()).setAccount(member.getAccount());
            return userDto;

        }

        return null;
    }
}
