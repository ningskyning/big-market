package com.ning.domain.Strategy.service.rule.impl;

import com.ning.domain.Strategy.model.entity.RuleActionEntity;
import com.ning.domain.Strategy.model.entity.RuleMatterEntity;
import com.ning.domain.Strategy.model.valobj.RuleLogicCheckTypeVO;
import com.ning.domain.Strategy.repository.IStrategyRepository;
import com.ning.domain.Strategy.service.annotation.LogicStrategy;
import com.ning.domain.Strategy.service.rule.ILogicFilter;
import com.ning.domain.Strategy.service.rule.factory.DefaultLogicFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
@Slf4j
@Component
@LogicStrategy(logicMode = DefaultLogicFactory.LogicModel.RULE_LOCK)
public class RuleLockLogicFilter implements ILogicFilter<RuleActionEntity.RaffleCenterEntity> {
    @Resource
    private IStrategyRepository repository;
    private final Long userCount = 0L;
    @Override
    public RuleActionEntity filter(RuleMatterEntity ruleMatterEntity) {
        String ruleValue = repository.queryStrategyRuleValue(ruleMatterEntity.getStrategyId(),ruleMatterEntity.getAwardId(), ruleMatterEntity.getRuleModel());
        Long count = Long.parseLong(ruleValue);
        if(userCount>=count){
           return RuleActionEntity.<RuleActionEntity.RaffleAfterEntity>builder()
                   .code(RuleLogicCheckTypeVO.ALLOW.getCode())
                   .info(RuleLogicCheckTypeVO.ALLOW.getInfo())
                   .build();
        }
        return RuleActionEntity.<RuleActionEntity.RaffleCenterEntity>builder()
                .code(RuleLogicCheckTypeVO.TAKE_OVER.getCode())
                .info(RuleLogicCheckTypeVO.TAKE_OVER.getInfo())
                .build();
    }
}
