package by.bsuir.repository.search;

import by.bsuir.domain.Videocard;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Videocard} entity.
 */
public interface VideocardSearchRepository extends ElasticsearchRepository<Videocard, Long> {
}
