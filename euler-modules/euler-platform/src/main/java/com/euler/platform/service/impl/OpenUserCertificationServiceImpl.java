package com.euler.platform.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.IdcardUtil;
import cn.hutool.core.util.PhoneUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.KeyValueDto;
import com.euler.common.core.utils.BeanCopyUtils;
import com.euler.common.core.utils.JsonUtils;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.common.satoken.utils.LoginHelper;
import com.euler.platform.domain.OpenUserCertification;
import com.euler.platform.domain.bo.OpenUserCertificationAuditRecordBo;
import com.euler.platform.domain.bo.OpenUserCertificationBo;
import com.euler.platform.domain.dto.OpenUserCertificationPageDto;
import com.euler.platform.domain.vo.OpenUserCertificationDataCountVo;
import com.euler.platform.domain.vo.OpenUserCertificationSearchVo;
import com.euler.platform.domain.vo.OpenUserCertificationVo;
import com.euler.platform.enums.OperationActionTypeEnum;
import com.euler.platform.mapper.OpenUserCertificationMapper;
import com.euler.platform.service.IOpenUserCertificationAuditRecordService;
import com.euler.platform.service.IOpenUserCertificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * 用户的资质认证记录Service业务层处理
 *
 * @author open
 * @date 2022-02-23
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class OpenUserCertificationServiceImpl implements IOpenUserCertificationService {

    private final OpenUserCertificationMapper baseMapper;

    @Autowired
    private IOpenUserCertificationAuditRecordService iOpenUserAuthAuditRecordService;

    /**
     * 资质审核基础数据统计
     */
    @Override
    public OpenUserCertificationDataCountVo selectCount() {
        List<KeyValueDto> keyValueDtos = baseMapper.selectAuditStatusCount();
        OpenUserCertificationDataCountVo vo = new OpenUserCertificationDataCountVo();
        for (var x : keyValueDtos) {
            if (x.getValue().equals("1")) {
                vo.setInCount(Convert.toInt(x.getKey()));
            } else if (x.getValue().equals("2")) {
                vo.setPassCount(Convert.toInt(x.getKey()));
            } else if (x.getValue().equals("3")) {
                vo.setRejectCount(Convert.toInt(x.getKey()));
            }
        }
        return vo;
    }

    /**
     * 查询用户的资质认证记录
     *
     * @return 用户的资质认证记录
     */
    @Override
    public OpenUserCertificationVo selectInfo(Integer id, Long userId) {
        // 设置基础查询
        LambdaQueryWrapper<OpenUserCertification> searchValue = Wrappers.<OpenUserCertification>lambdaQuery()
            .eq(LoginHelper.isFront(), OpenUserCertification::getUserId, userId);
        // 判断是否存在ID
        if (id != null && !id.equals(0)) {
            searchValue.eq(OpenUserCertification::getId, id);
        }
        List<OpenUserCertificationVo> openUserCertificationVos = baseMapper.selectVoList(searchValue);
        if (openUserCertificationVos != null && !openUserCertificationVos.isEmpty()) {
            return openUserCertificationVos.get(0);
        }
        return new OpenUserCertificationVo();
    }

    /**
     * 查询用户的资质认证记录列表
     *
     * @return 用户的资质认证记录
     */
    @Override
    public TableDataInfo<OpenUserCertificationVo> queryPageList(OpenUserCertificationPageDto dto) {
        // 设置基础查询条件
        LambdaQueryWrapper<OpenUserCertification> eq = Wrappers.<OpenUserCertification>lambdaQuery()
            .eq(LoginHelper.isFront(), OpenUserCertification::getUserId, dto.getUserId())
            .eq(Convert.toInt(dto.getAuditStatus(), -1) > -1, OpenUserCertification::getAuditStatus, dto.getAuditStatus())
            .likeRight(StringUtils.isNotBlank(dto.getCompanyName()), OpenUserCertification::getCompanyName, dto.getCompanyName())
            .orderByAsc(OpenUserCertification::getAuditStatus).orderByDesc(OpenUserCertification::getUpdateTime);

        // 进行数据查询
        Page<OpenUserCertificationVo> result = baseMapper.selectVoPage(dto.build(), eq);

        return TableDataInfo.build(result);
    }

    /**
     * 新增用户的资质认证记录
     *
     * @return 结果
     */
    @Override
    public R insertByBo(OpenUserCertificationBo bo) {
        OpenUserCertification add = BeanUtil.toBean(bo, OpenUserCertification.class);
        log.info("添加的字段列表->" + JsonUtils.toJsonString(add));
        // 开始进行数据的基础验证
        String s = validEntityBeforeSave(add);
        if (!s.equals("success")) {
            return R.fail(s);
        }
        // 验证当前用户是否存在未审核的数据
        // 检测 如果当前游戏正在处于审核中 是不会允许修改的
        OpenUserCertificationVo openUserAuthVo = this.selectInfo(0, bo.getUserId());
        if (openUserAuthVo != null && openUserAuthVo.getId() != null) {
            return R.fail("您已经存在认证信息了");
        }
        // 设置审核状态
        add.setAuditStatus(1);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
            // 认证信息添加完毕之后需要添加一个日志记录
            OpenUserCertificationAuditRecordBo recordBo = new OpenUserCertificationAuditRecordBo();
            recordBo.setUserAuthId(add.getId());
            recordBo.setOperationAction(OperationActionTypeEnum.Submit.getCode());
            iOpenUserAuthAuditRecordService.insertByBo(recordBo);

            return R.ok(add.getId());
        }
        return R.fail();
    }

    /**
     * 修改用户的资质认证记录
     *
     * @return 结果
     */
    @Override
    public R updateByBo(OpenUserCertificationBo bo) {
        // 基础参数判断
        if (bo.getId() == null || bo.getId() <= 0) {
            return R.fail("参数缺失");
        }
        if (LoginHelper.isFront()) {
            // 判断当前这个认证信息是否可以进行修改
            OpenUserCertificationVo openUserAuthVo = this.selectInfo(bo.getId(), bo.getUserId());
            if (openUserAuthVo != null && openUserAuthVo.getAuditStatus().equals(1)) {
                return R.fail("您当前存在待审核的认证记录");
            }
        }
        // 实体拷贝赋值
        OpenUserCertification update = BeanUtil.toBean(bo, OpenUserCertification.class);
        // 更新的数据进行一下日志记录
        // 更新的数据验证
        String s = validEntityBeforeSave(update);
        if (!s.equals("success")) {
            return R.fail(s);
        }
        // 更新需要变更审核状态
        update.setAuditStatus(1);
        int i = baseMapper.updateById(update);
        if (i > 0) {
            // 认证信息添加完毕之后需要添加一个日志记录
            OpenUserCertificationAuditRecordBo recordBo = new OpenUserCertificationAuditRecordBo();
            recordBo.setUserAuthId(bo.getId());
            recordBo.setOperationAction(OperationActionTypeEnum.Submit.getCode());
            iOpenUserAuthAuditRecordService.insertByBo(recordBo);
            return R.ok();
        }
        return R.fail("更新失败");
    }

    /**
     * 保存前的数据校验
     *
     * @param entity 实体类数据
     */
    private String validEntityBeforeSave(OpenUserCertification entity) {
        //TODO 做一些数据校验,如唯一约束
        // 验证用户身份证是否合法
        boolean validCard = IdcardUtil.isValidCard(entity.getContactIdCard());
        if (!validCard) {
            return "您填写的身份证号有误!";
        }
        // 验证用户填写的手机号是否正确
        boolean mobile = PhoneUtil.isMobile(entity.getContactPhone());
        if (!mobile) {
            return "您填写的手机号有误!";
        }
        // 验证经营期限
        if (entity.getOperatingPeriodStart() == null) {
            return "经营期限不能为空";
        }
        return "success";
    }

    /**
     * 批量删除用户的资质认证记录
     *
     * @param ids 需要删除的用户的资质认证记录主键
     * @return 结果
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Integer> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }


    /**
     * 撤销审核
     *
     * @param userId
     * @return
     */
    @Override
    public R revokeApproval(Long userId) {
        LambdaQueryWrapper<OpenUserCertification> eq = Wrappers.<OpenUserCertification>lambdaQuery()
            .eq(OpenUserCertification::getUserId, userId);
        OpenUserCertification openUserCertification = baseMapper.selectOne(eq);
        if (openUserCertification != null) {
            // 不为空的时候  只有你提交的数据是待审核状态才能进行提交
            if (!openUserCertification.getAuditStatus().equals(1)) {
                return R.fail("审核中的才能进行撤销");
            }
            OpenUserCertification update = new OpenUserCertification();
            update.setId(openUserCertification.getId());
            update.setAuditStatus(0);
            int i = baseMapper.updateById(update);
            if (i > 0) {
                return R.ok();
            }
        }
        return R.fail();
    }


    /**
     * 用户认证信息的审核
     *
     * @param bo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R auditUserCertification(OpenUserCertificationAuditRecordBo bo) {
        // 权限判断
        if (LoginHelper.isFront()) {
            return R.fail("您当前没有权限");
        }
        // 查询数据是否存在
        OpenUserCertificationVo openUserAuthVo = baseMapper.selectVoById(bo.getUserAuthId());
        if (openUserAuthVo == null) {
            return R.fail("当前数据不存在");
        }
        // 待审核不能进行审核
        if (openUserAuthVo.getAuditStatus().equals(0)) {
            return R.fail("待提交的不能进行审核");
        }
        // 如果当前数据已经审核通过了
        if (openUserAuthVo.getAuditStatus().equals(2)) {
            return R.fail("当前记录已经审核通过了");
        }
        try {
            OpenUserCertification openUserAuth = new OpenUserCertification();
            openUserAuth.setId(bo.getUserAuthId());
            openUserAuth.setAuditTime(new Date());
            // 判断用户填写的审核状态是否正确
            if (bo.getAuditStatus().equals(1)) {
                openUserAuth.setAuditStatus(2);
                bo.setOperationAction(OperationActionTypeEnum.Pass.getCode());
            } else if (bo.getAuditStatus().equals(2)) {
                openUserAuth.setAuditStatus(3);
                bo.setOperationAction(OperationActionTypeEnum.Reject.getCode());
            } else {
                return R.fail("参数有误");
            }
            int i = baseMapper.updateById(openUserAuth);
            // 数据更新完毕之后
            // 添加审核记录
            Boolean aBoolean = iOpenUserAuthAuditRecordService.insertByBo(bo);
            if (i > 0 && aBoolean) {
                return R.ok();
            }
        } catch (Exception e) {
            log.error("审核用户认证信息失败 ->", e);
            return R.fail("审核失败");
        }
        return R.fail();
    }


    /**
     * 查询
     *
     * @param dto
     * @return
     */
    @Override
    public R search(OpenUserCertificationPageDto dto) {
        // 判断搜索的公司名是否存在
        LambdaQueryWrapper<OpenUserCertification> eq = Wrappers.<OpenUserCertification>lambdaQuery()
            .eq(OpenUserCertification::getAuditStatus, 2)
            .likeRight(StringUtils.isNotBlank(dto.getCompanyName()), OpenUserCertification::getCompanyName, dto.getCompanyName())
            .eq(StringUtils.isNotBlank(dto.getContactName()), OpenUserCertification::getContactName, dto.getContactName())
            .eq(StringUtils.isNotBlank(dto.getContactPhone()), OpenUserCertification::getContactPhone, dto.getContactPhone());

        List<OpenUserCertificationVo> listVo = baseMapper.selectVoList(eq);
        List<OpenUserCertificationSearchVo> result = BeanCopyUtils.copyList(listVo, OpenUserCertificationSearchVo.class);
        return R.ok(result);
    }

}
