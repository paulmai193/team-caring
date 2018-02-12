/*
 * 
 */
package com.ttth.teamcaring.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ttth.teamcaring.domain.CustomUser;
import com.ttth.teamcaring.domain.Groups;
import com.ttth.teamcaring.domain.GroupsMember;
import com.ttth.teamcaring.domain.Team;
import com.ttth.teamcaring.repository.TeamRepository;
import com.ttth.teamcaring.repository.search.TeamSearchRepository;
import com.ttth.teamcaring.security.SecurityUtils;
import com.ttth.teamcaring.service.AppointmentService;
import com.ttth.teamcaring.service.CustomUserService;
import com.ttth.teamcaring.service.GroupsMemberService;
import com.ttth.teamcaring.service.GroupsService;
import com.ttth.teamcaring.service.NotificationService;
import com.ttth.teamcaring.service.TeamService;
import com.ttth.teamcaring.service.dto.AnwserResponseStatus;
import com.ttth.teamcaring.service.dto.CustomUserDTO;
import com.ttth.teamcaring.service.dto.GroupOfAppointmentDTO;
import com.ttth.teamcaring.service.dto.GroupsDTO;
import com.ttth.teamcaring.service.dto.GroupsMemberDTO;
import com.ttth.teamcaring.service.dto.ProfileDTO;
import com.ttth.teamcaring.service.dto.TeamDTO;
import com.ttth.teamcaring.service.dto.TeamDetailDTO;
import com.ttth.teamcaring.service.mapper.TeamMapper;
import com.ttth.teamcaring.web.rest.GroupsMemberResource;
import com.ttth.teamcaring.web.rest.TeamResource;
import com.ttth.teamcaring.web.rest.UserResource;
import com.ttth.teamcaring.web.rest.errors.BadRequestAlertException;
import com.ttth.teamcaring.web.rest.errors.ForbiddenException;
import com.ttth.teamcaring.web.rest.errors.InternalServerErrorException;
import com.ttth.teamcaring.web.rest.util.PaginationUtil;

/**
 * Service Implementation for managing Team.
 *
 * @author Dai Mai
 */
@Service
@Transactional
public class TeamServiceImpl implements TeamService {

    /** The log. */
    private final Logger               log = LoggerFactory.getLogger(TeamServiceImpl.class);

    /** The team repository. */
    private final TeamRepository       teamRepository;

    /** The team mapper. */
    private final TeamMapper           teamMapper;

    /** The team search repository. */
    private final TeamSearchRepository teamSearchRepository;

    /** The groups service. */
    @Inject
    private GroupsService              groupsService;

    /** The custom user service. */
    @Inject
    private CustomUserService          customUserService;

    /** The groups member service. */
    @Inject
    private GroupsMemberService        groupsMemberService;

    /** The notification service. */
    @Inject
    private NotificationService        notificationService;

    @Inject
    private AppointmentService         appointmentService;

