package com.ttth.teamcaring.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ttth.teamcaring.domain.GroupsMember;
import com.ttth.teamcaring.service.dto.GroupsMemberDTO;

/**
 * Mapper for the entity GroupsMember and its DTO GroupsMemberDTO.
 */
@Mapper(componentModel = "spring", uses = {CustomUserMapper.class})
public interface GroupsMemberMapper extends EntityMapper<GroupsMemberDTO, GroupsMember> {

    

    @Mapping(target = "groups", ignore = true)
    GroupsMember toEntity(GroupsMemberDTO groupsMemberDTO);

    default GroupsMember fromId(Long id) {
        if (id == null) {
            return null;
        }
        GroupsMember groupsMember = new GroupsMember();
        groupsMember.setId(id);
        return groupsMember;
    }
}
