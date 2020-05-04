package by.bsuir.repository;

import by.bsuir.domain.Videocard;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Videocard entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VideocardRepository extends JpaRepository<Videocard, Long> {
}
