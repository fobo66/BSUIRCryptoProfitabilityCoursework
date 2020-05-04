package by.bsuir.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import by.bsuir.web.rest.TestUtil;

public class HardwareInfoTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HardwareInfo.class);
        HardwareInfo hardwareInfo1 = new HardwareInfo();
        hardwareInfo1.setId(1L);
        HardwareInfo hardwareInfo2 = new HardwareInfo();
        hardwareInfo2.setId(hardwareInfo1.getId());
        assertThat(hardwareInfo1).isEqualTo(hardwareInfo2);
        hardwareInfo2.setId(2L);
        assertThat(hardwareInfo1).isNotEqualTo(hardwareInfo2);
        hardwareInfo1.setId(null);
        assertThat(hardwareInfo1).isNotEqualTo(hardwareInfo2);
    }
}
