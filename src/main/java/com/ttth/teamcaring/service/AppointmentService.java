/*
 * 
 */
package com.ttth.teamcaring.service;

import java.time.Instant;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ttth.teamcaring.domain.Appointment;
import com.ttth.teamcaring.domain.CustomUser;
import com.ttth.teamcaring.service.dto.AnwserResponseStatus;
import com.ttth.teamcaring.service.dto.AppointmentDTO;
import com.ttth.teamcaring.service.dto.CreateAppointmentDTO;
import com.ttth.teamcaring.service.dto.DetailAppointmentDTO;
import com.ttth.teamcaring.service.dto.GroupOfAppointmentDTO;
import com.ttth.teamcaring.service.dto.UpdateAppointmentDTO;
import com.ttth.teamcaring.web.rest.errors.BadRequestAlertException;

/**
 * Service Interface for managing Appointment.
 *
 * @author Dai Mai
 */
public interface AppointmentService {

    /**
     * Save a appointment.
     *
     * @param appointmentDTO
     *        the entity to save
     * @return the persisted entity
     */
    AppointmentDTO save(AppointmentDTO appointmentDTO);

    /**
     * Get all the appointments.
     *
     * @param pageable
     *        the pagination information
     * @return the list of entities
     */
    Page<AppointmentDTO> findAll(Pageable pageable);

    /**
     * Get the "id" appointment.
     *
     * @param id
     *        the id of the entity
     * @return the entity
     */
    AppointmentDTO findOne(Long id);

    /**
     * Delete the "id" appointment.
     *
     * @param id
     *        the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the appointment corresponding to the query.
     *
     * @param query
     *        the query of the search
     * 
     * @param pageable
     *        the pagination information
     * @return the list of entities
     */
    Page<AppointmentDTO> search(String query, Pageable pageable);

    /**
     * Creates the.
     *
     * @param createAppointmentDTO
     *        the create appointment DTO
     * @return the update appointment DTO
     */
    UpdateAppointmentDTO create(CreateAppointmentDTO createAppointmentDTO);

    /**
     * Find all by login.
     *
     * @param login the login
     * @param fromDate the from date
     * @param toDate the to date
     * @return the list
     */
    List<DetailAppointmentDTO> findAllByLogin(String login, Instant fromDate, Instant toDate);

    /**
     * Response the request appointment.
     *
     * @param currentUserLogin the current user login
     * @param attendeeId the attendee id
     * @param response the response
     */
    void response(String currentUserLogin, Long attendeeId, AnwserResponseStatus response);

    /**
     * Gets the one entity.
     *
     * @param appointmentId the appointment id
     * @return the one entity
     * @throws BadRequestAlertException the bad request alert exception if cannot found
     */
    Appointment getOneEntity(Long appointmentId) throws BadRequestAlertException;
    
    /**
     * Find one entity.
     *
     * @param appointmentId the appointment id
     * @return the appointment
     */
    Appointment findOneEntity(Long appointmentId);
    
    /**
     * Validate creator.
     *
     * @param appointment the appointment
     * @param creatorCustomUser the creator custom user
     * @return true, if successful
     */
    boolean validateCreator(Appointment appointment, CustomUser creatorCustomUser);
    
    /**
     * Validate attendee.
     *
     * @param appointment the appointment
     * @param attendeeCustomUser the attendee custom user
     * @return true, if successful
     */
    boolean validateAttendee(Appointment appointment, CustomUser attendeeCustomUser);

    /**
     * Find of member in team.
     *
     * @param currentUserLogin the current user login
     * @param userId the user id
     * @param teamId the team id
     * @param pageable the pageable
     * @return the page
     */
    Page<GroupOfAppointmentDTO> findOfMemberInTeam(String currentUserLogin, Long userId,
            Long teamId, Pageable pageable);

    /**
     * Find all of member in team.
     *
     * @param leaderCustomUser the leader custom user
     * @param userId the user id
     * @param teamId the team id
     * @return the sets the
     */
    Set<GroupOfAppointmentDTO> findAllOfMemberInTeam(CustomUser leaderCustomUser, Long userId,
            Long teamId);
}
