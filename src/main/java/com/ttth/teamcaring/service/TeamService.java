/*
 * 
 */
package com.ttth.teamcaring.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ttth.teamcaring.domain.CustomUser;
import com.ttth.teamcaring.service.dto.AnwserResponseStatus;
import com.ttth.teamcaring.service.dto.TeamDTO;
import com.ttth.teamcaring.service.dto.TeamDetailDTO;

/**
 * Service Interface for managing Team.
 *
 * @author Dai Mai
 */
public interface TeamService {

    /**
     * Creates the team for the CURRENT user. (get by current JWT)
     *
     * @param teamDTO
     *        the team DTO
     * @return the creates the team DTO
     */
    TeamDTO create(TeamDTO teamDTO);

    /**
     * Creates the team for input user.
     *
     * @param teamDTO
     *        the team DTO
     * @param userId
     *        the user id
     * @return the creates the team DTO
     */
    TeamDTO create(TeamDTO teamDTO, Long userId);

    /**
     * Update the team for the CURRENT user. (get by current JWT)
     *
     * @param teamDTO
     *        the team DTO
     * @return the team DTO
     */
    TeamDTO update(TeamDTO teamDTO);

    /**
     * Save a team.
     *
     * @param teamDTO
     *        the entity to save
     * @return the persisted entity
     */
    TeamDTO save(TeamDTO teamDTO);

    /**
     * Get all the teams.
     *
     * @param pageable
     *        the pagination information
     * @return the list of entities
     */
    Page<TeamDTO> findAll(Pageable pageable);

    /**
     * Get the "id" team.
     *
     * @param id
     *        the id of the entity
     * @return the entity
     */
    TeamDTO findOne(Long id);

    /**
     * Delete the "id" team.
     *
     * @param id
     *        the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the team corresponding to the query.
     *
     * @param query
     *        the query of the search
     * 
     * @param pageable
     *        the pagination information
     * @return the list of entities
     */
    Page<TeamDTO> search(String query, Pageable pageable);

    /**
     * Search for the new team corresponding to the query.
     *
     * @param query
     *        the query of the search
     * 
     * @param pageable
     *        the pagination information
     * @return the list of entities
     */
    Page<TeamDTO> searchNewTeam(String query, Pageable pageable);

    /**
     * Request join.
     *
     * @param login
     *        the login
     * @param teamId
     *        the team id
     * @param leaderId
     *        the leader id
     */
    void requestJoin(String login, Long teamId, Long leaderId);

    /**
     * Gets the team detail of authorized user.
     *
     * @param teamId
     *        the team id
     * @return the team detail of authorized user
     */
    TeamDetailDTO getTeamDetailOfAuthorizedUser(Long teamId);

    /**
     * Find all of authorized user.
     *
     * @param pageable
     *        the pageable
     * @return the page
     */
    Page<TeamDTO> findAllOfAuthorizedUser(Pageable pageable);

    /**
     * Search of authorized user.
     *
     * @param query
     *        the query
     * @param pageable
     *        the pageable
     * @return the page
     */
    Page<TeamDTO> searchOfAuthorizedUser(String query, Pageable pageable);

    /**
     * Response join.
     *
     * @param login
     *        the current user login
     * @param groupsMemberId
     *        the groups member id
     * @param responseJoinTeamStatus
     *        the response join team status
     */
    void responseJoin(String login, Long groupsMemberId,
            AnwserResponseStatus responseJoinTeamStatus);

    /**
     * Update level.
     *
     * @param login
     *        the current user login
     * @param groupsMemberId
     *        the groups member id
     * @param level
     *        the level
     */
    void updateMemberLevel(String login, Long groupsMemberId, Integer level);

    /**
     * Update team level.
     *
     * @param login
     *        the login
     * @param teamId
     *        the team id
     * @param level
     *        the level
     */
    void updateTeamLevel(String login, Long teamId, Integer level);

    /**
     * Gets the level of member in team.
     *
     * @param memberCustomUser
     *        the member custom user
     * @param teamId
     *        the team id
     * @return the level of member in team
     */
    Integer getLevelOfMemberInTeam(CustomUser memberCustomUser, Long teamId);
}
