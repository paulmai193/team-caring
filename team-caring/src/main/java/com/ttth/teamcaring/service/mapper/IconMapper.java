package com.ttth.teamcaring.service.mapper;

import com.ttth.teamcaring.domain.*;
import com.ttth.teamcaring.service.dto.IconDTO;

import org.mapstruct.*;

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
