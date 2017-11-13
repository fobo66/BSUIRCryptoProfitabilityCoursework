package by.bsuir.web.rest;

import by.bsuir.repository.CryptocurrencyRepository;
import by.bsuir.repository.UserRepository;
import by.bsuir.security.AuthoritiesConstants;
import by.bsuir.service.ProfitabilityCalculatorService;
import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * REST controller for calculating profitability of the cryptocurrency mining.
 */
@RestController
@RequestMapping("/api")
public class ProfitabilityResource {
    private final Logger log = LoggerFactory.getLogger(ProfitabilityResource.class);

    private final ProfitabilityCalculatorService profitabilityCalculatorService;
    private final CryptocurrencyRepository cryptocurrencyRepository;
    private final UserRepository userRepository;

    public ProfitabilityResource(ProfitabilityCalculatorService profitabilityCalculatorService,
                                 CryptocurrencyRepository cryptocurrencyRepository, UserRepository userRepository) {
        this.profitabilityCalculatorService = profitabilityCalculatorService;
        this.cryptocurrencyRepository = cryptocurrencyRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/profitability")
    @Timed
    @Secured(AuthoritiesConstants.USER)
    public ResponseEntity<Boolean> calculateProfitability(Principal principal) {
        log.debug("Calculating profitability for user {}", principal.getName());
        userRepository.findOneByLogin(principal.getName());
        return new ResponseEntity<>(
            profitabilityCalculatorService.isMiningProfitable(cryptocurrencyRepository.getOne(1L)), HttpStatus.OK);
    }
}
