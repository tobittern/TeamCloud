package com.euler.community.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.euler.common.core.domain.R;
import com.euler.community.domain.entity.UserBehavior;

/**
 * 用户行为记录Service接口
 *
 * @author euler
 * @date 2022-07-14
 */
public interface IUserBehaviorService extends IService<UserBehavior> {

    /**
     * 检测用户行为是否合法
     * @param userId
     * @param type
     * @param dynamicId
     * @return
     */
    Boolean checkUserBehaviorLegitimate(Long userId, Integer type, Long dynamicId);

    /**
     * 获取用户头像
     * @param sex
     * @param avatar
     * @return
     */
    String getAvatar(String sex, String avatar);
}
