package com.desk.market.service.mapper;


import com.desk.market.domain.*;
import com.desk.market.service.dto.PhotoDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Photo} and its DTO {@link PhotoDTO}.
 */
@Mapper(componentModel = "spring", uses = {DeskMapper.class})
public interface PhotoMapper extends EntityMapper<PhotoDTO, Photo> {

    @Mapping(source = "desk.id", target = "deskId")
    @Mapping(source = "desk.deskId", target = "deskDeskId")
    PhotoDTO toDto(Photo photo);

    @Mapping(source = "deskId", target = "desk")
    Photo toEntity(PhotoDTO photoDTO);

    default Photo fromId(Long id) {
        if (id == null) {
            return null;
        }
        Photo photo = new Photo();
        photo.setId(id);
        return photo;
    }
}
