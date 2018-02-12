/*
 * 
 */
package com.ttth.teamcaring.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.ttth.teamcaring.security.AuthoritiesConstants;
import com.ttth.teamcaring.security.SecurityUtils;
import com.ttth.teamcaring.service.AppointmentService;
import com.ttth.teamcaring.service.dto.AppointmentDTO;
import com.ttth.teamcaring.service.dto.CreateAppointmentDTO;
import com.ttth.teamcaring.service.dto.DetailAppointmentDTO;
import com.ttth.teamcaring.service.dto.GroupOfAppointmentDTO;
import com.ttth.teamcaring.service.dto.PageDTO;
import com.ttth.teamcaring.service.dto.ResponseAppointmentDTO;
import com.ttth.teamcaring.service.dto.UpdateAppointmentDTO;
import com.ttth.teamcaring.service.mapper.TimeMapper;
import com.ttth.teamcaring.web.rest.errors.BadRequestAlertException;
import com.ttth.teamcaring.web.rest.util.HeaderUtil;
import com.ttth.teamcaring.web.rest.util.PaginationUtil;

import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;

/**
 * REST controller for managing Appointment.
 *
 * @author Dai Mai
 */
@RestController
@RequestMapping("/api")
public class AppointmentResource {

    /** The log. */
    private final Logger             log         = LoggerFactory
            .getLogger(AppointmentResource.class);

    /** The Constant ENTITY_NAME. */
    public static final String       ENTITY_NAME = "appointment";

    /** The appointment service. */
    private final AppointmentService appointmentService;

    /** The time mapper. */
    @Inject
    private TimeMapper               timeMapper;

