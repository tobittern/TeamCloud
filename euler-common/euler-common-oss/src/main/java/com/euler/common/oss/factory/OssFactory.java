package com.euler.common.oss.factory;

import com.euler.common.oss.constant.OssConstant;
import com.euler.common.oss.enumd.OssEnumd;
import com.euler.common.oss.exception.OssException;
import com.euler.common.oss.properties.OssProperties;
import com.euler.common.oss.service.abstractd.AbstractOssStrategy;
import com.euler.common.core.utils.JsonUtils;
import com.euler.common.core.utils.SpringUtils;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.oss.service.IOssStrategy;
import com.euler.common.redis.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * 文件上传Factory
 *
 * @author euler
 */
@Slf4j
public class OssFactory {


    /**
     * 根据类型获取实例
     */
    public static IOssStrategy instance(String type) {
        if (StringUtils.isEmpty(type))
            type = OssEnumd.UPYUN.getValue();
        OssEnumd enumd = OssEnumd.find(type);
        if (enumd == null) {
            throw new OssException("文件存储服务类型无法找到!");
        }
        AbstractOssStrategy strategy = getStrategy(type);
        if (!strategy.isInit) {
            refresh(type);
        }
        return strategy;
    }


    /**
     * 根据类型获取实例
     */
    public static IOssStrategy instance(String type, OssProperties properties) {
        AbstractOssStrategy strategy = getStrategy(type);
        strategy.init(properties);
        return strategy;
    }


    private static void refresh(String type) {
        Object json = RedisUtils.getCacheObject(OssConstant.SYS_OSS_KEY + type);
        OssProperties properties = JsonUtils.parseObject(json.toString(), OssProperties.class);
        if (properties == null) {
            throw new OssException("系统异常, '" + type + "'配置信息不存在!");
        }
        getStrategy(type).init(properties);
    }

    private static AbstractOssStrategy getStrategy(String type) {
        OssEnumd enumd = OssEnumd.find(type);
        return (AbstractOssStrategy) SpringUtils.getBean(enumd.getBeanClass());
    }

}
