/*
 * 
 */
package com.ttth.teamcaring.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.ttth.teamcaring.TeamCaringApp;
import com.ttth.teamcaring.domain.Authority;
import com.ttth.teamcaring.domain.User;
import com.ttth.teamcaring.repository.AuthorityRepository;
import com.ttth.teamcaring.repository.UserRepository;
import com.ttth.teamcaring.repository.search.UserSearchRepository;
import com.ttth.teamcaring.security.AuthoritiesConstants;

/**
 * The Class SocialServiceIntTest.
 *
 * @author Dai Mai
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TeamCaringApp.class)
@Transactional
@Ignore
public class SocialServiceIntTest {

    /** The authority repository. */
    @Autowired
    private AuthorityRepository       authorityRepository;

    /** The password encoder. */
    @Autowired
    private PasswordEncoder           passwordEncoder;

    /** The user repository. */
    @Autowired
    private UserRepository            userRepository;

    /** The user search repository. */
    @Autowired
    private UserSearchRepository      userSearchRepository;

    /** The mock mail service. */
    @Mock
    private MailService               mockMailService;

    /** The mock users connection repository. */
    @Mock
    private UsersConnectionRepository mockUsersConnectionRepository;

    /** The mock connection repository. */
    @Mock
    private ConnectionRepository      mockConnectionRepository;

    /** The social service. */
    private SocialService             socialService;

    /**
     * Setup.
     */
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        doNothing().when(mockMailService).sendSocialRegistrationValidationEmail(anyObject(),
                anyString());
        doNothing().when(mockConnectionRepository).addConnection(anyObject());
        when(mockUsersConnectionRepository.createConnectionRepository(anyString()))
                .thenReturn(mockConnectionRepository);

        socialService = new SocialService(mockUsersConnectionRepository, authorityRepository,
                passwordEncoder, userRepository, mockMailService, userSearchRepository);
    }

    /**
     * Test delete user social connection.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void testDeleteUserSocialConnection() throws Exception {
        // Setup
        Connection<?> connection = createConnection("@LOGIN", "mail@mail.com", "FIRST_NAME",
                "LAST_NAME", "IMAGE_URL", "PROVIDER");
        socialService.createSocialUser(connection, "fr");
        MultiValueMap<String, Connection<?>> connectionsByProviderId = new LinkedMultiValueMap<>();
        connectionsByProviderId.put("PROVIDER", null);
        when(mockConnectionRepository.findAllConnections()).thenReturn(connectionsByProviderId);

        // Exercise
        socialService.deleteUserSocialConnection("@LOGIN");

        // Verify
        verify(mockConnectionRepository, times(1)).removeConnections("PROVIDER");
    }

    /**
     * Test create social user should throw exception if connection is null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateSocialUserShouldThrowExceptionIfConnectionIsNull() {
        // Exercise
        socialService.createSocialUser(null, "fr");
    }

    /**
     * Test create social user should throw exception if connection has no email
     * and no login.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateSocialUserShouldThrowExceptionIfConnectionHasNoEmailAndNoLogin() {
        // Setup
        Connection<?> connection = createConnection("", "", "FIRST_NAME", "LAST_NAME", "IMAGE_URL",
                "PROVIDER");

        // Exercise
        socialService.createSocialUser(connection, "fr");
    }

    /**
     * Test create social user should throw exception if connection has no email
     * and login already exist.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateSocialUserShouldThrowExceptionIfConnectionHasNoEmailAndLoginAlreadyExist() {
        // Setup
        User user = createExistingUser("@LOGIN", "mail@mail.com", "OTHER_FIRST_NAME",
                "OTHER_LAST_NAME", "OTHER_IMAGE_URL");
        Connection<?> connection = createConnection("@LOGIN", "", "FIRST_NAME", "LAST_NAME",
                "IMAGE_URL", "PROVIDER");

        // Exercise
        try {
            // Exercise
            socialService.createSocialUser(connection, "fr");
        }
        finally {
            // Teardown
            userRepository.delete(user);
        }
    }

    /**
     * Test create social user should create user if not exist.
     */
    @Test
    public void testCreateSocialUserShouldCreateUserIfNotExist() {
        // Setup
        Connection<?> connection = createConnection("@LOGIN", "mail@mail.com", "FIRST_NAME",
                "LAST_NAME", "IMAGE_URL", "PROVIDER");

        // Exercise
        socialService.createSocialUser(connection, "fr");

        // Verify
        final Optional<User> user = userRepository.findOneByEmailIgnoreCase("mail@mail.com");
        assertThat(user).isPresent();

        // Teardown
        userRepository.delete(user.get());
    }

    /**
     * Test create social user should create user with social information.
     */
    @Test
    public void testCreateSocialUserShouldCreateUserWithSocialInformation() {
        // Setup
        Connection<?> connection = createConnection("@LOGIN", "mail@mail.com", "FIRST_NAME",
                "LAST_NAME", "IMAGE_URL", "PROVIDER");

        // Exercise
        socialService.createSocialUser(connection, "fr");

        // Verify
        User user = userRepository.findOneByEmailIgnoreCase("mail@mail.com").get();
        assertThat(user.getFirstName()).isEqualTo("FIRST_NAME");
        assertThat(user.getLastName()).isEqualTo("LAST_NAME");
        assertThat(user.getImageUrl()).isEqualTo("IMAGE_URL");

        // Teardown
        userRepository.delete(user);
    }

    /**
     * Test create social user should create activated user with role user and
     * password.
     */
    @Test
    public void testCreateSocialUserShouldCreateActivatedUserWithRoleUserAndPassword() {
        // Setup
        Connection<?> connection = createConnection("@LOGIN", "mail@mail.com", "FIRST_NAME",
                "LAST_NAME", "IMAGE_URL", "PROVIDER");

        // Exercise
        socialService.createSocialUser(connection, "fr");

        // Verify
        User user = userRepository.findOneByEmailIgnoreCase("mail@mail.com").get();
        assertThat(user.getActivated()).isEqualTo(true);
        assertThat(user.getPassword()).isNotEmpty();
        Authority userAuthority = authorityRepository.findOne(AuthoritiesConstants.USER);
        assertThat(user.getAuthorities().toArray()).containsExactly(userAuthority);

        // Teardown
        userRepository.delete(user);
    }

    /**
     * Test create social user should create user with exact lang key.
     */
    @Test
    public void testCreateSocialUserShouldCreateUserWithExactLangKey() {
        // Setup
        Connection<?> connection = createConnection("@LOGIN", "mail@mail.com", "FIRST_NAME",
                "LAST_NAME", "IMAGE_URL", "PROVIDER");

        // Exercise
        socialService.createSocialUser(connection, "fr");

        // Verify
        final User user = userRepository.findOneByEmailIgnoreCase("mail@mail.com").get();
        assertThat(user.getLangKey()).isEqualTo("fr");

        // Teardown
        userRepository.delete(user);
    }

    /**
     * Test create social user should create user with login same as email if
     * not twitter.
     */
    @Test
    public void testCreateSocialUserShouldCreateUserWithLoginSameAsEmailIfNotTwitter() {
        // Setup
        Connection<?> connection = createConnection("@LOGIN", "mail@mail.com", "FIRST_NAME",
                "LAST_NAME", "IMAGE_URL", "PROVIDER_OTHER_THAN_TWITTER");

        // Exercise
        socialService.createSocialUser(connection, "fr");

        // Verify
        User user = userRepository.findOneByEmailIgnoreCase("mail@mail.com").get();
        assertThat(user.getLogin()).isEqualTo("mail@mail.com");

        // Teardown
        userRepository.delete(user);
    }

    /**
     * Test create social user should create user with social login when is
     * twitter.
     */
    @Test
    public void testCreateSocialUserShouldCreateUserWithSocialLoginWhenIsTwitter() {
        // Setup
        Connection<?> connection = createConnection("@LOGIN", "mail@mail.com", "FIRST_NAME",
                "LAST_NAME", "IMAGE_URL", "twitter");

        // Exercise
        socialService.createSocialUser(connection, "fr");

        // Verify
        User user = userRepository.findOneByEmailIgnoreCase("mail@mail.com").get();
        assertThat(user.getLogin()).isEqualToIgnoringCase("@LOGIN");

        // Teardown
        userRepository.delete(user);
    }

    /**
     * Test create social user should create social connection.
     */
    @Test
    public void testCreateSocialUserShouldCreateSocialConnection() {
        // Setup
        Connection<?> connection = createConnection("@LOGIN", "mail@mail.com", "FIRST_NAME",
                "LAST_NAME", "IMAGE_URL", "PROVIDER");

        // Exercise
        socialService.createSocialUser(connection, "fr");

        // Verify
        verify(mockConnectionRepository, times(1)).addConnection(connection);

        // Teardown
        User userToDelete = userRepository.findOneByEmailIgnoreCase("mail@mail.com").get();
        userRepository.delete(userToDelete);
    }

    /**
     * Test create social user should not create user if email already exist.
     */
    @Test
    public void testCreateSocialUserShouldNotCreateUserIfEmailAlreadyExist() {
        // Setup
        createExistingUser("@OTHER_LOGIN", "mail@mail.com", "OTHER_FIRST_NAME", "OTHER_LAST_NAME",
                "OTHER_IMAGE_URL");
        long initialUserCount = userRepository.count();
        Connection<?> connection = createConnection("@LOGIN", "mail@mail.com", "FIRST_NAME",
                "LAST_NAME", "IMAGE_URL", "PROVIDER");

        // Exercise
        socialService.createSocialUser(connection, "fr");

        // Verify
        assertThat(userRepository.count()).isEqualTo(initialUserCount);

        // Teardown
        User userToDelete = userRepository.findOneByEmailIgnoreCase("mail@mail.com").get();
        userRepository.delete(userToDelete);
    }

    /**
     * Test create social user should not change user if email already exist.
     */
    @Test
    public void testCreateSocialUserShouldNotChangeUserIfEmailAlreadyExist() {
        // Setup
        createExistingUser("@OTHER_LOGIN", "mail@mail.com", "OTHER_FIRST_NAME", "OTHER_LAST_NAME",
                "OTHER_IMAGE_URL");
        Connection<?> connection = createConnection("@LOGIN", "mail@mail.com", "FIRST_NAME",
                "LAST_NAME", "IMAGE_URL", "PROVIDER");

        // Exercise
        socialService.createSocialUser(connection, "fr");

        // Verify
        User userToVerify = userRepository.findOneByEmailIgnoreCase("mail@mail.com").get();
        assertThat(userToVerify.getLogin()).isEqualTo("@other_login");
        assertThat(userToVerify.getFirstName()).isEqualTo("OTHER_FIRST_NAME");
        assertThat(userToVerify.getLastName()).isEqualTo("OTHER_LAST_NAME");
        assertThat(userToVerify.getImageUrl()).isEqualTo("OTHER_IMAGE_URL");
        // Teardown
        userRepository.delete(userToVerify);
    }

    /**
     * Test create social user should send registration validation email.
     */
    @Test
    public void testCreateSocialUserShouldSendRegistrationValidationEmail() {
        // Setup
        Connection<?> connection = createConnection("@LOGIN", "mail@mail.com", "FIRST_NAME",
                "LAST_NAME", "IMAGE_URL", "PROVIDER");

        // Exercise
        socialService.createSocialUser(connection, "fr");

        // Verify
        verify(mockMailService, times(1)).sendSocialRegistrationValidationEmail(anyObject(),
                anyString());

        // Teardown
        User userToDelete = userRepository.findOneByEmailIgnoreCase("mail@mail.com").get();
        userRepository.delete(userToDelete);
    }

    /**
     * Creates the connection.
     *
     * @param login
     *        the login
     * @param email
     *        the email
     * @param firstName
     *        the first name
     * @param lastName
     *        the last name
     * @param imageUrl
     *        the image url
     * @param providerId
     *        the provider id
     * @return the connection
     */
    private Connection<?> createConnection(String login, String email, String firstName,
            String lastName, String imageUrl, String providerId) {
        UserProfile userProfile = mock(UserProfile.class);
        when(userProfile.getEmail()).thenReturn(email);
        when(userProfile.getUsername()).thenReturn(login);
        when(userProfile.getFirstName()).thenReturn(firstName);
        when(userProfile.getLastName()).thenReturn(lastName);

        Connection<?> connection = mock(Connection.class);
        ConnectionKey key = new ConnectionKey(providerId, "PROVIDER_USER_ID");
        when(connection.fetchUserProfile()).thenReturn(userProfile);
        when(connection.getKey()).thenReturn(key);
        when(connection.getImageUrl()).thenReturn(imageUrl);

        return connection;
    }

    /**
     * Creates the existing user.
     *
     * @param login
     *        the login
     * @param email
     *        the email
     * @param firstName
     *        the first name
     * @param lastName
     *        the last name
     * @param imageUrl
     *        the image url
     * @return the user
     */
    private User createExistingUser(String login, String email, String firstName, String lastName,
            String imageUrl) {
        User user = new User();
        user.setLogin(login);
        user.setPassword(passwordEncoder.encode("password"));
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setImageUrl(imageUrl);
        return userRepository.saveAndFlush(user);
    }
}
