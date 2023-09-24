package com.euler.system.mapper;


import com.euler.common.mybatis.core.mapper.BaseMapperPlus;
import com.euler.system.api.domain.AppConfig;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AppConfigMapper extends BaseMapperPlus<AppConfigMapper, AppConfig,AppConfig> {
}
