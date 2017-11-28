package com.ttth.teamcaring.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ttth.teamcaring.service.dto.GroupsMemberDTO;

/**
 * Service Interface for managing GroupsMember.
 */
public interface GroupsMemberService {

    /**
     * Save a groupsMember.
     *
     * @param groupsMemberDTO the entity to save
     * @return the persisted entity
     */
    GroupsMemberDTO save(GroupsMemberDTO groupsMemberDTO);

    /**
     *  Get all the groupsMembers.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<GroupsMemberDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" groupsMember.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    GroupsMemberDTO findOne(Long id);

    /**
     *  Delete the "id" groupsMember.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the groupsMember corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<GroupsMemberDTO> search(String query, Pageable pageable);
}
