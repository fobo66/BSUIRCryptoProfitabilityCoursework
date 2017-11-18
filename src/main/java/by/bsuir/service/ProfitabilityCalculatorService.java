package by.bsuir.service;

import by.bsuir.domain.MiningInfo;

public interface ProfitabilityCalculatorService {
    boolean isMiningProfitable(MiningInfo miningInfo);
}
