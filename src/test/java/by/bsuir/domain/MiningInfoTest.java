package by.bsuir.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import by.bsuir.web.rest.TestUtil;

public class MiningInfoTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MiningInfo.class);
        MiningInfo miningInfo1 = new MiningInfo();
        miningInfo1.setId(1L);
        MiningInfo miningInfo2 = new MiningInfo();
        miningInfo2.setId(miningInfo1.getId());
        assertThat(miningInfo1).isEqualTo(miningInfo2);
        miningInfo2.setId(2L);
        assertThat(miningInfo1).isNotEqualTo(miningInfo2);
        miningInfo1.setId(null);
        assertThat(miningInfo1).isNotEqualTo(miningInfo2);
    }
}
