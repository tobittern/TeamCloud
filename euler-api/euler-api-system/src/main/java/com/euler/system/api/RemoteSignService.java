package com.euler.system.api;

import com.euler.common.core.domain.dto.RequestHeaderDto;
import com.euler.system.api.domain.AppConfig;

import java.util.List;


public interface RemoteSignService {

    Integer addAppConfig(AppConfig appConfig);


    AppConfig getAppConfig(String appId);

}
