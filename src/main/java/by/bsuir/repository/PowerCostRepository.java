package by.bsuir.repository;

import by.bsuir.domain.PowerCost;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the PowerCost entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PowerCostRepository extends JpaRepository<PowerCost, Long> {
}
