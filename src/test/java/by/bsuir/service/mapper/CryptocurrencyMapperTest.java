package by.bsuir.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class CryptocurrencyMapperTest {

    private CryptocurrencyMapper cryptocurrencyMapper;

    @BeforeEach
    public void setUp() {
        cryptocurrencyMapper = new CryptocurrencyMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(cryptocurrencyMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(cryptocurrencyMapper.fromId(null)).isNull();
    }
}
