package com.euler.risk.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.euler.common.mybatis.core.mapper.BaseMapperPlus;
import com.euler.risk.domain.entity.Blacklist;
import com.euler.risk.domain.vo.BlacklistVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 黑名单Mapper接口
 *
 * @author euler
 * @date 2022-08-23
 */
@Mapper
public interface BlacklistMapper extends BaseMapperPlus<BlacklistMapper, Blacklist, BlacklistVo> {

    /**
     * 查询黑名单列表
     * @param page
     * @param queryWrapper
     * @return
     */
    Page<BlacklistVo> selectBlackList(@Param("page") Page<Blacklist> page, @Param(Constants.WRAPPER) Wrapper<Blacklist> queryWrapper);

}
