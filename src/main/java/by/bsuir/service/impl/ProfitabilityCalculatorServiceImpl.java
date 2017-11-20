package by.bsuir.service.impl;

import by.bsuir.domain.HardwareInfo;
import by.bsuir.domain.MiningInfo;
import by.bsuir.domain.PowerCost;
import by.bsuir.repository.HardwareInfoRepository;
import by.bsuir.repository.MiningInfoRepository;
import by.bsuir.repository.PowerCostRepository;
import by.bsuir.service.ProfitabilityCalculatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProfitabilityCalculatorServiceImpl implements ProfitabilityCalculatorService {

    private final Logger log = LoggerFactory.getLogger(ProfitabilityCalculatorServiceImpl.class);

    private final MiningInfoRepository miningInfoRepository;
    private final HardwareInfoRepository hardwareInfoRepository;
    private final PowerCostRepository powerCostRepository;

    public ProfitabilityCalculatorServiceImpl(MiningInfoRepository miningInfoRepository, HardwareInfoRepository hardwareInfoRepository, PowerCostRepository powerCostRepository) {
        this.miningInfoRepository = miningInfoRepository;
        this.hardwareInfoRepository = hardwareInfoRepository;
        this.powerCostRepository = powerCostRepository;
    }

    @Override
    public boolean isMiningProfitable(Long miningInfoId, Long hardwareInfoId, String city) {
        MiningInfo miningInfo = miningInfoRepository.getOne(miningInfoId);
        HardwareInfo hardwareInfo = hardwareInfoRepository.getOne(hardwareInfoId);
        PowerCost powerCost = powerCostRepository.getByCity(city);

        double hashTime = (miningInfo.getDifficulty()) * (Math.pow(2.0, 32) / (hardwareInfo.getHashPower() * 1000.0));
        double powerCostPerYear = 365.25 * 24.0 * hardwareInfo.getVideocard().getPower() /
            1000.0 * powerCost.getPricePerKilowatt();
        double totalCost = powerCostPerYear + hardwareInfo.getPrice();
        double blocksPerYear = (365.25 * 24.0 * 3600.0) / hashTime;
        double coinsPerYear = miningInfo.getBlockReward() * blocksPerYear;
        double revenuePerYear = coinsPerYear * miningInfo.getCryptocurrency().getPrice();

        log.debug("Revenue per year: {}", revenuePerYear);

        return revenuePerYear - totalCost > 0;
    }
}
