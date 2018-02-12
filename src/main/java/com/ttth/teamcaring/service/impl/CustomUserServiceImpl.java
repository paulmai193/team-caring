/*
 * 
 */
package com.ttth.teamcaring.service.impl;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

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
import com.ttth.teamcaring.domain.GroupsMember;
import com.ttth.teamcaring.domain.User;
import com.ttth.teamcaring.repository.CustomUserRepository;
import com.ttth.teamcaring.repository.UserRepository;
import com.ttth.teamcaring.repository.search.CustomUserSearchRepository;
import com.ttth.teamcaring.security.SecurityUtils;
import com.ttth.teamcaring.service.CustomUserService;
import com.ttth.teamcaring.service.GroupsMemberService;
import com.ttth.teamcaring.service.UserService;
import com.ttth.teamcaring.service.dto.CustomUserDTO;
import com.ttth.teamcaring.service.dto.ProfileDTO;
import com.ttth.teamcaring.service.dto.SimpleGroupsMemberDTO;
import com.ttth.teamcaring.service.mapper.CustomUserMapper;
import com.ttth.teamcaring.web.rest.UserResource;
import com.ttth.teamcaring.web.rest.errors.BadRequestAlertException;
import com.ttth.teamcaring.web.rest.errors.InternalServerErrorException;
import com.ttth.teamcaring.web.rest.util.PaginationUtil;

/**
 * Service Implementation for managing CustomUser.
 *
 * @author Dai Mai
 */
@Service
@Transactional
public class CustomUserServiceImpl implements CustomUserService {

    /** The log. */
    private final Logger                     log = LoggerFactory
            .getLogger(CustomUserServiceImpl.class);

    /** The custom user repository. */
    private final CustomUserRepository       customUserRepository;

    /** The custom user mapper. */
    private final CustomUserMapper           customUserMapper;

    /** The custom user search repository. */
    private final CustomUserSearchRepository customUserSearchRepository;

    /** The user repository. */
    @Inject
    private UserRepository                   userRepository;

    /** The user service. */
    @Inject
    private UserService                      userService;

    /** The groups member service. */
    @Inject
    private GroupsMemberService              groupsMemberService;

