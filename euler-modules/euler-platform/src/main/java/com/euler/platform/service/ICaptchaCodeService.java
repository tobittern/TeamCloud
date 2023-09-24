package com.euler.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.euler.common.core.domain.R;
import com.euler.common.mybatis.core.page.PageQuery;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.platform.api.domain.CaptchaCode;
import com.euler.platform.domain.bo.CaptchaCodeBo;
import com.euler.platform.domain.vo.CaptchaCodeVo;

import java.util.List;

/**
 * 验证码的Service接口
 *
 * @author open
 * @date 2022-03-01
 */
public interface ICaptchaCodeService extends IService<CaptchaCode> {

    /**
     * 根据id查询验证码
     *
     * @param id 主键
     * @return 验证码对象信息
     */
    CaptchaCodeVo queryById(Long id);

    /**
     * 查询验证码列表
     *
     * @param bo 验证码bo
     * @return 验证码对象信息集合
     */
    TableDataInfo<CaptchaCodeVo> queryPageList(CaptchaCodeBo bo, PageQuery pageQuery);

    /**
     * 查询验证码列表
     *
     * @param bo 验证码bo
     * @return 验证码对象信息集合
     */
    List<CaptchaCodeVo> queryList(CaptchaCodeBo bo);

    /**
     * 通过验证码查询
     *
     * @param email 验证码
     * @return 验证码对象信息
     */
    CaptchaCode selectByReceiver(String email);

    /**
     * 新增验证码对象信息
     *
     * @param bo 验证码bo
     * @return 新增成功与否的结果
     */
    Boolean insertByBo(CaptchaCodeBo bo);


    /**
     * 通过验证码对象进行更新
     *
     * @param bo 验证码bo
     * @return 验证码对象信息
     */
    boolean updateByReceiver(CaptchaCode bo);

    /**
     * 验证码校验
     *
     * @param userName 邮箱或手机号
     * @param code     验证码
     */
    void checkCode(String userName, String code);


    /**
     * 一键登录获取手机号
     *
     * @param token
     * @return
     */
    R getPhoneCode(String token);

}
