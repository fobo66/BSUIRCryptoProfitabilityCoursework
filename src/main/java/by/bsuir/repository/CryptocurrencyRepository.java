package by.bsuir.repository;

import by.bsuir.domain.Cryptocurrency;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Cryptocurrency entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CryptocurrencyRepository extends JpaRepository<Cryptocurrency, Long> {

}
