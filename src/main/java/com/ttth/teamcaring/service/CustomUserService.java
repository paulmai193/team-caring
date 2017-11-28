package com.ttth.teamcaring.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ttth.teamcaring.service.dto.CustomUserDTO;

/**
 * Service Interface for managing CustomUser.
 *
 * @author Dai Mai
 */
public interface CustomUserService {

    /**
     * Save a customUser.
     *
     * @param customUserDTO the entity to save
     * @return the persisted entity
     */
    CustomUserDTO save(CustomUserDTO customUserDTO);

    /**
     *  Get all the customUsers.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
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
     * @param login the login
     * @return the custom user DTO
     */
    Optional<CustomUserDTO> findOneByUserLogin(String login);

    /**
     * Find one by user id.
     *
     * @param userId the user id
     * @return the custom user DTO
     */
    Optional<CustomUserDTO> findOneByUserId(Long userId);

    /**
     *  Get the "id" customUser.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    CustomUserDTO findOne(Long id);
    
    /**
     *  Delete the "id" customUser.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the customUser corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<CustomUserDTO> search(String query, Pageable pageable);
}
