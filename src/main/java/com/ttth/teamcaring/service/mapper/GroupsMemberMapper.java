/*
 * 
 */
package com.ttth.teamcaring.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ttth.teamcaring.domain.GroupsMember;
import com.ttth.teamcaring.service.dto.GroupsMemberDTO;

/**
 * Mapper for the entity GroupsMember and its DTO GroupsMemberDTO.
 *
 * @author Dai Mai
 */
@Mapper(componentModel = "spring", uses = { CustomUserMapper.class, GroupsMapper.class })
public interface GroupsMemberMapper extends EntityMapper<GroupsMemberDTO, GroupsMember> {

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ttth.teamcaring.service.mapper.EntityMapper#toDto(java.lang.Object)
     */
    @Mapping(source = "customUser.id", target = "customUserId")
    @Mapping(source = "groups.id", target = "groupsId")
    GroupsMemberDTO toDto(GroupsMember groupsMember);

    /*
     * (non-Javadoc)
     * 
     * @see com.ttth.teamcaring.service.mapper.EntityMapper#toEntity(java.lang.
     * Object)
     */
    @Mapping(source = "customUserId", target = "customUser")
    @Mapping(source = "groupsId", target = "groups")
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    GroupsMember toEntity(GroupsMemberDTO groupsMemberDTO);

    /**
     * From id.
     *
     * @param id
     *        the id
     * @return the groups member
     */
    default GroupsMember fromId(Long id) {
        if (id == null) {
            return null;
        }
        GroupsMember groupsMember = new GroupsMember();
        groupsMember.setId(id);
        return groupsMember;
    }
}
