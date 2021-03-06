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

import com.ttth.teamcaring.domain.Groups;
import com.ttth.teamcaring.repository.GroupsRepository;
import com.ttth.teamcaring.repository.search.GroupsSearchRepository;
import com.ttth.teamcaring.service.GroupsService;
import com.ttth.teamcaring.service.dto.GroupsDTO;
import com.ttth.teamcaring.service.mapper.GroupsMapper;

/**
 * Service Implementation for managing Groups.
 *
 * @author Dai Mai
 */
@Service
@Transactional
public class GroupsServiceImpl implements GroupsService {

    /** The log. */
    private final Logger                 log = LoggerFactory.getLogger(GroupsServiceImpl.class);

    /** The groups repository. */
    private final GroupsRepository       groupsRepository;

    /** The groups mapper. */
    private final GroupsMapper           groupsMapper;

    /** The groups search repository. */
    private final GroupsSearchRepository groupsSearchRepository;

    /**
     * Instantiates a new groups service impl.
     *
     * @param groupsRepository
     *        the groups repository
     * @param groupsMapper
     *        the groups mapper
     * @param groupsSearchRepository
     *        the groups search repository
     */
    public GroupsServiceImpl(GroupsRepository groupsRepository, GroupsMapper groupsMapper,
            GroupsSearchRepository groupsSearchRepository) {
        this.groupsRepository = groupsRepository;
        this.groupsMapper = groupsMapper;
        this.groupsSearchRepository = groupsSearchRepository;
    }

    /**
     * Save a groups.
     *
     * @param groupsDTO
     *        the entity to save
     * @return the persisted entity
     */
    @Override
    public GroupsDTO save(GroupsDTO groupsDTO) {
        log.debug("Request to save Groups : {}", groupsDTO);
        Groups groups = groupsMapper.toEntity(groupsDTO);
        groups = groupsRepository.save(groups);
        GroupsDTO result = groupsMapper.toDto(groups);
        groupsSearchRepository.save(groups);
        return result;
    }

    /**
     * Get all the groups.
     *
     * @param pageable
     *        the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<GroupsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Groups");
        return groupsRepository.findAll(pageable).map(groupsMapper::toDto);
    }

    /**
     * Get one groups by id.
     *
     * @param id
     *        the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public GroupsDTO findOne(Long id) {
        log.debug("Request to get Groups : {}", id);
        Groups groups = groupsRepository.findOne(id);
        return groupsMapper.toDto(groups);
    }

    /**
     * Delete the groups by id.
     *
     * @param id
     *        the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Groups : {}", id);
        groupsRepository.delete(id);
        groupsSearchRepository.delete(id);
    }

    /**
     * Search for the groups corresponding to the query.
     *
     * @param query
     *        the query of the search
     * @param pageable
     *        the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<GroupsDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Groups for query {}", query);
        Page<Groups> result = groupsSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(groupsMapper::toDto);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ttth.teamcaring.service.GroupsService#findByTeamAndLeader(java.lang.
     * Long, java.lang.Long)
     */
    @Override
    public GroupsDTO findByTeamAndLeader(Long teamId, Long customUserId) {
        log.debug("Request to get Groups by team {0} and leader (customUser) {1}", teamId,
                customUserId);
        Groups groups = groupsRepository.findOneByTeamIdAndLeaderId(teamId, customUserId);
        return groupsMapper.toDto(groups);
    }
}
