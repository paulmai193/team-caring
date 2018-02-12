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
import com.ttth.teamcaring.domain.Notification;
import com.ttth.teamcaring.repository.NotificationRepository;
import com.ttth.teamcaring.repository.search.NotificationSearchRepository;
import com.ttth.teamcaring.service.NotificationService;
import com.ttth.teamcaring.service.dto.NotificationDTO;
import com.ttth.teamcaring.service.mapper.NotificationMapper;
import com.ttth.teamcaring.web.rest.errors.ExceptionTranslator;

/**
 * Test class for the NotificationResource REST controller.
 *
 * @see NotificationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TeamCaringApp.class)
@Ignore
public class NotificationResourceIntTest {

    /** The Constant DEFAULT_TITLE. */
    private static final String                   DEFAULT_TITLE     = "AAAAAAAAAA";

    /** The Constant UPDATED_TITLE. */
    private static final String                   UPDATED_TITLE     = "BBBBBBBBBB";

    /** The Constant DEFAULT_MESSAGE. */
    private static final String                   DEFAULT_MESSAGE   = "AAAAAAAAAA";

    /** The Constant UPDATED_MESSAGE. */
    private static final String                   UPDATED_MESSAGE   = "BBBBBBBBBB";

    /** The Constant DEFAULT_READ. */
    private static final Boolean                  DEFAULT_READ      = false;

    /** The Constant UPDATED_READ. */
    private static final Boolean                  UPDATED_READ      = true;

    /** The Constant DEFAULT_TYPE. */
    private static final Integer                  DEFAULT_TYPE      = 1;

    /** The Constant UPDATED_TYPE. */
    private static final Integer                  UPDATED_TYPE      = 2;

    /** The Constant DEFAULT_TARGET_ID. */
    private static final Long                     DEFAULT_TARGET_ID = 1L;

    /** The Constant UPDATED_TARGET_ID. */
    private static final Long                     UPDATED_TARGET_ID = 2L;

    /** The notification repository. */
    @Autowired
    private NotificationRepository                notificationRepository;

    /** The notification mapper. */
    @Autowired
    private NotificationMapper                    notificationMapper;

    /** The notification service. */
    @Autowired
    private NotificationService                   notificationService;

    /** The notification search repository. */
    @Autowired
    private NotificationSearchRepository          notificationSearchRepository;

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

    /** The rest notification mock mvc. */
    private MockMvc                               restNotificationMockMvc;

    /** The notification. */
    private Notification                          notification;

    /**
     * Setup.
     */
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final NotificationResource notificationResource = new NotificationResource(
                notificationService);
        this.restNotificationMockMvc = MockMvcBuilders.standaloneSetup(notificationResource)
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
     * @return the notification
     */
    public static Notification createEntity(EntityManager em) {
        Notification notification = new Notification().title(DEFAULT_TITLE).message(DEFAULT_MESSAGE)
                .read(DEFAULT_READ).type(DEFAULT_TYPE).targetId(DEFAULT_TARGET_ID);
        return notification;
    }

    /**
     * Inits the test.
     */
    @Before
    public void initTest() {
        notificationSearchRepository.deleteAll();
        notification = createEntity(em);
    }

    /**
     * Creates the notification.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    @Transactional
    public void createNotification() throws Exception {
        int databaseSizeBeforeCreate = notificationRepository.findAll().size();

        // Create the Notification
        NotificationDTO notificationDTO = notificationMapper.toDto(notification);
        restNotificationMockMvc
                .perform(post("/api/notifications").contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(notificationDTO)))
                .andExpect(status().isCreated());

        // Validate the Notification in the database
        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeCreate + 1);
        Notification testNotification = notificationList.get(notificationList.size() - 1);
        assertThat(testNotification.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testNotification.getMessage()).isEqualTo(DEFAULT_MESSAGE);
        assertThat(testNotification.isRead()).isEqualTo(DEFAULT_READ);
        assertThat(testNotification.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testNotification.getTargetId()).isEqualTo(DEFAULT_TARGET_ID);

        // Validate the Notification in Elasticsearch
        Notification notificationEs = notificationSearchRepository
                .findOne(testNotification.getId());
        assertThat(notificationEs).isEqualToComparingFieldByField(testNotification);
    }

    /**
     * Creates the notification with existing id.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    @Transactional
    public void createNotificationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = notificationRepository.findAll().size();

        // Create the Notification with an existing ID
        notification.setId(1L);
        NotificationDTO notificationDTO = notificationMapper.toDto(notification);

        // An entity with an existing ID cannot be created, so this API call
        // must fail
        restNotificationMockMvc
                .perform(post("/api/notifications").contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(notificationDTO)))
                .andExpect(status().isBadRequest());

        // Validate the Notification in the database
        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeCreate);
    }

    /**
     * Gets the all notifications.
     *
     * @return the all notifications
     * @throws Exception
     *         the exception
     */
    @Test
    @Transactional
    public void getAllNotifications() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList
        restNotificationMockMvc.perform(get("/api/notifications?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(notification.getId().intValue())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
                .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE.toString())))
                .andExpect(jsonPath("$.[*].read").value(hasItem(DEFAULT_READ.booleanValue())))
                .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
                .andExpect(jsonPath("$.[*].targetId").value(hasItem(DEFAULT_TARGET_ID.intValue())));
    }

    /**
     * Gets the notification.
     *
     * @return the notification
     * @throws Exception
     *         the exception
     */
    @Test
    @Transactional
    public void getNotification() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get the notification
        restNotificationMockMvc.perform(get("/api/notifications/{id}", notification.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id").value(notification.getId().intValue()))
                .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
                .andExpect(jsonPath("$.message").value(DEFAULT_MESSAGE.toString()))
                .andExpect(jsonPath("$.read").value(DEFAULT_READ.booleanValue()))
                .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
                .andExpect(jsonPath("$.targetId").value(DEFAULT_TARGET_ID.intValue()));
    }

    /**
     * Gets the non existing notification.
     *
     * @return the non existing notification
     * @throws Exception
     *         the exception
     */
    @Test
    @Transactional
    public void getNonExistingNotification() throws Exception {
        // Get the notification
        restNotificationMockMvc.perform(get("/api/notifications/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    /**
     * Update notification.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    @Transactional
    public void updateNotification() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);
        notificationSearchRepository.save(notification);
        int databaseSizeBeforeUpdate = notificationRepository.findAll().size();

        // Update the notification
        Notification updatedNotification = notificationRepository.findOne(notification.getId());
        updatedNotification.title(UPDATED_TITLE).message(UPDATED_MESSAGE).read(UPDATED_READ)
                .type(UPDATED_TYPE).targetId(UPDATED_TARGET_ID);
        NotificationDTO notificationDTO = notificationMapper.toDto(updatedNotification);

        restNotificationMockMvc
                .perform(put("/api/notifications").contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(notificationDTO)))
                .andExpect(status().isOk());

        // Validate the Notification in the database
        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeUpdate);
        Notification testNotification = notificationList.get(notificationList.size() - 1);
        assertThat(testNotification.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testNotification.getMessage()).isEqualTo(UPDATED_MESSAGE);
        assertThat(testNotification.isRead()).isEqualTo(UPDATED_READ);
        assertThat(testNotification.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testNotification.getTargetId()).isEqualTo(UPDATED_TARGET_ID);

        // Validate the Notification in Elasticsearch
        Notification notificationEs = notificationSearchRepository
                .findOne(testNotification.getId());
        assertThat(notificationEs).isEqualToComparingFieldByField(testNotification);
    }

    /**
     * Update non existing notification.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    @Transactional
    public void updateNonExistingNotification() throws Exception {
        int databaseSizeBeforeUpdate = notificationRepository.findAll().size();

        // Create the Notification
        NotificationDTO notificationDTO = notificationMapper.toDto(notification);

        // If the entity doesn't have an ID, it will be created instead of just
        // being updated
        restNotificationMockMvc
                .perform(put("/api/notifications").contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(notificationDTO)))
                .andExpect(status().isCreated());

        // Validate the Notification in the database
        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    /**
     * Delete notification.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    @Transactional
    public void deleteNotification() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);
        notificationSearchRepository.save(notification);
        int databaseSizeBeforeDelete = notificationRepository.findAll().size();

        // Get the notification
        restNotificationMockMvc.perform(delete("/api/notifications/{id}", notification.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8)).andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean notificationExistsInEs = notificationSearchRepository.exists(notification.getId());
        assertThat(notificationExistsInEs).isFalse();

        // Validate the database is empty
        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    /**
     * Search notification.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    @Transactional
    public void searchNotification() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);
        notificationSearchRepository.save(notification);

        // Search the notification
        restNotificationMockMvc
                .perform(get("/api/_search/notifications?query=id:" + notification.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(notification.getId().intValue())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
                .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE.toString())))
                .andExpect(jsonPath("$.[*].read").value(hasItem(DEFAULT_READ.booleanValue())))
                .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
                .andExpect(jsonPath("$.[*].targetId").value(hasItem(DEFAULT_TARGET_ID.intValue())));
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
        TestUtil.equalsVerifier(Notification.class);
        Notification notification1 = new Notification();
        notification1.setId(1L);
        Notification notification2 = new Notification();
        notification2.setId(notification1.getId());
        assertThat(notification1).isEqualTo(notification2);
        notification2.setId(2L);
        assertThat(notification1).isNotEqualTo(notification2);
        notification1.setId(null);
        assertThat(notification1).isNotEqualTo(notification2);
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
        TestUtil.equalsVerifier(NotificationDTO.class);
        NotificationDTO notificationDTO1 = new NotificationDTO();
        notificationDTO1.setId(1L);
        NotificationDTO notificationDTO2 = new NotificationDTO();
        assertThat(notificationDTO1).isNotEqualTo(notificationDTO2);
        notificationDTO2.setId(notificationDTO1.getId());
        assertThat(notificationDTO1).isEqualTo(notificationDTO2);
        notificationDTO2.setId(2L);
        assertThat(notificationDTO1).isNotEqualTo(notificationDTO2);
        notificationDTO1.setId(null);
        assertThat(notificationDTO1).isNotEqualTo(notificationDTO2);
    }

    /**
     * Test entity from id.
     */
    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(notificationMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(notificationMapper.fromId(null)).isNull();
    }
}
