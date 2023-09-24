package com.euler.community.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdNameTypeDicDto;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.community.domain.bo.PopupBo;
import com.euler.community.domain.dto.PopupDto;
import com.euler.community.domain.entity.Popup;
import com.euler.community.domain.vo.PopupVo;
import java.util.Collection;
import java.util.List;

/**
 * 弹窗管理Service接口
 *
 * @author euler
 * @date 2022-06-02
 */
public interface IPopupService extends IService<Popup> {

    /**
     * 查询弹窗管理
     *
     * @param id 弹窗管理主键
     * @return 弹窗管理
     */
    PopupVo queryById(Long id);

    /**
     * 查询弹窗管理列表
     *
     * @param dto 弹窗管理
     * @return 弹窗管理集合
     */
    TableDataInfo<PopupVo> queryPageList(PopupDto dto);

    /**
     * 查询弹窗管理列表
     *
     * @return 弹窗管理集合
     * @param position
     */
    List<PopupVo> queryList(PopupDto position);

    /**
     * 修改弹窗管理
     *
     * @param bo 弹窗管理
     * @return 结果
     */
   R  insertByBo(PopupBo bo);

    /**
     * 修改弹窗管理
     *
     * @param bo 弹窗管理
     * @return 结果
     */
    R updateByBo(PopupBo bo);

    /**
     * 逻辑删除
     * @param ids
     * @return
     */
    R deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 操作上下架
     */
    R operation(IdNameTypeDicDto dto, Long userId);

}
