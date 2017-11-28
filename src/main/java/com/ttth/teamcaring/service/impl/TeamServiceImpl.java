package com.ttth.teamcaring.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.util.Optional;

import javax.inject.Inject;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ttth.teamcaring.domain.Team;
import com.ttth.teamcaring.repository.TeamRepository;
import com.ttth.teamcaring.repository.search.TeamSearchRepository;
import com.ttth.teamcaring.service.CustomUserService;
import com.ttth.teamcaring.service.GroupsService;
import com.ttth.teamcaring.service.TeamService;
import com.ttth.teamcaring.service.dto.CreateTeamDTO;
import com.ttth.teamcaring.service.dto.CustomUserDTO;
import com.ttth.teamcaring.service.dto.GroupsDTO;
import com.ttth.teamcaring.service.dto.OfficalGroupDTO;
import com.ttth.teamcaring.service.dto.TeamDTO;
import com.ttth.teamcaring.service.mapper.TeamMapper;

/**
 * Service Implementation for managing Team.
 *
 * @author Dai Mai
 */
@Service
@Transactional
public class TeamServiceImpl implements TeamService{

    /** The log. */
    private final Logger log = LoggerFactory.getLogger(TeamServiceImpl.class);

    /** The team repository. */
    private final TeamRepository teamRepository;

    /** The team mapper. */
    private final TeamMapper teamMapper;

    /** The team search repository. */
    private final TeamSearchRepository teamSearchRepository;
    
    @Inject
    private GroupsService groupsService;
    
    @Inject
    private CustomUserService customUserService;

    /**
     * Instantiates a new team service impl.
     *
     * @param teamRepository the team repository
     * @param teamMapper the team mapper
     * @param teamSearchRepository the team search repository
     */
    public TeamServiceImpl(TeamRepository teamRepository, TeamMapper teamMapper, TeamSearchRepository teamSearchRepository) {
        this.teamRepository = teamRepository;
        this.teamMapper = teamMapper;
        this.teamSearchRepository = teamSearchRepository;
    }
    
    /* (non-Javadoc)
     * @see com.ttth.teamcaring.service.TeamService#create(com.ttth.teamcaring.service.dto.CreateTeamDTO)
     */
    @Override
    public CreateTeamDTO create(CreateTeamDTO createTeamDTO) {
    	Optional<CustomUserDTO> optCustomUser = this.customUserService.findOneByCurrentAuthorize();
    	if (optCustomUser.isPresent()) {
    		return this.create(createTeamDTO, optCustomUser.get().getUserId());
    	} else {
    		return null;
    	}
    }
    
    @Override
    public CreateTeamDTO create(CreateTeamDTO createTeamDTO, Long userId) {
    	// Create group
    	GroupsDTO groupsDTO = this.groupsService.save(createTeamDTO.getGroup());
    	
    	// Create Team
    	TeamDTO teamDTO = this.save(createTeamDTO);
    	
    	CreateTeamDTO result = (CreateTeamDTO) ObjectUtils.cloneIfPossible(teamDTO);
    	if (result != null) {
			result.setGroup((OfficalGroupDTO) groupsDTO);
		}    	
    	
    	return result;
    }

    /**
     * Save a team.
     *
     * @param teamDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public TeamDTO save(TeamDTO teamDTO) {
        log.debug("Request to save Team : {}", teamDTO);
        Team team = teamMapper.toEntity(teamDTO);
        team = teamRepository.save(team);
        TeamDTO result = teamMapper.toDto(team);
        teamSearchRepository.save(team);
        return result;
    }

    /**
     *  Get all the teams.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TeamDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Teams");
        return teamRepository.findAll(pageable)
            .map(teamMapper::toDto);
    }

    /**
     *  Get one team by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public TeamDTO findOne(Long id) {
        log.debug("Request to get Team : {}", id);
        Team team = teamRepository.findOne(id);
        return teamMapper.toDto(team);
    }

    /**
     *  Delete the  team by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Team : {}", id);
        teamRepository.delete(id);
        teamSearchRepository.delete(id);
    }

    /**
     * Search for the team corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TeamDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Teams for query {}", query);
        Page<Team> result = teamSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(teamMapper::toDto);
    }
}
