package com.euler.sdk.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.euler.sdk.domain.entity.CashOutRule;
import com.euler.sdk.domain.vo.CashOutRuleVo;

import java.util.List;

/**
 * 提现规则Service接口
 *
 * @author euler
 * @date 2022-05-26
 */
public interface ICashOutRuleService extends IService<CashOutRule> {

    /**
     * 查询提现规则
     *
     * @param id 提现规则主键
     * @return 提现规则
     */
    CashOutRuleVo queryById(Integer id);

    /**
     * 查询提现规则
     * @param ruleType
     * @param RuleStatus
     * @return
     */
    List<CashOutRuleVo> getCashOutList(Integer ruleType, String RuleStatus);

}
