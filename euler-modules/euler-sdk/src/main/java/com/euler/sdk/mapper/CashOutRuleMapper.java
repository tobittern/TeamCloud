package com.euler.sdk.mapper;


import com.euler.common.mybatis.core.mapper.BaseMapperPlus;
import com.euler.sdk.domain.entity.CashOutRule;
import com.euler.sdk.domain.vo.CashOutRuleVo;
import org.apache.ibatis.annotations.Mapper;


/**
 * 提现规则Mapper接口
 *
 * @author euler
 * @date 2022-05-26
 */
@Mapper
public interface CashOutRuleMapper extends BaseMapperPlus<CashOutRuleMapper, CashOutRule, CashOutRuleVo> {

}
