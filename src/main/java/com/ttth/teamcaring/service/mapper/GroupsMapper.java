/*
 * 
 */
package com.ttth.teamcaring.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ttth.teamcaring.domain.Groups;
import com.ttth.teamcaring.service.dto.GroupsDTO;

/**
 * Mapper for the entity Groups and its DTO GroupsDTO.
 *
 * @author Dai Mai
 */
@Mapper(componentModel = "spring", uses = { CustomUserMapper.class, TeamMapper.class })
public interface GroupsMapper extends EntityMapper<GroupsDTO, Groups> {

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ttth.teamcaring.service.mapper.EntityMapper#toDto(java.lang.Object)
     */
    @Mapping(source = "leader.id", target = "leaderId")
    @Mapping(source = "team.id", target = "teamId")
    GroupsDTO toDto(Groups groups);

    /*
     * (non-Javadoc)
     * 
     * @see com.ttth.teamcaring.service.mapper.EntityMapper#toEntity(java.lang.
     * Object)
     */
    @Mapping(target = "members", ignore = true)
    @Mapping(source = "leaderId", target = "leader")
    @Mapping(source = "teamId", target = "team")
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    Groups toEntity(GroupsDTO groupsDTO);

    /**
     * From id.
     *
     * @param id
     *        the id
     * @return the groups
     */
    default Groups fromId(Long id) {
        if (id == null) {
            return null;
        }
        Groups groups = new Groups();
        groups.setId(id);
        return groups;
    }
}
