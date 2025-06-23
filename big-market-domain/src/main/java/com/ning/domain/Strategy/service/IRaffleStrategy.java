package com.ning.domain.Strategy.service;

import com.ning.domain.Strategy.model.entity.RaffleAwardEntity;
import com.ning.domain.Strategy.model.entity.RaffleFactorEntity;

public interface IRaffleStrategy {
    RaffleAwardEntity performRaffle(RaffleFactorEntity raffleFactorEntity);
}
