/*
 * 
 */
package com.ttth.teamcaring.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ttth.teamcaring.domain.Appointment;
import com.ttth.teamcaring.domain.Attendee;
import com.ttth.teamcaring.domain.CustomUser;
import com.ttth.teamcaring.repository.AttendeeRepository;
import com.ttth.teamcaring.repository.search.AttendeeSearchRepository;
import com.ttth.teamcaring.service.AttendeeService;
import com.ttth.teamcaring.service.NotificationService;
import com.ttth.teamcaring.service.dto.AttendeeDTO;
import com.ttth.teamcaring.service.mapper.AttendeeMapper;
import com.ttth.teamcaring.web.rest.AttendeeResource;
import com.ttth.teamcaring.web.rest.errors.BadRequestAlertException;

/**
 * Service Implementation for managing Attendee.
 *
 * @author Dai Mai
 */
@Service
@Transactional
public class AttendeeServiceImpl implements AttendeeService {

    /** The log. */
    private final Logger                   log = LoggerFactory.getLogger(AttendeeServiceImpl.class);

    /** The attendee repository. */
    private final AttendeeRepository       attendeeRepository;

    /** The attendee mapper. */
    private final AttendeeMapper           attendeeMapper;

    /** The attendee search repository. */
    private final AttendeeSearchRepository attendeeSearchRepository;

    /** The notification service. */
    @Inject
    private NotificationService            notificationService;

    /**
     * Instantiates a new attendee service impl.
     *
     * @param attendeeRepository the attendee repository
     * @param attendeeMapper the attendee mapper
     * @param attendeeSearchRepository the attendee search repository
     */
    public AttendeeServiceImpl(AttendeeRepository attendeeRepository, AttendeeMapper attendeeMapper,
            AttendeeSearchRepository attendeeSearchRepository) {
        this.attendeeRepository = attendeeRepository;
        this.attendeeMapper = attendeeMapper;
        this.attendeeSearchRepository = attendeeSearchRepository;
    }

    /**
     * Save a attendee.
     *
     * @param attendeeDTO
     *        the entity to save
     * @return the persisted entity
     */
    @Override
    public AttendeeDTO save(AttendeeDTO attendeeDTO) {
        log.debug("Request to save Attendee : {}", attendeeDTO);
        Attendee attendee = this.saveEntity(attendeeDTO);
        AttendeeDTO result = attendeeMapper.toDto(attendee);
        attendeeSearchRepository.save(attendee);
        return result;
    }

    /**
     * Save entity.
     *
     * @param attendee the attendee
     * @return the attendee
     */
    private Attendee saveEntity(Attendee attendee) {
        return attendeeRepository.save(attendee);
    }

    /**
     * Save entity.
     *
     * @param attendeeDTO the attendee DTO
     * @return the attendee
     */
    private Attendee saveEntity(AttendeeDTO attendeeDTO) {
        Attendee attendee = attendeeMapper.toEntity(attendeeDTO);
        return this.saveEntity(attendee);
    }

    /* (non-Javadoc)
     * @see com.ttth.teamcaring.service.AttendeeService#create(com.ttth.teamcaring.domain.Appointment, com.ttth.teamcaring.domain.CustomUser)
     */
    @Override
    public AttendeeDTO create(Appointment appointment, CustomUser attendeeCustomUser) {
        // Create attendee
        Attendee attendee;
        Optional<Attendee> optAttendee = this.attendeeRepository
                .findOneByAppointmentAndCustomUser(appointment, attendeeCustomUser);
        if (optAttendee.isPresent()) {
            attendee = optAttendee.get();
        }
        else {
            attendee = this.saveEntity(new Attendee().appointment(appointment)
                    .customUser(attendeeCustomUser).status(Attendee.STATUS_PENDING));

            // send notification
            try {
                this.notificationService.sendRequestAppointment(attendee);
            }
            catch (Exception e) {
                this.log.error(MessageFormat.format(
                        "Cannot send notification of request create appointment {0}",
                        appointment.getId()), e);
                // Swallow this exception
            }

        }
        return this.mapToDto(attendee);
    }