    /**
     * Instantiates a new custom user service impl.
     *
     * @param customUserRepository
     *        the custom user repository
     * @param customUserMapper
     *        the custom user mapper
     * @param customUserSearchRepository
     *        the custom user search repository
     */
    public CustomUserServiceImpl(CustomUserRepository customUserRepository,
            CustomUserMapper customUserMapper,
            CustomUserSearchRepository customUserSearchRepository) {
        this.customUserRepository = customUserRepository;
        this.customUserMapper = customUserMapper;
        this.customUserSearchRepository = customUserSearchRepository;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ttth.teamcaring.service.CustomUserService#createForCurrentAuthorize()
     */
    @Override
    public CustomUserDTO createForCurrentAuthorize() {
        String login = SecurityUtils.getCurrentUserLogin();
        Optional<User> optUser = this.userRepository.findOneByLogin(login);
        if (optUser.isPresent()) {
            return this.create(optUser.get());
        }
        else {
            throw new BadRequestAlertException(
                    MessageFormat.format("Cannot create custom user, user {0} not exist", login),
                    UserResource.ENTITY_NAME, "userNotExists");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ttth.teamcaring.service.CustomUserService#create(com.ttth.teamcaring.
     * domain.User)
     */
    @Override
    public CustomUserDTO create(User user) {
        CustomUserDTO customUserDTO = new CustomUserDTO();
        customUserDTO.setUserId(user.getId());
        return this.save(customUserDTO);
    }

    /**
     * Save a customUser.
     *
     * @param customUserDTO
     *        the entity to save
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
     * Get all the customUsers.
     *
     * @param pageable
     *        the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CustomUserDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CustomUsers");
        return customUserRepository.findAll(pageable).map(customUserMapper::toDto);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ttth.teamcaring.service.CustomUserService#findOneByCurrentAuthorize()
     */
    @Override
    public Optional<CustomUserDTO> findOneByCurrentAuthorize() {
        String login = SecurityUtils.getCurrentUserLogin();
        return this.findOneByUserLogin(login);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ttth.teamcaring.service.CustomUserService#findOneByUserLogin(java.
     * lang.String)
     */
    @Override
    public Optional<CustomUserDTO> findOneByUserLogin(String login) {
        Optional<User> optUser = this.userRepository.findOneByLogin(login);
        if (optUser.isPresent()) {
            return this.findOneByUserId(optUser.get().getId());
        }
        else {
            return Optional.empty();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ttth.teamcaring.service.CustomUserService#findOneEntityByUserLogin(
     * java.lang.String)
     */
    @Override
    public CustomUser findOneEntityByUserLogin(String login) {
        log.debug("Request to get CustomUser by login: {}", login);
        Optional<User> optUser = this.userRepository.findOneByLogin(login);
        if (optUser.isPresent()) {
            return this.findOneEntityByUserId(optUser.get().getId());
        }
        else {
            return null;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ttth.teamcaring.service.CustomUserService#getOneEntityByUserLogin(java.lang.String)
     */
    @Override
    public CustomUser getOneEntityByUserLogin(String login) throws InternalServerErrorException {
        CustomUser customUser = this.findOneEntityByUserLogin(login);
        if (Objects.isNull(customUser))
            throw new InternalServerErrorException(
                    MessageFormat.format("Cannot create appointment, user {0} not exist", login));
        return customUser;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ttth.teamcaring.service.CustomUserService#findOneEntityByUserId(java.
     * lang.Long)
     */
    @Override
    public CustomUser findOneEntityByUserId(Long userId) {
        log.debug("Request to get CustomUser by user ID : {}", userId);
        return this.customUserRepository.findOneByUserId(userId).orElse(null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ttth.teamcaring.service.CustomUserService#findOneByUserId(java.lang.
     * Long)
     */
    @Override
    public Optional<CustomUserDTO> findOneByUserId(Long userId) {
        Optional<CustomUser> optCustomUser = this.customUserRepository.findOneByUserId(userId);
        if (optCustomUser.isPresent()) {
            return Optional.ofNullable(customUserMapper.toDto(optCustomUser.get()));
        }
        else {
            return Optional.empty();
        }
    }

    /**
     * Get one customUser by id.
     *
     * @param id
     *        the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public CustomUserDTO findOne(Long id) {
        log.debug("Request to get CustomUserDTO : {}", id);
        CustomUser customUser = this.findOneEntity(id);
        return customUserMapper.toDto(customUser);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ttth.teamcaring.service.CustomUserService#findOneEntity(java.lang.
     * Long)
     */
    @Override
    @Transactional(readOnly = true)
    public CustomUser findOneEntity(Long id) {
        log.debug("Request to get CustomUser : {}", id);
        return customUserRepository.findOne(id);
    }

    /**
     * Delete the customUser by id.
     *
     * @param id
     *        the id of the entity
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
     * @param query
     *        the query of the search
     * @param pageable
     *        the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CustomUserDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CustomUsers for query {}", query);
        QueryStringQueryBuilder queryBuilder = new QueryStringQueryBuilder(query)
                .field(CustomUser.SEARCH_FIELD_NICK_NAME, 10F)
                .field(CustomUser.SEARCH_FIELD_FULL_NAME, 5F);
        Page<CustomUser> result = customUserSearchRepository.search(queryBuilder, pageable);
        return result.map(customUserMapper::toDto);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ttth.teamcaring.service.CustomUserService#mapFromDTO(com.ttth.
     * teamcaring.service.dto.CustomUserDTO)
     */
    @Override
    public CustomUser mapFromDTO(CustomUserDTO customUserDTO) {
        return this.customUserMapper.toEntity(customUserDTO);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ttth.teamcaring.service.CustomUserService#mapFromEntity(com.ttth.
     * teamcaring.domain.CustomUser)
     */
    @Override
    public CustomUserDTO mapFromEntity(CustomUser customUser) {
        return this.customUserMapper.toDto(customUser);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ttth.teamcaring.service.CustomUserService#getUserProfileByUserLogin(
     * java.lang.String)
     */
    @Override
    @Transactional(readOnly = true)
    public ProfileDTO getUserProfileByUserLogin(String login) {
        CustomUser customUser = this.getOneEntityByUserLogin(login);
        ProfileDTO profileDTO = this.mapFromEntity(customUser).castToProfileDto();
        profileDTO.setNumberAppointments(this.getNumberAppointmentOfUser(customUser));
        return profileDTO;
    }
    
    /* (non-Javadoc)
     * @see com.ttth.teamcaring.service.CustomUserService#getNumberAppointmentOfUser(com.ttth.teamcaring.domain.CustomUser)
     */
    @Override
    public int getNumberAppointmentOfUser(CustomUser customUser) {
        return customUser.getAppointments().size() + customUser.getAttendees().size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ttth.teamcaring.service.CustomUserService#updateUserProfile(com.ttth.
     * teamcaring.domain.User, com.ttth.teamcaring.service.dto.ProfileDTO)
     */
    @Override
    public ProfileDTO updateUserProfile(User user, ProfileDTO profileDTO) {
        // Clear activation key to mark user activated
        user.setActivationKey(null);

        // Update avatar & email if have
        if (Objects.nonNull(profileDTO.getImageUrl())) {
            user.setImageUrl(profileDTO.getImageUrl());
        }
        if (Objects.nonNull(profileDTO.getEmail())) {
            user.setEmail(profileDTO.getEmail());
        }
        this.userService.updateUser(user);

        Optional<CustomUser> optCustomUser = this.customUserRepository
                .findOneByUserId(user.getId());
        CustomUser customUser;
        if (optCustomUser.isPresent()) {
            customUser = optCustomUser.get();
        }
        else {
            customUser = new CustomUser();
            customUser.user(user);
            customUser = this.customUserRepository.save(customUser);
        }
        // Update user profile
        if (Objects.nonNull(profileDTO.getFullName())) {
            customUser.setFullName(profileDTO.getFullName());
        }
        if (Objects.nonNull(profileDTO.getNickname())) {
            customUser.setNickname(profileDTO.getNickname());
        }
        if (Objects.nonNull(profileDTO.getPushToken())) {
            customUser.setPushToken(profileDTO.getPushToken());
        }
        if (Objects.nonNull(profileDTO.getExtraGroupDescription())) {
            customUser.setExtraGroupDescription(profileDTO.getExtraGroupDescription());
        }
        if (Objects.nonNull(profileDTO.getExtraGroupName())) {
            customUser.setExtraGroupName(profileDTO.getExtraGroupName());
        }
        if (Objects.nonNull(profileDTO.getExtraGroupTotalMember())) {
            customUser.setExtraGroupTotalMember(profileDTO.getExtraGroupTotalMember());
        }
        customUser = this.customUserSearchRepository.save(customUser);
        profileDTO = this.customUserMapper.toDto(customUser).castToProfileDto();
        profileDTO.setImageUrl(user.getImageUrl());
        log.debug("Changed profile for User: {}", customUser);

        return profileDTO;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ttth.teamcaring.service.CustomUserService#getUserProfileByUserId(java
     * .lang.Long)
     */
    @Override
    @Transactional(readOnly = true)
    public ProfileDTO getUserProfileByUserId(Long userId) {
        Optional<User> optUser = this.userRepository.findOneById(userId);
        if (optUser.isPresent()) {
            return this.getUserProfile(optUser.get()).castToSimpleProfileDto();
        }
        else {
            return null;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ttth.teamcaring.service.CustomUserService#searchLeadersInTeam(java.
     * lang.Long, java.lang.String, org.springframework.data.domain.Pageable)
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ProfileDTO> searchLeadersInTeam(Long teamId, String queryLeaderName,
            Pageable pageable) {
        CustomUser authorized = this.findOneEntityByUserLogin(SecurityUtils.getCurrentUserLogin());
        log.debug(
                "Request to search for a page of leader Teams {} for query {} (exclude current authorized user)",
                teamId, queryLeaderName);
        Set<ProfileDTO> leaders = new HashSet<>();
        QueryStringQueryBuilder queryBuilder = new QueryStringQueryBuilder(queryLeaderName)
                .field(CustomUser.SEARCH_FIELD_NICK_NAME, 10F)
                .field(CustomUser.SEARCH_FIELD_FULL_NAME, 5F);
        Page<CustomUser> searchResults = this.customUserSearchRepository.search(queryBuilder,
                new PageRequest(0, 10000));
        this.log.debug("Total search by query records: {}", searchResults.getTotalElements());
        searchResults.forEach(tmpCustomUser -> {
            CustomUser customUserLeader = this.customUserRepository.findOne(tmpCustomUser.getId());
            if (!customUserLeader.equals(authorized)) {
                // User is owner of teams
                customUserLeader.getOwners().stream()
                        .filter(ownerTeam -> ownerTeam.getId().equals(teamId)).findAny()
                        .ifPresent(team -> {
                            // Custom Simple group member DTO to match team
                            // owner level
                            SimpleGroupsMemberDTO teamOwnerLevel = new SimpleGroupsMemberDTO()
                                    .id(teamId).level(team.getLevel());
                            leaders.add(this.customUserMapper.toDto(customUserLeader)
                                    .castToSimpleProfileDto().joinedTeam(teamOwnerLevel));
                        });

                // User is member of groups
                customUserLeader.getMembers().stream().filter(
                        groupsMember -> groupsMember.getGroups().getTeam().getId().equals(teamId)
                                && groupsMember.getStatus().equals(GroupsMember.STATUS_ACCEPTED))
                        .forEach(groupsMember -> {
                            leaders.add(this.customUserMapper.toDto(customUserLeader)
                                    .castToSimpleProfileDto()
                                    .joinedTeam(this.groupsMemberService.mapToDto(groupsMember)));
                        });
                ;
            }
        });
        return PaginationUtil.pagingList(leaders, pageable);
    }

    /**
     * Gets the user profile.
     *
     * @param user
     *        the user
     * @return the user profile
     */
    private CustomUserDTO getUserProfile(User user) {
        Optional<CustomUserDTO> optCustomUser = this.findOneByUserId(user.getId());
        return optCustomUser.orElse(null);
    }

}
