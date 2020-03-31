package com.desk.market.service.mapper;


import com.desk.market.domain.*;
import com.desk.market.service.dto.PriceDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Price} and its DTO {@link PriceDTO}.
 */
@Mapper(componentModel = "spring", uses = {DeskMapper.class})
public interface PriceMapper extends EntityMapper<PriceDTO, Price> {

    @Mapping(source = "desk.id", target = "deskId")
    @Mapping(source = "desk.deskId", target = "deskDeskId")
    PriceDTO toDto(Price price);

    @Mapping(source = "deskId", target = "desk")
    Price toEntity(PriceDTO priceDTO);

    default Price fromId(Long id) {
        if (id == null) {
            return null;
        }
        Price price = new Price();
        price.setId(id);
        return price;
    }
}
