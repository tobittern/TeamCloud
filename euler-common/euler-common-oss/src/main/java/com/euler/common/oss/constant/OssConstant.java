package com.euler.common.oss.constant;

import com.euler.common.core.constant.Constants;

import java.util.Arrays;
import java.util.List;

/**
 * 对象存储常量
 *
 * @author euler
 */
public class OssConstant {


    /**
     * 缓存配置KEY
     */
    public static final String SYS_OSS_KEY = Constants.BASE_KEY+ "sys_oss:OssConfig:";
    /**
     * 预览列表资源开关Key
     */
    public static final String PEREVIEW_LIST_RESOURCE_KEY = "sys.oss.previewListResource";

    /**
     * 系统数据ids
     */
    public static final List<Integer> SYSTEM_DATA_IDS = Arrays.asList(1, 2, 3, 4);

}
