package com.euler.system.mapper;

import com.euler.common.mybatis.core.mapper.BaseMapperPlus;
import com.euler.system.domain.SdkSubMenu;
import com.euler.system.domain.vo.SdkSubMenuVo;
import org.apache.ibatis.annotations.Mapper;

/**
 * SDK子菜单Mapper接口
 *
 * @author euler
 * @date 2023-03-20
 */
@Mapper
public interface SdkSubMenuMapper extends BaseMapperPlus<SdkSubMenuMapper, SdkSubMenu, SdkSubMenuVo> {

}
