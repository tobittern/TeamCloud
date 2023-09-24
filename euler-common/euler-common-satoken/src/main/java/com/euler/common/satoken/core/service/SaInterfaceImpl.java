package com.euler.common.satoken.core.service;

import cn.dev33.satoken.stp.StpInterface;
import com.euler.common.core.enums.UserTypeEnum;
import com.euler.common.satoken.utils.LoginHelper;
import com.euler.common.core.domain.dto.LoginUser;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 权限认证接口实现
 *
 * @author euler
 */
@Component
public class SaInterfaceImpl implements StpInterface {

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        LoginUser loginUser = LoginHelper.getLoginUser();
        UserTypeEnum userTypeEnum = UserTypeEnum.getUserType(loginUser.getUserType());
        if (userTypeEnum == UserTypeEnum.SYS_USER) {
            return new ArrayList<>(loginUser.getMenuPermission());
        } else if (userTypeEnum == UserTypeEnum.OPEN_USER) {
            // app端权限返回 自行根据业务编写
            return new ArrayList<>(loginUser.getMenuPermission());

        }
        return new ArrayList<>();
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        LoginUser loginUser = LoginHelper.getLoginUser();
        UserTypeEnum userTypeEnum = UserTypeEnum.getUserType(loginUser.getUserType());
        if (userTypeEnum == UserTypeEnum.SYS_USER) {
            return new ArrayList<>(loginUser.getRolePermission());
        } else if (userTypeEnum == UserTypeEnum.OPEN_USER) {
            // app端权限返回 自行根据业务编写
            return new ArrayList<>(loginUser.getRolePermission());

        }
        return new ArrayList<>();
    }
}
