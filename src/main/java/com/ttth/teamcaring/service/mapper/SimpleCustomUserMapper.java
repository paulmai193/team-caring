/*
 * 
 */
package com.ttth.teamcaring.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ttth.teamcaring.domain.CustomUser;
import com.ttth.teamcaring.service.dto.CustomUserDTO;

/**
 * Mapper for the entity CustomUser and its DTO CustomUserDTO.
 *
 * @author Dai Mai
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface SimpleCustomUserMapper extends EntityMapper<CustomUserDTO, CustomUser> {

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ttth.teamcaring.service.mapper.EntityMapper#toDto(java.lang.Object)
     */
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.imageUrl", target = "imageUrl")
    @Mapping(source = "user.email", target = "email")
    @Mapping(target = "extraGroupName", ignore = true)
    @Mapping(target = "extraGroupDescription", ignore = true)
    @Mapping(target = "extraGroupTotalMember", ignore = true)
    CustomUserDTO toDto(CustomUser customUser);

    /*
     * (non-Javadoc)
     * 
     * @see com.ttth.teamcaring.service.mapper.EntityMapper#toEntity(java.lang.
     * Object)
     */
    @Mapping(source = "userId", target = "user")
    @Mapping(source = "email", target = "user.email")
    @Mapping(target = "leaders", ignore = true)
    @Mapping(target = "members", ignore = true)
    @Mapping(target = "notifications", ignore = true)
    @Mapping(target = "owners", ignore = true)
    @Mapping(target = "appointments", ignore = true)
    @Mapping(target = "attendees", ignore = true)
    @Mapping(target = "notes", ignore = true)
    CustomUser toEntity(CustomUserDTO customUserDTO);

    /**
     * From id.
     *
     * @param id
     *        the id
     * @return the custom user
     */
    default CustomUser fromId(Long id) {
        if (id == null) {
            return null;
        }
        CustomUser customUser = new CustomUser();
        customUser.setId(id);
        return customUser;
    }
}