    /**
     * Instantiates a new team service impl.
     *
     * @param teamRepository
     *        the team repository
     * @param teamMapper
     *        the team mapper
     * @param teamSearchRepository
     *        the team search repository
     */
    public TeamServiceImpl(TeamRepository teamRepository, TeamMapper teamMapper,
            TeamSearchRepository teamSearchRepository) {
        this.teamRepository = teamRepository;
        this.teamMapper = teamMapper;
        this.teamSearchRepository = teamSearchRepository;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ttth.teamcaring.service.TeamService#create(com.ttth.teamcaring.
     * service.dto.CreateTeamDTO)
     */
    @Override
    public TeamDTO create(TeamDTO teamDTO) {
        Optional<CustomUserDTO> optCustomUser = this.customUserService.findOneByCurrentAuthorize();
        if (optCustomUser.isPresent()) {
            return this.create(teamDTO, optCustomUser.get());
        }
        else {
            this.customUserService.createForCurrentAuthorize();
            return create(teamDTO);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ttth.teamcaring.service.TeamService#create(com.ttth.teamcaring.
     * service.dto.CreateTeamDTO, java.lang.Long)
     */
    @Override
    public TeamDTO create(TeamDTO teamDTO, Long userId) {
        Optional<CustomUserDTO> optCustomUser = this.customUserService.findOneByUserId(userId);
        if (optCustomUser.isPresent()) {
            return this.create(teamDTO, optCustomUser.get());
        }
        else {
            throw new InternalServerErrorException(
                    MessageFormat.format("Cannot create team, user {0} not exist", userId));
        }
    }

    /**
     * Creates the.
     *
     * @param teamDTO
     *        the team DTO
     * @param customUserDTO
     *        the custom user DTO
     * @return the team DTO
     */
    private TeamDTO create(TeamDTO teamDTO, CustomUserDTO customUserDTO) {
        CustomUser customUser = this.customUserService.mapFromDTO(customUserDTO);

        // Create Team
        teamDTO.setOwnerId(customUser.getId());
        TeamDTO result = this.save(teamDTO);

        // Create default group for this team
        GroupsDTO defaultGroupDTO = new GroupsDTO().leaderId(customUser.getId())
                .teamId(result.getId());
        this.groupsService.save(defaultGroupDTO);

        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ttth.teamcaring.service.TeamService#update(com.ttth.teamcaring.
     * service.dto.TeamDTO)
     */
    @Override
    public TeamDTO update(TeamDTO teamDTO) {
        String login = SecurityUtils.getCurrentUserLogin();
        Optional<CustomUserDTO> optCustomUser = this.customUserService.findOneByUserLogin(login);
        if (optCustomUser.isPresent()) {
            CustomUserDTO customUserDTO = optCustomUser.get();
            TeamDTO existTeamDTO = this.findOne(teamDTO.getId());
            if (Objects.isNull(existTeamDTO)) {
                throw new BadRequestAlertException(MessageFormat
                        .format("Cannot update team, team {0} not exist", teamDTO.getId()),
                        TeamResource.ENTITY_NAME, "teamNotExists");
            }
            else if (!existTeamDTO.getOwnerId().equals(customUserDTO.getId())) {
                // Denied if user not creator of this team
                throw new ForbiddenException("Cannot update team, user not team owner");
            }
            else {
                if (Objects.nonNull(teamDTO.getDescription())) {
                    existTeamDTO.setDescription(teamDTO.getDescription());
                }
                if (Objects.nonNull(teamDTO.getExtraGroupDescription())) {
                    existTeamDTO.setExtraGroupDescription(teamDTO.getExtraGroupDescription());
                }
                if (Objects.nonNull(teamDTO.getExtraGroupName())) {
                    existTeamDTO.setExtraGroupName(teamDTO.getExtraGroupName());
                }
                if (Objects.nonNull(teamDTO.getExtraGroupTotalMember())) {
                    existTeamDTO.setExtraGroupTotalMember(teamDTO.getExtraGroupTotalMember());
                }
                if (Objects.nonNull(teamDTO.getIconId())) {
                    existTeamDTO.setIconId(teamDTO.getIconId());
                }
                if (Objects.nonNull(teamDTO.getLevel())
                        && teamDTO.getLevel().compareTo(existTeamDTO.getLevel()) > 0) {
                    existTeamDTO.setLevel(teamDTO.getLevel());
                }
                if (Objects.nonNull(teamDTO.getName())) {
                    existTeamDTO.setName(teamDTO.getName());
                }
                if (Objects.nonNull(teamDTO.getTotalMember())) {
                    existTeamDTO.setTotalMember(teamDTO.getTotalMember());
                }
                return this.save(existTeamDTO);
            }
        }
        else {
            throw new InternalServerErrorException(
                    MessageFormat.format("Cannot update team, user {0} not exist", login));
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ttth.teamcaring.service.TeamService#save(com.ttth.teamcaring.service.
     * dto.TeamDTO)
     */
    @Override
    public TeamDTO save(TeamDTO teamDTO) {
        log.debug("Request to save Team : {}", teamDTO);
        Team team = teamMapper.toEntity(teamDTO);
        team = teamRepository.save(team);
        team = teamSearchRepository.save(team);
        TeamDTO result = teamMapper.toDto(team);
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ttth.teamcaring.service.TeamService#findAll(org.springframework.data.
     * domain.Pageable)
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TeamDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Teams");
        return teamRepository.findAll(pageable).map(teamMapper::toDto);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ttth.teamcaring.service.TeamService#findOne(java.lang.Long)
     */
    @Override
    @Transactional(readOnly = true)
    public TeamDTO findOne(Long id) {
        log.debug("Request to get Team : {}", id);
        Team team = teamRepository.findOne(id);
        return teamMapper.toDto(team);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ttth.teamcaring.service.TeamService#delete(java.lang.Long)
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Team : {}", id);
        teamRepository.delete(id);
        teamSearchRepository.delete(id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ttth.teamcaring.service.TeamService#search(java.lang.String,
     * org.springframework.data.domain.Pageable)
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TeamDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Teams for query {}", query);
        Page<Team> result = this.doSearch(query, pageable);
        return result.map(teamMapper::toDto);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ttth.teamcaring.service.TeamService#searchNewTeam(java.lang.String,
     * org.springframework.data.domain.Pageable)
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TeamDTO> searchNewTeam(String query, Pageable pageable) {
        log.debug("Request to search for a page of new teams for query {}", query);

        CustomUser customUser = this.customUserService
                .findOneEntityByUserLogin(SecurityUtils.getCurrentUserLogin());
        if (Objects.nonNull(customUser)) {

            // Find all team match query and not match those team IDs
            Set<TeamDTO> joinedTeams = this.findAllTeamOfUser(customUser);
            this.log.debug("Joined list size {}", joinedTeams.size());

            // Query results
            QueryStringQueryBuilder queryBuilder = new QueryStringQueryBuilder(query)
                    .field(Team.SEARCH_FIELD_NAME, 10F);
            List<TeamDTO> searchTeams = this.teamSearchRepository
                    .search(queryBuilder, new PageRequest(0, 10000)).map(this.teamMapper::toDto)
                    .getContent();
            this.log.debug("Search list size {}", searchTeams.size());

            // filter result
            List<TeamDTO> newTeams = new ArrayList<>(searchTeams);
            newTeams.removeAll(joinedTeams);
            this.log.debug("New list size {}", newTeams.size());

            return PaginationUtil.pagingList(newTeams, pageable);
        }
        else {
            // Or search all team match query
            return this.search(query, pageable);
        }
    }

    /**
     * Do search.
     *
     * @param query
     *        the query
     * @param pageable
     *        the pageable
     * @return the page
     */
    private Page<Team> doSearch(String query, Pageable pageable) {
        return teamSearchRepository.search(queryStringQuery(query), pageable);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ttth.teamcaring.service.TeamService#requestJoin(java.lang.String,
     * java.lang.Long, java.lang.Long)
     */
    @Override
    public void requestJoin(String login, Long teamId, Long leaderId) {
        CustomUser customUser = this.customUserService.findOneEntityByUserLogin(login);
        if (Objects.nonNull(customUser)) {
            // Some secure check
            Team team = this.teamRepository.findOne(teamId);
            if (Objects.isNull(team)) {
                throw new BadRequestAlertException(
                        MessageFormat.format("Cannot get team, team {0} not exist", teamId),
                        TeamResource.ENTITY_NAME, "teamNotExists");
            }
            CustomUser leaderCustomUser = this.customUserService.findOneEntityByUserId(leaderId);
            if (Objects.isNull(leaderCustomUser)) {
                throw new BadRequestAlertException(
                        MessageFormat.format("Cannot get leader, user {0} not exist", leaderId),
                        UserResource.ENTITY_NAME, "userNotExists");
            }

            // Find or create new group for this leaderId
            GroupsDTO groupsDTO = this.groupsService.findByTeamAndLeader(teamId,
                    leaderCustomUser.getId());
            if (Objects.isNull(groupsDTO)) {
                groupsDTO = new GroupsDTO().leaderId(leaderCustomUser.getId()).teamId(teamId);
                groupsDTO = this.groupsService.save(groupsDTO);
            }

            // Add member to this group
            GroupsMember groupsMember = this.groupsMemberService
                    .findOneEntityByMemberInGroup(customUser.getId(), groupsDTO.getId());
            GroupsMemberDTO groupsMemberDTO;
            if (Objects.isNull(groupsMember)) {
                groupsMemberDTO = new GroupsMemberDTO().customUserId(customUser.getId())
                        .groupsId(groupsDTO.getId()).status(GroupsMember.STATUS_PENDING).level(1);
                groupsMemberDTO = this.groupsMemberService.save(groupsMemberDTO);
                groupsMember = this.groupsMemberService.mapToEntity(groupsMemberDTO);
            }
            else {
                groupsMemberDTO = this.groupsMemberService.mapToDto(groupsMember);
            }

            // Send notification
            try {
                if (groupsMemberDTO.getStatus().equals(GroupsMember.STATUS_PENDING)) {
                    this.notificationService.sendRequestJoinTeam(customUser, leaderCustomUser, team,
                            groupsMemberDTO);
                }
                else {
                    this.notificationService.sendResponseJoinTeam(groupsMember);
                }
            }
            catch (Exception e) {
                this.log.error(MessageFormat
                        .format("Cannot send notification of request join team {0}", teamId), e);
                // Swallow this exception
            }

        }
        else {
            throw new InternalServerErrorException(
                    MessageFormat.format("Cannot join team, user {0} not exist", login));
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ttth.teamcaring.service.TeamService#responseJoin(java.lang.String,
     * java.lang.Long, com.ttth.teamcaring.service.dto.AnwserResponseStatus)
     */
    @Override
    public void responseJoin(String login, Long groupsMemberId,
            AnwserResponseStatus responseJoinTeamStatus) {
        CustomUser leaderCustomUser = this.customUserService.findOneEntityByUserLogin(login);
        if (Objects.nonNull(leaderCustomUser)) {
            GroupsMember groupsMember = this.groupsMemberService.findOneEntity(groupsMemberId);
            if (Objects.isNull(groupsMember)) {
                throw new BadRequestAlertException(
                        MessageFormat.format("Cannot response join team, request ID {0} not exist",
                                groupsMemberId),
                        GroupsMemberResource.ENTITY_NAME, "groupsMemberNotExists");
            }
            if (!groupsMember.getGroups().getLeader().equals(leaderCustomUser)) {
                throw new BadRequestAlertException(
                        MessageFormat.format(
                                "Cannot response join team, user ID {0} not the leader",
                                leaderCustomUser.getUser().getId()),
                        GroupsMemberResource.ENTITY_NAME, "notLeader");
            }
            if (groupsMember.getStatus().equals(GroupsMember.STATUS_PENDING)) {
                switch (responseJoinTeamStatus) {
                    case accept:
                        groupsMember.setStatus(GroupsMember.STATUS_ACCEPTED);
                        this.groupsMemberService
                                .save(this.groupsMemberService.mapToDto(groupsMember));

                        // Delete all pending request
                        this.groupsMemberService.removeAllPendingRequest(
                                groupsMember.getCustomUser().getId(),
                                groupsMember.getGroups().getTeam().getId());
                        break;

                    case reject:
                        groupsMember.setStatus(GroupsMember.STATUS_REJECTED);
                        this.groupsMemberService.delete(groupsMemberId);
                        break;

                    default:
                        throw new BadRequestAlertException(
                                MessageFormat.format(
                                        "Cannot response join team, status {0} not valid",
                                        responseJoinTeamStatus),
                                GroupsMemberResource.ENTITY_NAME, "wrongStatus");
                }
            }

            // Send notification
            try {
                this.notificationService.sendResponseJoinTeam(groupsMember);
            }
            catch (Exception e) {
                this.log.error(MessageFormat.format(
                        "Cannot send notification of response join team request ID {0}",
                        groupsMemberId), e);
                // Swallow this exception
            }
        }
        else {
            throw new InternalServerErrorException(
                    MessageFormat.format("Cannot response join team, user {0} not exist", login));
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ttth.teamcaring.service.TeamService#getTeamDetailOfAuthorizedUser(
     * java.lang.Long)
     */
    @Override
    public TeamDetailDTO getTeamDetailOfAuthorizedUser(Long teamId) {
        String login = SecurityUtils.getCurrentUserLogin();
        this.log.debug("Get detail of team {} of user {}", teamId, login);
        CustomUser leaderCustomUser = this.customUserService.findOneEntityByUserLogin(login);
        if (Objects.nonNull(leaderCustomUser)) {
            TeamDTO teamDTO = this.findOne(teamId);
            if (Objects.nonNull(teamDTO)) {
                TeamDetailDTO detailDTO = teamDTO.toTeamDetailDto();

                // Get level of leader
                Integer level = this.getLevelOfMemberInTeam(leaderCustomUser, teamId);
                detailDTO.setMemberLevel(level);

                // Add members information to ProfileDTO
                Set<ProfileDTO> members = this.getMembersOfLeaderInTeam(leaderCustomUser, teamId);
                detailDTO.setMembers(members);
                this.log.debug("Team {} of user {} have {} members", teamDTO.getName(),
                        leaderCustomUser.getFullName(), members.size());

                return detailDTO;
            }
            else {
                return null;
            }
        }
        else {
            throw new InternalServerErrorException(
                    MessageFormat.format("Cannot get team detail, user {0} not exist", login));
        }
    }

    /**
     * Gets the level of member in team.
     *
     * @param memberCustomUser
     *        the member custom user
     * @param teamId
     *        the team id
     * @return the level of member in team
     */
    @Override
    public Integer getLevelOfMemberInTeam(CustomUser memberCustomUser, Long teamId) {
        this.log.debug("Get level of member {} in team {}", memberCustomUser.getFullName(), teamId);
        Integer level = 0;
        Optional<Team> optLeaderTeam = memberCustomUser.getOwners().stream()
                .filter(team -> team.getId().equals(teamId)).findAny();
        if (optLeaderTeam.isPresent()) {
            level = optLeaderTeam.get().getLevel();
            this.log.debug("Member is owner of team, level {}", level);
        }
        else {
            GroupsMember leaderGroups = this.groupsMemberService
                    .findOneEntityByMemberAndTeam(memberCustomUser.getId(), teamId);
            if (Objects.nonNull(leaderGroups)) {
                level = leaderGroups.getLevel();
                this.log.debug("Member is member of team, level {}", level);
            }
            else {
                this.log.warn("User {} not member of team", memberCustomUser.getFullName());
            }
        }
        return level;
    }

    /**
     * Gets the members of leader in team.
     *
     * @param leaderCustomUser
     *        the leader custom user
     * @param teamId
     *        the team id
     * @return the members of leader in team
     */
    private Set<ProfileDTO> getMembersOfLeaderInTeam(CustomUser leaderCustomUser, Long teamId) {
        this.log.debug("Get members of leader {} in team {}", leaderCustomUser.getFullName(),
                teamId);

        // Get all groups which this user is leader in team
        Set<Groups> leaders = leaderCustomUser.getLeaders().stream()
                .filter(group -> group.getTeam().getId().equals(teamId))
                .collect(Collectors.toSet());
        this.log.debug("Leader {} have {} groups", leaderCustomUser.getFullName(), leaders.size());

        // Get all members in those groups
        Set<ProfileDTO> members = new HashSet<>();
        leaders.forEach(group -> {
            Set<GroupsMember> groupsMembers = group.getMembers();
            groupsMembers.forEach(groupsMember -> {
                if (groupsMember.getStatus().equals(GroupsMember.STATUS_ACCEPTED)) {
                    CustomUser memberCustomUser = groupsMember.getCustomUser();
                    Set<GroupOfAppointmentDTO> setGroupAppointments = this.appointmentService
                            .findAllOfMemberInTeam(leaderCustomUser,
                                    memberCustomUser.getUser().getId(), teamId);
                    int numberAppointment = 0;
                    for (GroupOfAppointmentDTO element : setGroupAppointments) {
                        numberAppointment += element.getList().size();
                    }
                    ProfileDTO memberProfileDTO = this.customUserService
                            .mapFromEntity(memberCustomUser).castToSimpleProfileDto()
                            .joinedTeam(this.groupsMemberService.mapToDto(groupsMember))
                            .numberAppointments(numberAppointment);

                    // Try to get sub member of this member
                    Set<ProfileDTO> subMembers = this.getMembersOfLeaderInTeam(memberCustomUser,
                            teamId);
                    memberProfileDTO.setMembers(subMembers);
                    members.add(memberProfileDTO);
                }
            });
            this.log.debug("Group {} have {} members", group.getId(), members.size());
        });

        return members;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ttth.teamcaring.service.TeamService#findAllOfAuthorizedUser(org.
     * springframework.data.domain.Pageable)
     */
    @Override
    public Page<TeamDTO> findAllOfAuthorizedUser(Pageable pageable) {
        String login = SecurityUtils.getCurrentUserLogin();
        CustomUser customUser = this.customUserService.findOneEntityByUserLogin(login);
        if (Objects.nonNull(customUser)) {
            Set<TeamDTO> teams = this.findAllTeamOfUser(customUser);

            return PaginationUtil.pagingList(new ArrayList<>(teams), pageable);
        }
        else {
            throw new InternalServerErrorException(
                    MessageFormat.format("Cannot get team detail, user {0} not exist", login));
        }
    }

    /**
     * Find all team of user.
     *
     * @param customUser
     *        the custom user
     * @return the sets the
     */
    private Set<TeamDTO> findAllTeamOfUser(CustomUser customUser) {
        Set<TeamDTO> teams = new HashSet<>();

        // Get all team this user is leader
        Set<Groups> leaders = customUser.getLeaders();
        this.log.debug("User {} is leader of {} teams", customUser.getUser().getId(),
                leaders.size());
        customUser.getLeaders().forEach(group -> {
            try {
                teams.add(this.teamMapper.toDto(group.getTeam()));
            }
            catch (Exception e) {
                this.log.warn("Team of group {} not valid", group.getId());
                // Swallow this exception
            }
        });

        // Get all team this user is member
        Set<GroupsMember> members = customUser.getMembers();
        this.log.debug("User {} requested or joined in {} team", customUser.getUser().getId(),
                members.size());
        customUser.getMembers().forEach(groupMember -> {
            try {
                if (groupMember.getStatus().equals(GroupsMember.STATUS_ACCEPTED)) {
                    teams.add(this.teamMapper.toDto(groupMember.getGroups().getTeam()));
                }
            }
            catch (Exception e) {
                this.log.warn("Team of groupmember {} not valid", groupMember.getId());
                // Swallow this exception
            }
        });

        return teams;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ttth.teamcaring.service.TeamService#searchOfAuthorizedUser(java.lang.
     * String, org.springframework.data.domain.Pageable)
     */
    @Override
    public Page<TeamDTO> searchOfAuthorizedUser(String query, Pageable pageable) {
        String login = SecurityUtils.getCurrentUserLogin();
        CustomUser customUser = this.customUserService.findOneEntityByUserLogin(login);
        if (Objects.nonNull(customUser)) {
            // Find all team match query and not match those team IDs
            Set<TeamDTO> joinedTeams = this.findAllTeamOfUser(customUser);
            this.log.debug("Joined list size {}", joinedTeams.size());

            // Query results
            QueryStringQueryBuilder queryBuilder = new QueryStringQueryBuilder(query)
                    .field(Team.SEARCH_FIELD_NAME, 10F);
            List<TeamDTO> searchTeams = this.teamSearchRepository
                    .search(queryBuilder, new PageRequest(0, 10000)).map(this.teamMapper::toDto)
                    .getContent();
            this.log.debug("Searched list size {}", searchTeams.size());

            // filter result
            List<TeamDTO> tempList = new ArrayList<>(searchTeams);
            tempList.retainAll(joinedTeams);
            this.log.debug("Joined match list size {}", tempList.size());

            return PaginationUtil.pagingList(tempList, pageable);
        }
        else {
            throw new InternalServerErrorException(
                    MessageFormat.format("Cannot get team detail, user {0} not exist", login));
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ttth.teamcaring.service.TeamService#updateMemberLevel(java.lang.
     * String, java.lang.Long, java.lang.Integer)
     */
    @Override
    public void updateMemberLevel(String login, Long groupsMemberId, Integer level) {
        CustomUser leaderCustomUser = this.customUserService.findOneEntityByUserLogin(login);
        if (Objects.nonNull(leaderCustomUser)) {
            GroupsMember groupsMember = this.groupsMemberService.findOneEntity(groupsMemberId);
            if (Objects.isNull(groupsMember)) {
                throw new BadRequestAlertException(
                        MessageFormat.format("Cannot update level, member ID {0} not exist",
                                groupsMemberId),
                        GroupsMemberResource.ENTITY_NAME, "groupsMemberNotExists");
            }
            if (!groupsMember.getGroups().getLeader().equals(leaderCustomUser)) {
                throw new BadRequestAlertException(
                        MessageFormat.format("Cannot update level, user ID {0} not the leader",
                                leaderCustomUser.getUser().getId()),
                        GroupsMemberResource.ENTITY_NAME, "notLeader");
            }
            if (Integer.compare(groupsMember.getLevel(), level) > 0) {
                throw new BadRequestAlertException(
                        MessageFormat.format("Cannot update level, new level must greater than {0}",
                                groupsMember.getLevel()),
                        GroupsMemberResource.ENTITY_NAME, "mustGreaterThanRecent");
            }
            // Check new level not greater than leader level, which get in
            // current group member given by this leader ID
            Integer maxLevel = this.getLevelOfMemberInTeam(leaderCustomUser,
                    groupsMember.getGroups().getTeam().getId());
            if (Integer.compare(maxLevel, level) < 0) {
                throw new BadRequestAlertException(
                        MessageFormat.format("Cannot update level, new level must smaller than {0}",
                                maxLevel),
                        GroupsMemberResource.ENTITY_NAME, "mustSmallerThanLeader");
            }
            // Pass all validate
            groupsMember.setLevel(level);
            this.groupsMemberService.save(this.groupsMemberService.mapToDto(groupsMember));
        }
        else {
            throw new InternalServerErrorException(
                    MessageFormat.format("Cannot update level, user {0} not exist", login));
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ttth.teamcaring.service.TeamService#updateTeamLevel(java.lang.String,
     * java.lang.Long, java.lang.Integer)
     */
    @Override
    public void updateTeamLevel(String login, Long teamId, Integer level) {
        CustomUser leaderCustomUser = this.customUserService.findOneEntityByUserLogin(login);
        if (Objects.nonNull(leaderCustomUser)) {
            Optional<Team> optTeam = leaderCustomUser.getOwners().stream()
                    .filter(team -> team.getId().equals(teamId)).findAny();
            if (!optTeam.isPresent()) {
                throw new BadRequestAlertException(
                        MessageFormat.format("Cannot update level, user {0} not team owner",
                                leaderCustomUser.getFullName()),
                        TeamResource.ENTITY_NAME, "notLeader");
            }
            Team team = optTeam.get();
            if (Integer.compare(team.getLevel(), level) > 0) {
                throw new BadRequestAlertException(
                        MessageFormat.format("Cannot update level, new level must larger than {0}",
                                team.getLevel()),
                        TeamResource.ENTITY_NAME, "mustGreaterThanRecent");
            }
            team.setLevel(level);
            this.save(this.teamMapper.toDto(team));
        }
        else {
            throw new InternalServerErrorException(
                    MessageFormat.format("Cannot update level, user {0} not exist", login));
        }
    }

}
