package com.ttth.teamcaring.service;

import com.ttth.teamcaring.service.dto.CustomUserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing CustomUser.
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
