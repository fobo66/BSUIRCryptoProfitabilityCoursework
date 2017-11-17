package by.bsuir.repository;

import by.bsuir.domain.MiningInfo;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the MiningInfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MiningInfoRepository extends JpaRepository<MiningInfo, Long> {

}
