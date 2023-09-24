package com.euler.community.mq;

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.euler.common.core.domain.dto.IdNameTypeDicDto;
import com.euler.common.core.utils.JsonUtils;
import com.euler.community.config.CommonCommunityConfig;
import com.euler.community.config.RabbitMqConfig;
import com.euler.community.domain.dto.IdTypeCommunityDto;
import com.euler.community.domain.entity.Dynamic;
import com.euler.community.domain.entity.DynamicOperationErrorLog;
import com.euler.community.enums.DynamicFieldIncrEnum;
import com.euler.community.esMapper.DynamicElasticsearch;
import com.euler.community.mapper.DynamicMapper;
import com.euler.community.mapper.DynamicOperationErrorLogMapper;
import com.euler.community.service.IDynamicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
@Slf4j
public class RabbitConsumer {
    @Autowired
    private RabbitMqConfig rabbitMqConfig;
    @Autowired
    private DynamicElasticsearch dynamicElasticsearch;
    @Autowired
    private DynamicMapper dynamicMapper;
    @Autowired
    private DynamicOperationErrorLogMapper dynamicOperationErrorLogMapper;
    @Autowired
    private CommonCommunityConfig commonCommunityConfig;
    @Autowired
    private IDynamicService iDynamicService;

    private String getMsgId(Message message) {
        String msgId = null;
        if (message == null || message.getMessageProperties() == null
            || message.getMessageProperties().getHeaders() == null
            || message.getMessageProperties().getHeaders().get("spring_returned_message_correlation") == null) {
            msgId = "未知msgId";
        } else {
            msgId = message.getMessageProperties().getHeaders().get("spring_returned_message_correlation").toString();
        }

        return msgId;
    }

    /**
     * 监听消息队列中的数据 获取到数据之后将数据存储到ElasticSearch中
     *
     * @param msg      我自己填入的动态数据 也可以是我们的动态ID
     * @param messages 里面存储我们对当前消息的标识字段
     */
    @RabbitListener(queues = "${webconfig.rabbitmq.addDynamicIntoEsQueue}")
    public void addDynamicIntoElasticSearch(String msg, Message messages) {
        // 自定义排查的msgId
        String msgId = getMsgId(messages);
        log.info("简单消费[数据入ES]，msgid：{}，queue：{}: msg：{}", msgId, rabbitMqConfig.getAddDynamicIntoEsQueue(), msg);
        IdTypeCommunityDto idTypeCommunityDto = JsonUtils.parseObject(msg, IdTypeCommunityDto.class);
        if (idTypeCommunityDto != null) {
            Long needInsertId = Convert.toLong(idTypeCommunityDto.getId());
            // 获取到我们想要的东西之后 开始调用查询数据存储到es的操作
            try {
                Boolean aBoolean = dynamicElasticsearch.insertDataToElasticSearch(needInsertId);
                int isOpenAudit = commonCommunityConfig.getIsOpenAudit();
                if (isOpenAudit == 1 && aBoolean) {
                    IdNameTypeDicDto<Long> objectIdNameTypeDicDto = new IdNameTypeDicDto<>();
                    objectIdNameTypeDicDto.setId(needInsertId);
                    objectIdNameTypeDicDto.setType(1);
                    iDynamicService.auditDynamic(objectIdNameTypeDicDto);
                }
            } catch (Exception e) {
                //  出现异常的时候
                DynamicOperationErrorLog dynamicOperationErrorLog = new DynamicOperationErrorLog();
                dynamicOperationErrorLog.setDynamicId(needInsertId);
                dynamicOperationErrorLog.setOperationType("A-0");
                dynamicOperationErrorLog.setErrorContent(JsonUtils.toJsonString(e));
                dynamicOperationErrorLogMapper.insert(dynamicOperationErrorLog);
            }
        } else {
            log.info("消费失败！");
        }
    }


