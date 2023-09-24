package com.euler.community.constant;

public class GitBagConstant {

    /**
     * 礼包存放缓消息队列msgId的前缀
     **/
    public final static String GIT_BAG_RABBIT_PREFIX = "giftBagPickToRabbit_";

    /**
     * 礼包存放缓存中的前缀
     **/
    public final static String GIT_BAG_PREFIX = "giftBag:Data:";

    /**
     * 礼包存放缓存中的前缀
     **/
    public final static String GIT_BAG_LOCK_PREFIX = "giftBag:lock:";

    /**
     * 礼包存放缓存中的前缀
     **/
    public final static String GIT_BAG_INCR = "giftBag:Incr:";

    /**
     * 礼包用户领取锁定的前缀
     **/
    public final static String GIT_BAG_LOCK_USER_PREFIX = "giftBag:PickLockUser:";

    /**
     * 礼包用户首次提交锁定的时间,防止同一个用户并发提交,单位 分钟
     **/
    public final static Long GIT_BAG_LOCK_USER_TIME = 5L;

    /**
     * http前缀
     **/
    public final static String HTTP = "http";

    /**
     * 属性 filePath
     **/
    public final static String FILE_PATH = "filePath";
    /**
     * 属性 filePath
     **/
    public final static String FILE_WIDTH = "fileWidth";
    /**
     * 属性 filePath
     **/
    public final static String FILE_HEIGHT = "fileHeight";
}
