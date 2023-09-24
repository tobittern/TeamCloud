package com.euler.payment.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.payment.domain.AliComplaint;
import com.euler.payment.domain.Complaint;
import com.euler.payment.domain.dto.AliComplaintPageDto;
import com.euler.payment.domain.dto.ComplaintInfoPageDto;
import com.euler.payment.domain.dto.ComplaintPageDto;
import com.euler.payment.domain.vo.AliComplaintInfoVo;
import com.euler.payment.domain.vo.AliComplaintVo;
import com.euler.payment.domain.vo.ComplaintInfoVo;
import com.euler.payment.domain.vo.ComplaintVo;

import java.util.Collection;
import java.util.List;

/**
 * 投诉Service接口
 *
 * @author euler
 * @date 2022-09-13
 */
public interface IComplaintService extends IService<Complaint> {

    /**
     * 查询投诉
     *
     * @param complaintId 投诉单号
     * @return 投诉
     */
    ComplaintInfoVo queryById(String complaintId);

    /**
     * 查询投诉列表
     *
     * @param pageDto 投诉
     * @return 投诉集合
     */
    TableDataInfo<ComplaintVo> queryPageList(ComplaintPageDto pageDto);

    /**
     * 查询投诉列表
     *
     * @param pageDto 投诉
     * @return 投诉集合
     */
    List<ComplaintVo> queryList(ComplaintPageDto pageDto);

    /**
     * 查询投诉列表
     *
     * @param pageDto 投诉
     * @return 投诉集合
     */
    TableDataInfo<ComplaintInfoVo> queryPageInfoList(ComplaintInfoPageDto pageDto);


    /**
     * 基础投诉数据入库
     *
     * @return 结果
     */
    Boolean insertComplaint(String entity);


    /**
     * 投诉详情信息入库
     *
     * @return 结果
     */
    Boolean insertComplaintInfo(String complaintId);

    /**
     * 校验并批量删除投诉信息
     *
     * @param ids 需要删除的投诉主键集合
     * @return 结果
     */
    Boolean deleteWithValidByIds(Collection<Long> ids);


    /**
     * ali基础投诉数据入库
     *
     * @return 结果
     */
    Boolean insertAliComplaintStr(String entity);

    /**
     * ali基础投诉数据入库
     *
     * @return 结果
     */
    Boolean insertAliComplaint(AliComplaintInfoVo entity);

    /**
     * ali新增投诉
     *
     * @return 结果
     */

    Boolean insertAliComplaintBatch(String entitys);

    /**
     * 查询投诉
     *
     * @param complaintId 投诉单号
     * @return 投诉
     */
    AliComplaintInfoVo queryAliById(String complaintId);


    /**
     * 查询投诉列表
     *
     * @param pageDto 投诉
     * @return 投诉集合
     */
    TableDataInfo<AliComplaintInfoVo> queryAliPageList(AliComplaintPageDto pageDto);
}
