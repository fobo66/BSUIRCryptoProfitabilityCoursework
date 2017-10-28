package by.bsuir.service.impl;

import by.bsuir.domain.Cryptocurrency;
import by.bsuir.service.ProfitabilityCalculatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProfitabilityCalculatorServiceImpl implements ProfitabilityCalculatorService {

    private final Logger log = LoggerFactory.getLogger(ProfitabilityCalculatorServiceImpl.class);

    @Override
    public boolean isMiningProfitable(Cryptocurrency cryptocurrency) {
        return false;
    }
}
