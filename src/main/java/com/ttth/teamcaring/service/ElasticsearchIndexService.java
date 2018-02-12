/*
 * 
 */
package com.ttth.teamcaring.service;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.List;

import org.elasticsearch.indices.IndexAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codahale.metrics.annotation.Timed;
import com.ttth.teamcaring.domain.Appointment;
import com.ttth.teamcaring.domain.Attendee;
import com.ttth.teamcaring.domain.CustomUser;
import com.ttth.teamcaring.domain.Groups;
import com.ttth.teamcaring.domain.GroupsMember;
import com.ttth.teamcaring.domain.Icon;
import com.ttth.teamcaring.domain.Note;
import com.ttth.teamcaring.domain.Notification;
import com.ttth.teamcaring.domain.Team;
import com.ttth.teamcaring.domain.User;
import com.ttth.teamcaring.repository.AppointmentRepository;
import com.ttth.teamcaring.repository.AttendeeRepository;
import com.ttth.teamcaring.repository.CustomUserRepository;
import com.ttth.teamcaring.repository.GroupsMemberRepository;
import com.ttth.teamcaring.repository.GroupsRepository;
import com.ttth.teamcaring.repository.IconRepository;
import com.ttth.teamcaring.repository.NoteRepository;
import com.ttth.teamcaring.repository.NotificationRepository;
import com.ttth.teamcaring.repository.TeamRepository;
import com.ttth.teamcaring.repository.UserRepository;
import com.ttth.teamcaring.repository.search.AppointmentSearchRepository;
import com.ttth.teamcaring.repository.search.AttendeeSearchRepository;
import com.ttth.teamcaring.repository.search.CustomUserSearchRepository;
import com.ttth.teamcaring.repository.search.GroupsMemberSearchRepository;
import com.ttth.teamcaring.repository.search.GroupsSearchRepository;
import com.ttth.teamcaring.repository.search.IconSearchRepository;
import com.ttth.teamcaring.repository.search.NoteSearchRepository;
import com.ttth.teamcaring.repository.search.NotificationSearchRepository;
import com.ttth.teamcaring.repository.search.TeamSearchRepository;
import com.ttth.teamcaring.repository.search.UserSearchRepository;

/**
 * The Class ElasticsearchIndexService.
 *
 * @author Dai Mai
 */
@Service
public class ElasticsearchIndexService {

    /** The log. */
    private final Logger log = LoggerFactory.getLogger(ElasticsearchIndexService.class);

    /** The appointment repository. */
    private final AppointmentRepository appointmentRepository;

    /** The appointment search repository. */
    private final AppointmentSearchRepository appointmentSearchRepository;

    /** The attendee repository. */
    private final AttendeeRepository attendeeRepository;

    /** The attendee search repository. */
    private final AttendeeSearchRepository attendeeSearchRepository;

    /** The custom user repository. */
    private final CustomUserRepository customUserRepository;

    /** The custom user search repository. */
    private final CustomUserSearchRepository customUserSearchRepository;

    /** The groups repository. */
    private final GroupsRepository groupsRepository;

    /** The groups search repository. */
    private final GroupsSearchRepository groupsSearchRepository;

    /** The groups member repository. */
    private final GroupsMemberRepository groupsMemberRepository;

    /** The groups member search repository. */
    private final GroupsMemberSearchRepository groupsMemberSearchRepository;

    /** The icon repository. */
    private final IconRepository iconRepository;

    /** The icon search repository. */
    private final IconSearchRepository iconSearchRepository;

    /** The note repository. */
    private final NoteRepository noteRepository;

    /** The note search repository. */
    private final NoteSearchRepository noteSearchRepository;

    /** The notification repository. */
    private final NotificationRepository notificationRepository;

    /** The notification search repository. */
    private final NotificationSearchRepository notificationSearchRepository;

    /** The team repository. */
    private final TeamRepository teamRepository;

    /** The team search repository. */
    private final TeamSearchRepository teamSearchRepository;

    /** The user repository. */
    private final UserRepository userRepository;

    /** The user search repository. */
    private final UserSearchRepository userSearchRepository;

    /** The elasticsearch template. */
    private final ElasticsearchTemplate elasticsearchTemplate;

