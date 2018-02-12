/*
 * 
 */
package com.ttth.teamcaring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ttth.teamcaring.domain.Groups;

/**
 * Spring Data JPA repository for the Groups entity.
 *
 * @author Dai Mai
 */
@SuppressWarnings("unused")
@Repository
public interface GroupsRepository extends JpaRepository<Groups, Long> {

    /**
     * Find one by team id and leader id.
     *
     * @param teamId
     *        the team id
     * @param leaderId
     *        the leader id
     * @return the groups
     */
    public Groups findOneByTeamIdAndLeaderId(Long teamId, Long leaderId);
}
