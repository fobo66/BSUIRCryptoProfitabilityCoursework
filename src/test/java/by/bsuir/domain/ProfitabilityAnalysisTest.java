package by.bsuir.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import by.bsuir.web.rest.TestUtil;

public class ProfitabilityAnalysisTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProfitabilityAnalysis.class);
        ProfitabilityAnalysis profitabilityAnalysis1 = new ProfitabilityAnalysis();
        profitabilityAnalysis1.setId(1L);
        ProfitabilityAnalysis profitabilityAnalysis2 = new ProfitabilityAnalysis();
        profitabilityAnalysis2.setId(profitabilityAnalysis1.getId());
        assertThat(profitabilityAnalysis1).isEqualTo(profitabilityAnalysis2);
        profitabilityAnalysis2.setId(2L);
        assertThat(profitabilityAnalysis1).isNotEqualTo(profitabilityAnalysis2);
        profitabilityAnalysis1.setId(null);
        assertThat(profitabilityAnalysis1).isNotEqualTo(profitabilityAnalysis2);
    }
}
