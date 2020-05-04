package by.bsuir.repository;

import by.bsuir.domain.MiningInfo;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the MiningInfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MiningInfoRepository extends JpaRepository<MiningInfo, Long> {
}
