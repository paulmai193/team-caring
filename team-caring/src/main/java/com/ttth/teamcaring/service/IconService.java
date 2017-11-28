package com.ttth.teamcaring.service;

import com.ttth.teamcaring.service.dto.IconDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Icon.
 */
public interface IconService {

    /**
     * Save a icon.
     *
     * @param iconDTO the entity to save
     * @return the persisted entity
     */
    IconDTO save(IconDTO iconDTO);

    /**
     *  Get all the icons.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<IconDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" icon.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    IconDTO findOne(Long id);

    /**
     *  Delete the "id" icon.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the icon corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<IconDTO> search(String query, Pageable pageable);
}
