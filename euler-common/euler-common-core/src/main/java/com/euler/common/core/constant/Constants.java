package com.euler.common.core.constant;

/**
 * 通用常量信息
 *
 * @author euler
 */
public interface Constants {

    /**
     * 基础key
     */
    String BASE_KEY = "euler:cloud:";

    /**
     * 过期时间
     */
    Long BASE_EXPIRE_TIME = 1440L;


    /**
     * UTF-8 字符集
     */
    String UTF8 = "UTF-8";

    /**
     * GBK 字符集
     */
    String GBK = "GBK";

    /**
     * http请求
     */
    String HTTP = "http://";

    /**
     * https请求
     */
    String HTTPS = "https://";

    /**
     * 成功标记
     */
    Integer SUCCESS = 200;

    /**
     * 失败标记
     */
    Integer FAIL = 500;

    /**
     * 登录成功
     */
    String LOGIN_SUCCESS = "Success";

    /**
     * 注销
     */
    String LOGOUT = "Logout";

    /**
     * 注册
     */
    String REGISTER = "Register";

    /**
     * 登录失败
     */
    String LOGIN_FAIL = "Error";

    /**
     * 验证码 redis key
     */
    String CAPTCHA_CODE_KEY = "euler:cloud:captcha_codes:";

    /**
     * 验证码有效期（分钟）
     */
    long CAPTCHA_EXPIRATION = 2;

    /**
     * 防重提交 redis key
     */
    String REPEAT_SUBMIT_KEY = "euler:cloud:repeat_submit:";

    /**
     * 参数管理 cache key
     */
    String SYS_CONFIG_KEY = "euler:cloud:sys_config:";

    /**
     * 字典管理 cache key
     */
    String SYS_DICT_KEY = "euler:cloud:sys_dict:";

    /**
     * 动态基础Key
     */
    String COMMUNITY_KEY = "euler:cloud:community:";

    /**
     * 风控基础Key
     */
    String RISK_KEY = "euler:cloud:risk:";

    /**
     * 通用状态，1：是，0：否
     */
    String COMMON_STATUS_YES = "1";

    /**
     * 通用状态，1：是，0：否
     */
    String COMMON_STATUS_NO = "0";

    /**
     * 通知公告类型--通知
     */
    String NOTICE_TYPE_NOTIFY = "1";

    /**
     * 通知公告类型--类型
     */
    String NOTICE_TYPE_PUBLIC = "2";

    /**
     * 邮箱验证码有效期（15分钟）
     */
    Integer EMAIL_CODE_EXPIRATION = 15;

    /**
     * 邮箱验证码有效期（15分钟）
     */
    Integer MOBILE_CODE_EXPIRATION = 15;

    /**
     * 注册请求次数 redis key
     */
    String REGISTER_REQUEST_TIME = "euler:cloud:register:request_time:";

    /**
     * 注册请求次数
     */
    Integer REGISTER_REQUEST_NUMBER = 3;

    /**
     * 注册锁定限制时间(分钟)
     */
    Integer REGISTER_LIMIT_TIME = 5;

    /**
     * 邮箱验证码有效期 redis key
     */
    String CAPTCHA_CODE_EXPIRE_TIME = "euler:cloud:code:expire_time:";

    /**
     * 审核关键词缓存
     */
    String AUDIT_SENSITIVE_WORD = "euler:cloud:sensitive:word";

    /**
     * 用户签到的前缀key
     */
    String USER_SIGN_PREFIX_KEY = "euler:cloud:user:sign:prefix:key";

    /**
     * 上架
     */
    String IS_UP_ON = "1";

    /**
     * 下架
     */
    String IS_UP_OFF = "2";

    /**
     * 平台标识: SDK
     */
    String PLATFORM_TYPE_SDK = "1";

    /**
     * 平台标识:开放平台
     */
    String PLATFORM_TYPE_OPEN = "2";

    /**
     * 平台标识:九区玩家App
     */
    String PLATFORM_TYPE_APP = "3";

    /**
     * 推送状态：未推送
     */
    String IS_PUSH_NO = "0";

    /**
     * 推送状态：已推送
     */
    String IS_PUSH_YES = "1";

    /**
     * 推送用户标识：全部用户
     */
    String PUSH_USER_ALL = "0";

    /**
     * 推送用户标识：部分用户
     */
    String PUSH_USER_PART = "1";

    /**
     * 自动推送标识：自动
     */
    String PUSH_AUTO_YES = "0";

    /**
     * 自动推送标识：手动
     */
    String PUSH_AUTO_NO = "1";

    /**
     * 阅读状态：未读
     */
    String UNREAD = "0";

    /**
     * 阅读状态：已读
     */
    String READ = "1";

