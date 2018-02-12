/*
 * 
 */
package com.ttth.teamcaring.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.ttth.teamcaring.service.AttendeeService;
import com.ttth.teamcaring.service.dto.AttendeeDTO;
import com.ttth.teamcaring.web.rest.errors.BadRequestAlertException;
import com.ttth.teamcaring.web.rest.util.HeaderUtil;
import com.ttth.teamcaring.web.rest.util.PaginationUtil;

import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;

/**
 * REST controller for managing Attendee.
 *
 * @author Dai Mai
 */
@RestController
@RequestMapping("/api")
public class AttendeeResource {

    /** The log. */
    private final Logger          log         = LoggerFactory.getLogger(AttendeeResource.class);

    /** The Constant ENTITY_NAME. */
    public static final String   ENTITY_NAME = "attendee";

    /** The attendee service. */
    private final AttendeeService attendeeService;

    /**
     * Instantiates a new attendee resource.
     *
     * @param attendeeService the attendee service
     */
    public AttendeeResource(AttendeeService attendeeService) {
        this.attendeeService = attendeeService;
    }

    /**
     * POST /attendees : Create a new attendee.
     *
     * @param attendeeDTO
     *        the attendeeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the
     *         new attendeeDTO, or with status 400 (Bad Request) if the attendee
     *         has already an ID
     * @throws URISyntaxException
     *         if the Location URI syntax is incorrect
     */
    @PostMapping("/attendees")
    @Timed
    public ResponseEntity<AttendeeDTO> createAttendee(@RequestBody AttendeeDTO attendeeDTO)
            throws URISyntaxException {
        log.debug("REST request to save Attendee : {}", attendeeDTO);
        if (attendeeDTO.getId() != null) {
            throw new BadRequestAlertException("A new attendee cannot already have an ID",
                    ENTITY_NAME, "idexists");
        }
        AttendeeDTO result = attendeeService.save(attendeeDTO);
        return ResponseEntity
                .created(new URI("/api/attendees/" + result.getId())).headers(HeaderUtil
                        .createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * PUT /attendees : Updates an existing attendee.
     *
     * @param attendeeDTO
     *        the attendeeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated
     *         attendeeDTO, or with status 400 (Bad Request) if the attendeeDTO
     *         is not valid, or with status 500 (Internal Server Error) if the
     *         attendeeDTO couldn't be updated
     * @throws URISyntaxException
     *         if the Location URI syntax is incorrect
     */
    @PutMapping("/attendees")
    @Timed
    public ResponseEntity<AttendeeDTO> updateAttendee(@RequestBody AttendeeDTO attendeeDTO)
            throws URISyntaxException {
        log.debug("REST request to update Attendee : {}", attendeeDTO);
        if (attendeeDTO.getId() == null) {
            return createAttendee(attendeeDTO);
        }
        AttendeeDTO result = attendeeService.save(attendeeDTO);
        return ResponseEntity.ok().headers(
                HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, attendeeDTO.getId().toString()))
                .body(result);
    }

    /**
     * GET /attendees : get all the attendees.
     *
     * @param pageable
     *        the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of attendees
     *         in body
     */
    @GetMapping("/attendees")
    @Timed
    public ResponseEntity<List<AttendeeDTO>> getAllAttendees(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Attendees");
        Page<AttendeeDTO> page = attendeeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/attendees");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET /attendees/:id : get the "id" attendee.
     *
     * @param id
     *        the id of the attendeeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the
     *         attendeeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/attendees/{id}")
    @Timed
    public ResponseEntity<AttendeeDTO> getAttendee(@PathVariable Long id) {
        log.debug("REST request to get Attendee : {}", id);
        AttendeeDTO attendeeDTO = attendeeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(attendeeDTO));
    }

    /**
     * DELETE /attendees/:id : delete the "id" attendee.
     *
     * @param id
     *        the id of the attendeeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/attendees/{id}")
    @Timed
    public ResponseEntity<Void> deleteAttendee(@PathVariable Long id) {
        log.debug("REST request to delete Attendee : {}", id);
        attendeeService.delete(id);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH /_search/attendees?query=:query : search for the attendee
     * corresponding to the query.
     *
     * @param query
     *        the query of the attendee search
     * @param pageable
     *        the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/attendees")
    @Timed
    public ResponseEntity<List<AttendeeDTO>> searchAttendees(@RequestParam String query,
            @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Attendees for query {}", query);
        Page<AttendeeDTO> page = attendeeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page,
                "/api/_search/attendees");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
