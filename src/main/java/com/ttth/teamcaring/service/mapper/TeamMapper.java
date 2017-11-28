package com.ttth.teamcaring.service.mapper;

import com.ttth.teamcaring.domain.*;
import com.ttth.teamcaring.service.dto.TeamDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Team and its DTO TeamDTO.
 */
@Mapper(componentModel = "spring", uses = {IconMapper.class})
public interface TeamMapper extends EntityMapper<TeamDTO, Team> {

    @Mapping(source = "icon.id", target = "iconId")
    TeamDTO toDto(Team team); 

    @Mapping(source = "iconId", target = "icon")
    @Mapping(target = "groups", ignore = true)
    Team toEntity(TeamDTO teamDTO);

    default Team fromId(Long id) {
        if (id == null) {
            return null;
        }
        Team team = new Team();
        team.setId(id);
        return team;
    }
}
