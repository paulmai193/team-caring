package com.ttth.teamcaring.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ttth.teamcaring.domain.Groups;
import com.ttth.teamcaring.service.dto.GroupsDTO;

/**
 * Mapper for the entity Groups and its DTO GroupsDTO.
 */
@Mapper(componentModel = "spring", uses = {CustomUserMapper.class, GroupsMemberMapper.class, TeamMapper.class})
public interface GroupsMapper extends EntityMapper<GroupsDTO, Groups> {

    @Mapping(source = "customUser.id", target = "customUserId")
    @Mapping(source = "team.id", target = "teamId")
    GroupsDTO toDto(Groups groups); 

    @Mapping(source = "customUserId", target = "customUser")
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
