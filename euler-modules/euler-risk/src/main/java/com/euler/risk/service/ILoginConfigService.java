package com.euler.risk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.euler.common.core.domain.R;
import com.euler.risk.api.domain.LoginConfigVo;
import com.euler.risk.domain.dto.LoginConfigDto;
import com.euler.risk.domain.entity.LoginConfig;
import com.euler.risk.domain.bo.LoginConfigBo;
import com.euler.common.mybatis.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 登录配置Service接口
 *
 * @author euler
 * @date 2022-08-23
 */
public interface ILoginConfigService extends IService<LoginConfig> {

    /**
     * 查询登录配置详细信息
     *
     * @param dto 登录配置
     * @return 登录配置
     */
    LoginConfigVo queryInfo(LoginConfigDto dto);

    /**
     * 查询SDK全局的登录配置列表
     *
     * @param dto 登录配置
     * @return 登录配置集合
     */
    List<LoginConfigVo> querySdkGlobalList(LoginConfigDto dto);

    /**
     * 查询APP全局的登录配置列表
     *
     * @param dto 登录配置
     * @return 登录配置集合
     */
    List<LoginConfigVo> queryAppGlobalList(LoginConfigDto dto);

    /**
     * 查询SDK单个游戏的登录配置列表
     *
     * @param dto 登录配置
     * @return 登录配置集合
     */
    List<LoginConfigVo> querySdkList(LoginConfigDto dto);

    /**
     * 查询SDK单个游戏的登录配置列表
     *
     * @param dto 登录配置
     * @return 登录配置集合
     */
    TableDataInfo<LoginConfigVo> queryPageList(LoginConfigDto dto);

    /**
     * 修改登录配置
     *
     * @param bo 登录配置
     * @return 结果
     */
    R insertByBo(LoginConfigBo bo);

    /**
     * 修改登录配置
     *
     * @param bo 登录配置
     * @return 结果
     */
    R updateByBo(LoginConfigBo bo);

    /**
     * 校验并批量删除登录配置信息
     *
     * @param ids 需要删除的登录配置主键集合
     * @param isValid 是否校验,true-删除前校验,false-不校验
     * @return 结果
     */
    R deleteWithValidByIds(Collection<Integer> ids, Boolean isValid);

}
