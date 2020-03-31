package com.desk.market.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.desk.market.web.rest.TestUtil;

public class DeskTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Desk.class);
        Desk desk1 = new Desk();
        desk1.setId(1L);
        Desk desk2 = new Desk();
        desk2.setId(desk1.getId());
        assertThat(desk1).isEqualTo(desk2);
        desk2.setId(2L);
        assertThat(desk1).isNotEqualTo(desk2);
        desk1.setId(null);
        assertThat(desk1).isNotEqualTo(desk2);
    }
}
