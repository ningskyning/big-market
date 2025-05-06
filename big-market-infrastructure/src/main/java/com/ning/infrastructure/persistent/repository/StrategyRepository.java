package com.ning.infrastructure.persistent.repository;

import com.ning.domain.Strategy.model.eneity.StrategyAwardEntity;
import com.ning.domain.Strategy.model.eneity.StrategyEntity;
import com.ning.domain.Strategy.model.eneity.StrategyRuleEntity;
import com.ning.domain.Strategy.reposity.IStrategyRepository;
import com.ning.infrastructure.persistent.dao.IStrategyAwardDao;
import com.ning.infrastructure.persistent.dao.IStrategyDao;
import com.ning.infrastructure.persistent.po.Strategy;
import com.ning.infrastructure.persistent.po.StrategyAward;
import com.ning.infrastructure.redis.IRedisService;
import com.ning.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
@Slf4j
@Repository
public class StrategyRepository implements IStrategyRepository {
    @Resource
    private IStrategyDao strategyDao;
    @Resource
    private IStrategyAwardDao strategyAwardDao;
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
    public Long queryStrategyIdByActivityId(Long activityId) {
        return null;
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
    public StrategyRuleEntity queryStrategyRule(Long strategyId, String ruleWeight) {
        return null;
    }

    @Override
    public void cacheStrategyAwardCount(String cacheKey, Integer awardCount) {

    }

    @Override
    public Boolean subtractionAwardStock(String cacheKey, Date endDateTime) {
        return null;
    }
}
