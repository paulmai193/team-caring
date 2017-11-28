package com.ttth.teamcaring.service;

import com.ttth.teamcaring.service.dto.SubjectDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Subject.
 */
public interface SubjectService {

    /**
     * Save a subject.
     *
     * @param subjectDTO the entity to save
     * @return the persisted entity
     */
    SubjectDTO save(SubjectDTO subjectDTO);

    /**
     *  Get all the subjects.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<SubjectDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" subject.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    SubjectDTO findOne(Long id);

    /**
     *  Delete the "id" subject.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the subject corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<SubjectDTO> search(String query, Pageable pageable);
}
