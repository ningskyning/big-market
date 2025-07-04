package com.ning.infrastructure.persistent.dao;

import com.ning.domain.Strategy.model.valobj.StrategyAwardRuleModelVO;
import com.ning.infrastructure.persistent.po.StrategyRule;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IStrategyRuleDao {

    List<StrategyRule> queryStrategyRuleList();

    StrategyRule queryStrategyRule(StrategyRule strategyRuleReq);

    String queryStrategyRuleValue(StrategyRule strategyRule);

    StrategyAwardRuleModelVO queryStrategyAwardRuleModel(Long strategyId, Integer awardId);
}