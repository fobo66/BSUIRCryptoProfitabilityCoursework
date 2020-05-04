package by.bsuir.repository;

import by.bsuir.domain.HardwareInfo;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the HardwareInfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HardwareInfoRepository extends JpaRepository<HardwareInfo, Long> {

}
