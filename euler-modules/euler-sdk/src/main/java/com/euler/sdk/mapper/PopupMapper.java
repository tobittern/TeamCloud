package com.euler.sdk.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.euler.common.mybatis.core.mapper.BaseMapperPlus;
import com.euler.sdk.api.domain.SdkPopupVo;
import com.euler.sdk.domain.entity.Popup;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 弹窗管理Mapper接口
 *
 * @author euler
 * @date 2022-09-05
 */
public interface PopupMapper extends BaseMapperPlus<PopupMapper, Popup, SdkPopupVo> {

    List<SdkPopupVo> getMapperList(@Param(Constants.WRAPPER) Wrapper<Popup> queryWrapper);

}
