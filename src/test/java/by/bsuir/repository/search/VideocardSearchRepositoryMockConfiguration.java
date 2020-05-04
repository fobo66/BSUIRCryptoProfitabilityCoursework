package by.bsuir.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link VideocardSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class VideocardSearchRepositoryMockConfiguration {

    @MockBean
    private VideocardSearchRepository mockVideocardSearchRepository;

}
