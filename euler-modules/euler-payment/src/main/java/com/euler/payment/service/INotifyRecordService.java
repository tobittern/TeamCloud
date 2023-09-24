package com.euler.payment.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.payment.api.domain.NotifyRecordVo;
import com.euler.payment.domain.BusinessOrder;
import com.euler.payment.domain.NotifyRecord;
import com.euler.payment.domain.PayOrder;
import com.euler.payment.domain.dto.NotifyRecordPageDto;

import java.util.List;

/**
 * 通知记录Service接口
 *
 * @author euler
 * @date 2022-03-29
 */
public interface INotifyRecordService extends IService<NotifyRecord> {

    /**
     * 查询通知记录
     *
     * @param id 通知记录主键
     * @return 通知记录
     */
    NotifyRecordVo queryById(Long id);

    /**
     * 查询通知记录列表
     *
     * @param notifyRecord 通知记录
     * @return 通知记录集合
     */
    TableDataInfo<NotifyRecordVo> queryPageList(NotifyRecordPageDto notifyRecord);

    /**
     * 查询通知记录列表
     *
     * @param notifyRecord 通知记录
     * @return 通知记录集合
     */
    List<NotifyRecordVo> queryList(NotifyRecordPageDto notifyRecord);

    /**
     * 通知业务方，业务方需要返回success，其他值认为是通知失败
     *
     * @param payOrder      支付订单
     * @param businessOrder 业务订单
     * @param businessType 1:支付，2：退款
     */
     void notifyBusiness(PayOrder payOrder, BusinessOrder businessOrder, Integer businessType);


}
