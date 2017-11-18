package by.bsuir.repository;

import by.bsuir.domain.PowerCost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Spring Data JPA repository for the PowerCost entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PowerCostRepository extends JpaRepository<PowerCost, Long> {
    Optional<PowerCost> findOneByCity(String city);
}
