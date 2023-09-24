package com.euler.common.core.constant;

/**
 * 缓存的key 常量
 *
 * @author euler
 */
public interface CacheConstants {

    /**
     * 登录用户 redis key
     */
    String LOGIN_TOKEN_KEY = "Authorization:login:token:";


    /**
     * 用户会话redis key
     */
    String SESSION_KEY ="Authorization:login:session:";

    /**
     * 用户会话token redis key
     */
    String TOKEN_SESSION_KEY="Authorization:login:token-session:";


    /**
     * 用户活跃 redis key
     */
    String LAST_ACTIVITY_KEY="Authorization:login:last-activity:";

    /**
     * 在线用户 redis key
     */
    String ONLINE_TOKEN_KEY = "Authorization:login:online_token:";

    /**
     * loginid构造拼接字符串
     */
    String LOGINID_JOIN_CODE = ":";



    /**
     * 登录错误次数
     */
    Integer LOGIN_ERROR_NUMBER = 5;




    /**
     * 登录错误限制时间(分钟)
     */
    Integer LOGIN_ERROR_LIMIT_TIME = 10;

}
