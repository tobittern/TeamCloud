package com.euler.system.dubbo;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.euler.common.core.constant.UserConstants;
import com.euler.common.core.enums.UserStatus;
import com.euler.common.core.exception.ServiceException;
import com.euler.common.core.exception.user.UserException;
import com.euler.system.api.RemoteUserService;
import com.euler.system.api.domain.SysUser;
import com.euler.common.core.domain.dto.LoginUser;
import com.euler.common.core.domain.dto.RoleDTO;
import com.euler.system.service.ISysConfigService;
import com.euler.system.service.ISysPermissionService;
import com.euler.system.service.ISysUserService;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * 操作日志记录
 *
 * @author euler
 */
@RequiredArgsConstructor
@Service
@DubboService
public class RemoteUserServiceImpl implements RemoteUserService {

    private final ISysUserService userService;

    private final ISysPermissionService permissionService;
    private final ISysConfigService configService;

    @Override
    public LoginUser getUserInfo(String username) throws UserException {
        SysUser sysUser = userService.selectUserByUserName(username);
        if (ObjectUtil.isNull(sysUser)) {
            throw new UserException("user.not.exists", username);
        }
        if (UserStatus.DELETED.getCode().equals(sysUser.getDelFlag())) {
            throw new UserException("user.password.delete", username);
        }
        if (UserStatus.DISABLE.getCode().equals(sysUser.getStatus())) {
            throw new UserException("user.blocked", username);
        }
        // 角色集合
        Set<String> rolePermission = permissionService.getRolePermission(sysUser.getUserId());
        // 权限集合
        Set<String> menuPermissions = permissionService.getMenuPermission(sysUser.getUserId());
        LoginUser loginUser = new LoginUser();
        loginUser.setUserId(sysUser.getUserId());
        loginUser.setUsername(sysUser.getUserName());
        loginUser.setPassword(sysUser.getPassword());
        loginUser.setUserType(sysUser.getUserType());
        loginUser.setMenuPermission(menuPermissions);
        loginUser.setRolePermission(rolePermission);
        List<RoleDTO> roles = BeanUtil.copyToList(sysUser.getRoles(), RoleDTO.class);
        loginUser.setRoles(roles);
        return loginUser;
    }

    @Override
    public Long registerUserInfo(SysUser sysUser) {
        String username = sysUser.getUserName();
        if (!("true".equals(configService.selectConfigByKey("sys.account.registerUser")))) {
            throw new ServiceException("当前系统没有开启注册功能");
        }
        if (UserConstants.NOT_UNIQUE.equals(userService.checkUserNameUnique(username))) {
            throw new UserException("user.register.save.error", username);
        }
        return userService.registerUser(sysUser);
    }

    /**
     * 校验email是否唯一
     *
     * @param email 用户信息
     * @return String
     */
    @Override
    public String checkEmailUnique(String email) {
        return userService.checkEmailUnique(email);
    }

    @Override
    public String checkEmailUnique(String email, Long userId) {
        return userService.checkEmailUnique(email, userId);
    }

    /**
     * 校验用户名是否唯一
     *
     * @param username 用户信息
     * @return String
     */
    @Override
    public String checkUserNameUnique(String username) {
        return userService.checkUserNameUnique(username);
    }

    @Override
    public SysUser selectUserById(Long userId) {
        return userService.selectUserById(userId);
    }

    /**
     * 通过邮箱查询用户
     *
     * @param email 邮箱
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserByEmail(String email) {
        return userService.selectUserByEmail(email);
    }

    @Override
    public Boolean updateById(SysUser user) {

        return userService.updateById(user);
    }


    @Override
    public boolean resetUserPwd(Long userId, String hashpw) {
        return userService.resetUserPwd(userId, hashpw) > 0;
    }
}
