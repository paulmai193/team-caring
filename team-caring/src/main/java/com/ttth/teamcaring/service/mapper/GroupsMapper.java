package com.ttth.teamcaring.service.mapper;

import com.ttth.teamcaring.domain.*;
import com.ttth.teamcaring.service.dto.GroupsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Groups and its DTO GroupsDTO.
 */
@Mapper(componentModel = "spring", uses = {GroupsMemberMapper.class, TeamMapper.class})
public interface GroupsMapper extends EntityMapper<GroupsDTO, Groups> {

    @Mapping(source = "team.id", target = "teamId")
    GroupsDTO toDto(Groups groups); 

    @Mapping(target = "leaders", ignore = true)
    @Mapping(source = "teamId", target = "team")
    Groups toEntity(GroupsDTO groupsDTO);

    default Groups fromId(Long id) {
        if (id == null) {
            return null;
        }
        Groups groups = new Groups();
        groups.setId(id);
        return groups;
    }
}
