package com.euler.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.euler.common.core.domain.R;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.system.domain.SdkSubMenu;
import com.euler.system.domain.bo.SdkSubMenuBo;
import com.euler.system.domain.dto.SdkSubMenuDto;
import com.euler.system.domain.vo.SdkSubMenuVo;

import java.util.List;

/**
 * SDK子菜单Service接口
 *
 * @author euler
 * @date 2023-03-20
 */
public interface ISdkSubMenuService extends IService<SdkSubMenu> {

    /**
     * 查询SDK子菜单
     *
     * @param dto SDK子菜单
     * @return SDK子菜单
     */
    SdkSubMenuVo queryByDto(SdkSubMenuDto dto);

    /**
     * 查询SDK子菜单列表
     *
     * @param dto SDK子菜单
     * @return SDK子菜单集合
     */
    TableDataInfo<SdkSubMenuVo> queryPageList(SdkSubMenuDto dto);

    /**
     * 查询SDK子菜单列表
     *
     * @param dto SDK子菜单
     * @return SDK子菜单集合
     */
    List<SdkSubMenuVo> queryList(SdkSubMenuDto dto);

    /**
     * 修改SDK子菜单
     *
     * @param bo SDK子菜单
     * @return 结果
     */
    R insertByBo(SdkSubMenuBo bo);

    /**
     * 修改SDK子菜单
     *
     * @param bo SDK子菜单
     * @return 结果
     */
    R updateByBo(SdkSubMenuBo bo);

    /**
     * 校验并批量删除SDK子菜单信息
     *
     * @param dto 需要删除的SDK子菜单
     * @return 结果
     */
    R deleteSubMenu(SdkSubMenuDto dto);

}
