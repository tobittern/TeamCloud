package com.euler.job.service;

import com.euler.sdk.api.RemoteMemberService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MemberJob {

    @DubboReference
    private RemoteMemberService remoteMemberService;


    @XxlJob("opCancellationMember")
    public void opCancellationMember() {
        log.info("定时注销会员--执行开始");

        var list = remoteMemberService.getOpCancellationMemberList();
        if (list != null && !list.isEmpty()) {
            for (var cancelMember : list) {
                try {
                    boolean flag = remoteMemberService.cancellationMember(cancelMember.getId(), cancelMember.getMemberId());

                    //下线用户
                    remoteMemberService.downLineUser(cancelMember.getMemberId(), -1, -1);

                    log.info("定时注销会员--注销会员id：{}，数据id：{}，执行结果：{}", cancelMember.getMemberId(), cancelMember.getId(), flag);
                } catch (Exception e) {
                    log.error("定时注销会员--注销会员id：{}，数据id：{}，执行异常", cancelMember.getMemberId(), cancelMember.getId(), e);
                }

            }
        } else {
            log.info("定时注销会员--暂无要执行的数据--执行结束");

        }
    }
}
