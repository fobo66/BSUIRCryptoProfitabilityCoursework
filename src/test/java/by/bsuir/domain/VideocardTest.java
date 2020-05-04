package by.bsuir.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import by.bsuir.web.rest.TestUtil;

public class VideocardTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Videocard.class);
        Videocard videocard1 = new Videocard();
        videocard1.setId(1L);
        Videocard videocard2 = new Videocard();
        videocard2.setId(videocard1.getId());
        assertThat(videocard1).isEqualTo(videocard2);
        videocard2.setId(2L);
        assertThat(videocard1).isNotEqualTo(videocard2);
        videocard1.setId(null);
        assertThat(videocard1).isNotEqualTo(videocard2);
    }
}
