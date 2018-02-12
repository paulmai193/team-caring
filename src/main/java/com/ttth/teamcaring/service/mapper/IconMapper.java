/*
 * 
 */
package com.ttth.teamcaring.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ttth.teamcaring.domain.Icon;
import com.ttth.teamcaring.service.dto.IconDTO;

/**
 * Mapper for the entity Icon and its DTO IconDTO.
 *
 * @author Dai Mai
 */
@Mapper(componentModel = "spring", uses = {})
public interface IconMapper extends EntityMapper<IconDTO, Icon> {

    /*
     * (non-Javadoc)
     * 
     * @see com.ttth.teamcaring.service.mapper.EntityMapper#toEntity(java.lang.
     * Object)
     */
    @Mapping(target = "teams", ignore = true)
    Icon toEntity(IconDTO iconDTO);

    /**
     * From id.
     *
     * @param id
     *        the id
     * @return the icon
     */
    default Icon fromId(Long id) {
        if (id == null) {
            return null;
        }
        Icon icon = new Icon();
        icon.setId(id);
        return icon;
    }
}
