package com.euler.community.dubbo;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.euler.common.core.domain.R;
import com.euler.common.core.utils.StringUtils;
import com.euler.community.api.RemoteUserExtendService;
import com.euler.community.api.domain.UserExtend;
import com.euler.community.mapper.UserExtendMapper;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author euler
 * @date 2022-06-01
 */
@RequiredArgsConstructor
@Service
@DubboService
public class RemoteUserExtendServiceImpl implements RemoteUserExtendService {

    @Autowired
    private UserExtendMapper baseMapper;

    /**
     * 更新关联表里的昵称/头像/是否是官方账号
     *
     * @param entity
     * @return
     */
    @Override
    public R updateUserDetail(UserExtend entity) {
        // 更新关联表里的昵称/头像/是否是官方账号
        baseMapper.update(null, new LambdaUpdateWrapper<UserExtend>()
            .set(StringUtils.isNotBlank(entity.getNickName()), UserExtend::getNickName, entity.getNickName())
            .set(StringUtils.isNotBlank(entity.getAvatar()), UserExtend::getAvatar, entity.getAvatar())
            .set(entity.getIsOfficial() != null, UserExtend::getIsOfficial, entity.getIsOfficial())
            .set(entity.getSex() != null, UserExtend::getSex, entity.getSex())

            .eq(UserExtend::getMemberId, entity.getMemberId()));

        return R.ok("更新成功");
    }

}
