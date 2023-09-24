package com.euler.sdk.service;

import com.euler.common.core.domain.R;
import com.euler.sdk.domain.vo.MemberRightsReceiveRecordVo;
import com.euler.sdk.domain.bo.MemberRightsReceiveRecordBo;
import com.euler.common.mybatis.core.page.PageQuery;
import com.euler.common.mybatis.core.page.TableDataInfo;

import java.util.List;

/**
 * 会员权益领取记录Service接口
 *
 * @author euler
 * @date 2022-04-08
 */
public interface IMemberRightsReceiveRecordService {

    /**
     * 查询会员权益领取记录
     *
     * @param id 会员权益领取记录主键
     * @return 会员权益领取记录
     */
    MemberRightsReceiveRecordVo queryById(Integer id);

    /**
     * 查询会员权益领取记录列表
     *
     * @param bo 会员权益领取记录
     * @return 会员权益领取记录集合
     */
    TableDataInfo<MemberRightsReceiveRecordVo> queryPageList(MemberRightsReceiveRecordBo bo, PageQuery pageQuery);

    /**
     * 查询会员权益领取记录列表
     *
     * @param bo 会员权益领取记录
     * @return 会员权益领取记录集合
     */
    List<MemberRightsReceiveRecordVo> queryList(MemberRightsReceiveRecordBo bo);

    /**
     * 修改会员权益领取记录
     *
     * @param bo 会员权益领取记录
     * @return 结果
     */
    R insertByBo(MemberRightsReceiveRecordBo bo);

    /**
     * 判断用户是否领取过
     * @param userId
     * @return
     */
    Boolean selectUserIsReceive(Long userId, Integer year, Integer month, Integer type);


}
