package by.bsuir.service;

public interface ProfitabilityCalculatorService {
    boolean isMiningProfitable(Long miningInfoId, Long hardwareInfoId, String city);
}
