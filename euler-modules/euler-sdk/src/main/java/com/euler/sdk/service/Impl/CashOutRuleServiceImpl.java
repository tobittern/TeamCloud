package com.euler.sdk.service.Impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.euler.sdk.domain.entity.CashOutRule;
import com.euler.sdk.domain.vo.CashOutRuleVo;
import com.euler.sdk.mapper.CashOutRuleMapper;
import com.euler.sdk.service.ICashOutRuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 提现规则Service业务层处理
 *
 * @author euler
 * @date 2022-05-26
 */
@RequiredArgsConstructor
@Service
public class CashOutRuleServiceImpl extends ServiceImpl<CashOutRuleMapper, CashOutRule> implements ICashOutRuleService {
    @Autowired
    private CashOutRuleMapper baseMapper;

    /**
     * 查询提现规则
     *
     * @param id 提现规则主键
     * @return 提现规则
     */
    @Override
    public CashOutRuleVo queryById(Integer id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询提现规则
     * @param ruleType
     * @param RuleStatus
     * @return
     */
    public List<CashOutRuleVo> getCashOutList(Integer ruleType, String RuleStatus) {
        return baseMapper.selectVoList(Wrappers.<CashOutRule>lambdaQuery()
            .eq(CashOutRule::getRuleType, ruleType)
            .eq(CashOutRule::getRuleStatus, RuleStatus));
    }
}
