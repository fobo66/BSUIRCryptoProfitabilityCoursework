package by.bsuir.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link ProfitabilityAnalysisSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class ProfitabilityAnalysisSearchRepositoryMockConfiguration {

    @MockBean
    private ProfitabilityAnalysisSearchRepository mockProfitabilityAnalysisSearchRepository;

}
