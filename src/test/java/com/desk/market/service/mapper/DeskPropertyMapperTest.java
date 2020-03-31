package com.desk.market.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class DeskPropertyMapperTest {

    private DeskPropertyMapper deskPropertyMapper;

    @BeforeEach
    public void setUp() {
        deskPropertyMapper = new DeskPropertyMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(deskPropertyMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(deskPropertyMapper.fromId(null)).isNull();
    }
}
