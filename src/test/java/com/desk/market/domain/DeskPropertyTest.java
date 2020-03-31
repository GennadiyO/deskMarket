package com.desk.market.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.desk.market.web.rest.TestUtil;

public class DeskPropertyTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DeskProperty.class);
        DeskProperty deskProperty1 = new DeskProperty();
        deskProperty1.setId(1L);
        DeskProperty deskProperty2 = new DeskProperty();
        deskProperty2.setId(deskProperty1.getId());
        assertThat(deskProperty1).isEqualTo(deskProperty2);
        deskProperty2.setId(2L);
        assertThat(deskProperty1).isNotEqualTo(deskProperty2);
        deskProperty1.setId(null);
        assertThat(deskProperty1).isNotEqualTo(deskProperty2);
    }
}
