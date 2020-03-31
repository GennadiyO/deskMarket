package com.desk.market.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.desk.market.web.rest.TestUtil;

public class DeskDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DeskDTO.class);
        DeskDTO deskDTO1 = new DeskDTO();
        deskDTO1.setId(1L);
        DeskDTO deskDTO2 = new DeskDTO();
        assertThat(deskDTO1).isNotEqualTo(deskDTO2);
        deskDTO2.setId(deskDTO1.getId());
        assertThat(deskDTO1).isEqualTo(deskDTO2);
        deskDTO2.setId(2L);
        assertThat(deskDTO1).isNotEqualTo(deskDTO2);
        deskDTO1.setId(null);
        assertThat(deskDTO1).isNotEqualTo(deskDTO2);
    }
}
