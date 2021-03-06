/*
 * 
 */
package com.ttth.teamcaring.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ttth.teamcaring.domain.Icon;
import com.ttth.teamcaring.repository.IconRepository;
import com.ttth.teamcaring.repository.search.IconSearchRepository;
import com.ttth.teamcaring.service.IconService;
import com.ttth.teamcaring.service.dto.IconDTO;
import com.ttth.teamcaring.service.mapper.IconMapper;

/**
 * Service Implementation for managing Icon.
 *
 * @author Dai Mai
 */
@Service
@Transactional
public class IconServiceImpl implements IconService {

    /** The log. */
    private final Logger               log = LoggerFactory.getLogger(IconServiceImpl.class);

    /** The icon repository. */
    private final IconRepository       iconRepository;

    /** The icon mapper. */
    private final IconMapper           iconMapper;

    /** The icon search repository. */
    private final IconSearchRepository iconSearchRepository;

    /**
     * Instantiates a new icon service impl.
     *
     * @param iconRepository
     *        the icon repository
     * @param iconMapper
     *        the icon mapper
     * @param iconSearchRepository
     *        the icon search repository
     */
    public IconServiceImpl(IconRepository iconRepository, IconMapper iconMapper,
            IconSearchRepository iconSearchRepository) {
        this.iconRepository = iconRepository;
        this.iconMapper = iconMapper;
        this.iconSearchRepository = iconSearchRepository;
    }

    /**
     * Save a icon.
     *
     * @param iconDTO
     *        the entity to save
     * @return the persisted entity
     */
    @Override
    public IconDTO save(IconDTO iconDTO) {
        log.debug("Request to save Icon : {}", iconDTO);
        Icon icon = iconMapper.toEntity(iconDTO);
        icon = iconRepository.save(icon);
        IconDTO result = iconMapper.toDto(icon);
        iconSearchRepository.save(icon);
        return result;
    }

    /**
     * Get all the icons.
     *
     * @param pageable
     *        the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<IconDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Icons");
        return iconRepository.findAll(pageable).map(iconMapper::toDto);
    }

    /**
     * Get one icon by id.
     *
     * @param id
     *        the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public IconDTO findOne(Long id) {
        log.debug("Request to get Icon : {}", id);
        Icon icon = iconRepository.findOne(id);
        return iconMapper.toDto(icon);
    }

    /**
     * Delete the icon by id.
     *
     * @param id
     *        the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Icon : {}", id);
        iconRepository.delete(id);
        iconSearchRepository.delete(id);
    }

    /**
     * Search for the icon corresponding to the query.
     *
     * @param query
     *        the query of the search
     * @param pageable
     *        the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<IconDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Icons for query {}", query);
        Page<Icon> result = iconSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(iconMapper::toDto);
    }
}