    /**
     * 监听消息队列中的数据 获取到数据之后将数据修改到ElasticSearch中
     *
     * @param msg      我自己填入的动态数据 也可以是我们的动态ID
     * @param messages 里面存储我们对当前消息的标识字段
     */
    @RabbitListener(queues = "${webconfig.rabbitmq.operationDynamicEsQueue}")
    public void operationDynamicIntoElasticSearch(String msg, Message messages) {
        // 自定义排查的msgId
        String msgId = getMsgId(messages);
        log.info("简单消费[修改数据]，msgid：{}，queue：{}: msg：{}", msgId, rabbitMqConfig.getOperationDynamicEsQueue(), msg);
        IdTypeCommunityDto idTypeCommunityDto = JsonUtils.parseObject(msg, IdTypeCommunityDto.class);
        if (idTypeCommunityDto != null) {
            Long id = Convert.toLong(idTypeCommunityDto.getId());
            Integer type = Convert.toInt(idTypeCommunityDto.getType());
            // 根据类型状态 我们判断当前用户执行的是什么操作
            try {
                // 首先根据修改的字段更新数据库的数据
                LambdaQueryWrapper<Dynamic> eq = Wrappers.<Dynamic>lambdaQuery().eq(Dynamic::getId, id);
                Dynamic dynamic = dynamicMapper.selectOne(eq);
                if (dynamic != null) {
                    HashMap<String, Object> map = new HashMap<>();
                    int incr = 0;
                    if (type.equals(DynamicFieldIncrEnum.FORWARD.getCode())) {
                        dynamic.setForwardNum(dynamic.getForwardNum() + 1);
                        incr = dynamic.getVirtualForwardNum() + 1;
                        dynamic.setVirtualForwardNum(incr);
                        map.put("forwardNum", incr);
                    } else if (type.equals(DynamicFieldIncrEnum.FAV.getCode())) {
                        dynamic.setPraiseNum(dynamic.getPraiseNum() + 1);
                        incr = dynamic.getVirtualPraiseNum() + 1;
                        dynamic.setVirtualPraiseNum(incr);
                        map.put("praiseNum", incr);
                    } else if (type.equals(DynamicFieldIncrEnum.CANCEL_FAV.getCode()) && dynamic.getPraiseNum() > 0) {
                        dynamic.setPraiseNum(dynamic.getPraiseNum() - 1);
                        incr = dynamic.getVirtualPraiseNum() - 1;
                        dynamic.setVirtualPraiseNum(incr);
                        map.put("praiseNum", incr);
                    } else if (type.equals(DynamicFieldIncrEnum.COMMENT.getCode())) {
                        dynamic.setCommentNum(dynamic.getCommentNum() + 1);
                        incr = dynamic.getVirtualCommentNum() + 1;
                        dynamic.setVirtualCommentNum(incr);
                        map.put("commentNum", incr);
                    } else if (type.equals(DynamicFieldIncrEnum.CANCEL_COMMENT.getCode()) && dynamic.getCommentNum() > 0) {
                        dynamic.setCommentNum(dynamic.getCommentNum() - 1);
                        incr = dynamic.getVirtualCommentNum() - 1;
                        dynamic.setVirtualCommentNum(incr);
                        map.put("commentNum", incr);
                    } else if (type.equals(DynamicFieldIncrEnum.COLLECTION.getCode())) {
                        dynamic.setCollectNum(dynamic.getCollectNum() + 1);
                        incr = dynamic.getVirtualCollectNum() + 1;
                        dynamic.setVirtualCollectNum(incr);
                        map.put("collectNum", incr);
                    } else if (type.equals(DynamicFieldIncrEnum.CANCEL_COLLECTION.getCode()) && dynamic.getCollectNum() > 0) {
                        dynamic.setCollectNum(dynamic.getCollectNum() - 1);
                        incr = dynamic.getVirtualCollectNum() - 1;
                        dynamic.setVirtualCollectNum(incr);
                        map.put("collectNum", incr);
                    } else if (type.equals(DynamicFieldIncrEnum.CLICK.getCode())) {
                        dynamic.setHitNum(dynamic.getHitNum() + 1);
                        incr = dynamic.getVirtualHitNum() + 1;
                        dynamic.setVirtualHitNum(incr);
                        map.put("hitNum", incr);
                    } else if (type.equals(DynamicFieldIncrEnum.REPORT.getCode())) {
                        incr = dynamic.getReportNum() + 1;
                        dynamic.setReportNum(incr);
                        map.put("reportNum", incr);
                    }
                    // 更新数据库
                    dynamicMapper.updateById(dynamic);
                    // 更新es
                    dynamicElasticsearch.updateAppointDocument(id, map);
                }
                // 在更新es的数据
            } catch (Exception e) {
                //  出现异常的时候
                DynamicOperationErrorLog dynamicOperationErrorLog = new DynamicOperationErrorLog();
                dynamicOperationErrorLog.setDynamicId(id);
                dynamicOperationErrorLog.setOperationType("C-" + type);
                dynamicOperationErrorLog.setErrorContent(JsonUtils.toJsonString(e));
                dynamicOperationErrorLogMapper.insert(dynamicOperationErrorLog);
            }
        } else {
            log.info("动态数据修改消费失败！");
        }
    }

}
