package com.euler.common.core.constant;

/**
 * 会员的key 常量
 *
 * @author euler
 */
public interface MemberConstants {

    /**
     * 会员状态：未开通
     */
    String MEMBER_STATUS_NOOPEN = "0";

    /**
     * 会员状态：生效中
     */
    String MEMBER_STATUS_NORMAL = "1";

    /**
     * 会员状态：已失效
     */
    String MEMBER_STATUS_DISABLE = "2";

    /**
     * 会员等级：初级
     */
    String MEMBER_LEVEL_BASIC = "1";

    /**
     * 会员等级：中级
     */
    String MEMBER_LEVEL_MIDDLE = "2";

    /**
     * 会员等级：高级
     */
    String MEMBER_LEVEL_SENIOR = "3";

    /**
     * 是否升级：未升级
     */
    String IS_UPGRADE_NO = "0";

    /**
     * 是否升级：升级
     */
    String IS_UPGRADE_YES = "1";

    /**
     * 是否上传：未上传
     */
    String IS_UPLOAD_NO = "0";

    /**
     * 是否上传：已上传
     */
    String IS_UPLOAD_YES = "1";

}
