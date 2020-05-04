package by.bsuir.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import by.bsuir.web.rest.TestUtil;

public class CryptocurrencyTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cryptocurrency.class);
        Cryptocurrency cryptocurrency1 = new Cryptocurrency();
        cryptocurrency1.setId(1L);
        Cryptocurrency cryptocurrency2 = new Cryptocurrency();
        cryptocurrency2.setId(cryptocurrency1.getId());
        assertThat(cryptocurrency1).isEqualTo(cryptocurrency2);
        cryptocurrency2.setId(2L);
        assertThat(cryptocurrency1).isNotEqualTo(cryptocurrency2);
        cryptocurrency1.setId(null);
        assertThat(cryptocurrency1).isNotEqualTo(cryptocurrency2);
    }
}
