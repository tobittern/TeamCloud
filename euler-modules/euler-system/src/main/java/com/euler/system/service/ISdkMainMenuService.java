package com.euler.system.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdNameTypeDicDto;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.system.domain.SdkMainMenu;
import com.euler.system.domain.bo.SdkMainMenuBo;
import com.euler.system.domain.dto.SdkMainMenuDto;
import com.euler.system.domain.vo.SdkMainMenuVo;

import java.util.Collection;
import java.util.List;

/**
 * SDK菜单Service接口
 *
 * @author euler
 * @date 2023-03-20
 */
public interface ISdkMainMenuService extends IService<SdkMainMenu> {

    /**
     * 查询SDK菜单
     *
     * @param id SDK菜单主键
     * @return SDK菜单
     */
    SdkMainMenuVo queryById(Integer id);

    /**
     * 查询SDK菜单列表
     *
     * @param dto SDK菜单
     * @return SDK菜单集合
     */
    TableDataInfo<SdkMainMenuVo> queryPageList(SdkMainMenuDto dto);

    /**
     * 查询SDK菜单列表
     *
     * @param dto SDK菜单
     * @return SDK菜单
     */
    List<SdkMainMenuVo> queryList(SdkMainMenuDto dto);

    /**
     * 修改SDK菜单
     *
     * @param bo SDK菜单
     * @return 结果
     */
    R insertByBo(SdkMainMenuBo bo);

    /**
     * 修改SDK菜单
     *
     * @param bo SDK菜单
     * @return 结果
     */
    R updateByBo(SdkMainMenuBo bo);

    /**
     * 操作上下线
     *
     * @param idNameTypeDicDto 字典Dto
     * @param userId 用户id
     * @return 结果
     */
    R operation(IdNameTypeDicDto idNameTypeDicDto, Long userId);

    /**
     * 校验并批量删除SDK菜单信息
     *
     * @param ids 需要删除的SDK菜单主键集合
     * @return 结果
     */
    R deleteWithValidByIds(Collection<Integer> ids);

}
