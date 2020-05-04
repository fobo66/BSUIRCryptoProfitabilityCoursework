package by.bsuir.repository.search;

import by.bsuir.domain.Cryptocurrency;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Cryptocurrency} entity.
 */
public interface CryptocurrencySearchRepository extends ElasticsearchRepository<Cryptocurrency, Long> {
}
