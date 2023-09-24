package com.euler.system.api;

import com.euler.common.core.exception.user.UserException;
import com.euler.system.api.domain.SysUser;
import com.euler.common.core.domain.dto.LoginUser;

/**
 * 用户服务
 *
 * @author euler
 */
public interface RemoteUserService {

    /**
     * 通过用户名查询用户信息
     *
     * @param username 用户名
     * @return 结果
     */
    LoginUser getUserInfo(String username) throws UserException;

    /**
     * 注册用户信息
     *
     * @param sysUser 用户信息
     * @return 结果
     */
    Long registerUserInfo(SysUser sysUser);

    /**
     * 校验邮箱是否唯一
     *
     * @param email
     * @return
     */
    String checkEmailUnique(String email);

    String checkEmailUnique(String email, Long userId);


    /**
     * 校验用户名是否唯一
     *
     * @param sysUser
     * @return
     */
    String checkUserNameUnique(String username);

    /**
     * 通过用户ID查询用户
     *
     * @param userId 用户ID
     * @return 用户对象信息
     */
    SysUser selectUserById(Long userId);

    /**
     * 通过邮箱查询用户
     *
     * @param email 邮箱
     * @return 用户对象信息
     */
    SysUser selectUserByEmail(String email);

    Boolean updateById(SysUser user);


    boolean resetUserPwd(Long userId, String hashpw);
}
