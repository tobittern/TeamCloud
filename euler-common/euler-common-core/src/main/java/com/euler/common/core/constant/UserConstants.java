package com.euler.common.core.constant;

/**
 * 用户常量信息
 *
 * @author euler
 */
public interface UserConstants {
    /**
     * 平台内系统用户的唯一标志
     */
    String SYS_USER = "SYS_USER";

    /**
     * 正常状态
     */
    String NORMAL = "0";

    /**
     * 异常状态
     */
    String EXCEPTION = "1";

    /**
     * 用户正常状态
     */
    String USER_NORMAL = "0";

    /**
     * 用户封禁状态
     */
    String USER_DISABLE = "1";

    /**
     * 角色正常状态
     */
    String ROLE_NORMAL = "0";

    /**
     * 角色封禁状态
     */
    String ROLE_DISABLE = "1";


    /**
     * 字典正常状态
     */
    String DICT_NORMAL = "0";

    /**
     * 是否为系统默认（是）
     */
    String YES = "Y";

    /**
     * 是否菜单外链（是）
     */
    String YES_FRAME = "0";

    /**
     * 是否菜单外链（否）
     */
    String NO_FRAME = "1";

    /**
     * 菜单正常状态
     */
    String MENU_NORMAL = "0";

    /**
     * 菜单停用状态
     */
    String MENU_DISABLE = "1";

    /**
     * 菜单类型（目录）
     */
    String TYPE_DIR = "M";

    /**
     * 菜单类型（菜单）
     */
    String TYPE_MENU = "C";

    /**
     * 菜单类型（按钮）
     */
    String TYPE_BUTTON = "F";

    /**
     * Layout组件标识
     */
    String LAYOUT = "Layout";

    /**
     * ParentView组件标识
     */
    String PARENT_VIEW = "ParentView";

    /**
     * InnerLink组件标识
     */
    String INNER_LINK = "InnerLink";

    /**
     * 校验返回结果码
     */
    String UNIQUE = "0";

    String NOT_UNIQUE = "1";

    /**
     * 用户名长度限制
     */
    int USERNAME_MIN_LENGTH = 2;

    int USERNAME_MAX_LENGTH = 50;

    /**
     * 密码长度限制
     */
    int PASSWORD_MIN_LENGTH = 5;

    int PASSWORD_MAX_LENGTH = 20;

    /**
     * 管理员ID
     */
    Long ADMIN_ID = 1L;
    Long DEFAULT_FRONT_ROLE_ID = 3L;
    Long DEFAULT_CHANNEL_ROLE_ID = 6L;

    /**
     * 邮箱验证码的使用类型 1注册 2登录 3更换邮箱地址
     */
    String TYPE_0 = "0";
    String TYPE_1 = "1";
    String TYPE_2 = "2";
    String TYPE_3 = "3";

    /**
     * 是否使用： 0：未使用过 1：使用过
     */
    int ISUSE_0 = 0;
    int ISUSE_1 = 1;

    /**
     * 是否使用： 0：未使用过 1：使用过
     */
    String SEX_MAN = "1";
    String SEX_WOMAN ="0";
    String SEX_UNKNOWN = "2";


    String MEMBER_RIGHT_LEVEL_0="0";
    String MEMBER_RIGHT_LEVEL_1="1";
    String MEMBER_RIGHT_LEVEL_2="2";
    String MEMBER_RIGHT_LEVEL_3="3";

}
