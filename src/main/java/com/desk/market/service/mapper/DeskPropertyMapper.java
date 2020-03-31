package com.desk.market.service.mapper;


import com.desk.market.domain.*;
import com.desk.market.service.dto.DeskPropertyDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link DeskProperty} and its DTO {@link DeskPropertyDTO}.
 */
@Mapper(componentModel = "spring", uses = {DeskMapper.class})
public interface DeskPropertyMapper extends EntityMapper<DeskPropertyDTO, DeskProperty> {

    @Mapping(source = "desk.id", target = "deskId")
    @Mapping(source = "desk.deskId", target = "deskDeskId")
    DeskPropertyDTO toDto(DeskProperty deskProperty);

    @Mapping(source = "deskId", target = "desk")
    DeskProperty toEntity(DeskPropertyDTO deskPropertyDTO);

    default DeskProperty fromId(Long id) {
        if (id == null) {
            return null;
        }
        DeskProperty deskProperty = new DeskProperty();
        deskProperty.setId(id);
        return deskProperty;
    }
}
