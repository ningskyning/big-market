package com.ning.infrastructure.persistent.repository;

import com.ning.domain.Strategy.model.entity.StrategyAwardEntity;
import com.ning.domain.Strategy.model.entity.StrategyEntity;
import com.ning.domain.Strategy.model.entity.StrategyRuleEntity;
import com.ning.domain.Strategy.model.valobj.StrategyAwardRuleModelVO;
import com.ning.domain.Strategy.repository.IStrategyRepository;
import com.ning.infrastructure.persistent.dao.IStrategyAwardDao;
import com.ning.infrastructure.persistent.dao.IStrategyDao;
import com.ning.infrastructure.persistent.dao.IStrategyRuleDao;
import com.ning.infrastructure.persistent.po.Strategy;
import com.ning.infrastructure.persistent.po.StrategyAward;
import com.ning.infrastructure.persistent.po.StrategyRule;
import com.ning.infrastructure.persistent.redis.IRedisService;
import com.ning.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
public class StrategyRepository implements IStrategyRepository {
    @Resource
    private IStrategyDao strategyDao;
    @Resource
    private IStrategyAwardDao strategyAwardDao;
    @Resource
    private IStrategyRuleDao strategyRuleDao;
    @Resource
    private IRedisService redisService;

    @Override
    public List<StrategyAwardEntity> queryStrategyAwardList(Long strategyId) {
        String cacheKey = Constants.RedisKey.STRATEGY_AWARD_LIST_KEY + strategyId;
        List<StrategyAwardEntity> strategyAwardEntities = redisService.getValue(cacheKey);
        if(strategyAwardEntities!=null&&!strategyAwardEntities.isEmpty()){
            return strategyAwardEntities;
        }
        List<StrategyAward> strategyAwards=strategyAwardDao.queryStrategyAwardListByStrategyId(strategyId);
        for(StrategyAward strategyAward:strategyAwards){
            StrategyAwardEntity strategyAwardEntity=StrategyAwardEntity.builder()
                    .awardId(strategyAward.getAwardId())
                    .awardCount(strategyAward.getAwardCount())
                    .awardCountSurplus(strategyAward.getAwardCountSurplus())
                    .awardRate(strategyAward.getAwardRate())
                    .awardSubtitle(strategyAward.getAwardSubtitle())
                    .awardTitle(strategyAward.getAwardTitle())
                    .ruleModels(strategyAward.getRuleModels())
                    .sort(strategyAward.getSort())
                    .strategyId(strategyAward.getStrategyId())
                    .build();
            strategyAwardEntities.add(strategyAwardEntity);
        }
        redisService.setValue(cacheKey,strategyAwardEntities);
        return strategyAwardEntities;
    }
    @Override
    public void storeStrategyAwardSearchRateTable(String key, Integer rateRange, Map<Integer, Integer> strategyAwardSearchRateTable) {
        // 1. 存储抽奖策略范围值，如10000，用于生成1000以内的随机数
        redisService.setValue(Constants.RedisKey.STRATEGY_RATE_RANGE_KEY + key, rateRange);
        // 2. 存储概率查找表
        Map<Integer, Integer> cacheRateTable = redisService.getMap(Constants.RedisKey.STRATEGY_RATE_TABLE_KEY + key);
        cacheRateTable.putAll(strategyAwardSearchRateTable);
    }
    @Override
    public Integer getStrategyAwardAssemble(String key, Integer rateKey) {
        return redisService.getFromMap(Constants.RedisKey.STRATEGY_RATE_TABLE_KEY + key, rateKey);
    }
    @Override
    public int getRateRange(Long strategyId) {
        return getRateRange(String.valueOf(strategyId));
    }

    @Override
    public int getRateRange(String key) {
        return redisService.getValue(Constants.RedisKey.STRATEGY_RATE_RANGE_KEY + key);
    }
    @Override
    public StrategyEntity queryStrategyEntityByStrategyId(Long strategyId) {
        String cacheKey= Constants.RedisKey.STRATEGY_KEY+strategyId;
        StrategyEntity strategyEntity = redisService.getValue(cacheKey);
        if(strategyEntity!=null){
            return strategyEntity;
        }
        Strategy strategy =strategyDao.queryStrategyByStrategyId(strategyId);
        if(strategy==null){
            return StrategyEntity.builder().build();
        }
        strategyEntity=StrategyEntity.builder()
                .strategyId(strategy.getStrategyId())
                .strategyDesc(strategy.getStrategyDesc())
                .ruleModels(strategy.getRuleModels())
                .build();
        redisService.setValue(cacheKey,strategyEntity);
        return strategyEntity;
    }

    @Override
    public StrategyRuleEntity queryStrategyRule(Long strategyId, String ruleModel) {
        StrategyRule strategyRuleReq = new StrategyRule();
        strategyRuleReq.setStrategyId(strategyId);
        strategyRuleReq.setRuleModel(ruleModel);
        StrategyRule strategyRuleRes = strategyRuleDao.queryStrategyRule(strategyRuleReq);
        return StrategyRuleEntity.builder()
                .strategyId(strategyRuleRes.getStrategyId())
                .awardId(strategyRuleRes.getAwardId())
                .ruleType(strategyRuleRes.getRuleType())
                .ruleModel(strategyRuleRes.getRuleModel())
                .ruleValue(strategyRuleRes.getRuleValue())
                .ruleDesc(strategyRuleRes.getRuleDesc())
                .build();
    }
    @Override
    public String queryStrategyRuleValue(Long strategyId, Integer awardId, String ruleModel) {
        StrategyRule strategyRule = new StrategyRule();
        strategyRule.setStrategyId(strategyId);
        strategyRule.setAwardId(awardId);
        strategyRule.setRuleModel(ruleModel);
        return strategyRuleDao.queryStrategyRuleValue(strategyRule);
    }

    @Override
    public StrategyAwardRuleModelVO queryStrategyAwardRuleModel(Long strategyId, Integer awardId) {
        StrategyAwardRuleModelVO strategyAwardRuleModelVO = strategyRuleDao.queryStrategyAwardRuleModel(strategyId,awardId);
        return strategyAwardRuleModelVO;
    }
}
