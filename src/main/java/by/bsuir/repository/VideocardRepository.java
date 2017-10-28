package by.bsuir.repository;

import by.bsuir.domain.Videocard;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Videocard entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VideocardRepository extends JpaRepository<Videocard, Long> {

}
