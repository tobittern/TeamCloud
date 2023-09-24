package com.euler.statistics.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.euler.platform.api.domain.OpenGame;
import com.euler.statistics.mapper.OpenGameMapper;
import com.euler.statistics.service.IOpenGameService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * OpenGameService业务层处理
 *
 * @author euler
 * @date 2022-09-05
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class OpenGameServiceImpl extends ServiceImpl<OpenGameMapper, OpenGame> implements IOpenGameService {

    @Autowired
    private OpenGameMapper baseMapper;


}
