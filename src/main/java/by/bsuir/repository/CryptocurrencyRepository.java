package by.bsuir.repository;

import by.bsuir.domain.Cryptocurrency;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Cryptocurrency entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CryptocurrencyRepository extends JpaRepository<Cryptocurrency, Long> {
}
