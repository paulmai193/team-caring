/*
 * 
 */
package com.ttth.teamcaring.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

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
import com.ttth.teamcaring.security.SecurityUtils;
import com.ttth.teamcaring.service.NoteService;
import com.ttth.teamcaring.service.dto.CreateNoteDTO;
import com.ttth.teamcaring.service.dto.NoteDTO;
import com.ttth.teamcaring.web.rest.errors.BadRequestAlertException;
import com.ttth.teamcaring.web.rest.util.HeaderUtil;
import com.ttth.teamcaring.web.rest.util.PaginationUtil;

import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;

/**
 * REST controller for managing Note.
 *
 * @author Dai Mai
 */
@RestController
@RequestMapping("/api")
public class NoteResource {

    /** The log. */
    private final Logger        log         = LoggerFactory.getLogger(NoteResource.class);

    /** The Constant ENTITY_NAME. */
    public static final String ENTITY_NAME = "note";

    /** The note service. */
    private final NoteService   noteService;

    /**
     * Instantiates a new note resource.
     *
     * @param noteService the note service
     */
    public NoteResource(NoteService noteService) {
        this.noteService = noteService;
    }

    /**
     * POST /notes : Create a new note.
     *
     * @param noteDTO
     *        the noteDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the
     *         new noteDTO, or with status 400 (Bad Request) if the note has
     *         already an ID
     * @throws URISyntaxException
     *         if the Location URI syntax is incorrect
     */
    @PostMapping("/notes")
    @Timed
    public ResponseEntity<NoteDTO> create(@RequestBody NoteDTO noteDTO)
            throws URISyntaxException {
        log.debug("REST request to save Note : {}", noteDTO);
        if (noteDTO.getId() != null) {
            throw new BadRequestAlertException("A new note cannot already have an ID", ENTITY_NAME,
                    "idexists");
        }
        NoteDTO result = noteService.save(noteDTO);
        return ResponseEntity
                .created(new URI("/api/notes/" + result.getId())).headers(HeaderUtil
                        .createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * PUT /notes : Updates an existing note.
     *
     * @param noteDTO
     *        the noteDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated
     *         noteDTO, or with status 400 (Bad Request) if the noteDTO is not
     *         valid, or with status 500 (Internal Server Error) if the noteDTO
     *         couldn't be updated
     * @throws URISyntaxException
     *         if the Location URI syntax is incorrect
     */
    @PutMapping("/notes")
    @Timed
    public ResponseEntity<NoteDTO> update(@RequestBody NoteDTO noteDTO)
            throws URISyntaxException {
        log.debug("REST request to update Note : {}", noteDTO);
        if (noteDTO.getId() == null) {
            return create(noteDTO);
        }
        NoteDTO result = noteService.save(noteDTO);
        return ResponseEntity.ok()
                .headers(
                        HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, noteDTO.getId().toString()))
                .body(result);
    }

    /**
     * GET /notes : get all the notes.
     *
     * @param pageable
     *        the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of notes in
     *         body
     */
    @GetMapping("/notes")
    @Timed
    public ResponseEntity<List<NoteDTO>> getAll(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Notes");
        Page<NoteDTO> page = noteService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/notes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET /notes/:id : get the "id" note.
     *
     * @param id
     *        the id of the noteDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the
     *         noteDTO, or with status 404 (Not Found)
     */
    @GetMapping("/notes/{id}")
    @Timed
    public ResponseEntity<NoteDTO> get(@PathVariable Long id) {
        log.debug("REST request to get Note : {}", id);
        NoteDTO noteDTO = noteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(noteDTO));
    }

    /**
     * DELETE /notes/:id : delete the "id" note.
     *
     * @param id
     *        the id of the noteDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/notes/{id}")
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete Note : {}", id);
        noteService.delete(id);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH /_search/notes?query=:query : search for the note corresponding to
     * the query.
     *
     * @param query
     *        the query of the note search
     * @param pageable
     *        the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/notes")
    @Timed
    public ResponseEntity<List<NoteDTO>> search(@RequestParam String query,
            @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Notes for query {}", query);
        Page<NoteDTO> page = noteService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page,
                "/api/_search/notes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    /**
     * Creates the note.
     *
     * @param noteDTO the note DTO
     * @return the response entity
     * @throws URISyntaxException the URI syntax exception
     */
    @PostMapping("/create-notes")
    @Timed
    public ResponseEntity<NoteDTO> createNote(@Valid @RequestBody CreateNoteDTO noteDTO)
            throws URISyntaxException {
        log.debug("REST request to create Note : {}", noteDTO);
        NoteDTO result = noteService.create(SecurityUtils.getCurrentUserLogin(), noteDTO);
        return ResponseEntity
                .created(new URI("/api/notes/" + result.getId())).headers(HeaderUtil
                        .createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
    }
    
    /**
     * Update note.
     *
     * @param noteDTO the note DTO
     * @return the response entity
     * @throws URISyntaxException the URI syntax exception
     */
//    @PutMapping("/update-notes")
    @Timed
    public ResponseEntity<NoteDTO> updateNote(@Valid @RequestBody NoteDTO noteDTO)
            throws URISyntaxException {
        log.debug("REST request to update Note : {}", noteDTO);
        NoteDTO result = noteService.update(SecurityUtils.getCurrentUserLogin(), noteDTO);
        return ResponseEntity
                .created(new URI("/api/notes/" + result.getId())).headers(HeaderUtil
                        .createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

}
