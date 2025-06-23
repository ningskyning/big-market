package com.ning.domain.Strategy.service.rule;

import com.ning.domain.Strategy.model.entity.RuleActionEntity;
import com.ning.domain.Strategy.model.entity.RuleMatterEntity;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 抽奖规则过滤接口
 * @create 2024-01-06 09:55
 */
public interface ILogicFilter<T extends RuleActionEntity.RaffleEntity> {

    RuleActionEntity<T> filter(RuleMatterEntity ruleMatterEntity);

}
