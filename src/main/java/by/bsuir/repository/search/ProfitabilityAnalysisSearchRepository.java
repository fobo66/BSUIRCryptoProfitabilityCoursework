package by.bsuir.repository.search;

import by.bsuir.domain.ProfitabilityAnalysis;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link ProfitabilityAnalysis} entity.
 */
public interface ProfitabilityAnalysisSearchRepository extends ElasticsearchRepository<ProfitabilityAnalysis, Long> {
}
