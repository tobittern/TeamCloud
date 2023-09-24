package com.euler.payment.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.euler.common.core.domain.dto.SelectGameDto;
import com.euler.common.core.utils.*;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.common.payment.enums.PayTypeEnumd;
import com.euler.common.payment.factory.PayFactory;
import com.euler.payment.domain.*;
import com.euler.payment.domain.dto.AliComplaintPageDto;
import com.euler.payment.domain.dto.ComplaintInfoPageDto;
import com.euler.payment.domain.dto.ComplaintPageDto;
import com.euler.payment.domain.vo.*;
import com.euler.payment.mapper.*;
import com.euler.payment.service.IComplaintService;
import com.euler.platform.api.domain.OpenGameDubboVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * 投诉Service业务层处理
 *
 * @author euler
 * @date 2022-09-13
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ComplaintServiceImpl extends ServiceImpl<ComplaintMapper, Complaint> implements IComplaintService {

    @Autowired
    private ComplaintMapper baseMapper;
    @Autowired
    private ComplaintInfoMapper complaintInfoMapper;
    @Autowired
    private ComplaintMediaListMapper complaintMediaListMapper;
    @Autowired
    private ServiceOrderInfoMapper serviceOrderInfoMapper;
    @Autowired
    private ComplaintLogMapper complaintLogMapper;
    @Autowired
    private AliComplaintMapper aliComplaintMapper;
    @Autowired
    private AliComplaintReplyDetailInfosMapper aliComplaintReplyDetailInfosMapper;

    /**
     * 查询投诉
     *
     * @param complaintId 投诉单号
     * @return 投诉
     */
    @Override

    public ComplaintInfoVo queryById(String complaintId) {
        // 查询完毕之后需要将数据存储或者更新到数据库中
        ComplaintInfoVo complaintInfoVo = complaintInfoMapper.selectVoOne(Wrappers.<ComplaintInfo>lambdaQuery().eq(ComplaintInfo::getComplaintId, complaintId));
        if (complaintInfoVo != null) {
            // 查询其他数据
            LambdaQueryWrapper<ComplaintMediaList> mediaListLambdaQueryWrapper = Wrappers.<ComplaintMediaList>lambdaQuery().eq(ComplaintMediaList::getComplaintInfoId, complaintInfoVo.getId());
            List<ComplaintMediaListVo> complaintMediaListVos = complaintMediaListMapper.selectVoList(mediaListLambdaQueryWrapper);
            LambdaQueryWrapper<ServiceOrderInfo> serviceOrderInfoVoLambdaQueryWrapper = Wrappers.<ServiceOrderInfo>lambdaQuery().eq(ServiceOrderInfo::getComplaintInfoId, complaintInfoVo.getId());
            List<ServiceOrderInfoVo> serviceOrderInfoVos = serviceOrderInfoMapper.selectVoList(serviceOrderInfoVoLambdaQueryWrapper);
            complaintInfoVo.setComplaintMediaList(complaintMediaListVos);
            complaintInfoVo.setServiceOrderInfo(serviceOrderInfoVos);
        }
        return complaintInfoVo;
    }


    /**
     * 查询投诉列表
     *
     * @param pageDto 投诉
     * @return 投诉
     */
    @Override
    public TableDataInfo<ComplaintVo> queryPageList(ComplaintPageDto pageDto) {
        LambdaQueryWrapper<Complaint> lqw = buildQueryWrapper(pageDto);
        Page<ComplaintVo> result = baseMapper.selectVoPage(pageDto.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询投诉列表
     *
     * @param pageDto 投诉
     * @return 投诉
     */
    @Override
    public List<ComplaintVo> queryList(ComplaintPageDto pageDto) {
        LambdaQueryWrapper<Complaint> lqw = buildQueryWrapper(pageDto);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<Complaint> buildQueryWrapper(ComplaintPageDto pageDto) {
        LambdaQueryWrapper<Complaint> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(pageDto.getNoticeId()), Complaint::getNoticeId, pageDto.getNoticeId());
        lqw.eq(StringUtils.isNotBlank(pageDto.getEventType()), Complaint::getEventType, pageDto.getEventType());
        lqw.eq(StringUtils.isNotBlank(pageDto.getResourceType()), Complaint::getResourceType, pageDto.getResourceType());
        lqw.eq(StringUtils.isNotBlank(pageDto.getComplaintId()), Complaint::getComplaintId, pageDto.getComplaintId());
        lqw.eq(StringUtils.isNotBlank(pageDto.getActionType()), Complaint::getActionType, pageDto.getActionType());
        lqw.ge(StringUtils.isNotBlank(pageDto.getBeginTime()), Complaint::getCreateTime, DateUtils.getBeginOfDay(pageDto.getBeginTime()));
        lqw.le(StringUtils.isNotBlank(pageDto.getEndTime()), Complaint::getCreateTime, DateUtils.getEndOfDay(pageDto.getEndTime()));
        lqw.orderByDesc(Complaint::getComplaintCreateTime);
        return lqw;
    }


    @Override
    public TableDataInfo<ComplaintInfoVo> queryPageInfoList(ComplaintInfoPageDto pageDto) {
        LambdaQueryWrapper<ComplaintInfo> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(pageDto.getComplaintId()), ComplaintInfo::getComplaintId, pageDto.getComplaintId());
        lqw.eq(StringUtils.isNotBlank(pageDto.getComplaintTime()), ComplaintInfo::getComplaintTime, pageDto.getComplaintTime());
        lqw.eq(StringUtils.isNotBlank(pageDto.getComplaintDetail()), ComplaintInfo::getComplaintDetail, pageDto.getComplaintDetail());
        lqw.eq(StringUtils.isNotBlank(pageDto.getComplaintedMchid()), ComplaintInfo::getComplaintedMchid, pageDto.getComplaintedMchid());
        lqw.eq(StringUtils.isNotBlank(pageDto.getComplaintState()), ComplaintInfo::getComplaintState, pageDto.getComplaintState());
        lqw.eq(StringUtils.isNotBlank(pageDto.getPayerPhone()), ComplaintInfo::getPayerPhone, pageDto.getPayerPhone());
        lqw.eq(StringUtils.isNotBlank(pageDto.getPayerOpenid()), ComplaintInfo::getPayerOpenid, pageDto.getPayerOpenid());
        lqw.eq(StringUtils.isNotBlank(pageDto.getComplaintFullRefunded()), ComplaintInfo::getComplaintFullRefunded, pageDto.getComplaintFullRefunded());
        lqw.eq(StringUtils.isNotBlank(pageDto.getIncomingUserResponse()), ComplaintInfo::getIncomingUserResponse, pageDto.getIncomingUserResponse());
        lqw.eq(StringUtils.isNotBlank(pageDto.getProblemDescription()), ComplaintInfo::getProblemDescription, pageDto.getProblemDescription());
        lqw.eq(pageDto.getUserComplaintTimes() != null, ComplaintInfo::getUserComplaintTimes, pageDto.getUserComplaintTimes());
        lqw.eq(StringUtils.isNotBlank(pageDto.getProblemType()), ComplaintInfo::getProblemType, pageDto.getProblemType());
        lqw.eq(pageDto.getApplyRefundAmount() != null, ComplaintInfo::getApplyRefundAmount, pageDto.getApplyRefundAmount());
        lqw.eq(StringUtils.isNotBlank(pageDto.getUserTagList()), ComplaintInfo::getUserTagList, pageDto.getUserTagList());
        lqw.eq(StringUtils.isNotBlank(pageDto.getTransactionId()), ComplaintInfo::getTransactionId, pageDto.getTransactionId());
        lqw.eq(StringUtils.isNotBlank(pageDto.getOutTradeNo()), ComplaintInfo::getOutTradeNo, pageDto.getOutTradeNo());
        lqw.eq(pageDto.getAmount() != null, ComplaintInfo::getAmount, pageDto.getAmount());
        lqw.ge(StringUtils.isNotBlank(pageDto.getBeginTime()), ComplaintInfo::getCreateTime, DateUtils.getBeginOfDay(pageDto.getBeginTime()));
        lqw.le(StringUtils.isNotBlank(pageDto.getEndTime()), ComplaintInfo::getCreateTime, DateUtils.getEndOfDay(pageDto.getEndTime()));
        lqw.orderByDesc(ComplaintInfo::getComplaintTime);
        Page<ComplaintInfoVo> result = complaintInfoMapper.selectVoPage(pageDto.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 新增投诉
     *
     * @return 结果
     */
    @Override
    public Boolean insertComplaint(String entity) {

        // 对json串进行解析
        ComplaintJsonObject complaintJsonObject = JsonUtils.parseObject(entity, ComplaintJsonObject.class);
        // 进行指定对象数据的copy
        Complaint complaint = BeanCopyUtils.copy(complaintJsonObject, Complaint.class);
        // 在对一些特殊字段进行一下复制操作
        if (complaintJsonObject != null && complaintJsonObject.getId() != null) {
            // 存储到log中
            ComplaintLog complaintLog = new ComplaintLog();
            complaintLog.setType(1);
            complaintLog.setContent(entity);
            complaintLogMapper.insert(complaintLog);
            // 先判断是否存在过
            LambdaQueryWrapper<Complaint> eq = Wrappers.<Complaint>lambdaQuery().eq(Complaint::getNoticeId, complaintJsonObject.getId());
            ComplaintVo complaintVo = baseMapper.selectVoOne(eq);
            int bool = 0;
            if (complaintVo != null && complaintVo.getId() != null) {
                // 执行更新操作
                complaint.setId(complaintVo.getId());
                bool = baseMapper.updateById(complaint);
            } else {
                // 执行添加操作
                complaint.setNoticeId(complaintJsonObject.getId());
                DateTime parse = DateUtil.parse(complaintJsonObject.getCreateTime());
                complaint.setComplaintCreateTime(parse.toString());
                bool = baseMapper.insert(complaint);
            }
            // 如果成功了
            if (bool > 0) {
                insertComplaintInfo(complaint.getComplaintId());
                return true;
            }
        }

        return false;
    }


    /**
     * 新增投诉
     *
     * @return 结果
     */
    @Override
    public Boolean insertComplaintInfo(String complaintId) {
        // 第一步 根据投诉单号查询投诉的详情信息
        var complaintService = PayFactory.getComplaintServiceType(PayTypeEnumd.WxComplain.getValue());
        String complainInfo = complaintService.getComplainInfo(complaintId);
        // 存储到log中
        ComplaintLog complaintLog = new ComplaintLog();
        complaintLog.setType(2);
        complaintLog.setContent(complainInfo);
        complaintLogMapper.insert(complaintLog);
        ComplaintInfoVo complaintInfoVo = JsonHelper.toObject(complainInfo, ComplaintInfoVo.class);
        if (complaintInfoVo != null && complaintInfoVo.getComplaintId() != null) {
            // 查询数据库中是否存在
            LambdaQueryWrapper<ComplaintInfo> eq = Wrappers.<ComplaintInfo>lambdaQuery()
                .eq(ComplaintInfo::getComplaintId, complaintInfoVo.getComplaintId())
                .orderByDesc(ComplaintInfo::getId)
                .last("limit 1");
            ComplaintInfoVo searchComplaintInfo = complaintInfoMapper.selectVoOne(eq);
            // 如果不存在
            if (searchComplaintInfo == null || searchComplaintInfo.getId() == null) {
                // 数据入库
                // 1 complaint_info 数据
                ComplaintInfo complaintInfo = BeanCopyUtils.copy(complaintInfoVo, ComplaintInfo.class);
                // 对事件进行格式化一下
                DateTime parse = DateUtil.parse(complaintInfo.getComplaintTime());
                complaintInfo.setComplaintTime(parse.toString());
                List<ComplaintOrderInfoVo> complaintOrderInfoVo = complaintInfoVo.getComplaintOrderInfo();
                if (complaintOrderInfoVo != null && complaintOrderInfoVo.size() > 0) {
                    // 将订单数据添加到表中
                    ComplaintOrderInfoVo complaintOrderInfoVoFirst = complaintOrderInfoVo.get(0);
                    complaintInfo.setTransactionId(complaintOrderInfoVoFirst.getTransactionId());
                    complaintInfo.setOutTradeNo(complaintOrderInfoVoFirst.getOutTradeNo());
                    complaintInfo.setAmount(complaintOrderInfoVoFirst.getAmount());
                }
                int insert = complaintInfoMapper.insert(complaintInfo);
                if (insert > 0) {
                    Long newInsertId = complaintInfo.getId();
                    insertOtherMysql(newInsertId, complaintInfoVo);
                }
                return true;
            } else {
                // 数据更新
                // 更新complaint_info
                ComplaintInfo complaintInfoCopy = BeanCopyUtils.copy(complaintInfoVo, ComplaintInfo.class);
                if (complaintInfoCopy != null && complaintInfoCopy.getId() != null) {
                    complaintInfoCopy.setId(searchComplaintInfo.getId());
                    DateTime parse = DateUtil.parse(searchComplaintInfo.getComplaintTime());
                    complaintInfoCopy.setComplaintTime(parse.toString());
                    List<ComplaintOrderInfoVo> complaintOrderInfoVo = complaintInfoVo.getComplaintOrderInfo();
                    if (complaintOrderInfoVo != null && complaintOrderInfoVo.size() > 0) {
                        // 将订单数据添加到表中
                        ComplaintOrderInfoVo complaintOrderInfoVoFirst = complaintOrderInfoVo.get(0);
                        complaintInfoCopy.setTransactionId(complaintOrderInfoVoFirst.getTransactionId());
                        complaintInfoCopy.setOutTradeNo(complaintOrderInfoVoFirst.getOutTradeNo());
                        complaintInfoCopy.setAmount(complaintOrderInfoVoFirst.getAmount());
                    }
                    complaintInfoMapper.updateById(complaintInfoCopy);
                    insertOtherMysql(searchComplaintInfo.getId(), complaintInfoVo);
                }
                return true;
            }
        }
        return false;
    }

    private void insertOtherMysql(Long newInsertId, ComplaintInfoVo complaintInfoVo) {
        // 外层数据入库之后我们需要把里面其他的数据入库
        List<ComplaintMediaList> complaintMediaLists = new ArrayList<>();
        List<ComplaintMediaListVo> complaintMediaListVo = complaintInfoVo.getComplaintMediaList();
        if (complaintMediaListVo != null && complaintMediaListVo.size() > 0) {
            // 先删除
            complaintMediaListMapper.delete(Wrappers.<ComplaintMediaList>lambdaQuery().eq(ComplaintMediaList::getComplaintInfoId, newInsertId));
            // 在新增
            complaintMediaListVo.forEach(a -> {
                ComplaintMediaList complaintMediaList = new ComplaintMediaList();
                complaintMediaList.setComplaintInfoId(newInsertId);
                complaintMediaList.setMediaType(a.getMediaType());
                complaintMediaList.setMediaUrl(a.getMediaUrl());
                complaintMediaLists.add(complaintMediaList);
            });
            complaintMediaListMapper.insertBatch(complaintMediaLists);
        }
        List<ServiceOrderInfo> serviceOrderInfos = new ArrayList<>();
        List<ServiceOrderInfoVo> serviceOrderInfoVo = complaintInfoVo.getServiceOrderInfo();
        if (serviceOrderInfoVo != null && serviceOrderInfoVo.size() > 0) {
            // 先删除
            serviceOrderInfoMapper.delete(Wrappers.<ServiceOrderInfo>lambdaQuery().eq(ServiceOrderInfo::getComplaintInfoId, newInsertId));
            // 在新增
            serviceOrderInfoVo.forEach(c -> {
                ServiceOrderInfo serviceOrderInfo = new ServiceOrderInfo();
                serviceOrderInfo.setComplaintInfoId(newInsertId);
                serviceOrderInfo.setOrderId(c.getOrderId());
                serviceOrderInfo.setOutOrderNo(c.getOutOrderNo());
                serviceOrderInfo.setState(c.getState());
                serviceOrderInfos.add(serviceOrderInfo);
            });
            serviceOrderInfoMapper.insertBatch(serviceOrderInfos);
        }
    }


    /**
     * 批量删除投诉
     *
     * @param ids 需要删除的投诉主键
     * @return 结果
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids) {
        return baseMapper.deleteBatchIds(ids) > 0;
    }


    /********************************************ali***************************************************/


    /**
     * 查询投诉
     *
     * @param complaintId 投诉单号
     * @return 投诉
     */
    @Override

    public AliComplaintInfoVo queryAliById(String complaintId) {
        // 查询完毕之后需要将数据存储或者更新到数据库中
        AliComplaintInfoVo aliComplaintInfoVo = aliComplaintMapper.selectVoOne(Wrappers.<AliComplaint>lambdaQuery().eq(AliComplaint::getComplainEventId, complaintId));
        if (aliComplaintInfoVo != null) {
            // 查询其他数据
            LambdaQueryWrapper<AliComplaintReplyDetailInfos> eq = Wrappers.<AliComplaintReplyDetailInfos>lambdaQuery()
                .eq(AliComplaintReplyDetailInfos::getComplainEventId, complaintId)
                .orderByDesc(AliComplaintReplyDetailInfos::getId);
            List<AliComplaintReplyDetailInfosVo> aliComplaintReplyDetailInfosVos = aliComplaintReplyDetailInfosMapper.selectVoList(eq);
            aliComplaintInfoVo.setReplyDetailInfos(aliComplaintReplyDetailInfosVos);
        }
        return aliComplaintInfoVo;
    }


    /**
     * 查询投诉列表
     *
     * @param pageDto 投诉
     * @return 投诉
     */
    @Override
    public TableDataInfo<AliComplaintInfoVo> queryAliPageList(AliComplaintPageDto pageDto) {
        LambdaQueryWrapper<AliComplaint> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(pageDto.getComplainEventId()), AliComplaint::getComplainEventId, pageDto.getComplainEventId());
        lqw.eq(StringUtils.isNotBlank(pageDto.getStatus()), AliComplaint::getStatus, pageDto.getStatus());
        lqw.ge(StringUtils.isNotBlank(pageDto.getBeginTime()), AliComplaint::getCreateTime, DateUtils.getBeginOfDay(pageDto.getBeginTime()));
        lqw.le(StringUtils.isNotBlank(pageDto.getEndTime()), AliComplaint::getCreateTime, DateUtils.getEndOfDay(pageDto.getEndTime()));
        lqw.orderByDesc(AliComplaint::getId);

        Page<AliComplaintInfoVo> result = aliComplaintMapper.selectVoPage(pageDto.build(), lqw);

        return TableDataInfo.build(result);
    }

    /**
     * ali新增投诉
     *
     * @return 结果
     */
    @Override
    public Boolean insertAliComplaintBatch(String entitys) {

        List<AliComplaintInfoVo> aliComplaintInfoVos = JsonUtils.parseArray(entitys, AliComplaintInfoVo.class);

        if (aliComplaintInfoVos != null && !aliComplaintInfoVos.isEmpty()) {

            for (var currentData : aliComplaintInfoVos) {
                insertAliComplaint(currentData);
            }
        }
        return  true;

    }

    @Override
    public Boolean insertAliComplaint(AliComplaintInfoVo aliComplaintInfoVo) {
        // 对json串进行解析
        // 判断数据
        if (aliComplaintInfoVo != null && aliComplaintInfoVo.getTargetId() != null) {
            // 数据都存在 我们需要将数据存储到
            ComplaintLog complaintLog = new ComplaintLog();
            complaintLog.setType(3);
            complaintLog.setContent(JsonHelper.toJson(aliComplaintInfoVo));
            complaintLogMapper.insert(complaintLog);
            // 先判断是否存在过
            LambdaQueryWrapper<AliComplaint> eq = Wrappers.<AliComplaint>lambdaQuery().eq(AliComplaint::getTargetId, aliComplaintInfoVo.getTargetId());
            AliComplaintInfoVo selectAliComplaintInfoVo = aliComplaintMapper.selectVoOne(eq);
            int bool = 0;
            if (selectAliComplaintInfoVo != null && selectAliComplaintInfoVo.getId() != null) {
                // 执行更新操作
                AliComplaint aliComplaint = new AliComplaint();
                aliComplaint.setId(selectAliComplaintInfoVo.getId());
                aliComplaint.setStatus(aliComplaintInfoVo.getStatus());
                aliComplaint.setGmtModified(aliComplaintInfoVo.getGmtModified());
                aliComplaint.setGmtFinished(aliComplaintInfoVo.getGmtFinished());
                aliComplaint.setUpdateBy("system");
                aliComplaint.setUpdateTime(new Date());
                bool = aliComplaintMapper.updateById(aliComplaint);
            } else {
                // 执行添加操作
                AliComplaint aliComplaint = BeanCopyUtils.copy(aliComplaintInfoVo, AliComplaint.class);
                // 处理一下图片
                if (aliComplaintInfoVo.getImages().size() > 0) {
                    aliComplaint.setImagesString(String.join(",", aliComplaintInfoVo.getImages()));
                }
                aliComplaint.setCreateBy("system");
                aliComplaint.setCreateTime(new Date());
                bool = aliComplaintMapper.insert(aliComplaint);
            }
            // 数据添加成功之后我们需要将 reply_detail_infos 里面的数据进行一下更新操作
            if (bool > 0 && aliComplaintInfoVo.getReplyDetailInfos() != null && aliComplaintInfoVo.getReplyDetailInfos().size() > 0) {

                insertComplaintReplyDetailInfos(aliComplaintInfoVo.getComplainEventId(), aliComplaintInfoVo.getReplyDetailInfos());
                return true;
            }
        }

        return false;
    }


    /**
     * ali新增投诉
     *
     * @return 结果
     */
    @Override
    public Boolean insertAliComplaintStr(String entity) {
        // 对json串进行解析
        AliComplaintInfoVo aliComplaintInfoVo = JsonUtils.parseObject(entity, AliComplaintInfoVo.class);
        return insertAliComplaint(aliComplaintInfoVo);
    }

    /**
     * 用户与商家之间的协商记录
     */
    private void insertComplaintReplyDetailInfos(String complainEventId, List<AliComplaintReplyDetailInfosVo> complaintReplyDetailInfosVos) {
        // 首先执行删除操作
        var updateChainWrapper = new LambdaUpdateChainWrapper<>(aliComplaintReplyDetailInfosMapper)
            .eq(AliComplaintReplyDetailInfos::getComplainEventId, complainEventId)
            .set(AliComplaintReplyDetailInfos::getDelFlag, "2");
        boolean update = updateChainWrapper.update();
        try {
            // 执行批量添加操作
            if (complaintReplyDetailInfosVos.size() > 0) {
                List<AliComplaintReplyDetailInfos> list = new ArrayList<AliComplaintReplyDetailInfos>();
                for (var x : complaintReplyDetailInfosVos) {
                    // 数据批量添加
                    AliComplaintReplyDetailInfos select = new AliComplaintReplyDetailInfos();
                    select.setComplainEventId(complainEventId);
                    select.setReplierName(x.getReplierName());
                    select.setReplierRole(x.getReplierRole());
                    select.setGmtCreate(x.getGmtCreate());
                    select.setContent(x.getContent());
                    select.setImagesString(x.getImages().size() > 0 ? String.join(",", x.getImages()) : "");
                    select.setCreateBy("system");
                    select.setCreateTime(new Date());
                    list.add(select);
                }
                if (list.size() > 0) {
                    aliComplaintReplyDetailInfosMapper.insertBatch(list);
                }
            }
        } catch (Exception e) {
            log.error("批量添加用户与商家之间的协商记录出错:", e);
        }

    }

}
