package by.bsuir.service.impl;

import by.bsuir.domain.MiningInfo;
import by.bsuir.service.ProfitabilityCalculatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProfitabilityCalculatorServiceImpl implements ProfitabilityCalculatorService {

    private final Logger log = LoggerFactory.getLogger(ProfitabilityCalculatorServiceImpl.class);

    /*
        hashTime = ((float) difficulty) * (Math.pow(2.0, 32) / (hashRate * 1000.0));
        powerCostPerYear = 365.25 * 24.0 * powerConsumption / 1000.0 * electricityRate;
        totalCost = powerCostPerYear + hardwareCost;
        blocksPerYear =  (365.25 * 24.0 * 3600.0) / hashTime;
        coinsPerYear = reward * blocksPerYear;
        revenuePerYear = coinsPerYear * price;
        profitPerYear = revenuePerYear - powerCostPerYear;
        return revenuePerYear - totalCost > 0;
    */
    @Override
    public boolean isMiningProfitable(MiningInfo miningInfo) {
        return false;
    }
}
