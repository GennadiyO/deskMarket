package com.desk.market.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class DeskMapperTest {

    private DeskMapper deskMapper;

    @BeforeEach
    public void setUp() {
        deskMapper = new DeskMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(deskMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(deskMapper.fromId(null)).isNull();
    }
}
