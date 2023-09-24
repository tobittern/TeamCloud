package com.euler.community.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.euler.common.core.domain.R;
import com.euler.common.mybatis.core.page.PageQuery;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.community.domain.bo.ResourceBo;
import com.euler.community.domain.dto.ResourceDto;
import com.euler.community.domain.entity.Resource;
import com.euler.community.domain.vo.ResourceVo;

import java.util.Collection;

/**
 * 动态所有资源Service接口
 *
 * @author euler
 * @date 2022-06-09
 */
public interface IResourceService extends IService<Resource> {

    /**
     * 查询动态所有资源
     *
     * @param id 动态所有资源主键
     * @return 动态所有资源
     */
    ResourceVo queryById(Long id);

    /**
     * 查询动态所有资源列表
     *
     * @return 动态所有资源集合
     */
    TableDataInfo<ResourceVo> queryPageList(ResourceDto dto);

    /**
     * 修改动态所有资源
     *
     * @return 结果
     */
    R insertByBo(ResourceBo bo);

    /**
     * 校验并批量删除动态所有资源信息
     *
     * @param ids 需要删除的动态所有资源主键集合
     * @param isValid 是否校验,true-删除前校验,false-不校验
     * @return 结果
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
