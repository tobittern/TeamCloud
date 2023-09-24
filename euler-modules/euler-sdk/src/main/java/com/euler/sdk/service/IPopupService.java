package com.euler.sdk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdNameTypeDicDto;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.sdk.api.domain.SdkPopupVo;
import com.euler.sdk.api.domain.dto.SdkPopupDto;
import com.euler.sdk.domain.bo.PopupBo;
import com.euler.sdk.domain.dto.PopupPageDto;
import com.euler.sdk.domain.entity.Popup;

import java.util.Collection;
import java.util.List;

/**
 * 弹窗管理Service接口
 *
 * @author euler
 * @date 2022-09-05
 */
public interface IPopupService extends IService<Popup> {

    /**
     * 查询弹窗管理
     *
     * @param id 弹窗管理主键
     * @return 弹窗管理
     */
    SdkPopupVo queryById(Integer id);

    /**
     * 查询弹窗管理列表
     *
     * @param pageDto 弹窗管理
     * @return 弹窗管理集合
     */
    TableDataInfo<SdkPopupVo> queryPageList(PopupPageDto pageDto);

    /**
     * 查询出当前用户是否需要展示出指定弹框
     *
     * @return 弹窗管理集合
     */
    List<SdkPopupVo> queryList(SdkPopupDto dto);

    /**
     * 修改弹窗管理
     *
     * @param bo 弹窗管理
     * @return 结果
     */
    R insertByBo(PopupBo bo);

    /**
     * 修改弹窗管理
     *
     * @param bo 弹窗管理
     * @return 结果
     */
    R updateByBo(PopupBo bo);

    /**
     * 校验并批量删除弹窗管理信息
     *
     * @param ids     需要删除的弹窗管理主键集合
     * @param isValid 是否校验,true-删除前校验,false-不校验
     * @return 结果
     */
    Boolean deleteWithValidByIds(Collection<Integer> ids, Boolean isValid);

    /**
     * 操作状态
     *
     * @return 结果
     */
    R operation(IdNameTypeDicDto idNameTypeDicDto);
}