    /**
     * 礼包类型：等级礼包
     */
    String GIFT_TYPE_GRADE = "1";

    /**
     * 礼包类型：活动礼包
     */
    String GIFT_TYPE_ACTIVITY = "2";

    /**
     * 礼包领取状态：未达成
     */
    String RECEIVE_STATUS_NOREACH = "0";

    /**
     * 礼包领取状态：待领取
     */
    String RECEIVE_STATUS_NO = "1";

    /**
     * 礼包领取状态：已领取
     */
    String RECEIVE_STATUS_YES = "2";

    /**
     * 1：首充礼包
     */
    String GIFT_ACTIVITY_TYPE_1 = "1";

    /**
     * 2：单笔充值
     */
    String GIFT_ACTIVITY_TYPE_2 = "2";

    /**
     * 3：累计充值
     */
    String GIFT_ACTIVITY_TYPE_3 = "3";

    /**
     * 4到达等级
     */
    String GIFT_ACTIVITY_TYPE_4 = "4";
    /**
     * 5累计在线时长
     */
    String GIFT_ACTIVITY_TYPE_5 = "5";

    /**
     * 6:新人礼包
     */
    String GIFT_ACTIVITY_TYPE_6 = "6";

    /**
     * 7:实名认证礼包
     */
    String GIFT_ACTIVITY_TYPE_7 = "7";

    /**
     * 商品类型：年费商品
     */
    Integer GOODS_TYPE_1 = 1;

    /**
     * 版本状态：1待发布
     */
    String VERSION_STATUS_1 = "1";

    /**
     * 版本状态：2已发布
     */
    String VERSION_STATUS_2 = "2";

    /**
     * 版本状态：3已下架
     */
    String VERSION_STATUS_3 = "3";

    /**
     * 应用系统，'1': android
     */
    String SYSTEM_TYPE_1 = "1";

    /**
     * 应用系统，'2': ios
     */
    String SYSTEM_TYPE_2 = "2";

    /**
     * 应用系统，'3': h5
     */
    String SYSTEM_TYPE_3 = "3";

    /**
     * 用户黑名单的前缀key
     */
    String MEMBER_BLACK_KEY = "euler:cloud:member:blacklist:";
    /**
     * 更新方式，'1':推荐更新
     */
    String UPDATE_TYPE_1 = "1";

    /**
     * 更新方式，'2':强制更新
     */
    String UPDATE_TYPE_2 = "2";

    /**
     * 消息类型，'1':点赞
     */
    String MESSAGE_TYPE_1 = "1";

    /**
     * 消息类型，'2':评论
     */
    String MESSAGE_TYPE_2 = "2";

    /**
     * 消息类型，'3':新粉丝
     */
    String MESSAGE_TYPE_3 = "3";

    /**
     * 消息类型，'4':系统消息
     */
    String MESSAGE_TYPE_4 = "4";

    /**
     * 关注状态 '1':未关注
     */
    String ATTENTION_STATUS_1 = "1";

    /**
     * 关注状态 '2':已关注
     */
    String ATTENTION_STATUS_2 = "2";

    /**
     * 关注状态 '3':互相关注
     */
    String ATTENTION_STATUS_3 = "3";

    /**
     * 实名认证状态，0：未认证
     */
    String VERIFY_STATUS_0 = "0";

    /**
     * 实名认证状态，1：已认证
     */
    String VERIFY_STATUS_1 = "1";

    /**
     * 弹窗类型: 1强退
     */
    Integer POPUP_TYPE_1 = 1;

    /**
     * 弹窗类型: 2奖励
     */
    Integer POPUP_TYPE_2 = 2;

    /**
     * 弹窗类型: 3运营
     */
    Integer POPUP_TYPE_3 = 3;

    /**
     * 启动时机：0:每次启动
     */
    String START_OCCASION_0 = "0";

    /**
     * 启动时机：1:首充
     */
    String START_OCCASION_1 = "1";

    /**
     * 启动时机：2:游戏到达等级
     */
    String START_OCCASION_2 = "2";

    /**
     * 启动时机：3:累计充值
     */
    String START_OCCASION_3 = "3";

    /**
     * 启动时机：4:累计在线时长(分)
     */
    String START_OCCASION_4 = "4";

    /**
     * 启动时机：5:实名认证
     */
    String START_OCCASION_5 = "5";

    /**
     * 启动时机：6:绑定手机号
     */
    String START_OCCASION_6 = "6";

    /**
     * 每次启动类型(0:打开App)
     */
    String EVERY_STARTUP_TYPE_0 = "0";

    /**
     * 每次启动类型(1:进入游戏)
     */
    String EVERY_STARTUP_TYPE_1 = "1";

}
