package com.euler.community.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.euler.common.core.domain.R;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.community.domain.bo.VersionBo;
import com.euler.community.domain.dto.VersionDto;
import com.euler.community.domain.dto.VersionPublishDto;
import com.euler.community.domain.entity.Version;
import com.euler.community.domain.vo.VersionVo;

import java.util.Collection;

/**
 * 版本管理Service接口
 *
 * @author euler
 * @date 2022-06-01
 */
public interface IVersionService extends IService<Version> {

    /**
     * 查询版本管理
     *
     * @param id 版本管理主键
     * @return 版本管理
     */
    VersionVo queryById(Long id);

    /**
     * 查询版本管理列表
     *
     * @param dto 版本管理
     * @return 版本管理集合
     */
    TableDataInfo<VersionVo> queryPageList(VersionDto dto);

    /**
     * 修改版本管理
     *
     * @param bo 版本管理
     * @return 结果
     */
    R insertByBo(VersionBo bo);

    /**
     * 修改版本管理
     *
     * @param bo 版本管理
     * @return 结果
     */
    R updateByBo(VersionBo bo);

    /**
     * 发布版本
     *
     * @param dto
     * @return 结果
     */
    R publish(VersionPublishDto dto);

    /**
     * 校验并批量删除版本管理信息
     *
     * @param ids 需要删除的版本管理主键集合
     * @param isValid 是否校验,true-删除前校验,false-不校验
     * @return 结果
     */
    R deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 判断用户是否需要更新版本
     * @param dto
     * @return true:需要强更 false:不需要强更
     */
    R isUpdate(VersionDto dto);

     Version getCurrentVersion(VersionDto versionDto);


    }
