package com.ttth.teamcaring.service.mapper;

import com.ttth.teamcaring.domain.*;
import com.ttth.teamcaring.service.dto.CustomUserDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity CustomUser and its DTO CustomUserDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, GroupsMapper.class})
public interface CustomUserMapper extends EntityMapper<CustomUserDTO, CustomUser> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "groups.id", target = "groupsId")
    CustomUserDTO toDto(CustomUser customUser); 

    @Mapping(source = "userId", target = "user")
    @Mapping(target = "members", ignore = true)
    @Mapping(source = "groupsId", target = "groups")
    CustomUser toEntity(CustomUserDTO customUserDTO);

    default CustomUser fromId(Long id) {
        if (id == null) {
            return null;
        }
        CustomUser customUser = new CustomUser();
        customUser.setId(id);
        return customUser;
    }
}
