package com.euler.sdk.service;

import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdNameTypeDicDto;
import com.euler.sdk.domain.dto.GiftManagementDetailDto;
import com.euler.sdk.domain.dto.GiftManagementDto;
import com.euler.sdk.domain.vo.GiftInfoVo;
import com.euler.sdk.domain.vo.GiftManagementVo;
import com.euler.sdk.domain.bo.GiftManagementBo;
import com.euler.common.mybatis.core.page.TableDataInfo;

import java.util.Collection;

/**
 * 礼包组管理Service接口
 *
 * @author euler
 * @date 2022-03-22
 */
public interface IGiftManagementService {

    /**
     * 查询礼包组管理
     *
     * @param id 礼包组管理主键
     * @return 礼包管理
     */
    GiftManagementVo queryById(Integer id);

    /**
     * 查询礼包组管理列表
     *
     * @param dto 礼包组管理
     * @return 礼包管理集合
     */
    TableDataInfo<GiftManagementVo> queryPageList(GiftManagementDto dto);

    /**
     * 根据渠道code获取礼包组管理详细信息
     *
     * @return 礼包组管理详细信息
     */
    GiftManagementVo getInfoByCodeKey();

    /**
     * 新增礼包组
     *
     * @param bo 礼包
     * @return 结果
     */
    R insertByBo(GiftManagementBo bo);

    /**
     * 修改礼包管理
     *
     * @param bo 礼包管理
     * @return 结果
     */
    R updateByBo(GiftManagementBo bo);

    /**
     * 商品操作上下线
     *
     * @param idNameTypeDicDto 字典Dto
     * @param userId 用户id
     * @return 结果
     */
    R operation(IdNameTypeDicDto idNameTypeDicDto, Long userId);

    /**
     * 查看礼包内容详情
     *
     * @param dto 礼包详情管理
     * @return 礼包内容详情
     */
    TableDataInfo<GiftInfoVo> getGiftContentsList(GiftManagementDetailDto dto);

    /**
     * 校验并批量删除礼包组管理的同时，删除对应的礼包详情
     *
     * @param ids 需要删除的礼包管理主键集合
     * @param isValid 是否校验,true-删除前校验,false-不校验
     * @return 结果
     */
    R deleteWithValidByIds(Collection<Integer> ids, Boolean isValid);
}
