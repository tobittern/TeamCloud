package com.euler.community.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.euler.community.api.domain.UserExtend;
import com.euler.community.mapper.UserExtendMapper;
import com.euler.community.service.IUserExtendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 用户扩展信息Service业务层处理
 *
 * @author euler
 * @date 2022-07-14
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class UserExtendServiceImpl extends ServiceImpl<UserExtendMapper, UserExtend> implements IUserExtendService {

    private final UserExtendMapper baseMapper;


}
