/*
 * 
 */
package com.ttth.teamcaring.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ttth.teamcaring.domain.GroupsMember;
import com.ttth.teamcaring.service.dto.GroupsMemberDTO;

/**
 * Service Interface for managing GroupsMember.
 *
 * @author Dai Mai
 */
public interface GroupsMemberService {

    /**
     * Save a groupsMember.
     *
     * @param groupsMemberDTO
     *        the entity to save
     * @return the persisted entity
     */
    GroupsMemberDTO save(GroupsMemberDTO groupsMemberDTO);

    /**
     * Get all the groupsMembers.
     *
     * @param pageable
     *        the pagination information
     * @return the list of entities
     */
    Page<GroupsMemberDTO> findAll(Pageable pageable);

    /**
     * Get the "id" groupsMember.
     *
     * @param id
     *        the id of the entity
     * @return the entity
     */
    GroupsMemberDTO findOne(Long id);

    /**
     * Delete the "id" groupsMember.
     *
     * @param id
     *        the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the groupsMember corresponding to the query.
     *
     * @param query
     *        the query of the search
     * 
     * @param pageable
     *        the pagination information
     * @return the list of entities
     */
    Page<GroupsMemberDTO> search(String query, Pageable pageable);

    /**
     * Find one entity.
     *
     * @param groupsMemberId
     *        the groups member id
     * @return the groups member
     */
    GroupsMember findOneEntity(Long groupsMemberId);

    /**
     * Map to dto.
     *
     * @param groupsMember
     *        the groups member
     * @return the groups member DTO
     */
    GroupsMemberDTO mapToDto(GroupsMember groupsMember);

    /**
     * Find one by member in group.
     *
     * @param memberCustomUserId
     *        the member custom user id
     * @param groupId
     *        the group id
     * @return the groups member DTO
     */
    GroupsMemberDTO findOneByMemberInGroup(Long memberCustomUserId, Long groupId);

    /**
     * Find one entity by member in group.
     *
     * @param memberCustomUserId
     *        the member custom user id
     * @param groupId
     *        the group id
     * @return the groups member
     */
    GroupsMember findOneEntityByMemberInGroup(Long memberCustomUserId, Long groupId);

    /**
     * Find one entity by member and team.
     *
     * @param memberCustomUserId
     *        the member custom user id
     * @param teamId
     *        the team id
     * @return the groups member
     */
    GroupsMember findOneEntityByMemberAndTeam(Long memberCustomUserId, Long teamId);

    /**
     * Map to entity.
     *
     * @param groupsMemberDTO
     *        the groups member DTO
     * @return the groups member
     */
    GroupsMember mapToEntity(GroupsMemberDTO groupsMemberDTO);

    /**
     * Removes the all pending request.
     *
     * @param memberCustomUserId
     *        the member custom user id
     * @param teamId
     *        the team id
     */
    void removeAllPendingRequest(Long memberCustomUserId, Long teamId);

    /**
     * Gets the one entity by member and team.
     *
     * @param userId the user id
     * @param teamId the team id
     * @return the one entity by member and team
     */
    GroupsMember getOneEntityByMemberAndTeam(Long userId, Long teamId);
}
