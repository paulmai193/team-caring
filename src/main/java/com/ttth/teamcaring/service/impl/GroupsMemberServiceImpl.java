/*
 * 
 */
package com.ttth.teamcaring.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ttth.teamcaring.domain.GroupsMember;
import com.ttth.teamcaring.repository.GroupsMemberRepository;
import com.ttth.teamcaring.repository.search.GroupsMemberSearchRepository;
import com.ttth.teamcaring.service.GroupsMemberService;
import com.ttth.teamcaring.service.NotificationService;
import com.ttth.teamcaring.service.dto.GroupsMemberDTO;
import com.ttth.teamcaring.service.mapper.GroupsMemberMapper;
import com.ttth.teamcaring.web.rest.GroupsMemberResource;
import com.ttth.teamcaring.web.rest.errors.BadRequestAlertException;

/**
 * Service Implementation for managing GroupsMember.
 *
 * @author Dai Mai
 */
@Service
@Transactional
public class GroupsMemberServiceImpl implements GroupsMemberService {

    /** The log. */
    private final Logger                       log = LoggerFactory
            .getLogger(GroupsMemberServiceImpl.class);

    /** The groups member repository. */
    private final GroupsMemberRepository       groupsMemberRepository;

    /** The groups member mapper. */
    private final GroupsMemberMapper           groupsMemberMapper;

    /** The groups member search repository. */
    private final GroupsMemberSearchRepository groupsMemberSearchRepository;

    /** The notification service. */
    @Inject
    private NotificationService                notificationService;

    /**
     * Instantiates a new groups member service impl.
     *
     * @param groupsMemberRepository
     *        the groups member repository
     * @param groupsMemberMapper
     *        the groups member mapper
     * @param groupsMemberSearchRepository
     *        the groups member search repository
     */
    public GroupsMemberServiceImpl(GroupsMemberRepository groupsMemberRepository,
            GroupsMemberMapper groupsMemberMapper,
            GroupsMemberSearchRepository groupsMemberSearchRepository) {
        this.groupsMemberRepository = groupsMemberRepository;
        this.groupsMemberMapper = groupsMemberMapper;
        this.groupsMemberSearchRepository = groupsMemberSearchRepository;
    }

    /**
     * Save a groupsMember.
     *
     * @param groupsMemberDTO
     *        the entity to save
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
     * Get all the groupsMembers.
     *
     * @param pageable
     *        the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<GroupsMemberDTO> findAll(Pageable pageable) {
        log.debug("Request to get all GroupsMembers");
        return groupsMemberRepository.findAll(pageable).map(groupsMemberMapper::toDto);
    }

    /**
     * Get one groupsMember by id.
     *
     * @param id
     *        the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public GroupsMemberDTO findOne(Long id) {
        log.debug("Request to get GroupsMember : {}", id);
        GroupsMember groupsMember = groupsMemberRepository.findOne(id);
        return groupsMemberMapper.toDto(groupsMember);
    }

    /**
     * Delete the groupsMember by id.
     *
     * @param id
     *        the id of the entity
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
     * @param query
     *        the query of the search
     * @param pageable
     *        the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<GroupsMemberDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of GroupsMembers for query {}", query);
        Page<GroupsMember> result = groupsMemberSearchRepository.search(queryStringQuery(query),
                pageable);
        return result.map(groupsMemberMapper::toDto);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ttth.teamcaring.service.GroupsMemberService#findOneEntity(java.lang.
     * Long)
     */
    @Override
    public GroupsMember findOneEntity(Long groupsMemberId) {
        return this.groupsMemberRepository.findOne(groupsMemberId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ttth.teamcaring.service.GroupsMemberService#mapToDto(com.ttth.
     * teamcaring.domain.GroupsMember)
     */
    @Override
    public GroupsMemberDTO mapToDto(GroupsMember groupsMember) {
        return this.groupsMemberMapper.toDto(groupsMember);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ttth.teamcaring.service.GroupsMemberService#findOneByMemberInGroup(
     * java.lang.Long, java.lang.Long)
     */
    @Override
    public GroupsMemberDTO findOneByMemberInGroup(Long memberCustomUserId, Long groupId) {
        return this.groupsMemberMapper
                .toDto(this.findOneEntityByMemberInGroup(memberCustomUserId, groupId));
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ttth.teamcaring.service.GroupsMemberService#
     * findOneEntityByMemberInGroup(java.lang.Long, java.lang.Long)
     */
    @Override
    public GroupsMember findOneEntityByMemberInGroup(Long memberCustomUserId, Long groupId) {
        return this.groupsMemberRepository.findOneByCustomUserIdAndGroupsId(memberCustomUserId,
                groupId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ttth.teamcaring.service.GroupsMemberService#
     * findOneEntityByMemberAndTeam(java.lang.Long, java.lang.Long)
     */
    @Override
    public GroupsMember findOneEntityByMemberAndTeam(Long memberCustomUserId, Long teamId) {
        return this.groupsMemberRepository.findOneByCustomUserIdAndGroupsTeamId(memberCustomUserId,
                teamId);
    }
    
    /* (non-Javadoc)
     * @see com.ttth.teamcaring.service.GroupsMemberService#getOneEntityByMemberAndTeam(java.lang.Long, java.lang.Long)
     */
    @Override
    public GroupsMember getOneEntityByMemberAndTeam(Long memberCustomUserId, Long teamId) {
        GroupsMember groupsMember = this.groupsMemberRepository.findOneByCustomUserIdAndGroupsTeamId(memberCustomUserId,
                teamId);
        if (Objects.isNull(groupsMember))
            throw new BadRequestAlertException(
                    MessageFormat.format("User {0} not member of team {1}",
                            memberCustomUserId, teamId),
                    GroupsMemberResource.ENTITY_NAME, "userNotMember");
        return groupsMember;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ttth.teamcaring.service.GroupsMemberService#mapToEntity(com.ttth.
     * teamcaring.service.dto.GroupsMemberDTO)
     */
    @Override
    public GroupsMember mapToEntity(GroupsMemberDTO groupsMemberDTO) {
        return this.groupsMemberMapper.toEntity(groupsMemberDTO);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ttth.teamcaring.service.GroupsMemberService#removeAllPendingRequest(
     * java.lang.Long, java.lang.Long)
     */
    @Override
    @Async
    public void removeAllPendingRequest(Long memberCustomUserId, Long teamId) {
        this.log.debug("Remove all pending request of member {} in team {}", memberCustomUserId,
                teamId);
        List<GroupsMember> deleted = this.groupsMemberRepository
                .deleteByCustomUserIdAndGroupsTeamIdAndStatus(memberCustomUserId, teamId,
                        GroupsMember.STATUS_PENDING);
        deleted.addAll(this.groupsMemberRepository.deleteByCustomUserIdAndGroupsTeamIdAndStatus(
                memberCustomUserId, teamId, GroupsMember.STATUS_REJECTED));
        log.debug("Total records: {}", deleted.size());

        // Delete relate notifications
        this.notificationService.deleteAllRequestJoinTeamNotifications(deleted);
    }

}
