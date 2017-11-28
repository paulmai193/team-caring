package com.ttth.teamcaring.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ttth.teamcaring.domain.GroupsMember;
import com.ttth.teamcaring.repository.GroupsMemberRepository;
import com.ttth.teamcaring.repository.search.GroupsMemberSearchRepository;
import com.ttth.teamcaring.service.GroupsMemberService;
import com.ttth.teamcaring.service.dto.GroupsMemberDTO;
import com.ttth.teamcaring.service.mapper.GroupsMemberMapper;

/**
 * Service Implementation for managing GroupsMember.
 */
@Service
@Transactional
public class GroupsMemberServiceImpl implements GroupsMemberService{

    private final Logger log = LoggerFactory.getLogger(GroupsMemberServiceImpl.class);

    private final GroupsMemberRepository groupsMemberRepository;

    private final GroupsMemberMapper groupsMemberMapper;

    private final GroupsMemberSearchRepository groupsMemberSearchRepository;

    public GroupsMemberServiceImpl(GroupsMemberRepository groupsMemberRepository, GroupsMemberMapper groupsMemberMapper, GroupsMemberSearchRepository groupsMemberSearchRepository) {
        this.groupsMemberRepository = groupsMemberRepository;
        this.groupsMemberMapper = groupsMemberMapper;
        this.groupsMemberSearchRepository = groupsMemberSearchRepository;
    }

    /**
     * Save a groupsMember.
     *
     * @param groupsMemberDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public GroupsMemberDTO save(GroupsMemberDTO groupsMemberDTO) {
        log.debug("Request to save GroupsMember : {}", groupsMemberDTO);
        GroupsMember groupsMember = groupsMemberMapper.toEntity(groupsMemberDTO);
        groupsMember = groupsMemberRepository.save(groupsMember);
        GroupsMemberDTO result = groupsMemberMapper.toDto(groupsMember);
        groupsMemberSearchRepository.save(groupsMember);
        return result;
    }

    /**
     *  Get all the groupsMembers.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<GroupsMemberDTO> findAll(Pageable pageable) {
        log.debug("Request to get all GroupsMembers");
        return groupsMemberRepository.findAll(pageable)
            .map(groupsMemberMapper::toDto);
    }

    /**
     *  Get one groupsMember by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public GroupsMemberDTO findOne(Long id) {
        log.debug("Request to get GroupsMember : {}", id);
        GroupsMember groupsMember = groupsMemberRepository.findOneWithEagerRelationships(id);
        return groupsMemberMapper.toDto(groupsMember);
    }

    /**
     *  Delete the  groupsMember by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete GroupsMember : {}", id);
        groupsMemberRepository.delete(id);
        groupsMemberSearchRepository.delete(id);
    }

    /**
     * Search for the groupsMember corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<GroupsMemberDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of GroupsMembers for query {}", query);
        Page<GroupsMember> result = groupsMemberSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(groupsMemberMapper::toDto);
    }
}
