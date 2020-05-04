package by.bsuir.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link CryptocurrencySearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class CryptocurrencySearchRepositoryMockConfiguration {

    @MockBean
    private CryptocurrencySearchRepository mockCryptocurrencySearchRepository;

}
