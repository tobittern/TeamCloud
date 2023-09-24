package com.euler.payment.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.payment.domain.ComplaintResponse;
import com.euler.payment.domain.dto.ComplaintResponsePageDto;
import com.euler.payment.domain.vo.ComplaintResponseVo;

import java.util.Collection;
import java.util.List;

/**
 * 投诉回复Service接口
 *
 * @author euler
 * @date 2022-09-13
 */
public interface IComplaintResponseService extends IService<ComplaintResponse> {

    /**
     * 查询投诉回复
     *
     * @param id 投诉回复主键
     * @return 投诉回复
     */
    ComplaintResponseVo queryById(Long id);

    /**
     * 查询投诉回复列表
     *
     * @param pageDto 投诉回复
     * @return 投诉回复集合
     */
    TableDataInfo<ComplaintResponseVo> queryPageList(ComplaintResponsePageDto pageDto);

    /**
     * 查询投诉回复列表
     *
     * @param pageDto 投诉回复
     * @return 投诉回复集合
     */
    List<ComplaintResponseVo> queryList(ComplaintResponsePageDto pageDto);

    /**
     * 修改投诉回复
     *
     * @param entity 投诉回复
     * @return 结果
     */
    Boolean insertByBo(ComplaintResponse entity);

    /**
     * 校验并批量删除投诉回复信息
     *
     * @param ids 需要删除的投诉回复主键集合
     * @return 结果
     */
    Boolean deleteWithValidByIds(Collection<Long> ids);
}
