/*
 * 
 */
package com.ttth.teamcaring.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.EntityManager;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.ttth.teamcaring.TeamCaringApp;
import com.ttth.teamcaring.domain.Authority;
import com.ttth.teamcaring.domain.User;
import com.ttth.teamcaring.repository.UserRepository;
import com.ttth.teamcaring.repository.search.UserSearchRepository;
import com.ttth.teamcaring.security.AuthoritiesConstants;
import com.ttth.teamcaring.service.MailService;
import com.ttth.teamcaring.service.UserService;
import com.ttth.teamcaring.service.dto.UserDTO;
import com.ttth.teamcaring.service.mapper.UserMapper;
import com.ttth.teamcaring.web.rest.errors.ExceptionTranslator;
import com.ttth.teamcaring.web.rest.vm.ManagedUserVM;

/**
 * Test class for the UserResource REST controller.
 *
 * @see UserResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TeamCaringApp.class)
@Ignore
public class UserResourceIntTest {

    /** The Constant DEFAULT_ID. */
    private static final Long                     DEFAULT_ID        = 1L;

    /** The Constant DEFAULT_LOGIN. */
    private static final String                   DEFAULT_LOGIN     = "johndoe";

    /** The Constant UPDATED_LOGIN. */
    private static final String                   UPDATED_LOGIN     = "jhipster";

    /** The Constant DEFAULT_PASSWORD. */
    private static final String                   DEFAULT_PASSWORD  = "passjohndoe";

    /** The Constant UPDATED_PASSWORD. */
    private static final String                   UPDATED_PASSWORD  = "passjhipster";

    /** The Constant DEFAULT_EMAIL. */
    private static final String                   DEFAULT_EMAIL     = "johndoe@localhost";

    /** The Constant UPDATED_EMAIL. */
    private static final String                   UPDATED_EMAIL     = "jhipster@localhost";

    /** The Constant DEFAULT_FIRSTNAME. */
    private static final String                   DEFAULT_FIRSTNAME = "john";

    /** The Constant UPDATED_FIRSTNAME. */
    private static final String                   UPDATED_FIRSTNAME = "jhipsterFirstName";

    /** The Constant DEFAULT_LASTNAME. */
    private static final String                   DEFAULT_LASTNAME  = "doe";

    /** The Constant UPDATED_LASTNAME. */
    private static final String                   UPDATED_LASTNAME  = "jhipsterLastName";

    /** The Constant DEFAULT_IMAGEURL. */
    private static final String                   DEFAULT_IMAGEURL  = "http://placehold.it/50x50";

    /** The Constant UPDATED_IMAGEURL. */
    private static final String                   UPDATED_IMAGEURL  = "http://placehold.it/40x40";

    /** The Constant DEFAULT_LANGKEY. */
    private static final String                   DEFAULT_LANGKEY   = "en";

    /** The Constant UPDATED_LANGKEY. */
    private static final String                   UPDATED_LANGKEY   = "fr";

    /** The user repository. */
    @Autowired
    private UserRepository                        userRepository;

    /** The user search repository. */
    @Autowired
    private UserSearchRepository                  userSearchRepository;

    /** The mail service. */
    @Autowired
    private MailService                           mailService;

    /** The user service. */
    @Autowired
    private UserService                           userService;

    /** The user mapper. */
    @Autowired
    private UserMapper                            userMapper;

    /** The jackson message converter. */
    @Autowired
    private MappingJackson2HttpMessageConverter   jacksonMessageConverter;

    /** The pageable argument resolver. */
    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    /** The exception translator. */
    @Autowired
    private ExceptionTranslator                   exceptionTranslator;

    /** The em. */
    @Autowired
    private EntityManager                         em;

    /** The rest user mock mvc. */
    private MockMvc                               restUserMockMvc;

    /** The user. */
    private User                                  user;

    /**
     * Setup.
     */
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        UserResource userResource = new UserResource(userRepository, userService, mailService,
                userSearchRepository);
        this.restUserMockMvc = MockMvcBuilders.standaloneSetup(userResource)
                .setCustomArgumentResolvers(pageableArgumentResolver)
                .setControllerAdvice(exceptionTranslator)
                .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create a User.
     * 
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which has a required relationship to the User
     * entity.
     *
     * @param em
     *        the em
     * @return the user
     */
    public static User createEntity(EntityManager em) {
        User user = new User();
        user.setLogin(DEFAULT_LOGIN + RandomStringUtils.randomAlphabetic(5));
        user.setPassword(RandomStringUtils.random(60));
        user.setActivated(true);
        user.setEmail(RandomStringUtils.randomAlphabetic(5) + DEFAULT_EMAIL);
        user.setFirstName(DEFAULT_FIRSTNAME);
        user.setLastName(DEFAULT_LASTNAME);
        user.setImageUrl(DEFAULT_IMAGEURL);
        user.setLangKey(DEFAULT_LANGKEY);
        return user;
    }

    /**
     * Inits the test.
     */
    @Before
    public void initTest() {
        user = createEntity(em);
        user.setLogin(DEFAULT_LOGIN);
        user.setEmail(DEFAULT_EMAIL);
    }

    /**
     * Creates the user.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    @Transactional
    public void createUser() throws Exception {
        int databaseSizeBeforeCreate = userRepository.findAll().size();

        // Create the User
        Set<String> authorities = new HashSet<>();
        authorities.add(AuthoritiesConstants.USER);
        ManagedUserVM managedUserVM = new ManagedUserVM(null, DEFAULT_LOGIN, DEFAULT_PASSWORD,
                DEFAULT_FIRSTNAME, DEFAULT_LASTNAME, DEFAULT_EMAIL, true, DEFAULT_IMAGEURL,
                DEFAULT_LANGKEY, null, null, null, null, authorities);

        restUserMockMvc
                .perform(post("/api/users").contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(managedUserVM)))
                .andExpect(status().isCreated());

        // Validate the User in the database
        List<User> userList = userRepository.findAll();
        assertThat(userList).hasSize(databaseSizeBeforeCreate + 1);
        User testUser = userList.get(userList.size() - 1);
        assertThat(testUser.getLogin()).isEqualTo(DEFAULT_LOGIN);
        assertThat(testUser.getFirstName()).isEqualTo(DEFAULT_FIRSTNAME);
        assertThat(testUser.getLastName()).isEqualTo(DEFAULT_LASTNAME);
        assertThat(testUser.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testUser.getImageUrl()).isEqualTo(DEFAULT_IMAGEURL);
        assertThat(testUser.getLangKey()).isEqualTo(DEFAULT_LANGKEY);
    }

    /**
     * Creates the user with existing id.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    @Transactional
    public void createUserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userRepository.findAll().size();

        Set<String> authorities = new HashSet<>();
        authorities.add(AuthoritiesConstants.USER);
        ManagedUserVM managedUserVM = new ManagedUserVM(1L, DEFAULT_LOGIN, DEFAULT_PASSWORD,
                DEFAULT_FIRSTNAME, DEFAULT_LASTNAME, DEFAULT_EMAIL, true, DEFAULT_IMAGEURL,
                DEFAULT_LANGKEY, null, null, null, null, authorities);

        // An entity with an existing ID cannot be created, so this API call
        // must fail
        restUserMockMvc
                .perform(post("/api/users").contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(managedUserVM)))
                .andExpect(status().isBadRequest());

        // Validate the User in the database
        List<User> userList = userRepository.findAll();
        assertThat(userList).hasSize(databaseSizeBeforeCreate);
    }

    /**
     * Creates the user with existing login.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    @Transactional
    public void createUserWithExistingLogin() throws Exception {
        // Initialize the database
        userRepository.saveAndFlush(user);
        userSearchRepository.save(user);
        int databaseSizeBeforeCreate = userRepository.findAll().size();

        Set<String> authorities = new HashSet<>();
        authorities.add(AuthoritiesConstants.USER);
        ManagedUserVM managedUserVM = new ManagedUserVM(null, DEFAULT_LOGIN, // this
                                                                             // login
                                                                             // should
                                                                             // already
                                                                             // be
                                                                             // used
                DEFAULT_PASSWORD, DEFAULT_FIRSTNAME, DEFAULT_LASTNAME, "anothermail@localhost",
                true, DEFAULT_IMAGEURL, DEFAULT_LANGKEY, null, null, null, null, authorities);

        // Create the User
        restUserMockMvc
                .perform(post("/api/users").contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(managedUserVM)))
                .andExpect(status().isBadRequest());

        // Validate the User in the database
        List<User> userList = userRepository.findAll();
        assertThat(userList).hasSize(databaseSizeBeforeCreate);
    }

    /**
     * Creates the user with existing email.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    @Transactional
    public void createUserWithExistingEmail() throws Exception {
        // Initialize the database
        userRepository.saveAndFlush(user);
        userSearchRepository.save(user);
        int databaseSizeBeforeCreate = userRepository.findAll().size();

        Set<String> authorities = new HashSet<>();
        authorities.add(AuthoritiesConstants.USER);
        ManagedUserVM managedUserVM = new ManagedUserVM(null, "anotherlogin", DEFAULT_PASSWORD,
                DEFAULT_FIRSTNAME, DEFAULT_LASTNAME, DEFAULT_EMAIL, // this email should already be
                                                                    // used
                true, DEFAULT_IMAGEURL, DEFAULT_LANGKEY, null, null, null, null, authorities);

        // Create the User
        restUserMockMvc
                .perform(post("/api/users").contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(managedUserVM)))
                .andExpect(status().isBadRequest());

        // Validate the User in the database
        List<User> userList = userRepository.findAll();
        assertThat(userList).hasSize(databaseSizeBeforeCreate);
    }

    /**
     * Gets the all users.
     *
     * @return the all users
     * @throws Exception
     *         the exception
     */
    @Test
    @Transactional
    public void getAllUsers() throws Exception {
        // Initialize the database
        userRepository.saveAndFlush(user);
        userSearchRepository.save(user);

        // Get all the users
        restUserMockMvc.perform(get("/api/users?sort=id,desc").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].login").value(hasItem(DEFAULT_LOGIN)))
                .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRSTNAME)))
                .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LASTNAME)))
                .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
                .andExpect(jsonPath("$.[*].imageUrl").value(hasItem(DEFAULT_IMAGEURL)))
                .andExpect(jsonPath("$.[*].langKey").value(hasItem(DEFAULT_LANGKEY)));
    }

    /**
     * Gets the user.
     *
     * @return the user
     * @throws Exception
     *         the exception
     */
    @Test
    @Transactional
    public void getUser() throws Exception {
        // Initialize the database
        userRepository.saveAndFlush(user);
        userSearchRepository.save(user);

        // Get the user
        restUserMockMvc.perform(get("/api/users/{login}", user.getLogin()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.login").value(user.getLogin()))
                .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRSTNAME))
                .andExpect(jsonPath("$.lastName").value(DEFAULT_LASTNAME))
                .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
                .andExpect(jsonPath("$.imageUrl").value(DEFAULT_IMAGEURL))
                .andExpect(jsonPath("$.langKey").value(DEFAULT_LANGKEY));
    }

    /**
     * Gets the non existing user.
     *
     * @return the non existing user
     * @throws Exception
     *         the exception
     */
    @Test
    @Transactional
    public void getNonExistingUser() throws Exception {
        restUserMockMvc.perform(get("/api/users/unknown")).andExpect(status().isNotFound());
    }

    /**
     * Update user.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    @Transactional
    public void updateUser() throws Exception {
        // Initialize the database
        userRepository.saveAndFlush(user);
        userSearchRepository.save(user);
        int databaseSizeBeforeUpdate = userRepository.findAll().size();

        // Update the user
        User updatedUser = userRepository.findOne(user.getId());

        Set<String> authorities = new HashSet<>();
        authorities.add(AuthoritiesConstants.USER);
        ManagedUserVM managedUserVM = new ManagedUserVM(updatedUser.getId(), updatedUser.getLogin(),
                UPDATED_PASSWORD, UPDATED_FIRSTNAME, UPDATED_LASTNAME, UPDATED_EMAIL,
                updatedUser.getActivated(), UPDATED_IMAGEURL, UPDATED_LANGKEY,
                updatedUser.getCreatedBy(), updatedUser.getCreatedDate(),
                updatedUser.getLastModifiedBy(), updatedUser.getLastModifiedDate(), authorities);

        restUserMockMvc
                .perform(put("/api/users").contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(managedUserVM)))
                .andExpect(status().isOk());

        // Validate the User in the database
        List<User> userList = userRepository.findAll();
        assertThat(userList).hasSize(databaseSizeBeforeUpdate);
        User testUser = userList.get(userList.size() - 1);
        assertThat(testUser.getFirstName()).isEqualTo(UPDATED_FIRSTNAME);
        assertThat(testUser.getLastName()).isEqualTo(UPDATED_LASTNAME);
        assertThat(testUser.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testUser.getImageUrl()).isEqualTo(UPDATED_IMAGEURL);
        assertThat(testUser.getLangKey()).isEqualTo(UPDATED_LANGKEY);
    }

    /**
     * Update user login.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    @Transactional
    public void updateUserLogin() throws Exception {
        // Initialize the database
        userRepository.saveAndFlush(user);
        userSearchRepository.save(user);
        int databaseSizeBeforeUpdate = userRepository.findAll().size();

        // Update the user
        User updatedUser = userRepository.findOne(user.getId());

        Set<String> authorities = new HashSet<>();
        authorities.add(AuthoritiesConstants.USER);
        ManagedUserVM managedUserVM = new ManagedUserVM(updatedUser.getId(), UPDATED_LOGIN,
                UPDATED_PASSWORD, UPDATED_FIRSTNAME, UPDATED_LASTNAME, UPDATED_EMAIL,
                updatedUser.getActivated(), UPDATED_IMAGEURL, UPDATED_LANGKEY,
                updatedUser.getCreatedBy(), updatedUser.getCreatedDate(),
                updatedUser.getLastModifiedBy(), updatedUser.getLastModifiedDate(), authorities);

        restUserMockMvc
                .perform(put("/api/users").contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(managedUserVM)))
                .andExpect(status().isOk());

        // Validate the User in the database
        List<User> userList = userRepository.findAll();
        assertThat(userList).hasSize(databaseSizeBeforeUpdate);
        User testUser = userList.get(userList.size() - 1);
        assertThat(testUser.getLogin()).isEqualTo(UPDATED_LOGIN);
        assertThat(testUser.getFirstName()).isEqualTo(UPDATED_FIRSTNAME);
        assertThat(testUser.getLastName()).isEqualTo(UPDATED_LASTNAME);
        assertThat(testUser.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testUser.getImageUrl()).isEqualTo(UPDATED_IMAGEURL);
        assertThat(testUser.getLangKey()).isEqualTo(UPDATED_LANGKEY);
    }

    /**
     * Update user existing email.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    @Transactional
    public void updateUserExistingEmail() throws Exception {
        // Initialize the database with 2 users
        userRepository.saveAndFlush(user);
        userSearchRepository.save(user);

        User anotherUser = new User();
        anotherUser.setLogin("jhipster");
        anotherUser.setPassword(RandomStringUtils.random(60));
        anotherUser.setActivated(true);
        anotherUser.setEmail("jhipster@localhost");
        anotherUser.setFirstName("java");
        anotherUser.setLastName("hipster");
        anotherUser.setImageUrl("");
        anotherUser.setLangKey("en");
        userRepository.saveAndFlush(anotherUser);
        userSearchRepository.save(anotherUser);

        // Update the user
        User updatedUser = userRepository.findOne(user.getId());

        Set<String> authorities = new HashSet<>();
        authorities.add(AuthoritiesConstants.USER);
        ManagedUserVM managedUserVM = new ManagedUserVM(updatedUser.getId(), updatedUser.getLogin(),
                updatedUser.getPassword(), updatedUser.getFirstName(), updatedUser.getLastName(),
                "jhipster@localhost", // this
                                      // email
                                      // should
                                      // already
                                      // be
                                      // used
                                      // by
                                      // anotherUser
                updatedUser.getActivated(), updatedUser.getImageUrl(), updatedUser.getLangKey(),
                updatedUser.getCreatedBy(), updatedUser.getCreatedDate(),
                updatedUser.getLastModifiedBy(), updatedUser.getLastModifiedDate(), authorities);

        restUserMockMvc
                .perform(put("/api/users").contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(managedUserVM)))
                .andExpect(status().isBadRequest());
    }

    /**
     * Update user existing login.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    @Transactional
    public void updateUserExistingLogin() throws Exception {
        // Initialize the database
        userRepository.saveAndFlush(user);
        userSearchRepository.save(user);

        User anotherUser = new User();
        anotherUser.setLogin("jhipster");
        anotherUser.setPassword(RandomStringUtils.random(60));
        anotherUser.setActivated(true);
        anotherUser.setEmail("jhipster@localhost");
        anotherUser.setFirstName("java");
        anotherUser.setLastName("hipster");
        anotherUser.setImageUrl("");
        anotherUser.setLangKey("en");
        userRepository.saveAndFlush(anotherUser);
        userSearchRepository.save(anotherUser);

        // Update the user
        User updatedUser = userRepository.findOne(user.getId());

        Set<String> authorities = new HashSet<>();
        authorities.add(AuthoritiesConstants.USER);
        ManagedUserVM managedUserVM = new ManagedUserVM(updatedUser.getId(), "jhipster", // this
                                                                                         // login
                                                                                         // should
                                                                                         // already
                                                                                         // be
                                                                                         // used
                                                                                         // by
                                                                                         // anotherUser
                updatedUser.getPassword(), updatedUser.getFirstName(), updatedUser.getLastName(),
                updatedUser.getEmail(), updatedUser.getActivated(), updatedUser.getImageUrl(),
                updatedUser.getLangKey(), updatedUser.getCreatedBy(), updatedUser.getCreatedDate(),
                updatedUser.getLastModifiedBy(), updatedUser.getLastModifiedDate(), authorities);

        restUserMockMvc
                .perform(put("/api/users").contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(managedUserVM)))
                .andExpect(status().isBadRequest());
    }

    /**
     * Delete user.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    @Transactional
    public void deleteUser() throws Exception {
        // Initialize the database
        userRepository.saveAndFlush(user);
        userSearchRepository.save(user);
        int databaseSizeBeforeDelete = userRepository.findAll().size();

        // Delete the user
        restUserMockMvc.perform(delete("/api/users/{login}", user.getLogin())
                .accept(TestUtil.APPLICATION_JSON_UTF8)).andExpect(status().isOk());

        // Validate the database is empty
        List<User> userList = userRepository.findAll();
        assertThat(userList).hasSize(databaseSizeBeforeDelete - 1);
    }

    /**
     * Gets the all authorities.
     *
     * @return the all authorities
     * @throws Exception
     *         the exception
     */
    @Test
    @Transactional
    public void getAllAuthorities() throws Exception {
        restUserMockMvc
                .perform(get("/api/users/authorities").accept(TestUtil.APPLICATION_JSON_UTF8)
                        .contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$").isArray()).andExpect(jsonPath("$").value(
                        containsInAnyOrder(AuthoritiesConstants.USER, AuthoritiesConstants.ADMIN)));
    }

    /**
     * Test user equals.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    @Transactional
    public void testUserEquals() throws Exception {
        TestUtil.equalsVerifier(User.class);
        User user1 = new User();
        user1.setId(1L);
        User user2 = new User();
        user2.setId(user1.getId());
        assertThat(user1).isEqualTo(user2);
        user2.setId(2L);
        assertThat(user1).isNotEqualTo(user2);
        user1.setId(null);
        assertThat(user1).isNotEqualTo(user2);
    }

    /**
     * Test user from id.
     */
    @Test
    public void testUserFromId() {
        assertThat(userMapper.userFromId(DEFAULT_ID).getId()).isEqualTo(DEFAULT_ID);
        assertThat(userMapper.userFromId(null)).isNull();
    }

    /**
     * Test user DT oto user.
     */
    @Test
    public void testUserDTOtoUser() {
        UserDTO userDTO = new UserDTO(DEFAULT_ID, DEFAULT_LOGIN, DEFAULT_FIRSTNAME,
                DEFAULT_LASTNAME, DEFAULT_EMAIL, true, DEFAULT_IMAGEURL, DEFAULT_LANGKEY,
                DEFAULT_LOGIN, null, DEFAULT_LOGIN, null,
                Stream.of(AuthoritiesConstants.USER).collect(Collectors.toSet()));
        User user = userMapper.userDTOToUser(userDTO);
        assertThat(user.getId()).isEqualTo(DEFAULT_ID);
        assertThat(user.getLogin()).isEqualTo(DEFAULT_LOGIN);
        assertThat(user.getFirstName()).isEqualTo(DEFAULT_FIRSTNAME);
        assertThat(user.getLastName()).isEqualTo(DEFAULT_LASTNAME);
        assertThat(user.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(user.getActivated()).isEqualTo(true);
        assertThat(user.getImageUrl()).isEqualTo(DEFAULT_IMAGEURL);
        assertThat(user.getLangKey()).isEqualTo(DEFAULT_LANGKEY);
        assertThat(user.getCreatedBy()).isNull();
        assertThat(user.getCreatedDate()).isNotNull();
        assertThat(user.getLastModifiedBy()).isNull();
        assertThat(user.getLastModifiedDate()).isNotNull();
        assertThat(user.getAuthorities()).extracting("name")
                .containsExactly(AuthoritiesConstants.USER);
    }

    /**
     * Test user to user DTO.
     */
    @Test
    public void testUserToUserDTO() {
        user.setId(DEFAULT_ID);
        user.setCreatedBy(DEFAULT_LOGIN);
        user.setCreatedDate(Instant.now());
        user.setLastModifiedBy(DEFAULT_LOGIN);
        user.setLastModifiedDate(Instant.now());

        Set<Authority> authorities = new HashSet<>();
        Authority authority = new Authority();
        authority.setName(AuthoritiesConstants.USER);
        authorities.add(authority);
        user.setAuthorities(authorities);

        UserDTO userDTO = userMapper.userToUserDTO(user);

        assertThat(userDTO.getId()).isEqualTo(DEFAULT_ID);
        assertThat(userDTO.getLogin()).isEqualTo(DEFAULT_LOGIN);
        assertThat(userDTO.getFirstName()).isEqualTo(DEFAULT_FIRSTNAME);
        assertThat(userDTO.getLastName()).isEqualTo(DEFAULT_LASTNAME);
        assertThat(userDTO.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(userDTO.isActivated()).isEqualTo(true);
        assertThat(userDTO.getImageUrl()).isEqualTo(DEFAULT_IMAGEURL);
        assertThat(userDTO.getLangKey()).isEqualTo(DEFAULT_LANGKEY);
        assertThat(userDTO.getCreatedBy()).isEqualTo(DEFAULT_LOGIN);
        assertThat(userDTO.getCreatedDate()).isEqualTo(user.getCreatedDate());
        assertThat(userDTO.getLastModifiedBy()).isEqualTo(DEFAULT_LOGIN);
        assertThat(userDTO.getLastModifiedDate()).isEqualTo(user.getLastModifiedDate());
        assertThat(userDTO.getAuthorities()).containsExactly(AuthoritiesConstants.USER);
        assertThat(userDTO.toString()).isNotNull();
    }

    /**
     * Test authority equals.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void testAuthorityEquals() throws Exception {
        Authority authorityA = new Authority();
        assertThat(authorityA).isEqualTo(authorityA);
        assertThat(authorityA).isNotEqualTo(null);
        assertThat(authorityA).isNotEqualTo(new Object());
        assertThat(authorityA.hashCode()).isEqualTo(0);
        assertThat(authorityA.toString()).isNotNull();

        Authority authorityB = new Authority();
        assertThat(authorityA).isEqualTo(authorityB);

        authorityB.setName(AuthoritiesConstants.ADMIN);
        assertThat(authorityA).isNotEqualTo(authorityB);

        authorityA.setName(AuthoritiesConstants.USER);
        assertThat(authorityA).isNotEqualTo(authorityB);

        authorityB.setName(AuthoritiesConstants.USER);
        assertThat(authorityA).isEqualTo(authorityB);
        assertThat(authorityA.hashCode()).isEqualTo(authorityB.hashCode());
    }
}
