package com.desk.market.service.mapper;


import com.desk.market.domain.*;
import com.desk.market.service.dto.DeskDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Desk} and its DTO {@link DeskDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DeskMapper extends EntityMapper<DeskDTO, Desk> {


    @Mapping(target = "photos", ignore = true)
    @Mapping(target = "removePhotos", ignore = true)
    @Mapping(target = "prices", ignore = true)
    @Mapping(target = "removePrices", ignore = true)
    @Mapping(target = "properties", ignore = true)
    @Mapping(target = "removeProperties", ignore = true)
    Desk toEntity(DeskDTO deskDTO);

    default Desk fromId(Long id) {
        if (id == null) {
            return null;
        }
        Desk desk = new Desk();
        desk.setId(id);
        return desk;
    }
}
