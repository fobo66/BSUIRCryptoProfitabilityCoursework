package by.bsuir.service;

import by.bsuir.domain.Cryptocurrency;

public interface ProfitabilityCalculatorService {
    boolean isMiningProfitable(Cryptocurrency cryptocurrency);
}
