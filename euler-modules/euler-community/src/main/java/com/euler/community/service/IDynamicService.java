package com.euler.community.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdDto;
import com.euler.common.core.domain.dto.IdNameTypeDicDto;
import com.euler.common.core.domain.dto.IdTypeDto;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.community.domain.bo.DynamicBo;
import com.euler.community.domain.dto.DynamicDto;
import com.euler.community.domain.entity.Dynamic;
import com.euler.community.domain.vo.DynamicVo;

import java.util.Collection;
import java.util.Date;

/**
 * 动态Service接口
 *
 * @author euler
 * @date 2022-06-01
 */
public interface IDynamicService extends IService<Dynamic> {

    /**
     * 查询动态
     *
     * @param id 动态主键
     * @return 动态
     */
    DynamicVo queryById(Long id);

    /**
     * 查询动态列表
     *
     * @return 动态集合
     */
    TableDataInfo<DynamicVo> queryPageList(DynamicDto dto);


    /**
     * 审核动态
     *
     * @param dto
     * @return
     */
    R auditDynamic(IdNameTypeDicDto<Long> dto);

    /**
     * 修改动态
     *
     * @return 结果
     */
    R insertByBo(DynamicBo bo);

    /**
     * 批量操作动态
     *
     * @param ids    需要删除的动态主键集合
     * @param userId 用户ID
     * @return 结果
     */
    Boolean operationWithValidByIds(Collection<Long> ids, Long userId, Integer operationType, String name);

    /**
     * 动态的一些基础数据进行自加操作
     * @param idTypeDto
     * @return
     */
    Boolean incrDynamicSomeFieldValue(IdTypeDto<String, Integer> idTypeDto);

    /**
     * 用户注销之后清空用户的动态数据
     * @param userId
     * @return
     */
    R cancellationClearDynamic(Long userId);


    /**
     * 统计用户动态数量
     * @return
     */
    Long count(Long userId);

    /**
     * 统计用户某天的动态数量
     */
    Long dynamicCountForDay(Long userId, Date date);

    /**
     * 删除动态
     * @param idDto
     * @return
     */
    R deleteWithValidByIds(IdDto<String> idDto);
}
