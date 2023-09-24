package com.euler.sdk.mapper;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.euler.common.mybatis.core.mapper.BaseMapperPlus;
import com.euler.sdk.api.domain.MemberDetailVo;
import com.euler.sdk.api.domain.MyGameVo;
import com.euler.sdk.domain.entity.MyGame;
import org.apache.ibatis.annotations.Param;

/**
 * 我的游戏Mapper接口
 *
 * @author euler
 * @date 2022-03-29
 */
public interface MyGameMapper extends BaseMapperPlus<MyGameMapper, MyGame, MyGameVo> {

    Page<MyGameVo> getMyGameVoPageList(@Param("page") Page<MemberDetailVo> page, @Param(Constants.WRAPPER) Wrapper<MyGame> queryWrapper);

    MyGameVo getMyGameVo(@Param(Constants.WRAPPER) Wrapper<MyGame> queryWrapper);


}
