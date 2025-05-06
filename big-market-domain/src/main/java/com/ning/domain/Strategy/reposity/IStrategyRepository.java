package com.ning.domain.Strategy.reposity;

import com.ning.domain.Strategy.model.eneity.StrategyAwardEntity;
import com.ning.domain.Strategy.model.eneity.StrategyEntity;
import com.ning.domain.Strategy.model.eneity.StrategyRuleEntity;

import java.util.Date;
import java.util.List;

public interface IStrategyRepository {
    List<StrategyAwardEntity> queryStrategyAwardList(Long strategyId);

    Long queryStrategyIdByActivityId(Long activityId);

    StrategyEntity queryStrategyEntityByStrategyId(Long strategyId);

    StrategyRuleEntity queryStrategyRule(Long strategyId, String ruleWeight);

    void cacheStrategyAwardCount(String cacheKey, Integer awardCount);

    Boolean subtractionAwardStock(String cacheKey, Date endDateTime);
}
