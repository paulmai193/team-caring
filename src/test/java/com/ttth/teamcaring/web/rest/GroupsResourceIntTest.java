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

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_OFFICAL = false;
    private static final Boolean UPDATED_OFFICAL = true;

    private static final Integer DEFAULT_TOTAL_MEMBER = 1;
    private static final Integer UPDATED_TOTAL_MEMBER = 2;

    @Autowired
    private GroupsRepository groupsRepository;

    @Autowired
    private GroupsMapper groupsMapper;

    @Autowired
    private GroupsService groupsService;

    @Autowired
    private GroupsSearchRepository groupsSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restGroupsMockMvc;

    private Groups groups;

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
     */
    public static Groups createEntity(EntityManager em) {
        Groups groups = new Groups()
            .description(DEFAULT_DESCRIPTION)
            .offical(DEFAULT_OFFICAL)
            .totalMember(DEFAULT_TOTAL_MEMBER);
        return groups;
    }

    @Before
    public void initTest() {
        groupsSearchRepository.deleteAll();
        groups = createEntity(em);
    }

    @Test
    @Transactional
    public void createGroups() throws Exception {
        int databaseSizeBeforeCreate = groupsRepository.findAll().size();

        // Create the Groups
        GroupsDTO groupsDTO = groupsMapper.toDto(groups);
        restGroupsMockMvc.perform(post("/api/groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(groupsDTO)))
            .andExpect(status().isCreated());

        // Validate the Groups in the database
        List<Groups> groupsList = groupsRepository.findAll();
        assertThat(groupsList).hasSize(databaseSizeBeforeCreate + 1);
        Groups testGroups = groupsList.get(groupsList.size() - 1);
        assertThat(testGroups.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testGroups.isOffical()).isEqualTo(DEFAULT_OFFICAL);
        assertThat(testGroups.getTotalMember()).isEqualTo(DEFAULT_TOTAL_MEMBER);

        // Validate the Groups in Elasticsearch
        Groups groupsEs = groupsSearchRepository.findOne(testGroups.getId());
        assertThat(groupsEs).isEqualToComparingFieldByField(testGroups);
    }

    @Test
    @Transactional
    public void createGroupsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = groupsRepository.findAll().size();

        // Create the Groups with an existing ID
        groups.setId(1L);
        GroupsDTO groupsDTO = groupsMapper.toDto(groups);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGroupsMockMvc.perform(post("/api/groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(groupsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Groups in the database
        List<Groups> groupsList = groupsRepository.findAll();
        assertThat(groupsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllGroups() throws Exception {
        // Initialize the database
        groupsRepository.saveAndFlush(groups);

        // Get all the groupsList
        restGroupsMockMvc.perform(get("/api/groups?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(groups.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].offical").value(hasItem(DEFAULT_OFFICAL.booleanValue())))
            .andExpect(jsonPath("$.[*].totalMember").value(hasItem(DEFAULT_TOTAL_MEMBER)));
    }

    @Test
    @Transactional
    public void getGroups() throws Exception {
        // Initialize the database
        groupsRepository.saveAndFlush(groups);

        // Get the groups
        restGroupsMockMvc.perform(get("/api/groups/{id}", groups.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(groups.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.offical").value(DEFAULT_OFFICAL.booleanValue()))
            .andExpect(jsonPath("$.totalMember").value(DEFAULT_TOTAL_MEMBER));
    }

    @Test
    @Transactional
    public void getNonExistingGroups() throws Exception {
        // Get the groups
        restGroupsMockMvc.perform(get("/api/groups/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGroups() throws Exception {
        // Initialize the database
        groupsRepository.saveAndFlush(groups);
        groupsSearchRepository.save(groups);
        int databaseSizeBeforeUpdate = groupsRepository.findAll().size();

        // Update the groups
        Groups updatedGroups = groupsRepository.findOne(groups.getId());
        updatedGroups
            .description(UPDATED_DESCRIPTION)
            .offical(UPDATED_OFFICAL)
            .totalMember(UPDATED_TOTAL_MEMBER);
        GroupsDTO groupsDTO = groupsMapper.toDto(updatedGroups);

        restGroupsMockMvc.perform(put("/api/groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(groupsDTO)))
            .andExpect(status().isOk());

        // Validate the Groups in the database
        List<Groups> groupsList = groupsRepository.findAll();
        assertThat(groupsList).hasSize(databaseSizeBeforeUpdate);
        Groups testGroups = groupsList.get(groupsList.size() - 1);
        assertThat(testGroups.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testGroups.isOffical()).isEqualTo(UPDATED_OFFICAL);
        assertThat(testGroups.getTotalMember()).isEqualTo(UPDATED_TOTAL_MEMBER);

        // Validate the Groups in Elasticsearch
        Groups groupsEs = groupsSearchRepository.findOne(testGroups.getId());
        assertThat(groupsEs).isEqualToComparingFieldByField(testGroups);
    }

    @Test
    @Transactional
    public void updateNonExistingGroups() throws Exception {
        int databaseSizeBeforeUpdate = groupsRepository.findAll().size();

        // Create the Groups
        GroupsDTO groupsDTO = groupsMapper.toDto(groups);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restGroupsMockMvc.perform(put("/api/groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(groupsDTO)))
            .andExpect(status().isCreated());

        // Validate the Groups in the database
        List<Groups> groupsList = groupsRepository.findAll();
        assertThat(groupsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteGroups() throws Exception {
        // Initialize the database
        groupsRepository.saveAndFlush(groups);
        groupsSearchRepository.save(groups);
        int databaseSizeBeforeDelete = groupsRepository.findAll().size();

        // Get the groups
        restGroupsMockMvc.perform(delete("/api/groups/{id}", groups.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean groupsExistsInEs = groupsSearchRepository.exists(groups.getId());
        assertThat(groupsExistsInEs).isFalse();

        // Validate the database is empty
        List<Groups> groupsList = groupsRepository.findAll();
        assertThat(groupsList).hasSize(databaseSizeBeforeDelete - 1);
    }

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
            .andExpect(jsonPath("$.[*].id").value(hasItem(groups.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].offical").value(hasItem(DEFAULT_OFFICAL.booleanValue())))
            .andExpect(jsonPath("$.[*].totalMember").value(hasItem(DEFAULT_TOTAL_MEMBER)));
    }

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

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(groupsMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(groupsMapper.fromId(null)).isNull();
    }
}
