package by.bsuir.repository;

import by.bsuir.domain.HardwareInfo;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the HardwareInfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HardwareInfoRepository extends JpaRepository<HardwareInfo, Long> {
}
