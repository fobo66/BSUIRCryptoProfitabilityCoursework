package by.bsuir.repository.search;

import by.bsuir.domain.Cryptocurrency;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Cryptocurrency entity.
 */
public interface CryptocurrencySearchRepository extends ElasticsearchRepository<Cryptocurrency, Long> {
}
