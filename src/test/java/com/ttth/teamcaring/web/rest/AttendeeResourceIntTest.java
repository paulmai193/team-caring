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
import com.ttth.teamcaring.domain.Attendee;
import com.ttth.teamcaring.repository.AttendeeRepository;
import com.ttth.teamcaring.repository.search.AttendeeSearchRepository;
import com.ttth.teamcaring.service.AttendeeService;
import com.ttth.teamcaring.service.dto.AttendeeDTO;
import com.ttth.teamcaring.service.mapper.AttendeeMapper;
import com.ttth.teamcaring.web.rest.errors.ExceptionTranslator;

/**
 * Test class for the AttendeeResource REST controller.
 *
 * @see AttendeeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TeamCaringApp.class)
@Ignore
public class AttendeeResourceIntTest {

    /** The Constant DEFAULT_STATUS. */
    private static final Integer                  DEFAULT_STATUS = 1;
    
    /** The Constant UPDATED_STATUS. */
    private static final Integer                  UPDATED_STATUS = 2;

    /** The attendee repository. */
    @Autowired
    private AttendeeRepository                    attendeeRepository;

    /** The attendee mapper. */
    @Autowired
    private AttendeeMapper                        attendeeMapper;

    /** The attendee service. */
    @Autowired
    private AttendeeService                       attendeeService;

    /** The attendee search repository. */
    @Autowired
    private AttendeeSearchRepository              attendeeSearchRepository;

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

    /** The rest attendee mock mvc. */
    private MockMvc                               restAttendeeMockMvc;

    /** The attendee. */
    private Attendee                              attendee;

    /**
     * Setup.
     */
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AttendeeResource attendeeResource = new AttendeeResource(attendeeService);
        this.restAttendeeMockMvc = MockMvcBuilders.standaloneSetup(attendeeResource)
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
     * @return the attendee
     */
    public static Attendee createEntity(EntityManager em) {
        Attendee attendee = new Attendee().status(DEFAULT_STATUS);
        return attendee;
    }

    /**
     * Inits the test.
     */
    @Before
    public void initTest() {
        attendeeSearchRepository.deleteAll();
        attendee = createEntity(em);
    }

    /**
     * Creates the attendee.
     *
     * @throws Exception the exception
     */
    @Test
    @Transactional
    public void createAttendee() throws Exception {
        int databaseSizeBeforeCreate = attendeeRepository.findAll().size();

        // Create the Attendee
        AttendeeDTO attendeeDTO = attendeeMapper.toDto(attendee);
        restAttendeeMockMvc
                .perform(post("/api/attendees").contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(attendeeDTO)))
                .andExpect(status().isCreated());

        // Validate the Attendee in the database
        List<Attendee> attendeeList = attendeeRepository.findAll();
        assertThat(attendeeList).hasSize(databaseSizeBeforeCreate + 1);
        Attendee testAttendee = attendeeList.get(attendeeList.size() - 1);
        assertThat(testAttendee.getStatus()).isEqualTo(DEFAULT_STATUS);

        // Validate the Attendee in Elasticsearch
        Attendee attendeeEs = attendeeSearchRepository.findOne(testAttendee.getId());
        assertThat(attendeeEs).isEqualToComparingFieldByField(testAttendee);
    }

    /**
     * Creates the attendee with existing id.
     *
     * @throws Exception the exception
     */
    @Test
    @Transactional
    public void createAttendeeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = attendeeRepository.findAll().size();

        // Create the Attendee with an existing ID
        attendee.setId(1L);
        AttendeeDTO attendeeDTO = attendeeMapper.toDto(attendee);

        // An entity with an existing ID cannot be created, so this API call
        // must fail
        restAttendeeMockMvc
                .perform(post("/api/attendees").contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(attendeeDTO)))
                .andExpect(status().isBadRequest());

        // Validate the Attendee in the database
        List<Attendee> attendeeList = attendeeRepository.findAll();
        assertThat(attendeeList).hasSize(databaseSizeBeforeCreate);
    }

    /**
     * Gets the all attendees.
     *
     * @return the all attendees
     * @throws Exception the exception
     */
    @Test
    @Transactional
    public void getAllAttendees() throws Exception {
        // Initialize the database
        attendeeRepository.saveAndFlush(attendee);

        // Get all the attendeeList
        restAttendeeMockMvc.perform(get("/api/attendees?sort=id,desc")).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(attendee.getId().intValue())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }

    /**
     * Gets the attendee.
     *
     * @return the attendee
     * @throws Exception the exception
     */
    @Test
    @Transactional
    public void getAttendee() throws Exception {
        // Initialize the database
        attendeeRepository.saveAndFlush(attendee);

        // Get the attendee
        restAttendeeMockMvc.perform(get("/api/attendees/{id}", attendee.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id").value(attendee.getId().intValue()))
                .andExpect(jsonPath("$.status").value(DEFAULT_STATUS));
    }

    /**
     * Gets the non existing attendee.
     *
     * @return the non existing attendee
     * @throws Exception the exception
     */
    @Test
    @Transactional
    public void getNonExistingAttendee() throws Exception {
        // Get the attendee
        restAttendeeMockMvc.perform(get("/api/attendees/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    /**
     * Update attendee.
     *
     * @throws Exception the exception
     */
    @Test
    @Transactional
    public void updateAttendee() throws Exception {
        // Initialize the database
        attendeeRepository.saveAndFlush(attendee);
        attendeeSearchRepository.save(attendee);
        int databaseSizeBeforeUpdate = attendeeRepository.findAll().size();

        // Update the attendee
        Attendee updatedAttendee = attendeeRepository.findOne(attendee.getId());
        updatedAttendee.status(UPDATED_STATUS);
        AttendeeDTO attendeeDTO = attendeeMapper.toDto(updatedAttendee);

        restAttendeeMockMvc
                .perform(put("/api/attendees").contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(attendeeDTO)))
                .andExpect(status().isOk());

        // Validate the Attendee in the database
        List<Attendee> attendeeList = attendeeRepository.findAll();
        assertThat(attendeeList).hasSize(databaseSizeBeforeUpdate);
        Attendee testAttendee = attendeeList.get(attendeeList.size() - 1);
        assertThat(testAttendee.getStatus()).isEqualTo(UPDATED_STATUS);

        // Validate the Attendee in Elasticsearch
        Attendee attendeeEs = attendeeSearchRepository.findOne(testAttendee.getId());
        assertThat(attendeeEs).isEqualToComparingFieldByField(testAttendee);
    }

    /**
     * Update non existing attendee.
     *
     * @throws Exception the exception
     */
    @Test
    @Transactional
    public void updateNonExistingAttendee() throws Exception {
        int databaseSizeBeforeUpdate = attendeeRepository.findAll().size();

        // Create the Attendee
        AttendeeDTO attendeeDTO = attendeeMapper.toDto(attendee);

        // If the entity doesn't have an ID, it will be created instead of just
        // being updated
        restAttendeeMockMvc
                .perform(put("/api/attendees").contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(attendeeDTO)))
                .andExpect(status().isCreated());

        // Validate the Attendee in the database
        List<Attendee> attendeeList = attendeeRepository.findAll();
        assertThat(attendeeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    /**
     * Delete attendee.
     *
     * @throws Exception the exception
     */
    @Test
    @Transactional
    public void deleteAttendee() throws Exception {
        // Initialize the database
        attendeeRepository.saveAndFlush(attendee);
        attendeeSearchRepository.save(attendee);
        int databaseSizeBeforeDelete = attendeeRepository.findAll().size();

        // Get the attendee
        restAttendeeMockMvc.perform(delete("/api/attendees/{id}", attendee.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8)).andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean attendeeExistsInEs = attendeeSearchRepository.exists(attendee.getId());
        assertThat(attendeeExistsInEs).isFalse();

        // Validate the database is empty
        List<Attendee> attendeeList = attendeeRepository.findAll();
        assertThat(attendeeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    /**
     * Search attendee.
     *
     * @throws Exception the exception
     */
    @Test
    @Transactional
    public void searchAttendee() throws Exception {
        // Initialize the database
        attendeeRepository.saveAndFlush(attendee);
        attendeeSearchRepository.save(attendee);

        // Search the attendee
        restAttendeeMockMvc.perform(get("/api/_search/attendees?query=id:" + attendee.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(attendee.getId().intValue())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }

    /**
     * Equals verifier.
     *
     * @throws Exception the exception
     */
    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Attendee.class);
        Attendee attendee1 = new Attendee();
        attendee1.setId(1L);
        Attendee attendee2 = new Attendee();
        attendee2.setId(attendee1.getId());
        assertThat(attendee1).isEqualTo(attendee2);
        attendee2.setId(2L);
        assertThat(attendee1).isNotEqualTo(attendee2);
        attendee1.setId(null);
        assertThat(attendee1).isNotEqualTo(attendee2);
    }

    /**
     * Dto equals verifier.
     *
     * @throws Exception the exception
     */
    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AttendeeDTO.class);
        AttendeeDTO attendeeDTO1 = new AttendeeDTO();
        attendeeDTO1.setId(1L);
        AttendeeDTO attendeeDTO2 = new AttendeeDTO();
        assertThat(attendeeDTO1).isNotEqualTo(attendeeDTO2);
        attendeeDTO2.setId(attendeeDTO1.getId());
        assertThat(attendeeDTO1).isEqualTo(attendeeDTO2);
        attendeeDTO2.setId(2L);
        assertThat(attendeeDTO1).isNotEqualTo(attendeeDTO2);
        attendeeDTO1.setId(null);
        assertThat(attendeeDTO1).isNotEqualTo(attendeeDTO2);
    }

    /**
     * Test entity from id.
     */
    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(attendeeMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(attendeeMapper.fromId(null)).isNull();
    }
}
