package com.euler.system.mapper;

import com.euler.common.mybatis.core.mapper.BaseMapperPlus;
import com.euler.system.domain.SdkMainMenu;
import com.euler.system.domain.vo.SdkMainMenuVo;
import org.apache.ibatis.annotations.Mapper;

/**
 * SDK菜单Mapper接口
 *
 * @author euler
 * @date 2023-03-20
 */
@Mapper
public interface SdkMainMenuMapper extends BaseMapperPlus<SdkMainMenuMapper, SdkMainMenu, SdkMainMenuVo> {

}
