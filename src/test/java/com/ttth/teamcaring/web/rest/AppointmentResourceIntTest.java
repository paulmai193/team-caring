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
import com.ttth.teamcaring.domain.Appointment;
import com.ttth.teamcaring.repository.AppointmentRepository;
import com.ttth.teamcaring.repository.search.AppointmentSearchRepository;
import com.ttth.teamcaring.service.AppointmentService;
import com.ttth.teamcaring.service.dto.AppointmentDTO;
import com.ttth.teamcaring.service.mapper.AppointmentMapper;
import com.ttth.teamcaring.web.rest.errors.ExceptionTranslator;

/**
 * Test class for the AppointmentResource REST controller.
 *
 * @see AppointmentResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TeamCaringApp.class)
@Ignore
public class AppointmentResourceIntTest {

    /** The Constant DEFAULT_NAME. */
    private static final String                   DEFAULT_NAME        = "AAAAAAAAAA";
    
    /** The Constant UPDATED_NAME. */
    private static final String                   UPDATED_NAME        = "BBBBBBBBBB";

    /** The Constant DEFAULT_DESCRIPTION. */
    private static final String                   DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    
    /** The Constant UPDATED_DESCRIPTION. */
    private static final String                   UPDATED_DESCRIPTION = "BBBBBBBBBB";

    /** The Constant DEFAULT_REPEAT_TYPE. */
    private static final Integer                  DEFAULT_REPEAT_TYPE = 1;
    
    /** The Constant UPDATED_REPEAT_TYPE. */
    private static final Integer                  UPDATED_REPEAT_TYPE = 2;

    /** The appointment repository. */
    @Autowired
    private AppointmentRepository                 appointmentRepository;

    /** The appointment mapper. */
    @Autowired
    private AppointmentMapper                     appointmentMapper;

    /** The appointment service. */
    @Autowired
    private AppointmentService                    appointmentService;

    /** The appointment search repository. */
    @Autowired
    private AppointmentSearchRepository           appointmentSearchRepository;

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

    /** The rest appointment mock mvc. */
    private MockMvc                               restAppointmentMockMvc;

    /** The appointment. */
    private Appointment                           appointment;

    /**
     * Setup.
     */
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AppointmentResource appointmentResource = new AppointmentResource(appointmentService);
        this.restAppointmentMockMvc = MockMvcBuilders.standaloneSetup(appointmentResource)
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
     * @return the appointment
     */
    public static Appointment createEntity(EntityManager em) {
        Appointment appointment = new Appointment().name(DEFAULT_NAME)
                .description(DEFAULT_DESCRIPTION).repeatType(DEFAULT_REPEAT_TYPE);
        return appointment;
    }

    /**
     * Inits the test.
     */
    @Before
    public void initTest() {
        appointmentSearchRepository.deleteAll();
        appointment = createEntity(em);
    }

    /**
     * Creates the appointment.
     *
     * @throws Exception the exception
     */
    @Test
    @Transactional
    public void createAppointment() throws Exception {
        int databaseSizeBeforeCreate = appointmentRepository.findAll().size();

        // Create the Appointment
        AppointmentDTO appointmentDTO = appointmentMapper.toDto(appointment);
        restAppointmentMockMvc
                .perform(post("/api/appointments").contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(appointmentDTO)))
                .andExpect(status().isCreated());

        // Validate the Appointment in the database
        List<Appointment> appointmentList = appointmentRepository.findAll();
        assertThat(appointmentList).hasSize(databaseSizeBeforeCreate + 1);
        Appointment testAppointment = appointmentList.get(appointmentList.size() - 1);
        assertThat(testAppointment.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAppointment.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAppointment.getRepeatType()).isEqualTo(DEFAULT_REPEAT_TYPE);

        // Validate the Appointment in Elasticsearch
        Appointment appointmentEs = appointmentSearchRepository.findOne(testAppointment.getId());
        assertThat(appointmentEs).isEqualToComparingFieldByField(testAppointment);
    }

    /**
     * Creates the appointment with existing id.
     *
     * @throws Exception the exception
     */
    @Test
    @Transactional
    public void createAppointmentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = appointmentRepository.findAll().size();

        // Create the Appointment with an existing ID
        appointment.setId(1L);
        AppointmentDTO appointmentDTO = appointmentMapper.toDto(appointment);

        // An entity with an existing ID cannot be created, so this API call
        // must fail
        restAppointmentMockMvc
                .perform(post("/api/appointments").contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(appointmentDTO)))
                .andExpect(status().isBadRequest());

        // Validate the Appointment in the database
        List<Appointment> appointmentList = appointmentRepository.findAll();
        assertThat(appointmentList).hasSize(databaseSizeBeforeCreate);
    }

    /**
     * Gets the all appointments.
     *
     * @return the all appointments
     * @throws Exception the exception
     */
    @Test
    @Transactional
    public void getAllAppointments() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        // Get all the appointmentList
        restAppointmentMockMvc.perform(get("/api/appointments?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(appointment.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description")
                        .value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].repeatType").value(hasItem(DEFAULT_REPEAT_TYPE)));
    }

    /**
     * Gets the appointment.
     *
     * @return the appointment
     * @throws Exception the exception
     */
    @Test
    @Transactional
    public void getAppointment() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        // Get the appointment
        restAppointmentMockMvc.perform(get("/api/appointments/{id}", appointment.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id").value(appointment.getId().intValue()))
                .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
                .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
                .andExpect(jsonPath("$.repeatType").value(DEFAULT_REPEAT_TYPE));
    }

    /**
     * Gets the non existing appointment.
     *
     * @return the non existing appointment
     * @throws Exception the exception
     */
    @Test
    @Transactional
    public void getNonExistingAppointment() throws Exception {
        // Get the appointment
        restAppointmentMockMvc.perform(get("/api/appointments/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    /**
     * Update appointment.
     *
     * @throws Exception the exception
     */
    @Test
    @Transactional
    public void updateAppointment() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);
        appointmentSearchRepository.save(appointment);
        int databaseSizeBeforeUpdate = appointmentRepository.findAll().size();

        // Update the appointment
        Appointment updatedAppointment = appointmentRepository.findOne(appointment.getId());
        updatedAppointment.name(UPDATED_NAME).description(UPDATED_DESCRIPTION)
                .repeatType(UPDATED_REPEAT_TYPE);
        AppointmentDTO appointmentDTO = appointmentMapper.toDto(updatedAppointment);

        restAppointmentMockMvc
                .perform(put("/api/appointments").contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(appointmentDTO)))
                .andExpect(status().isOk());

        // Validate the Appointment in the database
        List<Appointment> appointmentList = appointmentRepository.findAll();
        assertThat(appointmentList).hasSize(databaseSizeBeforeUpdate);
        Appointment testAppointment = appointmentList.get(appointmentList.size() - 1);
        assertThat(testAppointment.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAppointment.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAppointment.getRepeatType()).isEqualTo(UPDATED_REPEAT_TYPE);

        // Validate the Appointment in Elasticsearch
        Appointment appointmentEs = appointmentSearchRepository.findOne(testAppointment.getId());
        assertThat(appointmentEs).isEqualToComparingFieldByField(testAppointment);
    }

    /**
     * Update non existing appointment.
     *
     * @throws Exception the exception
     */
    @Test
    @Transactional
    public void updateNonExistingAppointment() throws Exception {
        int databaseSizeBeforeUpdate = appointmentRepository.findAll().size();

        // Create the Appointment
        AppointmentDTO appointmentDTO = appointmentMapper.toDto(appointment);

        // If the entity doesn't have an ID, it will be created instead of just
        // being updated
        restAppointmentMockMvc
                .perform(put("/api/appointments").contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(appointmentDTO)))
                .andExpect(status().isCreated());

        // Validate the Appointment in the database
        List<Appointment> appointmentList = appointmentRepository.findAll();
        assertThat(appointmentList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    /**
     * Delete appointment.
     *
     * @throws Exception the exception
     */
    @Test
    @Transactional
    public void deleteAppointment() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);
        appointmentSearchRepository.save(appointment);
        int databaseSizeBeforeDelete = appointmentRepository.findAll().size();

        // Get the appointment
        restAppointmentMockMvc.perform(delete("/api/appointments/{id}", appointment.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8)).andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean appointmentExistsInEs = appointmentSearchRepository.exists(appointment.getId());
        assertThat(appointmentExistsInEs).isFalse();

        // Validate the database is empty
        List<Appointment> appointmentList = appointmentRepository.findAll();
        assertThat(appointmentList).hasSize(databaseSizeBeforeDelete - 1);
    }

    /**
     * Search appointment.
     *
     * @throws Exception the exception
     */
    @Test
    @Transactional
    public void searchAppointment() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);
        appointmentSearchRepository.save(appointment);

        // Search the appointment
        restAppointmentMockMvc
                .perform(get("/api/_search/appointments?query=id:" + appointment.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(appointment.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description")
                        .value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].repeatType").value(hasItem(DEFAULT_REPEAT_TYPE)));
    }

    /**
     * Equals verifier.
     *
     * @throws Exception the exception
     */
    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Appointment.class);
        Appointment appointment1 = new Appointment();
        appointment1.setId(1L);
        Appointment appointment2 = new Appointment();
        appointment2.setId(appointment1.getId());
        assertThat(appointment1).isEqualTo(appointment2);
        appointment2.setId(2L);
        assertThat(appointment1).isNotEqualTo(appointment2);
        appointment1.setId(null);
        assertThat(appointment1).isNotEqualTo(appointment2);
    }

    /**
     * Dto equals verifier.
     *
     * @throws Exception the exception
     */
    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AppointmentDTO.class);
        AppointmentDTO appointmentDTO1 = new AppointmentDTO();
        appointmentDTO1.setId(1L);
        AppointmentDTO appointmentDTO2 = new AppointmentDTO();
        assertThat(appointmentDTO1).isNotEqualTo(appointmentDTO2);
        appointmentDTO2.setId(appointmentDTO1.getId());
        assertThat(appointmentDTO1).isEqualTo(appointmentDTO2);
        appointmentDTO2.setId(2L);
        assertThat(appointmentDTO1).isNotEqualTo(appointmentDTO2);
        appointmentDTO1.setId(null);
        assertThat(appointmentDTO1).isNotEqualTo(appointmentDTO2);
    }

    /**
     * Test entity from id.
     */
    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(appointmentMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(appointmentMapper.fromId(null)).isNull();
    }
}
