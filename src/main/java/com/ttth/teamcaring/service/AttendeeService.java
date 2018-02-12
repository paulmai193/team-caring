/*
 * 
 */
package com.ttth.teamcaring.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ttth.teamcaring.domain.Appointment;
import com.ttth.teamcaring.domain.Attendee;
import com.ttth.teamcaring.domain.CustomUser;
import com.ttth.teamcaring.service.dto.AttendeeDTO;
import com.ttth.teamcaring.web.rest.errors.BadRequestAlertException;

/**
 * Service Interface for managing Attendee.
 *
 * @author Dai Mai
 */
public interface AttendeeService {

    /**
     * Save a attendee.
     *
     * @param attendeeDTO
     *        the entity to save
     * @return the persisted entity
     */
    AttendeeDTO save(AttendeeDTO attendeeDTO);

    /**
     * Get all the attendees.
     *
     * @param pageable
     *        the pagination information
     * @return the list of entities
     */
    Page<AttendeeDTO> findAll(Pageable pageable);

    /**
     * Get the "id" attendee.
     *
     * @param id
     *        the id of the entity
     * @return the entity
     */
    AttendeeDTO findOne(Long id);

    /**
     * Delete the "id" attendee.
     *
     * @param id
     *        the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the attendee corresponding to the query.
     *
     * @param query
     *        the query of the search
     * 
     * @param pageable
     *        the pagination information
     * @return the list of entities
     */
    Page<AttendeeDTO> search(String query, Pageable pageable);

    /**
     * Creates the.
     *
     * @param appointment
     *        the new attendee
     * @param attendeeCustomUser
     *        the attendee custom user
     * @return the attendee DTO
     */
    AttendeeDTO create(Appointment appointment, CustomUser attendeeCustomUser);

    /**
     * Find one entity.
     *
     * @param attendeeId the attendee id
     * @return the attendee
     */
    Attendee findOneEntity(Long attendeeId);

    /**
     * Gets the one entity.
     *
     * @param attendeeId the attendee id
     * @return the one entity
     * @throws BadRequestAlertException the bad request alert exception when cannot found entity
     */
    Attendee getOneEntity(Long attendeeId) throws BadRequestAlertException;

    /**
     * Map to dto.
     *
     * @param attendee the attendee
     * @return the attendee DTO
     */
    AttendeeDTO mapToDto(Attendee attendee);

    /**
     * Removes the all pending request.
     *
     * @param customUserId the custom user id
     * @param appointmentId the appointment id
     */
    void removeAllPendingRequest(Long customUserId, Long appointmentId);

    /**
     * Accept.
     *
     * @param requestAttendee the request attendee
     * @return the attendee
     */
    Attendee accept(Attendee requestAttendee);

    /**
     * Reject.
     *
     * @param requestAttendee the request attendee
     * @return the attendee
     */
    Attendee reject(Attendee requestAttendee);
    
}
