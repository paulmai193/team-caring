/*
 * 
 */
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
import com.ttth.teamcaring.domain.Groups;
import com.ttth.teamcaring.repository.GroupsRepository;
import com.ttth.teamcaring.repository.search.GroupsSearchRepository;
import com.ttth.teamcaring.service.GroupsService;
import com.ttth.teamcaring.service.dto.GroupsDTO;
import com.ttth.teamcaring.service.mapper.GroupsMapper;
import com.ttth.teamcaring.web.rest.errors.ExceptionTranslator;

/**
 * Test class for the GroupsResource REST controller.
 *
 * @see GroupsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TeamCaringApp.class)
@Ignore
public class GroupsResourceIntTest {

    /** The groups repository. */
    @Autowired
    private GroupsRepository                      groupsRepository;

    /** The groups mapper. */
    @Autowired
    private GroupsMapper                          groupsMapper;

    /** The groups service. */
    @Autowired
    private GroupsService                         groupsService;

    /** The groups search repository. */
    @Autowired
    private GroupsSearchRepository                groupsSearchRepository;

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

    /** The rest groups mock mvc. */
    private MockMvc                               restGroupsMockMvc;

    /** The groups. */
    private Groups                                groups;

    /**
     * Setup.
     */
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final GroupsResource groupsResource = new GroupsResource(groupsService);
        this.restGroupsMockMvc = MockMvcBuilders.standaloneSetup(groupsResource)
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
     *
     * @param em
     *        the em
     * @return the groups
     */
    public static Groups createEntity(EntityManager em) {
        Groups groups = new Groups();
        return groups;
    }

    /**
     * Inits the test.
     */
    @Before
    public void initTest() {
        groupsSearchRepository.deleteAll();
        groups = createEntity(em);
    }

    /**
     * Creates the groups.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    @Transactional
    public void createGroups() throws Exception {
        int databaseSizeBeforeCreate = groupsRepository.findAll().size();

        // Create the Groups
        GroupsDTO groupsDTO = groupsMapper.toDto(groups);
        restGroupsMockMvc
                .perform(post("/api/groups").contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(groupsDTO)))
                .andExpect(status().isCreated());

        // Validate the Groups in the database
        List<Groups> groupsList = groupsRepository.findAll();
        assertThat(groupsList).hasSize(databaseSizeBeforeCreate + 1);
        Groups testGroups = groupsList.get(groupsList.size() - 1);

        // Validate the Groups in Elasticsearch
        Groups groupsEs = groupsSearchRepository.findOne(testGroups.getId());
        assertThat(groupsEs).isEqualToComparingFieldByField(testGroups);
    }

    /**
     * Creates the groups with existing id.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    @Transactional
    public void createGroupsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = groupsRepository.findAll().size();

        // Create the Groups with an existing ID
        groups.setId(1L);
        GroupsDTO groupsDTO = groupsMapper.toDto(groups);

        // An entity with an existing ID cannot be created, so this API call
        // must fail
        restGroupsMockMvc
                .perform(post("/api/groups").contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(groupsDTO)))
                .andExpect(status().isBadRequest());

        // Validate the Groups in the database
        List<Groups> groupsList = groupsRepository.findAll();
        assertThat(groupsList).hasSize(databaseSizeBeforeCreate);
    }

    /**
     * Gets the all groups.
     *
     * @return the all groups
     * @throws Exception
     *         the exception
     */
    @Test
    @Transactional
    public void getAllGroups() throws Exception {
        // Initialize the database
        groupsRepository.saveAndFlush(groups);

        // Get all the groupsList
        restGroupsMockMvc.perform(get("/api/groups?sort=id,desc")).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(groups.getId().intValue())));
    }

    /**
     * Gets the groups.
     *
     * @return the groups
     * @throws Exception
     *         the exception
     */
    @Test
    @Transactional
    public void getGroups() throws Exception {
        // Initialize the database
        groupsRepository.saveAndFlush(groups);

        // Get the groups
        restGroupsMockMvc.perform(get("/api/groups/{id}", groups.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id").value(groups.getId().intValue()));
    }

    /**
     * Gets the non existing groups.
     *
     * @return the non existing groups
     * @throws Exception
     *         the exception
     */
    @Test
    @Transactional
    public void getNonExistingGroups() throws Exception {
        // Get the groups
        restGroupsMockMvc.perform(get("/api/groups/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    /**
     * Update groups.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    @Transactional
    public void updateGroups() throws Exception {
        // Initialize the database
        groupsRepository.saveAndFlush(groups);
        groupsSearchRepository.save(groups);
        int databaseSizeBeforeUpdate = groupsRepository.findAll().size();

        // Update the groups
        Groups updatedGroups = groupsRepository.findOne(groups.getId());
        GroupsDTO groupsDTO = groupsMapper.toDto(updatedGroups);

        restGroupsMockMvc
                .perform(put("/api/groups").contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(groupsDTO)))
                .andExpect(status().isOk());

        // Validate the Groups in the database
        List<Groups> groupsList = groupsRepository.findAll();
        assertThat(groupsList).hasSize(databaseSizeBeforeUpdate);
        Groups testGroups = groupsList.get(groupsList.size() - 1);

        // Validate the Groups in Elasticsearch
        Groups groupsEs = groupsSearchRepository.findOne(testGroups.getId());
        assertThat(groupsEs).isEqualToComparingFieldByField(testGroups);
    }

    /**
     * Update non existing groups.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    @Transactional
    public void updateNonExistingGroups() throws Exception {
        int databaseSizeBeforeUpdate = groupsRepository.findAll().size();

        // Create the Groups
        GroupsDTO groupsDTO = groupsMapper.toDto(groups);

        // If the entity doesn't have an ID, it will be created instead of just
        // being updated
        restGroupsMockMvc
                .perform(put("/api/groups").contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(groupsDTO)))
                .andExpect(status().isCreated());

        // Validate the Groups in the database
        List<Groups> groupsList = groupsRepository.findAll();
        assertThat(groupsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    /**
     * Delete groups.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    @Transactional
    public void deleteGroups() throws Exception {
        // Initialize the database
        groupsRepository.saveAndFlush(groups);
        groupsSearchRepository.save(groups);
        int databaseSizeBeforeDelete = groupsRepository.findAll().size();

        // Get the groups
        restGroupsMockMvc.perform(
                delete("/api/groups/{id}", groups.getId()).accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean groupsExistsInEs = groupsSearchRepository.exists(groups.getId());
        assertThat(groupsExistsInEs).isFalse();

        // Validate the database is empty
        List<Groups> groupsList = groupsRepository.findAll();
        assertThat(groupsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    /**
     * Search groups.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    @Transactional
    public void searchGroups() throws Exception {
        // Initialize the database
        groupsRepository.saveAndFlush(groups);
        groupsSearchRepository.save(groups);

        // Search the groups
        restGroupsMockMvc.perform(get("/api/_search/groups?query=id:" + groups.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(groups.getId().intValue())));
    }

    /**
     * Equals verifier.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Groups.class);
        Groups groups1 = new Groups();
        groups1.setId(1L);
        Groups groups2 = new Groups();
        groups2.setId(groups1.getId());
        assertThat(groups1).isEqualTo(groups2);
        groups2.setId(2L);
        assertThat(groups1).isNotEqualTo(groups2);
        groups1.setId(null);
        assertThat(groups1).isNotEqualTo(groups2);
    }

    /**
     * Dto equals verifier.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GroupsDTO.class);
        GroupsDTO groupsDTO1 = new GroupsDTO();
        groupsDTO1.setId(1L);
        GroupsDTO groupsDTO2 = new GroupsDTO();
        assertThat(groupsDTO1).isNotEqualTo(groupsDTO2);
        groupsDTO2.setId(groupsDTO1.getId());
        assertThat(groupsDTO1).isEqualTo(groupsDTO2);
        groupsDTO2.setId(2L);
        assertThat(groupsDTO1).isNotEqualTo(groupsDTO2);
        groupsDTO1.setId(null);
        assertThat(groupsDTO1).isNotEqualTo(groupsDTO2);
    }

    /**
     * Test entity from id.
     */
    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(groupsMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(groupsMapper.fromId(null)).isNull();
    }
}
