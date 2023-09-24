package com.euler.community.service;

import com.euler.community.domain.entity.Shield;
import com.euler.community.domain.vo.ShieldVo;
import com.euler.community.domain.bo.ShieldBo;
import com.euler.community.domain.dto.ShieldPageDto;
import com.baomidou.mybatisplus.extension.service.IService;
import com.euler.common.mybatis.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 屏蔽信息Service接口
 *
 * @author euler
 * @date 2022-09-15
 */
public interface IShieldService extends IService<Shield> {

    /**
     * 查询屏蔽信息
     *
     * @param id 屏蔽信息主键
     * @return 屏蔽信息
     */
    ShieldVo queryById(Integer id);

    /**
     * 查询屏蔽信息列表
     *
     * @param pageDto 屏蔽信息
     * @return 屏蔽信息集合
     */
    TableDataInfo<ShieldVo> queryPageList(ShieldPageDto pageDto);

    /**
     * 查询屏蔽信息列表
     *
     * @param pageDto 屏蔽信息
     * @return 屏蔽信息集合
     */
    List<ShieldVo> queryList(ShieldPageDto pageDto);

    /**
     * 修改屏蔽信息
     *
     * @param bo 屏蔽信息
     * @return 结果
     */
    Boolean insertByBo(ShieldBo bo);

    /**
     * 修改屏蔽信息
     *
     * @param bo 屏蔽信息
     * @return 结果
     */
    Boolean updateByBo(ShieldBo bo);

    /**
     * 校验并批量删除屏蔽信息信息
     *
     * @param ids 需要删除的屏蔽信息主键集合
     * @return 结果
     */
    Boolean deleteWithValidByIds(Collection<Integer> ids);
}
