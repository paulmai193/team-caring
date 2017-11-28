package com.ttth.teamcaring.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ttth.teamcaring.config.Constants;
import com.ttth.teamcaring.domain.Authority;
import com.ttth.teamcaring.domain.CustomUser;
import com.ttth.teamcaring.domain.Groups;
import com.ttth.teamcaring.domain.Team;
import com.ttth.teamcaring.domain.User;
import com.ttth.teamcaring.repository.AuthorityRepository;
import com.ttth.teamcaring.repository.CustomUserRepository;
import com.ttth.teamcaring.repository.GroupsRepository;
import com.ttth.teamcaring.repository.TeamRepository;
import com.ttth.teamcaring.repository.UserRepository;
import com.ttth.teamcaring.repository.search.CustomUserSearchRepository;
import com.ttth.teamcaring.repository.search.GroupsSearchRepository;
import com.ttth.teamcaring.repository.search.TeamSearchRepository;
import com.ttth.teamcaring.repository.search.UserSearchRepository;
import com.ttth.teamcaring.security.AuthoritiesConstants;
import com.ttth.teamcaring.security.SecurityUtils;
import com.ttth.teamcaring.service.dto.AnonymousGroupDTO;
import com.ttth.teamcaring.service.dto.CustomUserDTO;
import com.ttth.teamcaring.service.dto.ProfileDTO;
import com.ttth.teamcaring.service.dto.UserDTO;
import com.ttth.teamcaring.service.mapper.CustomUserMapper;
import com.ttth.teamcaring.service.util.RandomUtil;
import com.ttth.teamcaring.web.rest.vm.ManagedUserVM;

/**
 * Service class for managing users.
 *
 * @author Dai Mai
 */
@Service
@Transactional
public class UserService {

    /** The log. */
    private final Logger log = LoggerFactory.getLogger(UserService.class);

    /** The Constant USERS_CACHE. */
    private static final String USERS_CACHE = "users";

    /** The user repository. */
    private final UserRepository userRepository;

    /** The password encoder. */
    private final PasswordEncoder passwordEncoder;

    /** The social service. */
    private final SocialService socialService;

    /** The user search repository. */
    private final UserSearchRepository userSearchRepository;

    /** The authority repository. */
    private final AuthorityRepository authorityRepository;

    /** The cache manager. */
    private final CacheManager cacheManager;
    
    /** The custom user repository. */
    @Inject
    private CustomUserRepository customUserRepository;
    
    /** The custom user search repository. */
    @Inject
    private CustomUserSearchRepository customUserSearchRepository;
    
    /** The custom user mapper. */
    @Inject
    private CustomUserMapper customUserMapper;
        
    /** The groups repository. */
    @Inject
    private GroupsRepository groupsRepository;
    
    /** The groups search repository. */
    @Inject
    private GroupsSearchRepository groupsSearchRepository;
    
    /** The team repository. */
    @Inject
    private TeamRepository teamRepository;
    
    /** The team search repository. */
    @Inject
    private TeamSearchRepository teamSearchRepository;
    
    /** The custom user service. */
    @Inject
    private CustomUserService customUserService;

