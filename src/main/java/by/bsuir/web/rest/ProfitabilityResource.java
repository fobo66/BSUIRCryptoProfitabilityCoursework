package by.bsuir.web.rest;

import by.bsuir.domain.ProfitabilityAnalysis;
import by.bsuir.repository.MiningInfoRepository;
import by.bsuir.repository.ProfitabilityAnalysisRepository;
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

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;

/**
 * REST controller for calculating profitability of the cryptocurrency mining.
 */
@SuppressWarnings("unused")
@RestController
@RequestMapping("/api")
public class ProfitabilityResource {
    private final Logger log = LoggerFactory.getLogger(ProfitabilityResource.class);

    private final ProfitabilityCalculatorService profitabilityCalculatorService;
    private final UserRepository userRepository;
    private final ProfitabilityAnalysisRepository profitabilityAnalysisRepository;

    public ProfitabilityResource(ProfitabilityCalculatorService profitabilityCalculatorService,
                                 MiningInfoRepository miningInfoRepository, UserRepository userRepository,
                                 ProfitabilityAnalysisRepository profitabilityAnalysisRepository) {
        this.profitabilityCalculatorService = profitabilityCalculatorService;
        this.userRepository = userRepository;
        this.profitabilityAnalysisRepository = profitabilityAnalysisRepository;
    }

    @GetMapping("/profitability")
    @Timed
    @Secured(AuthoritiesConstants.USER)
    public ResponseEntity<Boolean> calculateProfitability(HttpServletRequest req) {
        String username = req.getUserPrincipal().getName();
        long miningInfoId = Long.parseLong(req.getParameter("cryptoCurrencyMiningInfo"));
        long hardwareInfoId = Long.parseLong(req.getParameter("hardware"));
        String city = req.getParameter("city");

        log.debug("Calculating profitability for user {}", username);
        boolean miningProfitable = profitabilityCalculatorService.isMiningProfitable(miningInfoId, hardwareInfoId, city);
        saveProfitabilityAnalysisResult(username, miningProfitable);

        return new ResponseEntity<>(miningProfitable, HttpStatus.OK);
    }

    private void saveProfitabilityAnalysisResult(String username, Boolean miningProfitable) {
        userRepository.findOneByLogin(username).ifPresent((user -> {
            ProfitabilityAnalysis profitabilityAnalysis = new ProfitabilityAnalysis();
            profitabilityAnalysis
                .date(LocalDate.now())
                .user(user);
            profitabilityAnalysis.setResult(miningProfitable);
            profitabilityAnalysisRepository.save(profitabilityAnalysis);
        }));
    }
}
