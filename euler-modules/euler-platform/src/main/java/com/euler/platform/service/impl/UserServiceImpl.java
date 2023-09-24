package com.euler.platform.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.euler.common.core.constant.UserConstants;
import com.euler.common.core.domain.R;
import com.euler.common.core.utils.BeanCopyUtils;
import com.euler.platform.domain.OpenUserCertificationAuditRecord;
import com.euler.platform.domain.dto.FrontUserUpdateDto;
import com.euler.platform.domain.vo.OpenUserCertificationAuditRecordVo;
import com.euler.platform.domain.vo.OpenUserCertificationVo;
import com.euler.platform.domain.vo.UserVo;
import com.euler.platform.mapper.OpenUserCertificationAuditRecordMapper;
import com.euler.platform.service.ICaptchaCodeService;
import com.euler.platform.service.IOpenUserCertificationService;
import com.euler.platform.service.IUserService;
import com.euler.system.api.RemoteUserService;
import com.euler.system.api.domain.SysUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 【用户管理】Service业务层处理
 *
 * @author open
 * @date 2022-02-18
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements IUserService {

    @DubboReference
    private RemoteUserService remoteUserService;
    @Autowired
    private IOpenUserCertificationService iOpenUserCertificationService;
    @Autowired
    private ICaptchaCodeService captchaCodeService;
    @Autowired
    private OpenUserCertificationAuditRecordMapper openUserCertificationAuditRecordMapper;


    /**
     * 获取用户基础信息
     *
     * @param userId
     * @return
     */
    @Override
    public R<UserVo> getInfo(Long userId) {
        // 查询当前用户的基础信息
        SysUser sysUser = remoteUserService.selectUserById(userId);
        // 用户存在的时候 我们需要进行赋值操作
        UserVo userVo = BeanCopyUtils.copy(sysUser, UserVo.class);
        if (userVo == null) {
            return R.fail("当前用户不存在");
        }
        // 查询出当前用户的主体公司名称
        OpenUserCertificationVo userCertification = iOpenUserCertificationService.selectInfo(0, userId);
        if (userCertification != null && userCertification.getId() != null) {
            userVo.setAuditStatus(userCertification.getAuditStatus());
            userVo.setCompanyName(userCertification.getCompanyName());
            userVo.setAuditTime(userCertification.getAuditTime());
            userVo.setAuthType(userCertification.getAuthType());
            userVo.setCreateTime(userCertification.getCreateTime());
            // 查询一下审核原因
            LambdaQueryWrapper<OpenUserCertificationAuditRecord> last = Wrappers.<OpenUserCertificationAuditRecord>lambdaQuery().eq(OpenUserCertificationAuditRecord::getUserAuthId, userCertification.getId())
                .orderByDesc(OpenUserCertificationAuditRecord::getId)
                .last("limit 1");
            OpenUserCertificationAuditRecordVo openUserCertificationAuditRecordVo = openUserCertificationAuditRecordMapper.selectVoOne(last);
            if (openUserCertificationAuditRecordVo != null) {
                userVo.setAuditRecord(openUserCertificationAuditRecordVo.getAuditRecord());
            }
        }
        return R.ok(userVo);
    }

    /**
     * 修改用户基础信息
     *
     * @param frontUserUpdateDto
     * @return
     */
    @Override
    public R edit(FrontUserUpdateDto frontUserUpdateDto) {
        // 获取用户基础信息
        SysUser user = remoteUserService.selectUserById(frontUserUpdateDto.getUserId());
        if (ObjectUtil.isNull(user)) {
            return R.fail("当前用户不存在");
        }
        // 如果需要更新邮箱的时候 首先验证邮箱验证码是否正确
        if (frontUserUpdateDto.getEmail() != null) {
            captchaCodeService.checkCode(frontUserUpdateDto.getEmail(), frontUserUpdateDto.getCode());
            String res = remoteUserService.checkEmailUnique(frontUserUpdateDto.getEmail(), frontUserUpdateDto.getUserId());
            if (UserConstants.NOT_UNIQUE.equals(res))
                return R.fail("此邮箱已被使用");
            user.setEmail(frontUserUpdateDto.getEmail());
        }
        if (frontUserUpdateDto.getRealName() != null) {
            user.setRealName(frontUserUpdateDto.getRealName());
        }
        if (frontUserUpdateDto.getPhonenumber() != null) {
            user.setPhonenumber(frontUserUpdateDto.getPhonenumber());
        }
        Boolean flag = remoteUserService.updateById(user);
        if (flag) {
            return R.ok("修改成功");
        }
        return R.fail("修改失败");
    }


}
