package by.bsuir.repository;

import by.bsuir.domain.PowerCost;

import java.util.Optional;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the PowerCost entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PowerCostRepository extends JpaRepository<PowerCost, Long> {
    Optional<PowerCost> findOneByCity(String city);

    PowerCost getByCity(String city);
}
