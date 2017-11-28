package com.ttth.teamcaring.service.impl;

import com.ttth.teamcaring.service.CustomUserService;
import com.ttth.teamcaring.domain.CustomUser;
import com.ttth.teamcaring.repository.CustomUserRepository;
import com.ttth.teamcaring.repository.search.CustomUserSearchRepository;
import com.ttth.teamcaring.service.dto.CustomUserDTO;
import com.ttth.teamcaring.service.mapper.CustomUserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing CustomUser.
 */
@Service
@Transactional
public class CustomUserServiceImpl implements CustomUserService{

    private final Logger log = LoggerFactory.getLogger(CustomUserServiceImpl.class);

    private final CustomUserRepository customUserRepository;

    private final CustomUserMapper customUserMapper;

    private final CustomUserSearchRepository customUserSearchRepository;

    public CustomUserServiceImpl(CustomUserRepository customUserRepository, CustomUserMapper customUserMapper, CustomUserSearchRepository customUserSearchRepository) {
        this.customUserRepository = customUserRepository;
        this.customUserMapper = customUserMapper;
        this.customUserSearchRepository = customUserSearchRepository;
    }

    /**
     * Save a customUser.
     *
     * @param customUserDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public CustomUserDTO save(CustomUserDTO customUserDTO) {
        log.debug("Request to save CustomUser : {}", customUserDTO);
        CustomUser customUser = customUserMapper.toEntity(customUserDTO);
        customUser = customUserRepository.save(customUser);
        CustomUserDTO result = customUserMapper.toDto(customUser);
        customUserSearchRepository.save(customUser);
        return result;
    }

    /**
     *  Get all the customUsers.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CustomUserDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CustomUsers");
        return customUserRepository.findAll(pageable)
            .map(customUserMapper::toDto);
    }

    /**
     *  Get one customUser by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public CustomUserDTO findOne(Long id) {
        log.debug("Request to get CustomUser : {}", id);
        CustomUser customUser = customUserRepository.findOne(id);
        return customUserMapper.toDto(customUser);
    }

    /**
     *  Delete the  customUser by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CustomUser : {}", id);
        customUserRepository.delete(id);
        customUserSearchRepository.delete(id);
    }

    /**
     * Search for the customUser corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CustomUserDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CustomUsers for query {}", query);
        Page<CustomUser> result = customUserSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(customUserMapper::toDto);
    }
}