    /**
     * Instantiates a new user service.
     *
     * @param userRepository the user repository
     * @param passwordEncoder the password encoder
     * @param socialService the social service
     * @param userSearchRepository the user search repository
     * @param authorityRepository the authority repository
     * @param cacheManager the cache manager
     */
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, SocialService socialService, UserSearchRepository userSearchRepository, AuthorityRepository authorityRepository, CacheManager cacheManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.socialService = socialService;
        this.userSearchRepository = userSearchRepository;
        this.authorityRepository = authorityRepository;
        this.cacheManager = cacheManager;
    }

    /**
     * Activate registration.
     *
     * @param key the key
     * @return the optional
     */
    public Optional<User> activateRegistration(String key) {
        log.debug("Activating user for activation key {}", key);
        return userRepository.findOneByActivationKey(key)
            .map(user -> {
                // activate given user for the registration key.
                user.setActivated(true);
                user.setActivationKey(null);
                userSearchRepository.save(user);
                cacheManager.getCache(USERS_CACHE).evict(user.getLogin());
                log.debug("Activated user: {}", user);
                return user;
            });
    }

    /**
     * Complete password reset.
     *
     * @param newPassword the new password
     * @param key the key
     * @return the optional
     */
    public Optional<User> completePasswordReset(String newPassword, String key) {
       log.debug("Reset user password for reset key {}", key);

       return userRepository.findOneByResetKey(key)
           .filter(user -> user.getResetDate().isAfter(Instant.now().minusSeconds(86400)))
           .map(user -> {
                user.setPassword(passwordEncoder.encode(newPassword));
                user.setResetKey(null);
                user.setResetDate(null);
                cacheManager.getCache(USERS_CACHE).evict(user.getLogin());
                return user;
           });
    }

    /**
     * Request password reset.
     *
     * @param mail the mail
     * @return the optional
     */
    public Optional<User> requestPasswordReset(String mail) {
        return userRepository.findOneByEmailIgnoreCase(mail)
            .filter(User::getActivated)
            .map(user -> {
                user.setResetKey(RandomUtil.generateResetKey());
                user.setResetDate(Instant.now());
                cacheManager.getCache(USERS_CACHE).evict(user.getLogin());
                return user;
            });
    }

    /**
     * Register user.
     *
     * @param userDTO the user DTO
     * @return the user
     */
    public User registerUser(ManagedUserVM userDTO) {

        User newUser = new User();
        Authority authority = authorityRepository.findOne(AuthoritiesConstants.USER);
        Set<Authority> authorities = new HashSet<>();
        String encryptedPassword = passwordEncoder.encode(userDTO.getPassword());
        newUser.setLogin(userDTO.getLogin());
        // new user gets initially a generated password
        newUser.setPassword(encryptedPassword);
        newUser.setFirstName(userDTO.getFirstName());
        newUser.setLastName(userDTO.getLastName());
        newUser.setEmail(userDTO.getEmail());
        newUser.setImageUrl(userDTO.getImageUrl());
        newUser.setLangKey(userDTO.getLangKey());
        // new user is not active
        newUser.setActivated(false);
        // new user gets registration key
        newUser.setActivationKey(RandomUtil.generateActivationKey());
        authorities.add(authority);
        newUser.setAuthorities(authorities);
        userRepository.save(newUser);
        userSearchRepository.save(newUser);
        log.debug("Created Information for User: {}", newUser);
        
        // Create CustomUser entity as external user information
        this.createCustomUser(newUser);
        
        return newUser;
    }

    /**
     * Creates the user.
     *
     * @param userDTO the user DTO
     * @return the user
     */
    public User createUser(UserDTO userDTO) {
        User user = new User();
        user.setLogin(userDTO.getLogin());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setImageUrl(userDTO.getImageUrl());
        if (userDTO.getLangKey() == null) {
            user.setLangKey(Constants.DEFAULT_LANGUAGE); // default language
        } else {
            user.setLangKey(userDTO.getLangKey());
        }
        if (userDTO.getAuthorities() != null) {
            Set<Authority> authorities = userDTO.getAuthorities().stream()
                .map(authorityRepository::findOne)
                .collect(Collectors.toSet());
            user.setAuthorities(authorities);
        }
        String encryptedPassword = passwordEncoder.encode(RandomUtil.generatePassword());
        user.setPassword(encryptedPassword);
        user.setResetKey(RandomUtil.generateResetKey());
        user.setResetDate(Instant.now());
        user.setActivated(true);
        userRepository.save(user);
        userSearchRepository.save(user);
        log.debug("Created Information for User: {}", user);
        
        // Create CustomUser entity as external user information
        this.createCustomUser(user);
        
        return user;
    }

    /**
     * Update basic information (first name, last name, email, language) for the current user.
     *
     * @param firstName first name of user
     * @param lastName last name of user
     * @param email email id of user
     * @param langKey language key
     * @param imageUrl image URL of user
     */
    public void updateUser(String firstName, String lastName, String email, String langKey, String imageUrl) {
        userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).ifPresent(user -> {
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
            user.setLangKey(langKey);
            user.setImageUrl(imageUrl);
            userSearchRepository.save(user);
            cacheManager.getCache(USERS_CACHE).evict(user.getLogin());
            log.debug("Changed Information for User: {}", user);
        });
    }

    /**
     * Update all information for a specific user, and return the modified user.
     *
     * @param userDTO user to update
     * @return updated user
     */
    public Optional<UserDTO> updateUser(UserDTO userDTO) {
        return Optional.of(userRepository
            .findOne(userDTO.getId()))
            .map(user -> {
                user.setLogin(userDTO.getLogin());
                user.setFirstName(userDTO.getFirstName());
                user.setLastName(userDTO.getLastName());
                user.setEmail(userDTO.getEmail());
                user.setImageUrl(userDTO.getImageUrl());
                user.setActivated(userDTO.isActivated());
                user.setLangKey(userDTO.getLangKey());
                Set<Authority> managedAuthorities = user.getAuthorities();
                managedAuthorities.clear();
                userDTO.getAuthorities().stream()
                    .map(authorityRepository::findOne)
                    .forEach(managedAuthorities::add);
                userSearchRepository.save(user);
                cacheManager.getCache(USERS_CACHE).evict(user.getLogin());
                log.debug("Changed Information for User: {}", user);
                return user;
            })
            .map(UserDTO::new);
    }
    
    /**
     * Update basic profile information (full name, nickname, push token) and anonymous group information for the CURRENT user.
     * (get by current JWT)
     *
     * @param profile the profile
     * @param anonymousGroup the anonymous group
     */
    public void updateUserProfile(CustomUserDTO profile, AnonymousGroupDTO anonymousGroup) {
        userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).ifPresent(user -> {
        	// Clear activation key to mark user activated
        	user.setActivationKey(null);
        	this.userSearchRepository.save(user);
        	
            Optional<CustomUser> optCustomUser = this.customUserRepository.findOneByUserId(user.getId());
            CustomUser customUser;
            if (optCustomUser.isPresent()) {
            	customUser = optCustomUser.get();
            } else {
            	customUser = new CustomUser();
            	customUser.user(user);
            	customUser = this.customUserRepository.save(customUser);
            }
        	// Update user profile
        	if (profile != null) {
				customUser.fullName(profile.getFullName()).nickname(profile.getNickname()).pushToken(profile.getPushToken());				
			}
        	this.customUserSearchRepository.save(customUser);
        	log.debug("Changed profile for User: {}", customUser);  
        	
        	// Update user anonymous group
        	if (anonymousGroup != null) {
				Optional<Groups> optGroup = customUser.getLeaders().parallelStream().filter(group -> !group.isOffical()).findFirst();
				Groups group;
				Team team;
				if (optGroup.isPresent()) {
					group = optGroup.get();
					team = group.getTeam();
				} else {
					// Anonymous group not exist, create new one
					group = new Groups();
					group = this.groupsRepository.save(group);
					customUser.addLeader(group);
					this.customUserSearchRepository.save(customUser);
					
					// Create new temporary team for anonymous group
					team = new Team();
					team.addGroup(group);
					team = this.teamRepository.save(team);					
				}
				group.description(anonymousGroup.getDescription()).totalMember(anonymousGroup.getTotalMember()).setOffical(anonymousGroup.isOffical());
				this.groupsSearchRepository.save(group);
				
				team.name(anonymousGroup.getName());
				this.teamSearchRepository.save(team);
	        	log.debug("Changed anonymous groud for User: {}", group);
			}              
        });
    } 

    /**
     * Delete user.
     *
     * @param login the login
     */
    public void deleteUser(String login) {
        userRepository.findOneByLogin(login).ifPresent(user -> {
            socialService.deleteUserSocialConnection(user.getLogin());
            userRepository.delete(user);
            userSearchRepository.delete(user);
            cacheManager.getCache(USERS_CACHE).evict(login);
            log.debug("Deleted User: {}", user);
        });
    }

    /**
     * Change password.
     *
     * @param password the password
     */
    public void changePassword(String password) {
        userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).ifPresent(user -> {
            String encryptedPassword = passwordEncoder.encode(password);
            user.setPassword(encryptedPassword);
            cacheManager.getCache(USERS_CACHE).evict(user.getLogin());
            log.debug("Changed password for User: {}", user);
        });
    }

    /**
     * Gets the all managed users.
     *
     * @param pageable the pageable
     * @return the all managed users
     */
    @Transactional(readOnly = true)
    public Page<UserDTO> getAllManagedUsers(Pageable pageable) {
        return userRepository.findAllByLoginNot(pageable, Constants.ANONYMOUS_USER).map(UserDTO::new);
    }

    /**
     * Gets the user with authorities by login.
     *
     * @param login the login
     * @return the user with authorities by login
     */
    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthoritiesByLogin(String login) {
        return userRepository.findOneWithAuthoritiesByLogin(login);
    }

    /**
     * Gets the user with authorities.
     *
     * @param id the id
     * @return the user with authorities
     */
    @Transactional(readOnly = true)
    public User getUserWithAuthorities(Long id) {
        return userRepository.findOneWithAuthoritiesById(id);
    }

    /**
     * Gets the user with authorities.
     *
     * @return the user with authorities
     */
    @Transactional(readOnly = true)
    public User getUserWithAuthorities() {
        return userRepository.findOneWithAuthoritiesByLogin(SecurityUtils.getCurrentUserLogin()).orElse(null);
    }

    /**
     * Not activated users should be automatically deleted after 3 days.
     * <p>
     * This is scheduled to get fired everyday, at 01:00 (am).
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void removeNotActivatedUsers() {
        List<User> users = userRepository.findAllByActivatedIsFalseAndCreatedDateBefore(Instant.now().minus(3, ChronoUnit.DAYS));
        for (User user : users) {
            log.debug("Deleting not activated user {}", user.getLogin());
            userRepository.delete(user);
            userSearchRepository.delete(user);
            cacheManager.getCache(USERS_CACHE).evict(user.getLogin());
        }
    }

    /**
     * Gets the authorities.
     *
     * @return a list of all the authorities
     */
    public List<String> getAuthorities() {
        return authorityRepository.findAll().stream().map(Authority::getName).collect(Collectors.toList());
    }
    
    /**
     * Create the Custom User as external user information.
     *
     * @param user the user
     */
    private void createCustomUser(User user) {
        CustomUserDTO customUserDTO = new CustomUserDTO();
        customUserDTO.setUserId(user.getId());
        this.customUserService.save(customUserDTO);
    }

    /**
     * Gets the user profile with authorities.
     *
     * @return the user profile with authorities
     */
    @Transactional(readOnly = true)
	public ProfileDTO getUserProfileByUserId(Long userId) {
    	Optional<User> optUser = this.userRepository.findOneById(userId);
    	if (optUser.isPresent()) {
    		User user = optUser.get();
    		ProfileDTO profileDTO = new ProfileDTO();    			
			Optional<CustomUser> optCustomUser = this.customUserRepository.findOneByUserId(user.getId());
			CustomUser customUser;
    		if (optCustomUser.isPresent()) {
    			customUser = optCustomUser.get();
    			
    			// Get profile
    			CustomUserDTO customUserDTO = this.customUserMapper.toDto(optCustomUser.get());
    			profileDTO.setProfile(customUserDTO);
    			
    			// Get anonymous group
    			Optional<Groups> optAnonymousGroup = customUser.getLeaders().parallelStream().filter(group -> !group.isOffical()).findFirst();
    			if (optAnonymousGroup.isPresent()) {
    				Groups anonymousGroup = optAnonymousGroup.get();
    				AnonymousGroupDTO anonymousGroupDTO = new AnonymousGroupDTO();
    				anonymousGroupDTO.setDescription(anonymousGroup.getDescription());
    				anonymousGroupDTO.setTotalMember(anonymousGroup.getTotalMember());
    				profileDTO.setAnonymousGroup(anonymousGroupDTO);
    			}
    			
    			// TODO query groups of this user here    			
    			    			
    		} else {
    			customUser = new CustomUser().fullName("").nickname("").user(user);
    			profileDTO.setProfile(this.customUserMapper.toDto(customUser));    			
    		}
			return profileDTO;
    	} else {
    		return null;
    	}
	}
}
