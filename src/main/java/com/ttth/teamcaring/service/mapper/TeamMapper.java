/*
 * 
 */
package com.ttth.teamcaring.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ttth.teamcaring.domain.Team;
import com.ttth.teamcaring.service.dto.TeamDTO;

/**
 * Mapper for the entity Team and its DTO TeamDTO.
 *
 * @author Dai Mai
 */
@Mapper(componentModel = "spring", uses = { CustomUserMapper.class, IconMapper.class })
public interface TeamMapper extends EntityMapper<TeamDTO, Team> {

    /* (non-Javadoc)
     * @see com.ttth.teamcaring.service.mapper.EntityMapper#toDto(java.lang.Object)
     */
    @Mapping(source = "owner.id", target = "ownerId")
    @Mapping(source = "icon.id", target = "iconId")
    TeamDTO toDto(Team team);

    /* (non-Javadoc)
     * @see com.ttth.teamcaring.service.mapper.EntityMapper#toEntity(java.lang.Object)
     */
    @Mapping(target = "groups", ignore = true)
    @Mapping(target = "appointments", ignore = true)
    @Mapping(source = "ownerId", target = "owner")
    @Mapping(source = "iconId", target = "icon")
    Team toEntity(TeamDTO teamDTO);

    /**
     * From id.
     *
     * @param id the id
     * @return the team
     */
    default Team fromId(Long id) {
        if (id == null) {
            return null;
        }
        Team team = new Team();
        team.setId(id);
        return team;
    }
}
