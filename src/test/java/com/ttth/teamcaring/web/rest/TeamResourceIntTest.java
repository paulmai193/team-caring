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
import com.ttth.teamcaring.domain.Team;
import com.ttth.teamcaring.repository.TeamRepository;
import com.ttth.teamcaring.repository.search.TeamSearchRepository;
import com.ttth.teamcaring.service.TeamService;
import com.ttth.teamcaring.service.dto.TeamDTO;
import com.ttth.teamcaring.service.mapper.TeamMapper;
import com.ttth.teamcaring.web.rest.errors.ExceptionTranslator;

/**
 * Test class for the TeamResource REST controller.
 *
 * @see TeamResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TeamCaringApp.class)
@Ignore
public class TeamResourceIntTest {

    /** The Constant DEFAULT_NAME. */
    private static final String                   DEFAULT_NAME                     = "AAAAAAAAAA";
    
    /** The Constant UPDATED_NAME. */
    private static final String                   UPDATED_NAME                     = "BBBBBBBBBB";

    /** The Constant DEFAULT_DESCRIPTION. */
    private static final String                   DEFAULT_DESCRIPTION              = "AAAAAAAAAA";
    
    /** The Constant UPDATED_DESCRIPTION. */
    private static final String                   UPDATED_DESCRIPTION              = "BBBBBBBBBB";

    /** The Constant DEFAULT_LEVEL. */
    private static final Integer                  DEFAULT_LEVEL                    = 1;
    
    /** The Constant UPDATED_LEVEL. */
    private static final Integer                  UPDATED_LEVEL                    = 2;

    /** The Constant DEFAULT_TOTAL_MEMBER. */
    private static final Integer                  DEFAULT_TOTAL_MEMBER             = 1;
    
    /** The Constant UPDATED_TOTAL_MEMBER. */
    private static final Integer                  UPDATED_TOTAL_MEMBER             = 2;

    /** The Constant DEFAULT_EXTRA_GROUP_NAME. */
    private static final String                   DEFAULT_EXTRA_GROUP_NAME         = "AAAAAAAAAA";
    
    /** The Constant UPDATED_EXTRA_GROUP_NAME. */
    private static final String                   UPDATED_EXTRA_GROUP_NAME         = "BBBBBBBBBB";

    /** The Constant DEFAULT_EXTRA_GROUP_DESCRIPTION. */
    private static final String                   DEFAULT_EXTRA_GROUP_DESCRIPTION  = "AAAAAAAAAA";
    
    /** The Constant UPDATED_EXTRA_GROUP_DESCRIPTION. */
    private static final String                   UPDATED_EXTRA_GROUP_DESCRIPTION  = "BBBBBBBBBB";

    /** The Constant DEFAULT_EXTRA_GROUP_TOTAL_MEMBER. */
    private static final Integer                  DEFAULT_EXTRA_GROUP_TOTAL_MEMBER = 1;
    
    /** The Constant UPDATED_EXTRA_GROUP_TOTAL_MEMBER. */
    private static final Integer                  UPDATED_EXTRA_GROUP_TOTAL_MEMBER = 2;

    /** The team repository. */
    @Autowired
    private TeamRepository                        teamRepository;

    /** The team mapper. */
    @Autowired
    private TeamMapper                            teamMapper;

    /** The team service. */
    @Autowired
    private TeamService                           teamService;

    /** The team search repository. */
    @Autowired
    private TeamSearchRepository                  teamSearchRepository;

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

    /** The rest team mock mvc. */
    private MockMvc                               restTeamMockMvc;

    /** The team. */
    private Team                                  team;

    /**
     * Setup.
     */
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TeamResource teamResource = new TeamResource(teamService);
        this.restTeamMockMvc = MockMvcBuilders.standaloneSetup(teamResource)
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
     * @param em the em
     * @return the team
     */
    public static Team createEntity(EntityManager em) {
        Team team = new Team().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION)
                .level(DEFAULT_LEVEL).totalMember(DEFAULT_TOTAL_MEMBER)
                .extraGroupName(DEFAULT_EXTRA_GROUP_NAME)
                .extraGroupDescription(DEFAULT_EXTRA_GROUP_DESCRIPTION)
                .extraGroupTotalMember(DEFAULT_EXTRA_GROUP_TOTAL_MEMBER);
        return team;
    }

    /**
     * Inits the test.
     */
    @Before
    public void initTest() {
        teamSearchRepository.deleteAll();
        team = createEntity(em);
    }

    /**
     * Creates the team.
     *
     * @throws Exception the exception
     */
    @Test
    @Transactional
    public void createTeam() throws Exception {
        int databaseSizeBeforeCreate = teamRepository.findAll().size();

        // Create the Team
        TeamDTO teamDTO = teamMapper.toDto(team);
        restTeamMockMvc
                .perform(post("/api/teams").contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(teamDTO)))
                .andExpect(status().isCreated());

        // Validate the Team in the database
        List<Team> teamList = teamRepository.findAll();
        assertThat(teamList).hasSize(databaseSizeBeforeCreate + 1);
        Team testTeam = teamList.get(teamList.size() - 1);
        assertThat(testTeam.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTeam.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTeam.getLevel()).isEqualTo(DEFAULT_LEVEL);
        assertThat(testTeam.getTotalMember()).isEqualTo(DEFAULT_TOTAL_MEMBER);
        assertThat(testTeam.getExtraGroupName()).isEqualTo(DEFAULT_EXTRA_GROUP_NAME);
        assertThat(testTeam.getExtraGroupDescription()).isEqualTo(DEFAULT_EXTRA_GROUP_DESCRIPTION);
        assertThat(testTeam.getExtraGroupTotalMember()).isEqualTo(DEFAULT_EXTRA_GROUP_TOTAL_MEMBER);

        // Validate the Team in Elasticsearch
        Team teamEs = teamSearchRepository.findOne(testTeam.getId());
        assertThat(teamEs).isEqualToComparingFieldByField(testTeam);
    }

    /**
     * Creates the team with existing id.
     *
     * @throws Exception the exception
     */
    @Test
    @Transactional
    public void createTeamWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = teamRepository.findAll().size();

        // Create the Team with an existing ID
        team.setId(1L);
        TeamDTO teamDTO = teamMapper.toDto(team);

        // An entity with an existing ID cannot be created, so this API call
        // must fail
        restTeamMockMvc
                .perform(post("/api/teams").contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(teamDTO)))
                .andExpect(status().isBadRequest());

        // Validate the Team in the database
        List<Team> teamList = teamRepository.findAll();
        assertThat(teamList).hasSize(databaseSizeBeforeCreate);
    }

    /**
     * Gets the all teams.
     *
     * @return the all teams
     * @throws Exception the exception
     */
    @Test
    @Transactional
    public void getAllTeams() throws Exception {
        // Initialize the database
        teamRepository.saveAndFlush(team);

        // Get all the teamList
        restTeamMockMvc.perform(get("/api/teams?sort=id,desc")).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(team.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description")
                        .value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL)))
                .andExpect(jsonPath("$.[*].totalMember").value(hasItem(DEFAULT_TOTAL_MEMBER)))
                .andExpect(jsonPath("$.[*].extraGroupName")
                        .value(hasItem(DEFAULT_EXTRA_GROUP_NAME.toString())))
                .andExpect(jsonPath("$.[*].extraGroupDescription")
                        .value(hasItem(DEFAULT_EXTRA_GROUP_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].extraGroupTotalMember")
                        .value(hasItem(DEFAULT_EXTRA_GROUP_TOTAL_MEMBER)));
    }

    /**
     * Gets the team.
     *
     * @return the team
     * @throws Exception the exception
     */
    @Test
    @Transactional
    public void getTeam() throws Exception {
        // Initialize the database
        teamRepository.saveAndFlush(team);

        // Get the team
        restTeamMockMvc.perform(get("/api/teams/{id}", team.getId())).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id").value(team.getId().intValue()))
                .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
                .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
                .andExpect(jsonPath("$.level").value(DEFAULT_LEVEL))
                .andExpect(jsonPath("$.totalMember").value(DEFAULT_TOTAL_MEMBER))
                .andExpect(jsonPath("$.extraGroupName").value(DEFAULT_EXTRA_GROUP_NAME.toString()))
                .andExpect(jsonPath("$.extraGroupDescription")
                        .value(DEFAULT_EXTRA_GROUP_DESCRIPTION.toString()))
                .andExpect(jsonPath("$.extraGroupTotalMember")
                        .value(DEFAULT_EXTRA_GROUP_TOTAL_MEMBER));
    }

    /**
     * Gets the non existing team.
     *
     * @return the non existing team
     * @throws Exception the exception
     */
    @Test
    @Transactional
    public void getNonExistingTeam() throws Exception {
        // Get the team
        restTeamMockMvc.perform(get("/api/teams/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    /**
     * Update team.
     *
     * @throws Exception the exception
     */
    @Test
    @Transactional
    public void updateTeam() throws Exception {
        // Initialize the database
        teamRepository.saveAndFlush(team);
        teamSearchRepository.save(team);
        int databaseSizeBeforeUpdate = teamRepository.findAll().size();

        // Update the team
        Team updatedTeam = teamRepository.findOne(team.getId());
        updatedTeam.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).level(UPDATED_LEVEL)
                .totalMember(UPDATED_TOTAL_MEMBER).extraGroupName(UPDATED_EXTRA_GROUP_NAME)
                .extraGroupDescription(UPDATED_EXTRA_GROUP_DESCRIPTION)
                .extraGroupTotalMember(UPDATED_EXTRA_GROUP_TOTAL_MEMBER);
        TeamDTO teamDTO = teamMapper.toDto(updatedTeam);

        restTeamMockMvc
                .perform(put("/api/teams").contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(teamDTO)))
                .andExpect(status().isOk());

        // Validate the Team in the database
        List<Team> teamList = teamRepository.findAll();
        assertThat(teamList).hasSize(databaseSizeBeforeUpdate);
        Team testTeam = teamList.get(teamList.size() - 1);
        assertThat(testTeam.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTeam.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTeam.getLevel()).isEqualTo(UPDATED_LEVEL);
        assertThat(testTeam.getTotalMember()).isEqualTo(UPDATED_TOTAL_MEMBER);
        assertThat(testTeam.getExtraGroupName()).isEqualTo(UPDATED_EXTRA_GROUP_NAME);
        assertThat(testTeam.getExtraGroupDescription()).isEqualTo(UPDATED_EXTRA_GROUP_DESCRIPTION);
        assertThat(testTeam.getExtraGroupTotalMember()).isEqualTo(UPDATED_EXTRA_GROUP_TOTAL_MEMBER);

        // Validate the Team in Elasticsearch
        Team teamEs = teamSearchRepository.findOne(testTeam.getId());
        assertThat(teamEs).isEqualToComparingFieldByField(testTeam);
    }

    /**
     * Update non existing team.
     *
     * @throws Exception the exception
     */
    @Test
    @Transactional
    public void updateNonExistingTeam() throws Exception {
        int databaseSizeBeforeUpdate = teamRepository.findAll().size();

        // Create the Team
        TeamDTO teamDTO = teamMapper.toDto(team);

        // If the entity doesn't have an ID, it will be created instead of just
        // being updated
        restTeamMockMvc
                .perform(put("/api/teams").contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(teamDTO)))
                .andExpect(status().isCreated());

        // Validate the Team in the database
        List<Team> teamList = teamRepository.findAll();
        assertThat(teamList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    /**
     * Delete team.
     *
     * @throws Exception the exception
     */
    @Test
    @Transactional
    public void deleteTeam() throws Exception {
        // Initialize the database
        teamRepository.saveAndFlush(team);
        teamSearchRepository.save(team);
        int databaseSizeBeforeDelete = teamRepository.findAll().size();

        // Get the team
        restTeamMockMvc.perform(
                delete("/api/teams/{id}", team.getId()).accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean teamExistsInEs = teamSearchRepository.exists(team.getId());
        assertThat(teamExistsInEs).isFalse();

        // Validate the database is empty
        List<Team> teamList = teamRepository.findAll();
        assertThat(teamList).hasSize(databaseSizeBeforeDelete - 1);
    }

    /**
     * Search team.
     *
     * @throws Exception the exception
     */
    @Test
    @Transactional
    public void searchTeam() throws Exception {
        // Initialize the database
        teamRepository.saveAndFlush(team);
        teamSearchRepository.save(team);

        // Search the team
        restTeamMockMvc.perform(get("/api/_search/teams?query=id:" + team.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(team.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description")
                        .value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL)))
                .andExpect(jsonPath("$.[*].totalMember").value(hasItem(DEFAULT_TOTAL_MEMBER)))
                .andExpect(jsonPath("$.[*].extraGroupName")
                        .value(hasItem(DEFAULT_EXTRA_GROUP_NAME.toString())))
                .andExpect(jsonPath("$.[*].extraGroupDescription")
                        .value(hasItem(DEFAULT_EXTRA_GROUP_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].extraGroupTotalMember")
                        .value(hasItem(DEFAULT_EXTRA_GROUP_TOTAL_MEMBER)));
    }

    /**
     * Equals verifier.
     *
     * @throws Exception the exception
     */
    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Team.class);
        Team team1 = new Team();
        team1.setId(1L);
        Team team2 = new Team();
        team2.setId(team1.getId());
        assertThat(team1).isEqualTo(team2);
        team2.setId(2L);
        assertThat(team1).isNotEqualTo(team2);
        team1.setId(null);
        assertThat(team1).isNotEqualTo(team2);
    }

    /**
     * Dto equals verifier.
     *
     * @throws Exception the exception
     */
    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TeamDTO.class);
        TeamDTO teamDTO1 = new TeamDTO();
        teamDTO1.setId(1L);
        TeamDTO teamDTO2 = new TeamDTO();
        assertThat(teamDTO1).isNotEqualTo(teamDTO2);
        teamDTO2.setId(teamDTO1.getId());
        assertThat(teamDTO1).isEqualTo(teamDTO2);
        teamDTO2.setId(2L);
        assertThat(teamDTO1).isNotEqualTo(teamDTO2);
        teamDTO1.setId(null);
        assertThat(teamDTO1).isNotEqualTo(teamDTO2);
    }

    /**
     * Test entity from id.
     */
    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(teamMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(teamMapper.fromId(null)).isNull();
    }
}
