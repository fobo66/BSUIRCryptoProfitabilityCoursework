package by.bsuir.repository.search;

import by.bsuir.domain.MiningInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link MiningInfo} entity.
 */
public interface MiningInfoSearchRepository extends ElasticsearchRepository<MiningInfo, Long> {
}
