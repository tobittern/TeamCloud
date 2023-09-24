package com.euler.common.mybatis.handler;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.satoken.utils.LoginHelper;
import com.euler.common.core.domain.dto.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;

/**
 * MP注入处理器
 *
 * @author euler
 * @date 2021/4/25
 */
@Slf4j
public class CreateAndUpdateMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {


        if (metaObject.hasSetter("createBy")) {
            String userName = Convert.toStr(metaObject.getValue("createBy"));
            if (StringUtils.isBlank(userName))
                userName = getLoginUserId();
            this.setFieldValByName("createBy", userName, metaObject);
        }

        if (metaObject.hasSetter("createTime")) {
            String dateTime = Convert.toStr(metaObject.getValue("createTime"), DateUtil.now());
            this.setFieldValByName("createTime", Convert.toDate(dateTime), metaObject);
        }
        if (metaObject.hasSetter("delFlag")) {
            this.setFieldValByName("delFlag", "0", metaObject);
        }


    }

    @Override
    public void updateFill(MetaObject metaObject) {

        if (metaObject.hasSetter("updateBy")) {
            String userName = Convert.toStr(metaObject.getValue("updateBy"));
            if (StringUtils.isBlank(userName))
                userName = getLoginUserId();
            this.setFieldValByName("updateBy", userName, metaObject);
        }

        if (metaObject.hasSetter("updateTime")) {
            String dateTime = Convert.toStr(metaObject.getValue("updateTime"), DateUtil.now());
            this.setFieldValByName("updateTime", Convert.toDate(dateTime), metaObject);
        }

    }

    /**
     * 获取登录用户名
     */
    private String getLoginUserId() {
        LoginUser loginUser;
        try {
            loginUser = LoginHelper.getLoginUser();
        } catch (Exception e) {
            log.warn("自动注入警告 => 用户未登录");
            return null;
        }
        return loginUser.getUserId() + "";
    }

}
