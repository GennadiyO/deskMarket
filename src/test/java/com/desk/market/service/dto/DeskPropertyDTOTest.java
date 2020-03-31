package com.desk.market.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.desk.market.web.rest.TestUtil;

public class DeskPropertyDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DeskPropertyDTO.class);
        DeskPropertyDTO deskPropertyDTO1 = new DeskPropertyDTO();
        deskPropertyDTO1.setId(1L);
        DeskPropertyDTO deskPropertyDTO2 = new DeskPropertyDTO();
        assertThat(deskPropertyDTO1).isNotEqualTo(deskPropertyDTO2);
        deskPropertyDTO2.setId(deskPropertyDTO1.getId());
        assertThat(deskPropertyDTO1).isEqualTo(deskPropertyDTO2);
        deskPropertyDTO2.setId(2L);
        assertThat(deskPropertyDTO1).isNotEqualTo(deskPropertyDTO2);
        deskPropertyDTO1.setId(null);
        assertThat(deskPropertyDTO1).isNotEqualTo(deskPropertyDTO2);
    }
}
