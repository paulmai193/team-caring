/*
 * 
 */
package com.ttth.teamcaring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ttth.teamcaring.domain.GroupsMember;

/**
 * Spring Data JPA repository for the GroupsMember entity.
 *
 * @author Dai Mai
 */
@Repository
public interface GroupsMemberRepository extends JpaRepository<GroupsMember, Long> {

    /**
     * Find one by custom user id and groups id.
     *
     * @param memberCustomUserId
     *        the member custom user id
     * @param groupId
     *        the group id
     * @return the groups member
     */
    GroupsMember findOneByCustomUserIdAndGroupsId(Long memberCustomUserId, Long groupId);

    /**
     * Find one by custom user id and groups team id.
     *
     * @param memberCustomUserId
     *        the member custom user id
     * @param teamId
     *        the team id
     * @return the groups member
     */
    GroupsMember findOneByCustomUserIdAndGroupsTeamId(Long memberCustomUserId, Long teamId);

    /**
     * Delete by custom user id and groups team id and status.
     *
     * @param memberCustomUserId
     *        the member custom user id
     * @param teamId
     *        the team id
     * @param statusPending
     *        the status pending
     * @return the list
     */
    List<GroupsMember> deleteByCustomUserIdAndGroupsTeamIdAndStatus(Long memberCustomUserId,
            Long teamId, int statusPending);

}
