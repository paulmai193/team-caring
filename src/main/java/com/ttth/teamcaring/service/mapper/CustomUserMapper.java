package com.ttth.teamcaring.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ttth.teamcaring.domain.CustomUser;
import com.ttth.teamcaring.service.dto.CustomUserDTO;

/**
 * Mapper for the entity CustomUser and its DTO CustomUserDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface CustomUserMapper extends EntityMapper<CustomUserDTO, CustomUser> {

    @Mapping(source = "user.id", target = "userId")
    CustomUserDTO toDto(CustomUser customUser); 

    @Mapping(source = "userId", target = "user")
    @Mapping(target = "leaders", ignore = true)
    @Mapping(target = "members", ignore = true)
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
