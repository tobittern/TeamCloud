package com.euler.system.dubbo;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.euler.system.api.RemoteSignService;
import com.euler.system.api.domain.AppConfig;
import com.euler.system.mapper.AppConfigMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 */
@RequiredArgsConstructor
@Service
@DubboService
@Slf4j
public class RemoteSignServiceImpl implements RemoteSignService {

    @Autowired
    private AppConfigMapper appConfigMapper;





    @Override
    public Integer addAppConfig(AppConfig appConfig) {
        return appConfigMapper.insert(appConfig);
    }

    /**
     * 防止缓存穿透获取appid信息
     *
     * @param appId
     * @return
     */
    @Override
    public AppConfig getAppConfig(String appId) {

        var queryWrapper = new LambdaQueryWrapper<AppConfig>().eq(AppConfig::getIsShow, 1).eq(AppConfig::getAppId, appId).last("limit 1");
        var appConfig = appConfigMapper.selectOne(queryWrapper);
        return appConfig;
    }



}
