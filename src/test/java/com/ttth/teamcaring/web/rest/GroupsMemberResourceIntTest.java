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
import com.ttth.teamcaring.domain.GroupsMember;
import com.ttth.teamcaring.repository.GroupsMemberRepository;
import com.ttth.teamcaring.repository.search.GroupsMemberSearchRepository;
import com.ttth.teamcaring.service.GroupsMemberService;
import com.ttth.teamcaring.service.dto.GroupsMemberDTO;
import com.ttth.teamcaring.service.mapper.GroupsMemberMapper;
import com.ttth.teamcaring.web.rest.errors.ExceptionTranslator;

/**
 * Test class for the GroupsMemberResource REST controller.
 *
 * @see GroupsMemberResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TeamCaringApp.class)
@Ignore
public class GroupsMemberResourceIntTest {

    /** The Constant DEFAULT_LEVEL. */
    private static final Integer                  DEFAULT_LEVEL  = 1;

    /** The Constant UPDATED_LEVEL. */
    private static final Integer                  UPDATED_LEVEL  = 2;

    /** The Constant DEFAULT_STATUS. */
    private static final Integer                  DEFAULT_STATUS = 1;

    /** The Constant UPDATED_STATUS. */
    private static final Integer                  UPDATED_STATUS = 2;

    /** The groups member repository. */
    @Autowired
    private GroupsMemberRepository                groupsMemberRepository;

    /** The groups member mapper. */
    @Autowired
    private GroupsMemberMapper                    groupsMemberMapper;

    /** The groups member service. */
    @Autowired
    private GroupsMemberService                   groupsMemberService;

    /** The groups member search repository. */
    @Autowired
    private GroupsMemberSearchRepository          groupsMemberSearchRepository;

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

    /** The rest groups member mock mvc. */
    private MockMvc                               restGroupsMemberMockMvc;

    /** The groups member. */
    private GroupsMember                          groupsMember;

    /**
     * Setup.
     */
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final GroupsMemberResource groupsMemberResource = new GroupsMemberResource(
                groupsMemberService);
        this.restGroupsMemberMockMvc = MockMvcBuilders.standaloneSetup(groupsMemberResource)
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
     * @return the groups member
     */
    public static GroupsMember createEntity(EntityManager em) {
        GroupsMember groupsMember = new GroupsMember().level(DEFAULT_LEVEL).status(DEFAULT_STATUS);
        return groupsMember;
    }

    /**
     * Inits the test.
     */
    @Before
    public void initTest() {
        groupsMemberSearchRepository.deleteAll();
        groupsMember = createEntity(em);
    }

    /**
     * Creates the groups member.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    @Transactional
    public void createGroupsMember() throws Exception {
        int databaseSizeBeforeCreate = groupsMemberRepository.findAll().size();

        // Create the GroupsMember
        GroupsMemberDTO groupsMemberDTO = groupsMemberMapper.toDto(groupsMember);
        restGroupsMemberMockMvc
                .perform(post("/api/groups-members").contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(groupsMemberDTO)))
                .andExpect(status().isCreated());

        // Validate the GroupsMember in the database
        List<GroupsMember> groupsMemberList = groupsMemberRepository.findAll();
        assertThat(groupsMemberList).hasSize(databaseSizeBeforeCreate + 1);
        GroupsMember testGroupsMember = groupsMemberList.get(groupsMemberList.size() - 1);
        assertThat(testGroupsMember.getLevel()).isEqualTo(DEFAULT_LEVEL);
        assertThat(testGroupsMember.getStatus()).isEqualTo(DEFAULT_STATUS);

        // Validate the GroupsMember in Elasticsearch
        GroupsMember groupsMemberEs = groupsMemberSearchRepository
                .findOne(testGroupsMember.getId());
        assertThat(groupsMemberEs).isEqualToComparingFieldByField(testGroupsMember);
    }

    /**
     * Creates the groups member with existing id.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    @Transactional
    public void createGroupsMemberWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = groupsMemberRepository.findAll().size();

        // Create the GroupsMember with an existing ID
        groupsMember.setId(1L);
        GroupsMemberDTO groupsMemberDTO = groupsMemberMapper.toDto(groupsMember);

        // An entity with an existing ID cannot be created, so this API call
        // must fail
        restGroupsMemberMockMvc
                .perform(post("/api/groups-members").contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(groupsMemberDTO)))
                .andExpect(status().isBadRequest());

        // Validate the GroupsMember in the database
        List<GroupsMember> groupsMemberList = groupsMemberRepository.findAll();
        assertThat(groupsMemberList).hasSize(databaseSizeBeforeCreate);
    }

    /**
     * Gets the all groups members.
     *
     * @return the all groups members
     * @throws Exception
     *         the exception
     */
    @Test
    @Transactional
    public void getAllGroupsMembers() throws Exception {
        // Initialize the database
        groupsMemberRepository.saveAndFlush(groupsMember);

        // Get all the groupsMemberList
        restGroupsMemberMockMvc.perform(get("/api/groups-members?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(groupsMember.getId().intValue())))
                .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL)))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }

    /**
     * Gets the groups member.
     *
     * @return the groups member
     * @throws Exception
     *         the exception
     */
    @Test
    @Transactional
    public void getGroupsMember() throws Exception {
        // Initialize the database
        groupsMemberRepository.saveAndFlush(groupsMember);

        // Get the groupsMember
        restGroupsMemberMockMvc.perform(get("/api/groups-members/{id}", groupsMember.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id").value(groupsMember.getId().intValue()))
                .andExpect(jsonPath("$.level").value(DEFAULT_LEVEL))
                .andExpect(jsonPath("$.status").value(DEFAULT_STATUS));
    }

    /**
     * Gets the non existing groups member.
     *
     * @return the non existing groups member
     * @throws Exception
     *         the exception
     */
    @Test
    @Transactional
    public void getNonExistingGroupsMember() throws Exception {
        // Get the groupsMember
        restGroupsMemberMockMvc.perform(get("/api/groups-members/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    /**
     * Update groups member.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    @Transactional
    public void updateGroupsMember() throws Exception {
        // Initialize the database
        groupsMemberRepository.saveAndFlush(groupsMember);
        groupsMemberSearchRepository.save(groupsMember);
        int databaseSizeBeforeUpdate = groupsMemberRepository.findAll().size();

        // Update the groupsMember
        GroupsMember updatedGroupsMember = groupsMemberRepository.findOne(groupsMember.getId());
        updatedGroupsMember.level(UPDATED_LEVEL).status(UPDATED_STATUS);
        GroupsMemberDTO groupsMemberDTO = groupsMemberMapper.toDto(updatedGroupsMember);

        restGroupsMemberMockMvc
                .perform(put("/api/groups-members").contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(groupsMemberDTO)))
                .andExpect(status().isOk());

        // Validate the GroupsMember in the database
        List<GroupsMember> groupsMemberList = groupsMemberRepository.findAll();
        assertThat(groupsMemberList).hasSize(databaseSizeBeforeUpdate);
        GroupsMember testGroupsMember = groupsMemberList.get(groupsMemberList.size() - 1);
        assertThat(testGroupsMember.getLevel()).isEqualTo(UPDATED_LEVEL);
        assertThat(testGroupsMember.getStatus()).isEqualTo(UPDATED_STATUS);

        // Validate the GroupsMember in Elasticsearch
        GroupsMember groupsMemberEs = groupsMemberSearchRepository
                .findOne(testGroupsMember.getId());
        assertThat(groupsMemberEs).isEqualToComparingFieldByField(testGroupsMember);
    }

    /**
     * Update non existing groups member.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    @Transactional
    public void updateNonExistingGroupsMember() throws Exception {
        int databaseSizeBeforeUpdate = groupsMemberRepository.findAll().size();

        // Create the GroupsMember
        GroupsMemberDTO groupsMemberDTO = groupsMemberMapper.toDto(groupsMember);

        // If the entity doesn't have an ID, it will be created instead of just
        // being updated
        restGroupsMemberMockMvc
                .perform(put("/api/groups-members").contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(groupsMemberDTO)))
                .andExpect(status().isCreated());

        // Validate the GroupsMember in the database
        List<GroupsMember> groupsMemberList = groupsMemberRepository.findAll();
        assertThat(groupsMemberList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    /**
     * Delete groups member.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    @Transactional
    public void deleteGroupsMember() throws Exception {
        // Initialize the database
        groupsMemberRepository.saveAndFlush(groupsMember);
        groupsMemberSearchRepository.save(groupsMember);
        int databaseSizeBeforeDelete = groupsMemberRepository.findAll().size();

        // Get the groupsMember
        restGroupsMemberMockMvc.perform(delete("/api/groups-members/{id}", groupsMember.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8)).andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean groupsMemberExistsInEs = groupsMemberSearchRepository.exists(groupsMember.getId());
        assertThat(groupsMemberExistsInEs).isFalse();

        // Validate the database is empty
        List<GroupsMember> groupsMemberList = groupsMemberRepository.findAll();
        assertThat(groupsMemberList).hasSize(databaseSizeBeforeDelete - 1);
    }

    /**
     * Search groups member.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    @Transactional
    public void searchGroupsMember() throws Exception {
        // Initialize the database
        groupsMemberRepository.saveAndFlush(groupsMember);
        groupsMemberSearchRepository.save(groupsMember);

        // Search the groupsMember
        restGroupsMemberMockMvc
                .perform(get("/api/_search/groups-members?query=id:" + groupsMember.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(groupsMember.getId().intValue())))
                .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL)))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
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
        TestUtil.equalsVerifier(GroupsMember.class);
        GroupsMember groupsMember1 = new GroupsMember();
        groupsMember1.setId(1L);
        GroupsMember groupsMember2 = new GroupsMember();
        groupsMember2.setId(groupsMember1.getId());
        assertThat(groupsMember1).isEqualTo(groupsMember2);
        groupsMember2.setId(2L);
        assertThat(groupsMember1).isNotEqualTo(groupsMember2);
        groupsMember1.setId(null);
        assertThat(groupsMember1).isNotEqualTo(groupsMember2);
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
        TestUtil.equalsVerifier(GroupsMemberDTO.class);
        GroupsMemberDTO groupsMemberDTO1 = new GroupsMemberDTO();
        groupsMemberDTO1.setId(1L);
        GroupsMemberDTO groupsMemberDTO2 = new GroupsMemberDTO();
        assertThat(groupsMemberDTO1).isNotEqualTo(groupsMemberDTO2);
        groupsMemberDTO2.setId(groupsMemberDTO1.getId());
        assertThat(groupsMemberDTO1).isEqualTo(groupsMemberDTO2);
        groupsMemberDTO2.setId(2L);
        assertThat(groupsMemberDTO1).isNotEqualTo(groupsMemberDTO2);
        groupsMemberDTO1.setId(null);
        assertThat(groupsMemberDTO1).isNotEqualTo(groupsMemberDTO2);
    }

    /**
     * Test entity from id.
     */
    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(groupsMemberMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(groupsMemberMapper.fromId(null)).isNull();
    }
}
