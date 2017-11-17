package by.bsuir.repository;

import by.bsuir.domain.ProfitabilityAnalysis;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ProfitabilityAnalysis entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProfitabilityAnalysisRepository extends JpaRepository<ProfitabilityAnalysis, Long> {

    @Query("select profitability_analysis from ProfitabilityAnalysis profitability_analysis where profitability_analysis.user.login = ?#{principal.username}")
    List<ProfitabilityAnalysis> findByUserIsCurrentUser();

}
