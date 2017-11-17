package by.bsuir.repository;

import by.bsuir.domain.PowerCost;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the PowerCost entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PowerCostRepository extends JpaRepository<PowerCost, Long> {

}
