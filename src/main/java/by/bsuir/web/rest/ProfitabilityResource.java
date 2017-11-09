package by.bsuir.web.rest;

import by.bsuir.service.ProfitabilityCalculatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for calculating profitability of the cryptocurrency mining.
 */
@RestController
@RequestMapping("/api")
public class ProfitabilityResource {
    private final Logger log = LoggerFactory.getLogger(ProfitabilityResource.class);

    private final ProfitabilityCalculatorService profitabilityCalculatorService;

    public ProfitabilityResource(ProfitabilityCalculatorService profitabilityCalculatorService) {
        this.profitabilityCalculatorService = profitabilityCalculatorService;
    }
}