    /**
     * Get all the attendees.
     *
     * @param pageable
     *        the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AttendeeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Attendees");
        return attendeeRepository.findAll(pageable).map(attendeeMapper::toDto);
    }

    /**
     * Get one attendee by id.
     *
     * @param id
     *        the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public AttendeeDTO findOne(Long id) {
        log.debug("Request to get Attendee : {}", id);
        Attendee attendee = attendeeRepository.findOne(id);
        return attendeeMapper.toDto(attendee);
    }

    /**
     * Delete the attendee by id.
     *
     * @param id
     *        the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Attendee : {}", id);
        attendeeRepository.delete(id);
        attendeeSearchRepository.delete(id);
    }

    /**
     * Search for the attendee corresponding to the query.
     *
     * @param query
     *        the query of the search
     * @param pageable
     *        the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AttendeeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Attendees for query {}", query);
        Page<Attendee> result = attendeeSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(attendeeMapper::toDto);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ttth.teamcaring.service.AttendeeService#findOneEntity(java.lang.Long)
     */
    @Override
    public Attendee findOneEntity(Long attendeeId) {
        return this.attendeeRepository.findOne(attendeeId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ttth.teamcaring.service.AttendeeService#getOneEntity(java.lang.Long)
     */
    @Override
    public Attendee getOneEntity(Long attendeeId) throws BadRequestAlertException {
        Attendee attendee = this.findOneEntity(attendeeId);
        if (Objects.isNull(attendee)) {
            throw new BadRequestAlertException(
                    MessageFormat.format("Cannot find attendee of ID {0}", attendee),
                    AttendeeResource.ENTITY_NAME, "attendeeNotExist");
        }
        return attendee;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ttth.teamcaring.service.AttendeeService#mapToDto(com.ttth.teamcaring.domain.Attendee)
     */
    @Override
    public AttendeeDTO mapToDto(Attendee attendee) {
        return this.attendeeMapper.toDto(attendee);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ttth.teamcaring.service.AttendeeService#removeAllPendingRequest(java.lang.Long,
     * java.lang.Long)
     */
    @Override
    public void removeAllPendingRequest(Long customUserId, Long appointmentId) {
        this.log.debug("Remove all pending request appointment ID {}", appointmentId);
        List<Attendee> deleted = this.attendeeRepository
                .deleteByAppointmentIdAndStatusNot(appointmentId, Attendee.STATUS_ACCEPTED);
        log.debug("Total records: {}", deleted.size());

        // Delete relate notifications
        this.notificationService.deleteAllRequestAppointmentNotifications(deleted);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ttth.teamcaring.service.AttendeeService#accept(com.ttth.teamcaring.domain.Attendee)
     */
    @Override
    public Attendee accept(Attendee requestAttendee) {
        Appointment appointment = requestAttendee.getAppointment();
        requestAttendee.setStatus(Attendee.STATUS_ACCEPTED);
        Attendee attendee = this.attendeeRepository.save(requestAttendee);

        // send notification
        try {
            this.notificationService.sendResponseAppointment(attendee);
        }
        catch (Exception e) {
            this.log.error(MessageFormat.format(
                    "Cannot send notification of response appointment {0}",
                    appointment.getId()), e);
            // Swallow this exception
        }
        return attendee;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ttth.teamcaring.service.AttendeeService#reject(com.ttth.teamcaring.domain.Attendee)
     */
    @Override
    public Attendee reject(Attendee requestAttendee) {
        Appointment appointment = requestAttendee.getAppointment();
        this.attendeeRepository.delete(requestAttendee);
        requestAttendee.setStatus(Attendee.STATUS_REJECTED);

        // send notification
        try {
            this.notificationService.sendResponseAppointment(requestAttendee);
        }
        catch (Exception e) {
            this.log.error(MessageFormat.format(
                    "Cannot send notification of response appointment {0}",
                    appointment.getId()), e);
            // Swallow this exception
        }
        return requestAttendee;
    }

}
