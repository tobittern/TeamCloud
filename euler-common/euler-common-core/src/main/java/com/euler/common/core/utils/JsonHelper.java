package com.euler.common.core.utils;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.util.TypeUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Json工具类
 */

public class JsonHelper {


    public static SerializeConfig config;
    public static SerializerFeature[] features = {
            //输出空值字段
            SerializerFeature.WriteMapNullValue,
            //如果数组结果为null,则输出为[],而不是null
            SerializerFeature.WriteNullListAsEmpty,
            //数值字段为null,则输出为0,而不是null
            SerializerFeature.WriteNullNumberAsZero,
            //Boolean字段为null,则输出为false,而不是null
            SerializerFeature.WriteNullBooleanAsFalse,
//            //字符类型如果为null,则输出为"",而不是null
//            SerializerFeature.WriteNullStringAsEmpty,
            SerializerFeature.WriteDateUseDateFormat

    };

    static {
        JSONObject.DEFFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";//设置日期格式
        config = new SerializeConfig();
        TypeUtils.compatibleWithJavaBean = true;
        //使用json-lib兼容的日期输出格式
        //config.put(java.util.Date.class, new JSONLibDataFormatSerializer());
    }


    /**
     * 将一个对象装换为Json字符串
     */
    public static String toJson(Object object) {
        return JSONObject.toJSONString(object, config, features);
    }


    public static String toJson(Object object, SerializerFeature... ofeatures) {
        if (ofeatures == null) ofeatures = features;
        return JSONObject.toJSONString(object, config, ofeatures);
    }


    /**
     * 将Json字符串转换为实例
     */
    public static <T> T toObject(String str, Class<T> t) {
        if (StringUtils.isBlank(str))
            return  null;
        return JSON.parseObject(str, t);
    }


    /**
     * 将Json字符串转换为实例
     */
    public static <T> T toObject(String str, TypeReference<T> tClass) {
        if (StringUtils.isBlank(str))
            return  null;
        return JSON.parseObject(str, tClass);
    }


    public static <T> T toObject(Object obj, Class<T> t) {
        if (obj == null)
            return null;
        return JSON.parseObject(obj.toString(), t);
    }

    /**
     * 将Json转换为指定类型的List
     */
    public static <T> List<T> toList(String str, Class<T> t) {
        if (StringUtils.isBlank(str))
            return  null;
        return JSON.parseArray(str, t);
    }

    /**
     * 判断字符串是否是json
     *
     * @return json
     */
    public static boolean isJson(String json) {
        try {
            JSON.parse(json);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static <T> List copyList(List<T> list, Class resClass) {
        if (list == null || CollectionUtils.isEmpty(list)) {
            return new ArrayList();
        }


        return toList(toJson(list), resClass);
    }

    public static <T> T copyObj(Object obj, Class<T> resClass) {
        if (obj==null)
            return  null;

        String json=toJson(obj);
        return toObject(json, resClass);
    }

    public static Map<String, Object> copyMap(Map map) {
        return JSON.parseObject(toJson(map));
    }


}
