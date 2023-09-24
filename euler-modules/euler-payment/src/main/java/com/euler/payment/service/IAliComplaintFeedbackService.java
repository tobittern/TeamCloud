package com.euler.payment.service;

import com.euler.payment.domain.AliComplaintFeedback;
import com.euler.payment.domain.dto.AliComplaintFeedbackPageDto;
import com.euler.payment.domain.vo.AliComplaintFeedbackVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.euler.common.mybatis.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 商家处理交易投诉Service接口
 *
 * @author euler
 * @date 2022-10-18
 */
public interface IAliComplaintFeedbackService extends IService<AliComplaintFeedback> {

    /**
     * 查询商家处理交易投诉
     *
     * @param id 商家处理交易投诉主键
     * @return 商家处理交易投诉
     */
    AliComplaintFeedbackVo queryById(Integer id);

    /**
     * 查询商家处理交易投诉列表
     *
     * @param pageDto 商家处理交易投诉
     * @return 商家处理交易投诉集合
     */
    TableDataInfo<AliComplaintFeedbackVo> queryPageList(AliComplaintFeedbackPageDto pageDto);

    /**
     * 查询商家处理交易投诉列表
     *
     * @param pageDto 商家处理交易投诉
     * @return 商家处理交易投诉集合
     */
    List<AliComplaintFeedbackVo> queryList(AliComplaintFeedbackPageDto pageDto);

    /**
     * 修改商家处理交易投诉
     *
     * @param bo 商家处理交易投诉
     * @return 结果
     */
    Boolean insertByBo(AliComplaintFeedback entity);

    /**
     * 修改商家处理交易投诉
     *
     * @param bo 商家处理交易投诉
     * @return 结果
     */
    Boolean updateByBo(AliComplaintFeedback entity);

    /**
     * 校验并批量删除商家处理交易投诉信息
     *
     * @param ids 需要删除的商家处理交易投诉主键集合
     * @return 结果
     */
    Boolean deleteWithValidByIds(Collection<Integer> ids);
}
