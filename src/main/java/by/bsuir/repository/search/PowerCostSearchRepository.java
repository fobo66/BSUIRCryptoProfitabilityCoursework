package by.bsuir.repository.search;

import by.bsuir.domain.PowerCost;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link PowerCost} entity.
 */
public interface PowerCostSearchRepository extends ElasticsearchRepository<PowerCost, Long> {
}
