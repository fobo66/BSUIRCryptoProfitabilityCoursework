package by.bsuir.repository.search;

import by.bsuir.domain.HardwareInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link HardwareInfo} entity.
 */
public interface HardwareInfoSearchRepository extends ElasticsearchRepository<HardwareInfo, Long> {
}
