package com.euler.sdk.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.euler.common.core.domain.R;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.sdk.domain.bo.VersionBo;
import com.euler.sdk.domain.dto.VersionDto;
import com.euler.sdk.domain.entity.Version;
import com.euler.sdk.domain.vo.VersionVo;

import java.util.Collection;
import java.util.List;

/**
 * sdk版本管理Service接口
 *
 * @author euler
 *  2022-07-08
 */
public interface IVersionService extends IService<Version> {

    /**
     * 查询sdk版本管理
     *
     * @param id sdk版本管理主键
     * @return sdk版本管理
     */
    VersionVo queryById(Long id);

    /**
     * 查询sdk版本管理列表
     *
     * @param versionDto sdk版本管理
     * @return sdk版本管理集合
     */
    TableDataInfo<VersionVo> queryPageList(VersionDto versionDto);

    /**
     * 查询sdk版本管理列表
     *
     * @param versionDto sdk版本管理
     * @return sdk版本管理集合
     */
    List<VersionVo> queryList(VersionDto versionDto);

    /**
     * 修改sdk版本管理
     *
     * @param bo sdk版本管理
     * @return 结果
     */
    R<Void> insertByBo(VersionBo bo);

    /**
     * 修改sdk版本管理
     *
     * @param bo sdk版本管理
     * @return 结果
     */
    R<Void> updateByBo(VersionBo bo);

    /**
     * 校验并批量删除sdk版本管理信息
     *
     * @param ids 需要删除的sdk版本管理主键集合
     * @param isValid 是否校验,true-删除前校验,false-不校验
     * @return 结果
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 删除
     * @param id 主键
     */
    R<Void> delete(Long id);

    /**
     * 获取列表展示
     * @return
     */
    Object getNewVersions();

    /**
     * 官网Sdk版本下载数据，返回特定格式
     */
    Object getSdkVersions();
}
