package by.bsuir.repository;

import by.bsuir.domain.ProfitabilityAnalysis;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the ProfitabilityAnalysis entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProfitabilityAnalysisRepository extends JpaRepository<ProfitabilityAnalysis, Long> {

    @Query("select profitabilityAnalysis from ProfitabilityAnalysis profitabilityAnalysis where profitabilityAnalysis.user.login = ?#{principal.username}")
    List<ProfitabilityAnalysis> findByUserIsCurrentUser();
}
