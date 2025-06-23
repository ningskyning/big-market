package com.ning.domain.Strategy.repository;

import com.ning.domain.Strategy.model.entity.StrategyAwardEntity;
import com.ning.domain.Strategy.model.entity.StrategyEntity;
import com.ning.domain.Strategy.model.entity.StrategyRuleEntity;
import com.ning.domain.Strategy.model.valobj.StrategyAwardRuleModelVO;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IStrategyRepository {
    List<StrategyAwardEntity> queryStrategyAwardList(Long strategyId);
    void storeStrategyAwardSearchRateTable(String key, Integer rateRange, Map<Integer, Integer> strategyAwardSearchRateTable);

    int getRateRange(Long strategyId);
    int getRateRange(String key);
    Integer getStrategyAwardAssemble(String key, Integer rateKey);

    StrategyEntity queryStrategyEntityByStrategyId(Long strategyId);

    StrategyRuleEntity queryStrategyRule(Long strategyId, String ruleWeight);

    String queryStrategyRuleValue(Long strategyId, Integer awardId, String ruleModel);

    StrategyAwardRuleModelVO queryStrategyAwardRuleModel(Long strategyId, Integer awardId);
}
