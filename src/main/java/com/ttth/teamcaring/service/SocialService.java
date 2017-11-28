package com.ttth.teamcaring.service;

import java.time.Instant;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ttth.teamcaring.domain.Authority;
import com.ttth.teamcaring.domain.User;
import com.ttth.teamcaring.repository.AuthorityRepository;
import com.ttth.teamcaring.repository.UserRepository;
import com.ttth.teamcaring.repository.search.UserSearchRepository;
import com.ttth.teamcaring.security.AuthoritiesConstants;
import com.ttth.teamcaring.service.dto.CustomUserDTO;
import com.ttth.teamcaring.service.mapper.UserMapper;

@Service
@Transactional
public class SocialService {

    private final Logger log = LoggerFactory.getLogger(SocialService.class);

    private final UsersConnectionRepository usersConnectionRepository;

    private final AuthorityRepository authorityRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final MailService mailService;

    private final UserSearchRepository userSearchRepository;
    
    @Inject
    private CustomUserService customUserService;
    
    @Inject
    private UserMapper userMapper;

    public SocialService(UsersConnectionRepository usersConnectionRepository, AuthorityRepository authorityRepository,
            PasswordEncoder passwordEncoder, UserRepository userRepository,
            MailService mailService, UserSearchRepository userSearchRepository) {

        this.usersConnectionRepository = usersConnectionRepository;
        this.authorityRepository = authorityRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.mailService = mailService;
        this.userSearchRepository = userSearchRepository;
    }

    public void deleteUserSocialConnection(String login) {
        ConnectionRepository connectionRepository = usersConnectionRepository.createConnectionRepository(login);
        connectionRepository.findAllConnections().keySet().stream()
            .forEach(providerId -> {
                connectionRepository.removeConnections(providerId);
                log.debug("Delete user social connection providerId: {}", providerId);
            });
    }

    public User createSocialUser(Connection<?> connection) {
        return this.createSocialUser(connection, "vi");
    }
    
    public User createSocialUser(Connection<?> connection, String langKey) {
        if (connection == null) {
            log.error("Cannot create social user because connection is null");
            throw new IllegalArgumentException("Connection cannot be null");
        }
        UserProfile userProfile = connection.fetchUserProfile();
        String providerId = connection.getKey().getProviderId();
        String imageUrl = connection.getImageUrl();
        Instant auditTime = Instant.now();
        User user = createUserIfNotExist(userProfile, langKey, providerId, imageUrl);
        
        // Check audit time to make sure this is new user, so create social connection instant
        if (auditTime.isBefore(user.getCreatedDate())) {
        	createSocialConnection(user.getLogin(), connection);	
        }        
//        mailService.sendSocialRegistrationValidationEmail(user, providerId); NOT NEED SEND EMAIL AT THIS TIME
        
        // Rollback to raw password (is providerId) for next create credential step
        User rollbackUser;
		try {
			rollbackUser = user.clone();
			rollbackUser.setPassword(providerId);
			return rollbackUser;
		} catch (CloneNotSupportedException e) {
			this.log.error("Cannot clone User object", e);
			return user; 
		}
    }

    private User createUserIfNotExist(UserProfile userProfile, String langKey, String providerId, String imageUrl) {
//        String email = userProfile.getEmail(); // for Facebook & other social network framework
//        String userName = userProfile.getUsername(); // for Twitter        
//        if (!StringUtils.isBlank(userName)) {
//            userName = userName.toLowerCase(Locale.ENGLISH);
//        }
//        if (StringUtils.isBlank(email) && StringUtils.isBlank(userName)) {
//            log.error("Cannot create social user because email and login are null");
//            throw new IllegalArgumentException("Email and login cannot be null");
//        }
//        if (StringUtils.isBlank(email) && userRepository.findOneByLogin(userName).isPresent()) {
//            log.error("Cannot create social user because email is null and login already exist, login -> {}", userName);
//            throw new IllegalArgumentException("Email cannot be null with an existing login");
//        }
//        if (!StringUtils.isBlank(email)) {
//        	String login = getLoginDependingOnProviderId(userProfile, providerId);            
//            Optional<User> user = userRepository.findOneByEmailIgnoreCase(email);
//            if (user.isPresent()) {
//                log.info("User already exist associate the connection to this account");
//                return user.get();
//            }
//        }
        
        String login = getLoginDependingOnProviderId(userProfile, providerId);            
        Optional<User> user = userRepository.findOneByLogin(login);
        if (user.isPresent()) {
            log.info("User already exist associate the connection to this account");
            return user.get();
        }
        
        String rawPassword = providerId;
        String encryptedPassword = passwordEncoder.encode(rawPassword);
        Set<Authority> authorities = new HashSet<>(1);
        authorities.add(authorityRepository.findOne(AuthoritiesConstants.USER));

        User newUser = new User();
        newUser.setLogin(login);
        newUser.setPassword(encryptedPassword);
        newUser.setFirstName(userProfile.getFirstName());
        newUser.setLastName(userProfile.getLastName());
//        newUser.setEmail(email);
        // new social user is activated
        newUser.setActivated(true);
        // new user gets registration key
        newUser.setActivationKey(providerId);
        newUser.setAuthorities(authorities);
        newUser.setLangKey(langKey);
        newUser.setImageUrl(imageUrl);

        newUser = this.userRepository.save(newUser);
        userSearchRepository.save(newUser);
        
        
        // Create CustomUser entity as external user information
        CustomUserDTO customUserDTO = new CustomUserDTO();
        customUserDTO.setUserId(newUser.getId());
        this.customUserService.save(customUserDTO);
        
        return newUser;        
    }

    /**
     * @return login if provider manage a login like Twitter or GitHub otherwise email address.
     *         Because provider like Google or Facebook didn't provide login or login like "12099388847393"
     */
    private String getLoginDependingOnProviderId(UserProfile userProfile, String providerId) {
        switch (providerId) {
            case "twitter":
                return "twitter:" + userProfile.getUsername().toLowerCase();
            default:
            	String email = userProfile.getEmail();
                return providerId + ":" + (StringUtils.isNotBlank(email) ? email : userProfile.getId());
        }
    }

    private void createSocialConnection(String login, Connection<?> connection) {
        ConnectionRepository connectionRepository = usersConnectionRepository.createConnectionRepository(login);
        connectionRepository.addConnection(connection);
    }
}
