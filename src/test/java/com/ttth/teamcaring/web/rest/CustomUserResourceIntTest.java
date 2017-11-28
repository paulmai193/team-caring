package com.ttth.teamcaring.web.rest;

import static com.ttth.teamcaring.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import javax.persistence.EntityManager;

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
import com.ttth.teamcaring.domain.CustomUser;
import com.ttth.teamcaring.repository.CustomUserRepository;
import com.ttth.teamcaring.repository.search.CustomUserSearchRepository;
import com.ttth.teamcaring.service.CustomUserService;
import com.ttth.teamcaring.service.dto.CustomUserDTO;
import com.ttth.teamcaring.service.mapper.CustomUserMapper;
import com.ttth.teamcaring.web.rest.errors.ExceptionTranslator;

/**
 * Test class for the CustomUserResource REST controller.
 *
 * @see CustomUserResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TeamCaringApp.class)
@Ignore
public class CustomUserResourceIntTest {

    private static final String DEFAULT_FULL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FULL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_NICKNAME = "AAAAAAAAAA";
    private static final String UPDATED_NICKNAME = "BBBBBBBBBB";

    private static final String DEFAULT_PUSH_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_PUSH_TOKEN = "BBBBBBBBBB";

    @Autowired
    private CustomUserRepository customUserRepository;

    @Autowired
    private CustomUserMapper customUserMapper;

    @Autowired
    private CustomUserService customUserService;

    @Autowired
    private CustomUserSearchRepository customUserSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCustomUserMockMvc;

    private CustomUser customUser;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CustomUserResource customUserResource = new CustomUserResource(customUserService);
        this.restCustomUserMockMvc = MockMvcBuilders.standaloneSetup(customUserResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomUser createEntity(EntityManager em) {
        CustomUser customUser = new CustomUser()
            .fullName(DEFAULT_FULL_NAME)
            .nickname(DEFAULT_NICKNAME)
            .pushToken(DEFAULT_PUSH_TOKEN);
        return customUser;
    }

    @Before
    public void initTest() {
        customUserSearchRepository.deleteAll();
        customUser = createEntity(em);
    }

    @Test
    @Transactional
    public void createCustomUser() throws Exception {
        int databaseSizeBeforeCreate = customUserRepository.findAll().size();

        // Create the CustomUser
        CustomUserDTO customUserDTO = customUserMapper.toDto(customUser);
        restCustomUserMockMvc.perform(post("/api/custom-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customUserDTO)))
            .andExpect(status().isCreated());

        // Validate the CustomUser in the database
        List<CustomUser> customUserList = customUserRepository.findAll();
        assertThat(customUserList).hasSize(databaseSizeBeforeCreate + 1);
        CustomUser testCustomUser = customUserList.get(customUserList.size() - 1);
        assertThat(testCustomUser.getFullName()).isEqualTo(DEFAULT_FULL_NAME);
        assertThat(testCustomUser.getNickname()).isEqualTo(DEFAULT_NICKNAME);
        assertThat(testCustomUser.getPushToken()).isEqualTo(DEFAULT_PUSH_TOKEN);

        // Validate the CustomUser in Elasticsearch
        CustomUser customUserEs = customUserSearchRepository.findOne(testCustomUser.getId());
        assertThat(customUserEs).isEqualToComparingFieldByField(testCustomUser);
    }

    @Test
    @Transactional
    public void createCustomUserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = customUserRepository.findAll().size();

        // Create the CustomUser with an existing ID
        customUser.setId(1L);
        CustomUserDTO customUserDTO = customUserMapper.toDto(customUser);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomUserMockMvc.perform(post("/api/custom-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customUserDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CustomUser in the database
        List<CustomUser> customUserList = customUserRepository.findAll();
        assertThat(customUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCustomUsers() throws Exception {
        // Initialize the database
        customUserRepository.saveAndFlush(customUser);

        // Get all the customUserList
        restCustomUserMockMvc.perform(get("/api/custom-users?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME.toString())))
            .andExpect(jsonPath("$.[*].nickname").value(hasItem(DEFAULT_NICKNAME.toString())))
            .andExpect(jsonPath("$.[*].pushToken").value(hasItem(DEFAULT_PUSH_TOKEN.toString())));
    }

    @Test
    @Transactional
    public void getCustomUser() throws Exception {
        // Initialize the database
        customUserRepository.saveAndFlush(customUser);

        // Get the customUser
        restCustomUserMockMvc.perform(get("/api/custom-users/{id}", customUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(customUser.getId().intValue()))
            .andExpect(jsonPath("$.fullName").value(DEFAULT_FULL_NAME.toString()))
            .andExpect(jsonPath("$.nickname").value(DEFAULT_NICKNAME.toString()))
            .andExpect(jsonPath("$.pushToken").value(DEFAULT_PUSH_TOKEN.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCustomUser() throws Exception {
        // Get the customUser
        restCustomUserMockMvc.perform(get("/api/custom-users/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCustomUser() throws Exception {
        // Initialize the database
        customUserRepository.saveAndFlush(customUser);
        customUserSearchRepository.save(customUser);
        int databaseSizeBeforeUpdate = customUserRepository.findAll().size();

        // Update the customUser
        CustomUser updatedCustomUser = customUserRepository.findOne(customUser.getId());
        updatedCustomUser
            .fullName(UPDATED_FULL_NAME)
            .nickname(UPDATED_NICKNAME)
            .pushToken(UPDATED_PUSH_TOKEN);
        CustomUserDTO customUserDTO = customUserMapper.toDto(updatedCustomUser);

        restCustomUserMockMvc.perform(put("/api/custom-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customUserDTO)))
            .andExpect(status().isOk());

        // Validate the CustomUser in the database
        List<CustomUser> customUserList = customUserRepository.findAll();
        assertThat(customUserList).hasSize(databaseSizeBeforeUpdate);
        CustomUser testCustomUser = customUserList.get(customUserList.size() - 1);
        assertThat(testCustomUser.getFullName()).isEqualTo(UPDATED_FULL_NAME);
        assertThat(testCustomUser.getNickname()).isEqualTo(UPDATED_NICKNAME);
        assertThat(testCustomUser.getPushToken()).isEqualTo(UPDATED_PUSH_TOKEN);

        // Validate the CustomUser in Elasticsearch
        CustomUser customUserEs = customUserSearchRepository.findOne(testCustomUser.getId());
        assertThat(customUserEs).isEqualToComparingFieldByField(testCustomUser);
    }

    @Test
    @Transactional
    public void updateNonExistingCustomUser() throws Exception {
        int databaseSizeBeforeUpdate = customUserRepository.findAll().size();

        // Create the CustomUser
        CustomUserDTO customUserDTO = customUserMapper.toDto(customUser);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCustomUserMockMvc.perform(put("/api/custom-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customUserDTO)))
            .andExpect(status().isCreated());

        // Validate the CustomUser in the database
        List<CustomUser> customUserList = customUserRepository.findAll();
        assertThat(customUserList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCustomUser() throws Exception {
        // Initialize the database
        customUserRepository.saveAndFlush(customUser);
        customUserSearchRepository.save(customUser);
        int databaseSizeBeforeDelete = customUserRepository.findAll().size();

        // Get the customUser
        restCustomUserMockMvc.perform(delete("/api/custom-users/{id}", customUser.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean customUserExistsInEs = customUserSearchRepository.exists(customUser.getId());
        assertThat(customUserExistsInEs).isFalse();

        // Validate the database is empty
        List<CustomUser> customUserList = customUserRepository.findAll();
        assertThat(customUserList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCustomUser() throws Exception {
        // Initialize the database
        customUserRepository.saveAndFlush(customUser);
        customUserSearchRepository.save(customUser);

        // Search the customUser
        restCustomUserMockMvc.perform(get("/api/_search/custom-users?query=id:" + customUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME.toString())))
            .andExpect(jsonPath("$.[*].nickname").value(hasItem(DEFAULT_NICKNAME.toString())))
            .andExpect(jsonPath("$.[*].pushToken").value(hasItem(DEFAULT_PUSH_TOKEN.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomUser.class);
        CustomUser customUser1 = new CustomUser();
        customUser1.setId(1L);
        CustomUser customUser2 = new CustomUser();
        customUser2.setId(customUser1.getId());
        assertThat(customUser1).isEqualTo(customUser2);
        customUser2.setId(2L);
        assertThat(customUser1).isNotEqualTo(customUser2);
        customUser1.setId(null);
        assertThat(customUser1).isNotEqualTo(customUser2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomUserDTO.class);
        CustomUserDTO customUserDTO1 = new CustomUserDTO();
        customUserDTO1.setId(1L);
        CustomUserDTO customUserDTO2 = new CustomUserDTO();
        assertThat(customUserDTO1).isNotEqualTo(customUserDTO2);
        customUserDTO2.setId(customUserDTO1.getId());
        assertThat(customUserDTO1).isEqualTo(customUserDTO2);
        customUserDTO2.setId(2L);
        assertThat(customUserDTO1).isNotEqualTo(customUserDTO2);
        customUserDTO1.setId(null);
        assertThat(customUserDTO1).isNotEqualTo(customUserDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(customUserMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(customUserMapper.fromId(null)).isNull();
    }
}
