package com.ttth.teamcaring.service.mapper;

import org.mapstruct.Mapper;

import com.ttth.teamcaring.domain.Icon;
import com.ttth.teamcaring.service.dto.IconDTO;

/**
 * Mapper for the entity Icon and its DTO IconDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface IconMapper extends EntityMapper<IconDTO, Icon> {

    

    

    default Icon fromId(Long id) {
        if (id == null) {
            return null;
        }
        Icon icon = new Icon();
        icon.setId(id);
        return icon;
    }
}
