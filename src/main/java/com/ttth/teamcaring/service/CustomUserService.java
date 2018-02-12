/*
 * 
 */
package com.ttth.teamcaring.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ttth.teamcaring.domain.CustomUser;
import com.ttth.teamcaring.domain.User;
import com.ttth.teamcaring.service.dto.CustomUserDTO;
import com.ttth.teamcaring.service.dto.ProfileDTO;
import com.ttth.teamcaring.web.rest.errors.InternalServerErrorException;

/**
 * Service Interface for managing CustomUser.
 *
 * @author Dai Mai
 */
public interface CustomUserService {

    /**
     * Creates the for current authorize.
     *
     * @return the custom user DTO
     */
    CustomUserDTO createForCurrentAuthorize();

    /**
     * Creates the.
     *
     * @param user
     *        the user
     * @return the custom user DTO
     */
    CustomUserDTO create(User user);

    /**
     * Save a customUser.
     *
     * @param customUserDTO
     *        the entity to save
     * @return the persisted entity
     */
    CustomUserDTO save(CustomUserDTO customUserDTO);

    /**
     * Get all the customUsers.
     *
     * @param pageable
     *        the pagination information
     * @return the list of entities
     */
    Page<CustomUserDTO> findAll(Pageable pageable);

    /**
     * Find one by current authorize.
     *
     * @return the custom user DTO
     */
    Optional<CustomUserDTO> findOneByCurrentAuthorize();

    /**
     * Find one by user login.
     *
     * @param login
     *        the login
     * @return the custom user DTO
     */
    Optional<CustomUserDTO> findOneByUserLogin(String login);

    /**
     * Find one entity by user login.
     *
     * @param login
     *        the login
     * @return the custom user
     */
    CustomUser findOneEntityByUserLogin(String login);

    /**
     * Find one by user id.
     *
     * @param userId
     *        the user id
     * @return the custom user DTO
     */
    Optional<CustomUserDTO> findOneByUserId(Long userId);

    /**
     * Get the "id" customUser.
     *
     * @param id
     *        the id of the entity
     * @return the entity
     */
    CustomUserDTO findOne(Long id);

    /**
     * Get the "id" custome user entity.
     *
     * @param id
     *        the id
     * @return the custom user
     */
    CustomUser findOneEntity(Long id);

    /**
     * Delete the "id" customUser.
     *
     * @param id
     *        the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the customUser corresponding to the query.
     *
     * @param query
     *        the query of the search
     * 
     * @param pageable
     *        the pagination information
     * @return the list of entities
     */
    Page<CustomUserDTO> search(String query, Pageable pageable);

    /**
     * Map from DTO.
     *
     * @param customUserDTO
     *        the custom user DTO
     * @return the custom user
     */
    CustomUser mapFromDTO(CustomUserDTO customUserDTO);

    /**
     * Map from entity.
     *
     * @param customUser
     *        the custom user
     * @return the custom user DTO
     */
    CustomUserDTO mapFromEntity(CustomUser customUser);

    /**
     * Find one entity by user id.
     *
     * @param userId
     *        the user id
     * @return the custom user
     */
    CustomUser findOneEntityByUserId(Long userId);

    /**
     * Gets the user profile by user login.
     *
     * @param currentUserLogin
     *        the current user login
     * @return the user profile by user login
     */
    ProfileDTO getUserProfileByUserLogin(String currentUserLogin);

    /**
     * Update basic profile information (full name, nickname, push token) and
     * anonymous group information for the CURRENT user. (get by current JWT)
     *
     * @param user
     *        the user
     * @param profileDTO
     *        the profile DTO
     * @return the profile DTO
     */
    ProfileDTO updateUserProfile(User user, ProfileDTO profileDTO);

    /**
     * Gets the user profile with authorities.
     *
     * @param userId
     *        the user id
     * @return the user profile with authorities
     */
    ProfileDTO getUserProfileByUserId(Long userId);

    /**
     * Search leaders in team.
     *
     * @param teamId
     *        the team id
     * @param query
     *        the query
     * @param pageable
     *        the pageable
     * @return the page
     */
    Page<ProfileDTO> searchLeadersInTeam(Long teamId, String query, Pageable pageable);

    /**
     * Gets the one entity by user login.
     *
     * @param login the login
     * @return the one entity by user login
     * @throws InternalServerErrorException the internal server error exception if entity not exist,
     *         because it REQUIRED
     */
    CustomUser getOneEntityByUserLogin(String login) throws InternalServerErrorException;

    /**
     * Gets the number appointment of user.
     *
     * @param customUser the custom user
     * @return the number appointment of user
     */
    int getNumberAppointmentOfUser(CustomUser customUser);

}