    /**
     * Instantiates a new elasticsearch index service.
     *
     * @param userRepository the user repository
     * @param userSearchRepository the user search repository
     * @param appointmentRepository the appointment repository
     * @param appointmentSearchRepository the appointment search repository
     * @param attendeeRepository the attendee repository
     * @param attendeeSearchRepository the attendee search repository
     * @param customUserRepository the custom user repository
     * @param customUserSearchRepository the custom user search repository
     * @param groupsRepository the groups repository
     * @param groupsSearchRepository the groups search repository
     * @param groupsMemberRepository the groups member repository
     * @param groupsMemberSearchRepository the groups member search repository
     * @param iconRepository the icon repository
     * @param iconSearchRepository the icon search repository
     * @param noteRepository the note repository
     * @param noteSearchRepository the note search repository
     * @param notificationRepository the notification repository
     * @param notificationSearchRepository the notification search repository
     * @param teamRepository the team repository
     * @param teamSearchRepository the team search repository
     * @param elasticsearchTemplate the elasticsearch template
     */
    public ElasticsearchIndexService(
        UserRepository userRepository,
        UserSearchRepository userSearchRepository,
        AppointmentRepository appointmentRepository,
        AppointmentSearchRepository appointmentSearchRepository,
        AttendeeRepository attendeeRepository,
        AttendeeSearchRepository attendeeSearchRepository,
        CustomUserRepository customUserRepository,
        CustomUserSearchRepository customUserSearchRepository,
        GroupsRepository groupsRepository,
        GroupsSearchRepository groupsSearchRepository,
        GroupsMemberRepository groupsMemberRepository,
        GroupsMemberSearchRepository groupsMemberSearchRepository,
        IconRepository iconRepository,
        IconSearchRepository iconSearchRepository,
        NoteRepository noteRepository,
        NoteSearchRepository noteSearchRepository,
        NotificationRepository notificationRepository,
        NotificationSearchRepository notificationSearchRepository,
        TeamRepository teamRepository,
        TeamSearchRepository teamSearchRepository,
        ElasticsearchTemplate elasticsearchTemplate) {
        this.userRepository = userRepository;
        this.userSearchRepository = userSearchRepository;
        this.appointmentRepository = appointmentRepository;
        this.appointmentSearchRepository = appointmentSearchRepository;
        this.attendeeRepository = attendeeRepository;
        this.attendeeSearchRepository = attendeeSearchRepository;
        this.customUserRepository = customUserRepository;
        this.customUserSearchRepository = customUserSearchRepository;
        this.groupsRepository = groupsRepository;
        this.groupsSearchRepository = groupsSearchRepository;
        this.groupsMemberRepository = groupsMemberRepository;
        this.groupsMemberSearchRepository = groupsMemberSearchRepository;
        this.iconRepository = iconRepository;
        this.iconSearchRepository = iconSearchRepository;
        this.noteRepository = noteRepository;
        this.noteSearchRepository = noteSearchRepository;
        this.notificationRepository = notificationRepository;
        this.notificationSearchRepository = notificationSearchRepository;
        this.teamRepository = teamRepository;
        this.teamSearchRepository = teamSearchRepository;
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    /**
     * Reindex all.
     * 
     * This is scheduled to get fired everyday, at 02:00 (am).
     */
    @Async
    @Timed
    @Scheduled(cron = "0 0 2 * * ?")
    public void reindexAll() {
        reindexForClass(Appointment.class, appointmentRepository, appointmentSearchRepository);
        reindexForClass(Attendee.class, attendeeRepository, attendeeSearchRepository);
        reindexForClass(CustomUser.class, customUserRepository, customUserSearchRepository);
        reindexForClass(Groups.class, groupsRepository, groupsSearchRepository);
        reindexForClass(GroupsMember.class, groupsMemberRepository, groupsMemberSearchRepository);
        reindexForClass(Icon.class, iconRepository, iconSearchRepository);
        reindexForClass(Note.class, noteRepository, noteSearchRepository);
        reindexForClass(Notification.class, notificationRepository, notificationSearchRepository);
        reindexForClass(Team.class, teamRepository, teamSearchRepository);
        reindexForClass(User.class, userRepository, userSearchRepository);

        log.info("Elasticsearch: Successfully performed reindexing");
    }

    /**
     * Reindex for class.
     *
     * @param <T> the generic type
     * @param <ID> the generic type
     * @param entityClass the entity class
     * @param jpaRepository the jpa repository
     * @param elasticsearchRepository the elasticsearch repository
     */
    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    private <T, ID extends Serializable> void reindexForClass(Class<T> entityClass, JpaRepository<T, ID> jpaRepository,
                                                              ElasticsearchRepository<T, ID> elasticsearchRepository) {
        elasticsearchTemplate.deleteIndex(entityClass);
        try {
            elasticsearchTemplate.createIndex(entityClass);
        } catch (IndexAlreadyExistsException e) {
            // Do nothing. Index was already concurrently recreated by some other service.
        }
        elasticsearchTemplate.putMapping(entityClass);
        if (jpaRepository.count() > 0) {
            try {
                Method m = jpaRepository.getClass().getMethod("findAllWithEagerRelationships");
                elasticsearchRepository.save((List<T>) m.invoke(jpaRepository));
            } catch (Exception e) {
                elasticsearchRepository.save(jpaRepository.findAll());
            }
        }
        log.info("Elasticsearch: Indexed all rows for " + entityClass.getSimpleName());
    }
}