    /**
     * Instantiates a new appointment resource.
     *
     * @param appointmentService the appointment service
     */
    public AppointmentResource(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    /**
     * POST /appointments : Create a new appointment.
     *
     * @param appointmentDTO
     *        the appointmentDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the
     *         new appointmentDTO, or with status 400 (Bad Request) if the
     *         appointment has already an ID
     * @throws URISyntaxException
     *         if the Location URI syntax is incorrect
     */
    @PostMapping("/appointments")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<AppointmentDTO> createAppointment(
            @RequestBody AppointmentDTO appointmentDTO) throws URISyntaxException {
        log.debug("REST request to save Appointment : {}", appointmentDTO);
        if (appointmentDTO.getId() != null) {
            throw new BadRequestAlertException("A new appointment cannot already have an ID",
                    ENTITY_NAME, "idexists");
        }
        AppointmentDTO result = appointmentService.save(appointmentDTO);
        return ResponseEntity
                .created(new URI("/api/appointments/" + result.getId())).headers(HeaderUtil
                        .createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * PUT /appointments : Updates an existing appointment.
     *
     * @param appointmentDTO
     *        the appointmentDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated
     *         appointmentDTO, or with status 400 (Bad Request) if the
     *         appointmentDTO is not valid, or with status 500 (Internal Server
     *         Error) if the appointmentDTO couldn't be updated
     * @throws URISyntaxException
     *         if the Location URI syntax is incorrect
     */
    @PutMapping("/appointments")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<AppointmentDTO> updateAppointment(
            @RequestBody AppointmentDTO appointmentDTO) throws URISyntaxException {
        log.debug("REST request to update Appointment : {}", appointmentDTO);
        if (appointmentDTO.getId() == null) {
            return createAppointment(appointmentDTO);
        }
        AppointmentDTO result = appointmentService.save(appointmentDTO);
        return ResponseEntity.ok().headers(
                HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, appointmentDTO.getId().toString()))
                .body(result);
    }

    /**
     * GET /appointments : get all the appointments.
     *
     * @param pageable
     *        the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of
     *         appointments in body
     */
    @GetMapping("/appointments")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<List<AppointmentDTO>> getAllAppointments(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Appointments");
        Page<AppointmentDTO> page = appointmentService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page,
                "/api/appointments");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET /appointments/:id : get the "id" appointment.
     *
     * @param id
     *        the id of the appointmentDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the
     *         appointmentDTO, or with status 404 (Not Found)
     */
    @GetMapping("/appointments/{id}")
    @Timed
    public ResponseEntity<AppointmentDTO> getAppointment(@PathVariable Long id) {
        log.debug("REST request to get Appointment : {}", id);
        AppointmentDTO appointmentDTO = appointmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(appointmentDTO));
    }

    /**
     * DELETE /appointments/:id : delete the "id" appointment.
     *
     * @param id
     *        the id of the appointmentDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/appointments/{id}")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
        log.debug("REST request to delete Appointment : {}", id);
        appointmentService.delete(id);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH /_search/appointments?query=:query : search for the appointment
     * corresponding to the query.
     *
     * @param query
     *        the query of the appointment search
     * @param pageable
     *        the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/appointments")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<List<AppointmentDTO>> searchAppointments(@RequestParam String query,
            @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Appointments for query {}", query);
        Page<AppointmentDTO> page = appointmentService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page,
                "/api/_search/appointments");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * POST /create-appointment : Create a new appointment with specific team &
     * user.
     *
     * @param appointmentDTO
     *        the appointmentDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the
     *         new appointmentDTO, or with status 400 (Bad Request) if the
     *         appointment has already an ID
     * @throws URISyntaxException
     *         if the Location URI syntax is incorrect
     */
    @PostMapping("/create-appointment")
    @Timed
    public ResponseEntity<UpdateAppointmentDTO> createAppointment(
            @RequestBody CreateAppointmentDTO appointmentDTO) throws URISyntaxException {
        log.debug("REST request to create appointment : {}", appointmentDTO);
        UpdateAppointmentDTO result = appointmentService.create(appointmentDTO);
        return ResponseEntity
                .created(new URI("/api/appointments/" + result.getId())).headers(HeaderUtil
                        .createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * GET /my-appointments : get all the appointments of CURRENT authorized user.
     *
     * @param fromDate the from date
     * @param toDate the to date
     * @return the ResponseEntity with status 200 (OK) and the list of
     *         appointments in body
     */
    @GetMapping("/my-appointments")
    @Timed
    public ResponseEntity<List<DetailAppointmentDTO>> getAllAppointmentsOfCurrentAuthorizedUser(
            @RequestParam String fromDate, @RequestParam String toDate) {
        log.debug(
                "REST request to get a page of Appointments of current authorized user from {} to {}",
                fromDate, toDate);
        Instant from = this.timeMapper.mapStringToInstant(fromDate);
        Instant to = this.timeMapper.mapStringToInstant(toDate);
        List<DetailAppointmentDTO> page = this.appointmentService
                .findAllByLogin(SecurityUtils.getCurrentUserLogin(), from, to);
        return ResponseEntity.ok(page);
    }
    
    /**
     * POST /create-appointment/response : The CURRENT user send response to request appointment. (get
     * by current JWT)
     *
     * @param responseAppointmentDTO the response appointment DTO
     * @return the ResponseEntity with status 200 if success, or with status 400
     *         (Bad Request) if some validation fail
     */
    @PostMapping("/create-appointment/response")
    @Timed
    public void responseCreateAppointment(@Valid @RequestBody ResponseAppointmentDTO responseAppointmentDTO) {
        log.debug("REST response appointment : {}", responseAppointmentDTO);
        this.appointmentService.response(SecurityUtils.getCurrentUserLogin(),
                responseAppointmentDTO.getAttendeeId(), responseAppointmentDTO.getResponse());
    }
    
    /**
     * GET /member-appointments : get all the appointments of specific user.
     *
     * @param teamId the team id
     * @param userId the user id
     * @param pageable        the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of
     *         appointments in body
     */
    @GetMapping("/member-appointments")
    @Timed
    public ResponseEntity<PageDTO<GroupOfAppointmentDTO>> getAllAppointmentsOfMember(
            @RequestParam Long teamId, @RequestParam("memberId") Long userId, @ApiParam Pageable pageable) {
        log.debug(
                "REST request to get a page of Appointments of user from {} in team {}",
                userId, teamId);
        Page<GroupOfAppointmentDTO> page = this.appointmentService.findOfMemberInTeam(SecurityUtils.getCurrentUserLogin(), userId, teamId, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/team");
        PageDTO<GroupOfAppointmentDTO> result = PaginationUtil.generateSearchPaginationDto(page);
        return new ResponseEntity<>(result, headers, HttpStatus.OK);
    }

}
