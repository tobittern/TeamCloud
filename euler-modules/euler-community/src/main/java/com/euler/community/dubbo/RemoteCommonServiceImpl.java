package com.euler.community.dubbo;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.euler.common.core.domain.R;
import com.euler.community.api.RemoteCommonService;
import com.euler.community.api.domain.UserExtend;
import com.euler.community.mapper.UserExtendMapper;
import com.euler.community.service.ICommentService;
import com.euler.community.service.IDynamicService;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@DubboService
public class RemoteCommonServiceImpl implements RemoteCommonService {

    @Autowired
    private IDynamicService iDynamicService;
    @Autowired
    private ICommentService iCommentService;
    @Autowired
    private UserExtendMapper userExtendMapper;

    /**
     * 根据渠道code查询出分包渠道的基础信息
     *
     * @return 渠道分包信息
     */
    @Override
    public R userCancellationClear(Long userId) {
        R dynamicR = iDynamicService.cancellationClearDynamic(userId);
        R commentR = iCommentService.cancellationClearComment(userId);
        if (dynamicR.getCode() == commentR.getCode()) {
            // 删除掉用户扩展表中存在的基础数据
            userExtendMapper.delete(Wrappers.<UserExtend>lambdaQuery().eq(UserExtend::getMemberId, userId));
            return R.ok();
        }
        return R.fail();
    }
}
