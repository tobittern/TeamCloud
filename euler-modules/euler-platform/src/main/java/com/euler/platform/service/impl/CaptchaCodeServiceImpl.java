package com.euler.platform.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.euler.common.core.constant.Constants;
import com.euler.common.core.constant.UserConstants;
import com.euler.common.core.domain.R;
import com.euler.common.core.exception.ServiceException;
import com.euler.common.core.exception.user.UserException;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.mybatis.core.page.PageQuery;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.common.redis.utils.RedisUtils;
import com.euler.common.sms.SmsHelper;
import com.euler.platform.api.domain.CaptchaCode;
import com.euler.platform.domain.bo.CaptchaCodeBo;
import com.euler.platform.domain.vo.CaptchaCodeVo;
import com.euler.platform.mapper.CaptchaCodeMapper;
import com.euler.platform.service.ICaptchaCodeService;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 邮箱Service业务层处理
 *
 * @author open
 * @date 2022-03-01
 */
@RequiredArgsConstructor
@Service
public class CaptchaCodeServiceImpl extends ServiceImpl<CaptchaCodeMapper, CaptchaCode> implements ICaptchaCodeService {

    private final CaptchaCodeMapper baseMapper;

    @Autowired
    private SmsHelper smsHelper;

    /**
     * 根据id查询邮箱
     *
     * @param id 主键
     * @return 邮箱对象信息
     */
    @Override
    public CaptchaCodeVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询邮箱列表
     *
     * @param bo 邮箱bo
     * @return 邮箱对象信息集合
     */
    @Override
    public TableDataInfo<CaptchaCodeVo> queryPageList(CaptchaCodeBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<CaptchaCode> lqw = buildQueryWrapper(bo);
        Page<CaptchaCodeVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询邮箱列表
     *
     * @param bo 邮箱bo
     * @return 邮箱对象信息集合
     */
    @Override
    public List<CaptchaCodeVo> queryList(CaptchaCodeBo bo) {
        LambdaQueryWrapper<CaptchaCode> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 通过接收方查询
     *
     * @param receiver 接收方
     * @return 验证码对象信息
     */
    @Override
    public CaptchaCode selectByReceiver(String receiver) {
        return baseMapper.selectByReceiver(receiver);
    }

    private LambdaQueryWrapper<CaptchaCode> buildQueryWrapper(CaptchaCodeBo bo) {
        LambdaQueryWrapper<CaptchaCode> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getReceiver()), CaptchaCode::getReceiver, bo.getReceiver());
        lqw.eq(StringUtils.isNotBlank(bo.getCode()), CaptchaCode::getCode, bo.getCode());
        lqw.eq(StringUtils.isNotBlank(bo.getType()), CaptchaCode::getType, bo.getType());
        return lqw;
    }

    /**
     * 新增邮箱对象信息
     *
     * @param bo 邮箱bo
     * @return 新增成功与否的结果
     */
    @Override
    public Boolean insertByBo(CaptchaCodeBo bo) {
        CaptchaCode add = BeanUtil.toBean(bo, CaptchaCode.class);
        add.setIsUse(UserConstants.ISUSE_0);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }


    /**
     * 修改邮箱对象信息
     *
     * @param bo 邮箱bo
     * @return 更新成功与否的结果
     */


    @Override
    public boolean updateByReceiver(CaptchaCode bo) {

        var updateChainWrapper = new LambdaUpdateChainWrapper<>(baseMapper).eq(CaptchaCode::getReceiver, bo.getReceiver())
            .set(CaptchaCode::getUserId, bo.getUserId()).set(CaptchaCode::getIsUse, bo.getIsUse());
        return updateChainWrapper.update();

    }

    /**
     * 保存前的数据校验
     *
     * @param entity 实体类数据
     */
    private void validEntityBeforeSave(CaptchaCode entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 验证码校验
     *
     * @param userName 邮箱或手机号
     * @param code     验证码
     */
    @Override
    public void checkCode(String userName, String code) {



        // 首先验证当前邮箱验证码是否正确 是否超过了15分钟
        String codeExpireTimeKey = Constants.CAPTCHA_CODE_EXPIRE_TIME + userName;
        String checkCode = RedisUtils.getCacheObject(codeExpireTimeKey);

        if (!StringUtils.isEmpty(checkCode)) {
            // 在有效期15分钟以内，验证邮箱验证码是否正确
            if (!StringUtils.equals(code, checkCode)) {
                throw new ServiceException("请输入正确的验证码");
            }
        } else {
            // 验证码已失效
            throw new UserException("user.jcaptcha.expire");
        }

        // 清空邮箱验证码的缓存
        RedisUtils.deleteObject(codeExpireTimeKey);

    }

    /**
     * 一键登录获取手机号
     *
     * @param token
     * @return
     */
    @Override
    public R getPhoneCode(String token){
      return   smsHelper.getPhoneCode(token);
    }


}
