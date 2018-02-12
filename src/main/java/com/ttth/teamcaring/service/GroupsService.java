/*
 * 
 */
package com.ttth.teamcaring.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ttth.teamcaring.service.dto.GroupsDTO;

/**
 * Service Interface for managing Groups.
 *
 * @author Dai Mai
 */
public interface GroupsService {

    /**
     * Save a groups.
     *
     * @param groupsDTO
     *        the entity to save
     * @return the persisted entity
     */
    GroupsDTO save(GroupsDTO groupsDTO);

    /**
     * Get all the groups.
     *
     * @param pageable
     *        the pagination information
     * @return the list of entities
     */
    Page<GroupsDTO> findAll(Pageable pageable);

    /**
     * Get the "id" groups.
     *
     * @param id
     *        the id of the entity
     * @return the entity
     */
    GroupsDTO findOne(Long id);

    /**
     * Delete the "id" groups.
     *
     * @param id
     *        the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the groups corresponding to the query.
     *
     * @param query
     *        the query of the search
     * 
     * @param pageable
     *        the pagination information
     * @return the list of entities
     */
    Page<GroupsDTO> search(String query, Pageable pageable);

    /**
     * Find by team and leader.
     *
     * @param teamId
     *        the team id
     * @param customUserId
     *        the leader id
     * @return the groups DTO
     */
    GroupsDTO findByTeamAndLeader(Long teamId, Long customUserId);
}
