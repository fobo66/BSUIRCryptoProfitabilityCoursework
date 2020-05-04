package by.bsuir.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import by.bsuir.web.rest.TestUtil;

public class PowerCostTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PowerCost.class);
        PowerCost powerCost1 = new PowerCost();
        powerCost1.setId(1L);
        PowerCost powerCost2 = new PowerCost();
        powerCost2.setId(powerCost1.getId());
        assertThat(powerCost1).isEqualTo(powerCost2);
        powerCost2.setId(2L);
        assertThat(powerCost1).isNotEqualTo(powerCost2);
        powerCost1.setId(null);
        assertThat(powerCost1).isNotEqualTo(powerCost2);
    }
}
